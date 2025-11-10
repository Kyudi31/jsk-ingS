package com.jsk.jsk.repository;

import com.jsk.jsk.entity.Ruta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RutaRepository extends JpaRepository<Ruta, Long> {
    
    // Encontrar rutas por coordinador
    List<Ruta> findByCoordinadorId(Long coordinadorId);
    
    // Encontrar rutas disponibles (sin vendedor asignado)
    List<Ruta> findByVendedorIsNull();

    // 🆕 AGREGAR ESTE MÉTODO
    List<Ruta> findByVendedorIsNullAndZona(String zona);
    
    // Encontrar rutas por impulsador
    List<Ruta> findByImpulsadorId(Long impulsadorId);
    
    // Encontrar rutas por vendedor
    List<Ruta> findByVendedorId(Long vendedorId);
    
    // Encontrar rutas por estado
    List<Ruta> findByEstado(Ruta.EstadoRuta estado);
}