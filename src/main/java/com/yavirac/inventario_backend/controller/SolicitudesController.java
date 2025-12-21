package com.yavirac.inventario_backend.controller;

import com.yavirac.inventario_backend.entity.Solicitudes;
import com.yavirac.inventario_backend.service.SolicitudesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/solicitudes")
@CrossOrigin(origins = "*")
public class SolicitudesController {
    
    @Autowired
    private SolicitudesService solicitudesService;
    
    @GetMapping
    public ResponseEntity<List<Solicitudes>> findAll() {
        return ResponseEntity.ok(solicitudesService.findAll());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Solicitudes> findById(@PathVariable Long id) {
        Optional<Solicitudes> solicitud = solicitudesService.findById(id);
        return solicitud.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<Solicitudes> save(@RequestBody Map<String, Object> request) {
        Long idBien = Long.valueOf(request.get("idBien").toString());
        Long idSolicitud = Long.valueOf(request.get("idSolicitud").toString());
        String estado = request.get("estado") != null ? request.get("estado").toString() : "PENDIENTE";
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(solicitudesService.save(idBien, idSolicitud, estado));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Solicitudes> update(@PathVariable Long id, @RequestBody Map<String, Object> request) {
        Long idBien = request.get("idBien") != null ? Long.valueOf(request.get("idBien").toString()) : null;
        Long idSolicitud = request.get("idSolicitud") != null ? Long.valueOf(request.get("idSolicitud").toString()) : null;
        String estado = request.get("estado") != null ? request.get("estado").toString() : null;
        try {
            return ResponseEntity.ok(solicitudesService.update(id, idBien, idSolicitud, estado));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        try {
            solicitudesService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}

