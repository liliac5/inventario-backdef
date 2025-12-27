package com.yavirac.inventario_backend.config;

import com.yavirac.inventario_backend.entity.Rol;
import com.yavirac.inventario_backend.entity.Usuario;
import com.yavirac.inventario_backend.repository.RolRepository;
import com.yavirac.inventario_backend.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DataInitializer implements CommandLineRunner {
    
    @Autowired
    private RolRepository rolRepository;
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Override
    @Transactional
    public void run(String... args) throws Exception {
        try {
            // Crear rol Administrador si no existe
            Rol rolAdmin = rolRepository.findAll().stream()
                    .filter(rol -> "Administrador".equalsIgnoreCase(rol.getNombre()))
                    .findFirst()
                    .orElse(null);
            
            if (rolAdmin == null) {
                rolAdmin = new Rol();
                rolAdmin.setNombre("Administrador");
                rolAdmin = rolRepository.save(rolAdmin);
                System.out.println("✅ Rol 'Administrador' creado con ID: " + rolAdmin.getIdRol());
            } else {
                System.out.println("ℹ️  Rol 'Administrador' ya existe con ID: " + rolAdmin.getIdRol());
            }
            
            // Crear o actualizar usuario administrador
            Usuario existingUser = usuarioRepository.findByEmail("edison@gmail.com").orElse(null);
            
            if (existingUser == null) {
                // Crear nuevo usuario
                Usuario admin = new Usuario();
                admin.setNombre("Edison");
                admin.setEmail("edison@gmail.com");
                admin.setContraseña(passwordEncoder.encode("admin123")); // Contraseña por defecto
                admin.setRol(rolAdmin);
                admin.setEstado(true);
                admin = usuarioRepository.save(admin);
                System.out.println("✅ Usuario administrador creado:");
                System.out.println("   ID: " + admin.getIdUsuario());
                System.out.println("   Email: edison@gmail.com");
                System.out.println("   Contraseña: admin123");
                System.out.println("   Rol: " + admin.getRol().getNombre());
            } else {
                // Actualizar usuario existente: asegurar que tenga el rol correcto y contraseña actualizada
                boolean updated = false;
                
                if (existingUser.getRol() == null || !existingUser.getRol().getIdRol().equals(rolAdmin.getIdRol())) {
                    existingUser.setRol(rolAdmin);
                    updated = true;
                }
                
                if (existingUser.getEstado() == null || !existingUser.getEstado()) {
                    existingUser.setEstado(true);
                    updated = true;
                }
                
                // Actualizar contraseña a admin123 (por si fue creado con otra contraseña)
                String nuevaContraseña = passwordEncoder.encode("admin123");
                if (!passwordEncoder.matches("admin123", existingUser.getContraseña())) {
                    existingUser.setContraseña(nuevaContraseña);
                    updated = true;
                    System.out.println("ℹ️  Contraseña del usuario actualizada a: admin123");
                }
                
                if (updated) {
                    usuarioRepository.save(existingUser);
                    System.out.println("✅ Usuario administrador actualizado:");
                } else {
                    System.out.println("ℹ️  El usuario edison@gmail.com ya existe y está actualizado");
                }
                
                System.out.println("   Usuario ID: " + existingUser.getIdUsuario());
                System.out.println("   Email: edison@gmail.com");
                System.out.println("   Contraseña: admin123");
                System.out.println("   Rol: " + (existingUser.getRol() != null ? existingUser.getRol().getNombre() : "Sin rol"));
            }
        } catch (Exception e) {
            System.err.println("❌ Error al inicializar datos: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

