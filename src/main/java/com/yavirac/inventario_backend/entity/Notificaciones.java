package com.yavirac.inventario_backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "notificaciones")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Notificaciones {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_notificacion")
    private Long idNotificacion;
    
    // Datos del RESUMEN - Primera tabla (Resumen por Cuenta Contable)
    @Column(name = "cuenta_contable", length = 50)
    private String cuentaContable; // Ej: "141.01.03"
    
    @Column(name = "nombre_cuenta", length = 200)
    private String nombreCuenta; // Ej: "Mobiliarios"
    
    @Column(name = "bienes_constatados")
    private Integer bienesConstatados;
    
    @Column(name = "bienes_no_identificados")
    private Integer bienesNoIdentificados;
    
    @Column(name = "total")
    private Integer total;
    
    // Datos del RESUMEN - Segunda tabla (Resumen por Sistema de Control)
    @Column(name = "sistema_control", length = 50)
    private String sistemaControl; // Ej: "OLYMPO 2015", "OLYMPO SENESCYT", "eByE"
    
    @Column(name = "cantidad_olympo_2015")
    private Integer cantidadOlympo2015;
    
    @Column(name = "cantidad_olympo_senescyt")
    private Integer cantidadOlympoSenescyt;
    
    @Column(name = "cantidad_otro_sistema")
    private Integer cantidadOtroSistema;
    
    @Column(name = "total_sistema")
    private Integer totalSistema;
    
    @Column(name = "tipo_notificacion", length = 50)
    private String tipoNotificacion; // "RESUMEN_CUENTA" o "RESUMEN_SISTEMA"
    
    @CreationTimestamp
    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;
    
    @Column(name = "leida", nullable = false)
    private Boolean leida = false;
}

