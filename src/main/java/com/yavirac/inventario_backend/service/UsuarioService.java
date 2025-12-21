package com.yavirac.inventario_backend.service;

import com.yavirac.inventario_backend.dto.UsuarioRequest;
import com.yavirac.inventario_backend.entity.Rol;
import com.yavirac.inventario_backend.entity.Usuario;
import com.yavirac.inventario_backend.repository.RolRepository;
import com.yavirac.inventario_backend.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private RolRepository rolRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }
    
    public Optional<Usuario> findById(Long id) {
        return usuarioRepository.findById(id);
    }
    
    public Usuario save(UsuarioRequest request) {
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("El email ya está registrado");
        }
        
        Rol rol = rolRepository.findById(request.getIdRol())
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));
        
        Usuario usuario = new Usuario();
        usuario.setNombre(request.getNombre());
        usuario.setRol(rol);
        usuario.setEmail(request.getEmail());
        usuario.setContraseña(passwordEncoder.encode(request.getContraseña()));
        usuario.setEstado(request.getEstado() != null ? request.getEstado() : true);
        
        return usuarioRepository.save(usuario);
    }
    
    public Usuario update(Long id, UsuarioRequest request) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        if (!usuario.getEmail().equals(request.getEmail()) && 
            usuarioRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("El email ya está registrado");
        }
        
        Rol rol = rolRepository.findById(request.getIdRol())
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));
        
        usuario.setNombre(request.getNombre());
        usuario.setRol(rol);
        usuario.setEmail(request.getEmail());
        if (request.getContraseña() != null && !request.getContraseña().isEmpty()) {
            usuario.setContraseña(passwordEncoder.encode(request.getContraseña()));
        }
        usuario.setEstado(request.getEstado() != null ? request.getEstado() : usuario.getEstado());
        
        return usuarioRepository.save(usuario);
    }
    
    public void deleteById(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new RuntimeException("Usuario no encontrado");
        }
        usuarioRepository.deleteById(id);
    }
}

