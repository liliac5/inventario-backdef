package com.yavirac.inventario_backend.repository;

import java.util.List;

import com.yavirac.inventario_backend.entity.Asignacion;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AsignacionRepository extends JpaRepository<Asignacion, Long> {
        List<Asignacion> findByUsuarioIdUsuarioAndEstadoTrue(Long idUsuario);

}

