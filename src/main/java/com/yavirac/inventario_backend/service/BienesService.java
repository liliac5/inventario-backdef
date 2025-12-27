package com.yavirac.inventario_backend.service;

import com.yavirac.inventario_backend.entity.Bienes;
import com.yavirac.inventario_backend.entity.Categoria;
import com.yavirac.inventario_backend.entity.Aula;
import com.yavirac.inventario_backend.repository.BienesRepository;
import com.yavirac.inventario_backend.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.yavirac.inventario_backend.repository.AulaRepository;

import java.util.List;
import java.util.Optional;

@Service
public class BienesService {
    
    @Autowired
    private BienesRepository bienesRepository;
    
    @Autowired
    private AulaRepository aulaRepository;


    @Autowired
    private CategoriaRepository categoriaRepository;
    
    public List<Bienes> findAll() {
        return bienesRepository.findAll();
    }
    
    public Optional<Bienes> findById(Long id) {
        return bienesRepository.findById(id);
    }
    
    public Optional<Bienes> findByCodigoBien(String codigoBien) {
        return bienesRepository.findByCodigoBien(codigoBien);
    }
    
    public Bienes save(Bienes bienes) {
        if (bienes.getCodigoBien() != null && 
            bienesRepository.existsByCodigoBien(bienes.getCodigoBien())) {
            throw new RuntimeException("El código de bien ya está registrado");
        }
        
        if (bienes.getCategoria() != null && bienes.getCategoria().getIdCategoria() != null) {
            Categoria categoria = categoriaRepository.findById(bienes.getCategoria().getIdCategoria())
                    .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
            bienes.setCategoria(categoria);
        }

        if (bienes.getAula() != null && bienes.getAula().getIdAula() != null) {
    Aula aula = aulaRepository.findById(bienes.getAula().getIdAula())
            .orElseThrow(() -> new RuntimeException("Aula no encontrada"));
    bienes.setAula(aula);
}

        
        return bienesRepository.save(bienes);
    }
    
    public Bienes update(Long id, Bienes bienes) {
        Bienes existingBienes = bienesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bien no encontrado"));
        
        if (bienes.getCodigoBien() != null && 
            !existingBienes.getCodigoBien().equals(bienes.getCodigoBien()) &&
            bienesRepository.existsByCodigoBien(bienes.getCodigoBien())) {
            throw new RuntimeException("El código de bien ya está registrado");
        }
        
        // Actualizar campos
        if (bienes.getCodigoBien() != null) existingBienes.setCodigoBien(bienes.getCodigoBien());
        if (bienes.getCuentaTipoBien() != null) existingBienes.setCuentaTipoBien(bienes.getCuentaTipoBien());
        if (bienes.getTipoBien() != null) existingBienes.setTipoBien(bienes.getTipoBien());
        if (bienes.getClaseBien() != null) existingBienes.setClaseBien(bienes.getClaseBien());
        if (bienes.getDescripcion() != null) existingBienes.setDescripcion(bienes.getDescripcion());
        if (bienes.getMarca() != null) existingBienes.setMarca(bienes.getMarca());
        if (bienes.getModelo() != null) existingBienes.setModelo(bienes.getModelo());
        if (bienes.getSerie() != null) existingBienes.setSerie(bienes.getSerie());
        if (bienes.getValorConIva() != null) existingBienes.setValorConIva(bienes.getValorConIva());
        if (bienes.getEstado() != null) existingBienes.setEstado(bienes.getEstado());
        if (bienes.getCustodio() != null) existingBienes.setCustodio(bienes.getCustodio());
        if (bienes.getUbicacion() != null) existingBienes.setUbicacion(bienes.getUbicacion());
        if (bienes.getProvincia() != null) existingBienes.setProvincia(bienes.getProvincia());
        if (bienes.getObservaciones() != null) existingBienes.setObservaciones(bienes.getObservaciones());
        if (bienes.getObservaciones2() != null) existingBienes.setObservaciones2(bienes.getObservaciones2());
        if (bienes.getCodigoSecap() != null) existingBienes.setCodigoSecap(bienes.getCodigoSecap());
        if (bienes.getCodigoInventario() != null) existingBienes.setCodigoInventario(bienes.getCodigoInventario());
        if (bienes.getNombreBien() != null) existingBienes.setNombreBien(bienes.getNombreBien());
        if (bienes.getEspecificaciones() != null) existingBienes.setEspecificaciones(bienes.getEspecificaciones());
        if (bienes.getValorCompraInicial() != null) existingBienes.setValorCompraInicial(bienes.getValorCompraInicial());
        if (bienes.getDetalleEstado() != null) existingBienes.setDetalleEstado(bienes.getDetalleEstado());
        if (bienes.getOrigen() != null) existingBienes.setOrigen(bienes.getOrigen());
        
        if (bienes.getCategoria() != null && bienes.getCategoria().getIdCategoria() != null) {
            Categoria categoria = categoriaRepository.findById(bienes.getCategoria().getIdCategoria())
                    .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
            existingBienes.setCategoria(categoria);
        }
        if (bienes.getAula() != null && bienes.getAula().getIdAula() != null) {
        Aula aula = aulaRepository.findById(bienes.getAula().getIdAula())
            .orElseThrow(() -> new RuntimeException("Aula no encontrada"));
           existingBienes.setAula(aula);
}

        
        return bienesRepository.save(existingBienes);
    }
    
    public void deleteById(Long id) {
        if (!bienesRepository.existsById(id)) {
            throw new RuntimeException("Bien no encontrado");
        }
        bienesRepository.deleteById(id);
    }
}

