package com.yavirac.inventario_backend.repository;

import com.yavirac.inventario_backend.entity.Notificaciones;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificacionesRepository extends JpaRepository<Notificaciones, Long> {
    List<Notificaciones> findByLeidaFalse();
    List<Notificaciones> findByTipoNotificacion(String tipoNotificacion);
}

