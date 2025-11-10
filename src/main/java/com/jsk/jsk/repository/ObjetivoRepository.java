package com.jsk.jsk.repository;

import com.jsk.jsk.entity.Objetivo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ObjetivoRepository extends JpaRepository<Objetivo, Long> {
    
    // Objetivos creados por un coordinador
    List<Objetivo> findByCoordinadorId(Long coordinadorId);
    
    // Objetivos asignados a un impulsador
    List<Objetivo> findByImpulsadorId(Long impulsadorId);
    
    // Objetivos asignados a un vendedor
    List<Objetivo> findByVendedorId(Long vendedorId);
    
    // Objetivos activos
    List<Objetivo> findByEstado(Objetivo.EstadoObjetivo estado);
    
    // Objetivos por tipo
    List<Objetivo> findByTipo(Objetivo.TipoObjetivo tipo);
}