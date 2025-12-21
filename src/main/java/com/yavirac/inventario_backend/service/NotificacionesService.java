package com.yavirac.inventario_backend.service;

import com.yavirac.inventario_backend.entity.Notificaciones;
import com.yavirac.inventario_backend.repository.NotificacionesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NotificacionesService {
    
    @Autowired
    private NotificacionesRepository notificacionesRepository;
    
    public List<Notificaciones> findAll() {
        return notificacionesRepository.findAll();
    }
    
    public List<Notificaciones> findNoLeidas() {
        return notificacionesRepository.findByLeidaFalse();
    }
    
    public List<Notificaciones> findByTipo(String tipoNotificacion) {
        return notificacionesRepository.findByTipoNotificacion(tipoNotificacion);
    }
    
    public Optional<Notificaciones> findById(Long id) {
        return notificacionesRepository.findById(id);
    }
    
    public Notificaciones save(Notificaciones notificacion) {
        return notificacionesRepository.save(notificacion);
    }
    
    public Notificaciones update(Long id, Notificaciones notificacion) {
        Notificaciones existingNotificacion = notificacionesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notificación no encontrada"));
        
        if (notificacion.getCuentaContable() != null) existingNotificacion.setCuentaContable(notificacion.getCuentaContable());
        if (notificacion.getNombreCuenta() != null) existingNotificacion.setNombreCuenta(notificacion.getNombreCuenta());
        if (notificacion.getBienesConstatados() != null) existingNotificacion.setBienesConstatados(notificacion.getBienesConstatados());
        if (notificacion.getBienesNoIdentificados() != null) existingNotificacion.setBienesNoIdentificados(notificacion.getBienesNoIdentificados());
        if (notificacion.getTotal() != null) existingNotificacion.setTotal(notificacion.getTotal());
        if (notificacion.getSistemaControl() != null) existingNotificacion.setSistemaControl(notificacion.getSistemaControl());
        if (notificacion.getCantidadOlympo2015() != null) existingNotificacion.setCantidadOlympo2015(notificacion.getCantidadOlympo2015());
        if (notificacion.getCantidadOlympoSenescyt() != null) existingNotificacion.setCantidadOlympoSenescyt(notificacion.getCantidadOlympoSenescyt());
        if (notificacion.getCantidadOtroSistema() != null) existingNotificacion.setCantidadOtroSistema(notificacion.getCantidadOtroSistema());
        if (notificacion.getTotalSistema() != null) existingNotificacion.setTotalSistema(notificacion.getTotalSistema());
        if (notificacion.getTipoNotificacion() != null) existingNotificacion.setTipoNotificacion(notificacion.getTipoNotificacion());
        if (notificacion.getLeida() != null) existingNotificacion.setLeida(notificacion.getLeida());
        
        return notificacionesRepository.save(existingNotificacion);
    }
    
    public Notificaciones marcarComoLeida(Long id) {
        Notificaciones notificacion = notificacionesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notificación no encontrada"));
        notificacion.setLeida(true);
        return notificacionesRepository.save(notificacion);
    }
    
    public void deleteById(Long id) {
        if (!notificacionesRepository.existsById(id)) {
            throw new RuntimeException("Notificación no encontrada");
        }
        notificacionesRepository.deleteById(id);
    }
}

