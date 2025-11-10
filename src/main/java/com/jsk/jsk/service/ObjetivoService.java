package com.jsk.jsk.service;

import com.jsk.jsk.entity.*;
import com.jsk.jsk.repository.ObjetivoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ObjetivoService {
    
    private final ObjetivoRepository objetivoRepository;
    private final UsuarioService usuarioService;
    
    // Crear objetivo para impulsador
    public Objetivo crearObjetivoImpulsador(Coordinador coordinador, Impulsador impulsador, 
                                          String titulo, String descripcion, Objetivo.TipoObjetivo tipo,
                                          Integer metaVentas, Double metaValor, 
                                          LocalDate fechaInicio, LocalDate fechaFin) {
        Objetivo objetivo = new Objetivo();
        objetivo.setTitulo(titulo);
        objetivo.setDescripcion(descripcion);
        objetivo.setTipo(tipo);
        objetivo.setMetaVentas(metaVentas);
        objetivo.setMetaValor(metaValor);
        objetivo.setFechaInicio(fechaInicio);
        objetivo.setFechaFin(fechaFin);
        objetivo.setCoordinador(coordinador);
        objetivo.setImpulsador(impulsador);
        
        return objetivoRepository.save(objetivo);
    }
    
    // Crear objetivo para vendedor
    public Objetivo crearObjetivoVendedor(Coordinador coordinador, Vendedor vendedor,
                                        String titulo, String descripcion, Objetivo.TipoObjetivo tipo,
                                        Integer metaVentas, Double metaValor,
                                        LocalDate fechaInicio, LocalDate fechaFin) {
        Objetivo objetivo = new Objetivo();
        objetivo.setTitulo(titulo);
        objetivo.setDescripcion(descripcion);
        objetivo.setTipo(tipo);
        objetivo.setMetaVentas(metaVentas);
        objetivo.setMetaValor(metaValor);
        objetivo.setFechaInicio(fechaInicio);
        objetivo.setFechaFin(fechaFin);
        objetivo.setCoordinador(coordinador);
        objetivo.setVendedor(vendedor);
        objetivo.setImpulsador(vendedor.getImpulsador()); // El impulsador del vendedor
        
        return objetivoRepository.save(objetivo);
    }
    
    // Obtener objetivos por coordinador
    public List<Objetivo> obtenerObjetivosPorCoordinador(Long coordinadorId) {
        return objetivoRepository.findByCoordinadorId(coordinadorId);
    }
    
    // Obtener objetivos por impulsador
    public List<Objetivo> obtenerObjetivosPorImpulsador(Long impulsadorId) {
        return objetivoRepository.findByImpulsadorId(impulsadorId);
    }
    
    // Obtener objetivos por vendedor
    public List<Objetivo> obtenerObjetivosPorVendedor(Long vendedorId) {
        return objetivoRepository.findByVendedorId(vendedorId);
    }
    
    // Actualizar progreso de objetivo
    public Objetivo actualizarProgreso(Long objetivoId, Integer ventasAdicionales, Double valorAdicional) {
        Objetivo objetivo = objetivoRepository.findById(objetivoId)
                .orElseThrow(() -> new RuntimeException("Objetivo no encontrado"));
        
        if (ventasAdicionales != null) {
            objetivo.setProgresoVentas(objetivo.getProgresoVentas() + ventasAdicionales);
        }
        
        if (valorAdicional != null) {
            objetivo.setProgresoValor(objetivo.getProgresoValor() + valorAdicional);
        }
        
        // Verificar si se completó el objetivo
        if (objetivo.getPorcentajeCompletado() >= 100) {
            objetivo.setEstado(Objetivo.EstadoObjetivo.COMPLETADO);
        }
        
        return objetivoRepository.save(objetivo);
    }
    
    // Eliminar objetivo
    public void eliminarObjetivo(Long objetivoId) {
        objetivoRepository.deleteById(objetivoId);
    }
}