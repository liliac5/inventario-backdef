package com.yavirac.inventario_backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "solicitudes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Solicitudes {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_detalle")
    private Long idDetalle;
    
    @Column(name = "id_solicitud", nullable = false)
    private Long idSolicitud;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_bien", nullable = false)
    private Bienes bien;
    
    @Column(name = "estado", length = 50)
    private String estado;
}

