package com.yavirac.inventario_backend.controller;

import com.yavirac.inventario_backend.dto.DocenteRequest;
import com.yavirac.inventario_backend.entity.Docente;
import com.yavirac.inventario_backend.service.DocenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/docentes")
@CrossOrigin(origins = "*")
public class DocenteController {
    
    @Autowired
    private DocenteService docenteService;
    
    @GetMapping
    public ResponseEntity<List<Docente>> findAll() {
        return ResponseEntity.ok(docenteService.findAll());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Docente> findById(@PathVariable Long id) {
        Optional<Docente> docente = docenteService.findById(id);
        return docente.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/cedula/{cedula}")
    public ResponseEntity<Docente> findByCedula(@PathVariable String cedula) {
        Optional<Docente> docente = docenteService.findByCedula(cedula);
        return docente.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<Docente> save(@RequestBody DocenteRequest request) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(docenteService.save(request));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Docente> update(@PathVariable Long id, @RequestBody DocenteRequest request) {
        try {
            return ResponseEntity.ok(docenteService.update(id, request));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        try {
            docenteService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}

