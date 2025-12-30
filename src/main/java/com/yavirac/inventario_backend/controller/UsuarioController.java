package com.yavirac.inventario_backend.controller;

import com.yavirac.inventario_backend.dto.UsuarioRequest;
import com.yavirac.inventario_backend.entity.Usuario;
import com.yavirac.inventario_backend.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "*")
public class UsuarioController {
    
    @Autowired
    private UsuarioService usuarioService;
    
    // Método para enmascarar emails en logs
    private String maskEmail(String email) {
        if (email == null || email.isEmpty()) {
            return "***";
        }
        int atIndex = email.indexOf('@');
        if (atIndex <= 0) {
            return "***";
        }
        String localPart = email.substring(0, atIndex);
        String domain = email.substring(atIndex);
        
        if (localPart.length() <= 2) {
            return localPart.charAt(0) + "***" + domain;
        }
        return localPart.charAt(0) + "***" + localPart.charAt(localPart.length() - 1) + domain;
    }
    
    @GetMapping
    public ResponseEntity<List<Usuario>> findAll() {
        return ResponseEntity.ok(usuarioService.findAll());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> findById(@PathVariable Long id) {
        Optional<Usuario> usuario = usuarioService.findById(id);
        return usuario.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<?> save(@RequestBody UsuarioRequest request) {
        try {
            System.out.println("📝 Creando usuario: " + maskEmail(request.getEmail()));
            System.out.println("   Nombre: " + (request.getNombre() != null ? request.getNombre() : "N/A"));
            System.out.println("   Email: " + maskEmail(request.getEmail()));
            System.out.println("   Rol ID: " + request.getIdRol());
            System.out.println("   Contraseña: " + (request.getContraseña() != null && !request.getContraseña().isEmpty() ? "***" : "No proporcionada"));
            
            // Validar campos requeridos
            if (request.getNombre() == null || request.getNombre().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("El nombre es requerido");
            }
            if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("El email es requerido");
            }
            if (request.getContraseña() == null || request.getContraseña().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("La contraseña es requerida");
            }
            if (request.getIdRol() == null) {
                return ResponseEntity.badRequest().body("El rol es requerido");
            }
            
            Usuario usuario = usuarioService.save(request);
            System.out.println("✅ Usuario creado exitosamente: " + maskEmail(usuario.getEmail()));
            return ResponseEntity.status(HttpStatus.CREATED).body(usuario);
        } catch (RuntimeException e) {
            System.err.println("❌ Error al crear usuario: " + e.getMessage());
            e.printStackTrace();
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        } catch (Exception e) {
            System.err.println("❌ Error inesperado al crear usuario: " + e.getMessage());
            e.printStackTrace();
            Map<String, String> error = new HashMap<>();
            error.put("error", "Error al crear el usuario: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody UsuarioRequest request) {
        try {
            System.out.println("✏️  Actualizando usuario ID: " + id);
            
            // Validar campos requeridos
            if (request.getNombre() == null || request.getNombre().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("El nombre es requerido");
            }
            if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("El email es requerido");
            }
            if (request.getIdRol() == null) {
                return ResponseEntity.badRequest().body("El rol es requerido");
            }
            
            Usuario usuario = usuarioService.update(id, request);
            System.out.println("✅ Usuario actualizado exitosamente: " + maskEmail(usuario.getEmail()));
            return ResponseEntity.ok(usuario);
        } catch (RuntimeException e) {
            System.err.println("❌ Error al actualizar usuario: " + e.getMessage());
            e.printStackTrace();
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        } catch (Exception e) {
            System.err.println("❌ Error inesperado al actualizar usuario: " + e.getMessage());
            e.printStackTrace();
            Map<String, String> error = new HashMap<>();
            error.put("error", "Error al actualizar el usuario: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        try {
            usuarioService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PutMapping("/{id}/estado")
    public ResponseEntity<?> cambiarEstado(@PathVariable Long id, @RequestBody Map<String, Boolean> request) {
        try {
            Boolean estado = request.get("estado");
            if (estado == null) {
                return ResponseEntity.badRequest().body("El campo 'estado' es requerido");
            }
            Usuario usuario = usuarioService.cambiarEstado(id, estado);
            return ResponseEntity.ok(usuario);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al actualizar el estado: " + e.getMessage());
        }
    }
    
    @PutMapping("/{id}/desactivar")
    public ResponseEntity<?> desactivar(@PathVariable Long id) {
        try {
            Usuario usuario = usuarioService.desactivar(id);
            return ResponseEntity.ok(usuario);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al desactivar el usuario: " + e.getMessage());
        }
    }
    
    @PutMapping("/{id}/activar")
    public ResponseEntity<?> activar(@PathVariable Long id) {
        try {
            Usuario usuario = usuarioService.activar(id);
            return ResponseEntity.ok(usuario);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al activar el usuario: " + e.getMessage());
        }
    }
}

