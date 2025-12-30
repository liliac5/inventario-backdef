package com.yavirac.inventario_backend.config;

import com.yavirac.inventario_backend.entity.Aula;
import com.yavirac.inventario_backend.entity.Categoria;
import com.yavirac.inventario_backend.entity.Rol;
import com.yavirac.inventario_backend.entity.Usuario;
import com.yavirac.inventario_backend.repository.AulaRepository;
import com.yavirac.inventario_backend.repository.CategoriaRepository;
import com.yavirac.inventario_backend.repository.RolRepository;
import com.yavirac.inventario_backend.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {
    
    @Autowired
    private RolRepository rolRepository;
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private AulaRepository aulaRepository;
    
    @Autowired
    private CategoriaRepository categoriaRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Override
    @Transactional
    public void run(String... args) throws Exception {
        try {
            // Crear roles necesarios si no existen
            String[] rolesNecesarios = {"Administrador", "Docente", "Coordinador"};
            
            System.out.println("🔧 Inicializando roles del sistema...");
            for (String nombreRol : rolesNecesarios) {
                Rol rol = rolRepository.findAll().stream()
                        .filter(r -> nombreRol.equalsIgnoreCase(r.getNombre()))
                        .findFirst()
                        .orElse(null);
                
                if (rol == null) {
                    rol = new Rol();
                    rol.setNombre(nombreRol);
                    rol = rolRepository.save(rol);
                    System.out.println("✅ Rol '" + nombreRol + "' creado con ID: " + rol.getIdRol());
                } else {
                    System.out.println("ℹ️  Rol '" + nombreRol + "' ya existe con ID: " + rol.getIdRol());
                }
            }
            
            // Mostrar todos los roles disponibles
            List<Rol> todosLosRoles = rolRepository.findAll();
            System.out.println("📋 Roles disponibles en el sistema:");
            for (Rol r : todosLosRoles) {
                System.out.println("   - ID: " + r.getIdRol() + " | Nombre: " + r.getNombre());
            }
            
            // Crear aulas necesarias si no existen
            String[] aulasNecesarias = {"Aula Xian", "Aula Sarsota", "Aula Gori", "Lab Inf 2", "Lab Idiomas"};
            
            System.out.println("🔧 Inicializando aulas del sistema...");
            for (String nombreAula : aulasNecesarias) {
                Aula aula = aulaRepository.findAll().stream()
                        .filter(a -> nombreAula.equalsIgnoreCase(a.getNombre()))
                        .findFirst()
                        .orElse(null);
                
                if (aula == null) {
                    aula = new Aula();
                    aula.setNombre(nombreAula);
                    // Determinar tipo según el nombre
                    if (nombreAula.toLowerCase().contains("lab")) {
                        aula.setTipo("Laboratorio");
                        aula.setCapacidad(25);
                    } else {
                        aula.setTipo("Aula");
                        aula.setCapacidad(30);
                    }
                    aula.setEstado(true);
                    aula = aulaRepository.save(aula);
                    System.out.println("✅ Aula '" + nombreAula + "' creada con ID: " + aula.getIdAula());
                } else {
                    System.out.println("ℹ️  Aula '" + nombreAula + "' ya existe con ID: " + aula.getIdAula());
                }
            }
            
            // Mostrar todas las aulas disponibles
            List<Aula> todasLasAulas = aulaRepository.findAll();
            System.out.println("📋 Aulas disponibles en el sistema:");
            for (Aula a : todasLasAulas) {
                System.out.println("   - ID: " + a.getIdAula() + " | Nombre: " + a.getNombre() + 
                                 " | Tipo: " + (a.getTipo() != null ? a.getTipo() : "N/A"));
            }
            
            // Crear categorías necesarias si no existen
            String[] categoriasNecesarias = {"Bienes Muebles", "Bienes Secap", "Bienes Consumo"};
            
            System.out.println("🔧 Inicializando categorías del sistema...");
            for (String nombreCategoria : categoriasNecesarias) {
                Categoria categoria = categoriaRepository.findAll().stream()
                        .filter(c -> nombreCategoria.equalsIgnoreCase(c.getNombre()))
                        .findFirst()
                        .orElse(null);
                
                if (categoria == null) {
                    categoria = new Categoria();
                    categoria.setNombre(nombreCategoria);
                    categoria = categoriaRepository.save(categoria);
                    System.out.println("✅ Categoría '" + nombreCategoria + "' creada con ID: " + categoria.getIdCategoria());
                } else {
                    System.out.println("ℹ️  Categoría '" + nombreCategoria + "' ya existe con ID: " + categoria.getIdCategoria());
                }
            }
            
            // Mostrar todas las categorías disponibles
            List<Categoria> todasLasCategorias = categoriaRepository.findAll();
            System.out.println("📋 Categorías disponibles en el sistema:");
            for (Categoria c : todasLasCategorias) {
                System.out.println("   - ID: " + c.getIdCategoria() + " | Nombre: " + c.getNombre());
            }
            
            // Obtener rol Administrador para el usuario admin
            Rol rolAdmin = rolRepository.findAll().stream()
                    .filter(rol -> "Administrador".equalsIgnoreCase(rol.getNombre()))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("No se pudo crear el rol Administrador"));
            
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

