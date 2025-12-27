package com.yavirac.inventario_backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yavirac.inventario_backend.dto.AuthRequest;
import com.yavirac.inventario_backend.dto.AuthResponse;
import com.yavirac.inventario_backend.entity.Usuario;
import com.yavirac.inventario_backend.repository.UsuarioRepository;
import com.yavirac.inventario_backend.security.JwtService;

@Service
public class AuthService {
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private JwtService jwtService;
    
    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Transactional(readOnly = true)
    public AuthResponse login(AuthRequest request) {
        try {
            System.out.println("🔍 Verificando credenciales para: " + request.getEmail());
            
            // Autenticar usuario (esto internamente llama a loadUserByUsername)
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
            
            System.out.println("✅ Autenticación exitosa, obteniendo usuario...");
            
            // Obtener el usuario con su rol en una sola consulta
            Usuario usuario = usuarioRepository.findByEmailWithRol(request.getEmail())
                    .orElseGet(() -> {
                        System.out.println("⚠️  Usando método fallback para obtener usuario...");
                        // Fallback: intentar con el método normal si el JOIN FETCH falla
                        return usuarioRepository.findByEmail(request.getEmail())
                                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
                    });
            
            System.out.println("✅ Usuario encontrado: " + usuario.getEmail());
            
            // Verificar que el usuario tenga rol
            if (usuario.getRol() == null) {
                System.err.println("❌ Usuario sin rol asignado");
                throw new RuntimeException("El usuario no tiene un rol asignado");
            }
            
            // Verificar que el usuario esté activo
            if (usuario.getEstado() == null || !usuario.getEstado()) {
                System.err.println("❌ Usuario inactivo");
                throw new RuntimeException("El usuario está inactivo");
            }
            
            System.out.println("✅ Usuario válido, generando token...");
            
            // Crear UserDetails desde el usuario ya obtenido
            UserDetails userDetails = org.springframework.security.core.userdetails.User
                    .withUsername(usuario.getEmail())
                    .password(usuario.getContraseña())
                    .authorities(usuario.getRol().getNombre())
                    .build();
            
            String token = jwtService.generateToken(userDetails);
            
            System.out.println("✅ Token generado exitosamente");
            
            return new AuthResponse(token, usuario.getEmail(), usuario.getRol().getNombre());
        } catch (BadCredentialsException e) {
            System.err.println("❌ BadCredentialsException: " + e.getMessage());
            throw new RuntimeException("Credenciales incorrectas. Verifica tu email y contraseña.", e);
        } catch (RuntimeException e) {
            System.err.println("❌ RuntimeException: " + e.getMessage());
            throw e; // Re-lanzar RuntimeException tal cual
        } catch (Exception e) {
            System.err.println("❌ Exception inesperada: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error al iniciar sesión: " + e.getMessage(), e);
        }
    }
    
    public Usuario register(Usuario usuario) {
        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new RuntimeException("El email ya está registrado");
        }
        usuario.setContraseña(passwordEncoder.encode(usuario.getContraseña()));
        return usuarioRepository.save(usuario);
    }
}

