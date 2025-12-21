package com.yavirac.inventario_backend.repository;

import com.yavirac.inventario_backend.entity.Solicitudes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SolicitudesRepository extends JpaRepository<Solicitudes, Long> {
}

