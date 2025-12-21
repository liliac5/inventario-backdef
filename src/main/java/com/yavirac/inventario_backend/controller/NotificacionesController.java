package com.yavirac.inventario_backend.controller;

import com.yavirac.inventario_backend.entity.Notificaciones;
import com.yavirac.inventario_backend.service.NotificacionesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/notificaciones")
@CrossOrigin(origins = "*")
public class NotificacionesController {
    
    @Autowired
    private NotificacionesService notificacionesService;
    
    @GetMapping
    public ResponseEntity<List<Notificaciones>> findAll() {
        return ResponseEntity.ok(notificacionesService.findAll());
    }
    
    @GetMapping("/no-leidas")
    public ResponseEntity<List<Notificaciones>> findNoLeidas() {
        return ResponseEntity.ok(notificacionesService.findNoLeidas());
    }
    
    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<Notificaciones>> findByTipo(@PathVariable String tipo) {
        return ResponseEntity.ok(notificacionesService.findByTipo(tipo));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Notificaciones> findById(@PathVariable Long id) {
        Optional<Notificaciones> notificacion = notificacionesService.findById(id);
        return notificacion.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<Notificaciones> save(@RequestBody Notificaciones notificacion) {
        return ResponseEntity.status(HttpStatus.CREATED).body(notificacionesService.save(notificacion));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Notificaciones> update(@PathVariable Long id, @RequestBody Notificaciones notificacion) {
        try {
            return ResponseEntity.ok(notificacionesService.update(id, notificacion));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PutMapping("/{id}/marcar-leida")
    public ResponseEntity<Notificaciones> marcarComoLeida(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(notificacionesService.marcarComoLeida(id));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        try {
            notificacionesService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}

