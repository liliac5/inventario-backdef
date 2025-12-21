package com.yavirac.inventario_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocenteRequest {
    private Long idUsuario;
    private String cedula;
    private String nombres;
    private String apellidos;
    private String regimen;
    private String observaciones;
}

