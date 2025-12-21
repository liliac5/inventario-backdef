package com.yavirac.inventario_backend.repository;

import com.yavirac.inventario_backend.entity.Bienes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BienesRepository extends JpaRepository<Bienes, Long> {
    Optional<Bienes> findByCodigoBien(String codigoBien);
    boolean existsByCodigoBien(String codigoBien);
}

