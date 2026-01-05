package com.yavirac.inventario_backend.config;

import com.yavirac.inventario_backend.entity.Aula;
import com.yavirac.inventario_backend.entity.Categoria;
import com.yavirac.inventario_backend.entity.Notificaciones;
import com.yavirac.inventario_backend.entity.ReporteIncidencia;
import com.yavirac.inventario_backend.entity.Rol;
import com.yavirac.inventario_backend.entity.Usuario;
import com.yavirac.inventario_backend.repository.AulaRepository;
import com.yavirac.inventario_backend.repository.CategoriaRepository;
import com.yavirac.inventario_backend.repository.NotificacionesRepository;
import com.yavirac.inventario_backend.repository.ReporteIncidenciaRepository;
import com.yavirac.inventario_backend.repository.RolRepository;
import com.yavirac.inventario_backend.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
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
    
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    @Autowired
    private ReporteIncidenciaRepository reporteIncidenciaRepository;
    
    @Autowired
    private NotificacionesRepository notificacionesRepository;
    
    @Override
    @Transactional
    public void run(String... args) throws Exception {
        try {
            // Actualizar tabla notificaciones para agregar campos de reportes de incidencias
            actualizarTablaNotificaciones();
            
            // Limpiar columnas antiguas que no se usan para reportes de incidencias
            limpiarColumnasAntiguas();
            
            // Migrar reportes existentes a notificaciones
            migrarReportesExistentesANotificaciones();
            
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
    
    /**
     * Actualiza la tabla notificaciones agregando columnas para reportes de incidencias
     */
    private void actualizarTablaNotificaciones() {
        try {
            System.out.println("🔧 Actualizando tabla notificaciones para soportar reportes de incidencias...");
            
            // Verificar y agregar columna id_reporte
            try {
                jdbcTemplate.execute("ALTER TABLE public.notificaciones ADD COLUMN IF NOT EXISTS id_reporte BIGINT");
                System.out.println("   ✅ Columna 'id_reporte' agregada o ya existe");
            } catch (Exception e) {
                System.out.println("   ⚠️  Columna 'id_reporte': " + e.getMessage());
            }
            
            // Verificar y agregar columna estado
            try {
                jdbcTemplate.execute("ALTER TABLE public.notificaciones ADD COLUMN IF NOT EXISTS estado VARCHAR(50)");
                System.out.println("   ✅ Columna 'estado' agregada o ya existe");
            } catch (Exception e) {
                System.out.println("   ⚠️  Columna 'estado': " + e.getMessage());
            }
            
            // Verificar y agregar columna tipo_incidencia
            try {
                jdbcTemplate.execute("ALTER TABLE public.notificaciones ADD COLUMN IF NOT EXISTS tipo_incidencia VARCHAR(100)");
                System.out.println("   ✅ Columna 'tipo_incidencia' agregada o ya existe");
            } catch (Exception e) {
                System.out.println("   ⚠️  Columna 'tipo_incidencia': " + e.getMessage());
            }
            
            // Verificar y agregar columna detalle_problema
            try {
                jdbcTemplate.execute("ALTER TABLE public.notificaciones ADD COLUMN IF NOT EXISTS detalle_problema TEXT");
                System.out.println("   ✅ Columna 'detalle_problema' agregada o ya existe");
            } catch (Exception e) {
                System.out.println("   ⚠️  Columna 'detalle_problema': " + e.getMessage());
            }
            
            // Verificar y agregar columna elemento_afectado
            try {
                jdbcTemplate.execute("ALTER TABLE public.notificaciones ADD COLUMN IF NOT EXISTS elemento_afectado VARCHAR(200)");
                System.out.println("   ✅ Columna 'elemento_afectado' agregada o ya existe");
            } catch (Exception e) {
                System.out.println("   ⚠️  Columna 'elemento_afectado': " + e.getMessage());
            }
            
            // Verificar y agregar columna reportado_por
            try {
                jdbcTemplate.execute("ALTER TABLE public.notificaciones ADD COLUMN IF NOT EXISTS reportado_por VARCHAR(200)");
                System.out.println("   ✅ Columna 'reportado_por' agregada o ya existe");
            } catch (Exception e) {
                System.out.println("   ⚠️  Columna 'reportado_por': " + e.getMessage());
            }
            
            // Verificar y agregar columna fecha_hora_reporte
            try {
                jdbcTemplate.execute("ALTER TABLE public.notificaciones ADD COLUMN IF NOT EXISTS fecha_hora_reporte TIMESTAMP(6) WITHOUT TIME ZONE");
                System.out.println("   ✅ Columna 'fecha_hora_reporte' agregada o ya existe");
            } catch (Exception e) {
                System.out.println("   ⚠️  Columna 'fecha_hora_reporte': " + e.getMessage());
            }
            
            System.out.println("✅ Tabla 'notificaciones' actualizada correctamente");
            
        } catch (Exception e) {
            System.err.println("❌ Error al actualizar tabla notificaciones: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Migra los reportes de incidencias existentes a notificaciones
     */
    private void migrarReportesExistentesANotificaciones() {
        try {
            System.out.println("🔧 Migrando reportes existentes a notificaciones...");
            
            List<ReporteIncidencia> reportesExistentes = reporteIncidenciaRepository.findAll();
            int reportesMigrados = 0;
            
            for (ReporteIncidencia reporte : reportesExistentes) {
                // Verificar si ya existe una notificación para este reporte
                boolean existeNotificacion = notificacionesRepository
                    .findByTipoNotificacionAndIdReporte("REPORTE_INCIDENCIA", reporte.getIdReporte())
                    .isPresent();
                
                if (!existeNotificacion) {
                    // Crear notificación para este reporte
                    Notificaciones notificacion = new Notificaciones();
                    notificacion.setTipoNotificacion("REPORTE_INCIDENCIA");
                    notificacion.setIdReporte(reporte.getIdReporte());
                    notificacion.setEstado(reporte.getEstado());
                    notificacion.setTipoIncidencia(reporte.getTipoIncidencia());
                    notificacion.setDetalleProblema(reporte.getDetalleProblema());
                    notificacion.setFechaHoraReporte(reporte.getFechaHora());
                    
                    // Obtener nombre del elemento afectado
                    if (reporte.getBien() != null) {
                        String elementoAfectado = reporte.getBien().getCodigoBien() != null && !reporte.getBien().getCodigoBien().isEmpty() 
                            ? reporte.getBien().getCodigoBien() 
                            : (reporte.getBien().getNombreBien() != null && !reporte.getBien().getNombreBien().isEmpty() 
                                ? reporte.getBien().getNombreBien() 
                                : reporte.getBien().getClaseBien());
                        notificacion.setElementoAfectado(elementoAfectado != null ? elementoAfectado : "Bien #" + reporte.getBien().getIdBien());
                    } else {
                        notificacion.setElementoAfectado("No disponible");
                    }
                    
                    // Obtener nombre del usuario que reporta
                    if (reporte.getUsuario() != null) {
                        String reportadoPor = reporte.getUsuario().getNombre() != null && !reporte.getUsuario().getNombre().isEmpty() 
                            ? reporte.getUsuario().getNombre() 
                            : reporte.getUsuario().getEmail();
                        notificacion.setReportadoPor(reportadoPor != null ? reportadoPor : "Usuario #" + reporte.getUsuario().getIdUsuario());
                    } else {
                        notificacion.setReportadoPor("No disponible");
                    }
                    
                    notificacion.setLeida(false);
                    
                    notificacionesRepository.save(notificacion);
                    reportesMigrados++;
                    System.out.println("   ✅ Notificación creada para reporte ID: " + reporte.getIdReporte());
                }
            }
            
            if (reportesMigrados > 0) {
                System.out.println("✅ " + reportesMigrados + " reporte(s) migrado(s) a notificaciones");
            } else {
                System.out.println("ℹ️  Todos los reportes ya tienen sus notificaciones correspondientes");
            }
            
        } catch (Exception e) {
            System.err.println("❌ Error al migrar reportes a notificaciones: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Elimina las columnas antiguas que no se usan para reportes de incidencias
     */
    private void limpiarColumnasAntiguas() {
        try {
            System.out.println("🔧 Eliminando columnas antiguas de notificaciones que están en null...");
            
            // Lista de columnas a eliminar (solo para reportes de incidencias)
            String[] columnasAntiguas = {
                "bienes_constatados",
                "bienes_no_identificados",
                "cantidad_olympo_2015",
                "cantidad_olympo_senescyt",
                "cantidad_otro_sistema",
                "cuenta_contable",
                "nombre_cuenta",
                "sistema_control",
                "total",
                "total_sistema",
                "control"
            };
            
            for (String columna : columnasAntiguas) {
                try {
                    // Verificar si la columna existe antes de eliminarla
                    String checkColumnQuery = "SELECT COUNT(*) FROM information_schema.columns " +
                        "WHERE table_schema = 'public' AND table_name = 'notificaciones' AND column_name = ?";
                    
                    Integer count = jdbcTemplate.queryForObject(checkColumnQuery, Integer.class, columna);
                    
                    if (count != null && count > 0) {
                        // Eliminar la columna
                        jdbcTemplate.execute("ALTER TABLE public.notificaciones DROP COLUMN IF EXISTS " + columna + " CASCADE");
                        System.out.println("   ✅ Columna '" + columna + "' eliminada");
                    } else {
                        System.out.println("   ℹ️  Columna '" + columna + "' no existe");
                    }
                } catch (Exception e) {
                    System.out.println("   ⚠️  Error al eliminar columna '" + columna + "': " + e.getMessage());
                }
            }
            
            System.out.println("✅ Limpieza de columnas completada");
            
        } catch (Exception e) {
            System.err.println("❌ Error al limpiar columnas antiguas: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

