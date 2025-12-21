package com.yavirac.inventario_backend.service;

import com.yavirac.inventario_backend.entity.Bienes;
import com.yavirac.inventario_backend.entity.Solicitudes;
import com.yavirac.inventario_backend.repository.BienesRepository;
import com.yavirac.inventario_backend.repository.SolicitudesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SolicitudesService {
    
    @Autowired
    private SolicitudesRepository solicitudesRepository;
    
    @Autowired
    private BienesRepository bienesRepository;
    
    public List<Solicitudes> findAll() {
        return solicitudesRepository.findAll();
    }
    
    public Optional<Solicitudes> findById(Long id) {
        return solicitudesRepository.findById(id);
    }
    
    public Solicitudes save(Long idBien, Long idSolicitud, String estado) {
        Bienes bien = bienesRepository.findById(idBien)
                .orElseThrow(() -> new RuntimeException("Bien no encontrado"));
        
        Solicitudes solicitud = new Solicitudes();
        solicitud.setIdSolicitud(idSolicitud);
        solicitud.setBien(bien);
        solicitud.setEstado(estado);
        
        return solicitudesRepository.save(solicitud);
    }
    
    public Solicitudes update(Long id, Long idBien, Long idSolicitud, String estado) {
        Solicitudes solicitud = solicitudesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Solicitud no encontrada"));
        
        if (idBien != null) {
            Bienes bien = bienesRepository.findById(idBien)
                    .orElseThrow(() -> new RuntimeException("Bien no encontrado"));
            solicitud.setBien(bien);
        }
        
        if (idSolicitud != null) {
            solicitud.setIdSolicitud(idSolicitud);
        }
        
        if (estado != null) {
            solicitud.setEstado(estado);
        }
        
        return solicitudesRepository.save(solicitud);
    }
    
    public void deleteById(Long id) {
        if (!solicitudesRepository.existsById(id)) {
            throw new RuntimeException("Solicitud no encontrada");
        }
        solicitudesRepository.deleteById(id);
    }
}

