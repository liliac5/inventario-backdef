package com.yavirac.inventario_backend.controller;

import com.yavirac.inventario_backend.dto.AuthRequest;
import com.yavirac.inventario_backend.dto.AuthResponse;
import com.yavirac.inventario_backend.entity.Usuario;
import com.yavirac.inventario_backend.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {
    
    @Autowired
    private AuthService authService;
    
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
    
    // Método para enmascarar tokens en logs
    private String maskToken(String token) {
        if (token == null || token.isEmpty()) {
            return "***";
        }
        if (token.length() <= 10) {
            return "***";
        }
        return token.substring(0, 6) + "..." + token.substring(token.length() - 4);
    }
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        try {
            System.out.println("🔐 Intento de login para: " + maskEmail(request.getEmail()));
            AuthResponse response = authService.login(request);
            System.out.println("✅ Login exitoso para: " + maskEmail(response.getEmail()));
            System.out.println("📤 Enviando respuesta: token=" + maskToken(response.getToken()) + 
                             ", email=" + maskEmail(response.getEmail()) + ", rol=" + response.getRol());
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            System.err.println("❌ Error en login: " + e.getMessage());
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        } catch (Exception e) {
            System.err.println("❌ Error inesperado en login");
            Map<String, String> error = new HashMap<>();
            error.put("error", "Error al procesar la solicitud");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Usuario usuario) {
        try {
            Usuario nuevoUsuario = authService.register(usuario);
            return ResponseEntity.ok(nuevoUsuario);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Error al procesar la solicitud: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
}

