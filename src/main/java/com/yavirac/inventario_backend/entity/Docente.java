package com.yavirac.inventario_backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "docentes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Docente {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_docente")
    private Long idDocente;
    
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;
    
    @Column(name = "cedula", length = 10, unique = true, nullable = false)
    private String cedula;
    
    @Column(name = "nombres", length = 100, nullable = false)
    private String nombres;
    
    @Column(name = "apellidos", length = 100, nullable = false)
    private String apellidos;
    
    @Column(name = "regimen", length = 50)
    private String regimen;
    
    @Column(name = "observaciones", columnDefinition = "TEXT")
    private String observaciones;
}

