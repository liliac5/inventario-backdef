package com.yavirac.inventario_backend.service;

import com.yavirac.inventario_backend.dto.DocenteRequest;
import com.yavirac.inventario_backend.entity.Docente;
import com.yavirac.inventario_backend.entity.Usuario;
import com.yavirac.inventario_backend.repository.DocenteRepository;
import com.yavirac.inventario_backend.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DocenteService {
    
    @Autowired
    private DocenteRepository docenteRepository;
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    public List<Docente> findAll() {
        return docenteRepository.findAll();
    }
    
    public Optional<Docente> findById(Long id) {
        return docenteRepository.findById(id);
    }
    
    public Optional<Docente> findByCedula(String cedula) {
        return docenteRepository.findByCedula(cedula);
    }
    
    public Docente save(DocenteRequest request) {
        if (docenteRepository.existsByCedula(request.getCedula())) {
            throw new RuntimeException("La cédula ya está registrada");
        }
        
        Usuario usuario = usuarioRepository.findById(request.getIdUsuario())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        Docente docente = new Docente();
        docente.setUsuario(usuario);
        docente.setCedula(request.getCedula());
        docente.setNombres(request.getNombres());
        docente.setApellidos(request.getApellidos());
        docente.setRegimen(request.getRegimen());
        docente.setObservaciones(request.getObservaciones());
        
        return docenteRepository.save(docente);
    }
    
    public Docente update(Long id, DocenteRequest request) {
        Docente docente = docenteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Docente no encontrado"));
        
        if (!docente.getCedula().equals(request.getCedula()) && 
            docenteRepository.existsByCedula(request.getCedula())) {
            throw new RuntimeException("La cédula ya está registrada");
        }
        
        Usuario usuario = usuarioRepository.findById(request.getIdUsuario())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        docente.setUsuario(usuario);
        docente.setCedula(request.getCedula());
        docente.setNombres(request.getNombres());
        docente.setApellidos(request.getApellidos());
        docente.setRegimen(request.getRegimen());
        docente.setObservaciones(request.getObservaciones());
        
        return docenteRepository.save(docente);
    }
    
    public void deleteById(Long id) {
        if (!docenteRepository.existsById(id)) {
            throw new RuntimeException("Docente no encontrado");
        }
        docenteRepository.deleteById(id);
    }
}

