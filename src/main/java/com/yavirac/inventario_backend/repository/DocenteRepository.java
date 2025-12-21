package com.yavirac.inventario_backend.repository;

import com.yavirac.inventario_backend.entity.Docente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DocenteRepository extends JpaRepository<Docente, Long> {
    Optional<Docente> findByCedula(String cedula);
    boolean existsByCedula(String cedula);
}

