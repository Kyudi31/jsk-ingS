package com.jsk.jsk.service;

import com.jsk.jsk.entity.*;
import com.jsk.jsk.repository.RutaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RutaService {
    
    private final RutaRepository rutaRepository;
    private final UsuarioService usuarioService;
    
    public Ruta crearRuta(Coordinador coordinador, String nombre, String descripcion, String zona) {
    Ruta ruta = new Ruta(nombre, descripcion, zona, coordinador);
    return rutaRepository.save(ruta);
    }
    
    // Asignar ruta a vendedor (Impulsador)
    public Ruta asignarRutaAVendedor(Long rutaId, Impulsador impulsador, Long vendedorId) {
        Ruta ruta = rutaRepository.findById(rutaId)
                .orElseThrow(() -> new RuntimeException("Ruta no encontrada"));
        
        Vendedor vendedor = (Vendedor) usuarioService.buscarPorId(vendedorId);
        
        ruta.setImpulsador(impulsador);
        ruta.setVendedor(vendedor);
        ruta.setEstado(Ruta.EstadoRuta.ASIGNADA);
        
        return rutaRepository.save(ruta);
    }

    public Ruta seleccionarRuta(Long rutaId, Vendedor vendedor, Impulsador impulsador) {
    Ruta ruta = rutaRepository.findById(rutaId)
            .orElseThrow(() -> new RuntimeException("Ruta no encontrada"));
    
    // Verificar que la ruta esté disponible
    if (ruta.getVendedor() != null) {
        throw new RuntimeException("Esta ruta ya está asignada");
    }
    
    ruta.setVendedor(vendedor);
    ruta.setImpulsador(impulsador); // El impulsador del vendedor
    ruta.setEstado(Ruta.EstadoRuta.ASIGNADA);
    ruta.setFechaAsignacion(LocalDateTime.now());
    
    return rutaRepository.save(ruta);
    }

// 🆕 OBTENER RUTAS DISPONIBLES POR ZONA
    public List<Ruta> obtenerRutasDisponiblesPorZona(String zona) {
        return rutaRepository.findByVendedorIsNullAndZona(zona);
    }
    
    // Obtener rutas por coordinador
    public List<Ruta> obtenerRutasPorCoordinador(Long coordinadorId) {
        return rutaRepository.findByCoordinadorId(coordinadorId);
    }
    
    // Obtener rutas disponibles (sin asignar)
    public List<Ruta> obtenerRutasDisponibles() {
        return rutaRepository.findByVendedorIsNull();
    }
    
    // Obtener rutas por impulsador
    public List<Ruta> obtenerRutasPorImpulsador(Long impulsadorId) {
        return rutaRepository.findByImpulsadorId(impulsadorId);
    }
    
    // Obtener rutas por vendedor
    public List<Ruta> obtenerRutasPorVendedor(Long vendedorId) {
        return rutaRepository.findByVendedorId(vendedorId);
    }
    
    // Eliminar ruta
    public void eliminarRuta(Long rutaId) {
        rutaRepository.deleteById(rutaId);
    }
}