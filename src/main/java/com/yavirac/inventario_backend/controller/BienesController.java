package com.yavirac.inventario_backend.controller;

import com.yavirac.inventario_backend.entity.Bienes;
import com.yavirac.inventario_backend.service.BienesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/bienes")
@CrossOrigin(origins = "*")
public class BienesController {
    
    @Autowired
    private BienesService bienesService;
    
    @GetMapping
    public ResponseEntity<List<Bienes>> findAll() {
        return ResponseEntity.ok(bienesService.findAll());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Bienes> findById(@PathVariable Long id) {
        Optional<Bienes> bien = bienesService.findById(id);
        return bien.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/codigo/{codigoBien}")
    public ResponseEntity<Bienes> findByCodigoBien(@PathVariable String codigoBien) {
        Optional<Bienes> bien = bienesService.findByCodigoBien(codigoBien);
        return bien.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<Bienes> save(@RequestBody Bienes bienes) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(bienesService.save(bienes));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Bienes> update(@PathVariable Long id, @RequestBody Bienes bienes) {
        try {
            return ResponseEntity.ok(bienesService.update(id, bienes));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        try {
            bienesService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}

