package com.yavirac.inventario_backend.controller;

import com.yavirac.inventario_backend.dto.AuthRequest;
import com.yavirac.inventario_backend.dto.AuthResponse;
import com.yavirac.inventario_backend.entity.Usuario;
import com.yavirac.inventario_backend.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {
    
    @Autowired
    private AuthService authService;
    
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/register")
    public ResponseEntity<Usuario> register(@RequestBody Usuario usuario) {
        Usuario nuevoUsuario = authService.register(usuario);
        return ResponseEntity.ok(nuevoUsuario);
    }
}

