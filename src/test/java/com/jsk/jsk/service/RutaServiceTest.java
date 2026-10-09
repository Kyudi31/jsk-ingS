package com.jsk.jsk.service;

import com.jsk.jsk.entity.*;
import com.jsk.jsk.repository.RutaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RutaServiceTest {

    @Mock
    private RutaRepository rutaRepository;

    @Mock
    private UsuarioService usuarioService;

    @InjectMocks
    private RutaService rutaService;

    @Test
    @DisplayName("Debe permitir seleccionar ruta cuando está disponible")
    void deberiaSeleccionarRutaExitosamente_CuandoEstaDisponible() {
        // Arrange
        Long rutaId = 1L;
        Ruta ruta = new Ruta();
        ruta.setId(rutaId);
        ruta.setVendedor(null); // Disponible

        Vendedor vendedor = new Vendedor();
        Impulsador impulsador = new Impulsador();

        when(rutaRepository.findById(rutaId)).thenReturn(Optional.of(ruta));
        when(rutaRepository.save(any(Ruta.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Ruta resultado = rutaService.seleccionarRuta(rutaId, vendedor, impulsador);

        // Assertl
        assertNotNull(resultado);
        assertEquals(vendedor, resultado.getVendedor());
        assertEquals(impulsador, resultado.getImpulsador());
        assertEquals(Ruta.EstadoRuta.ASIGNADA, resultado.getEstado());
        assertNotNull(resultado.getFechaAsignacion());
        verify(rutaRepository, times(1)).save(ruta);
    }

    @Test
    @DisplayName("Debe lanzar excepción si la ruta ya tiene un vendedor asignado")
    void deberiaLanzarExcepcion_CuandoRutaYaEstaAsignada() {
        // Arrange
        Long rutaId = 1L;
        Ruta rutaExistente = new Ruta();
        rutaExistente.setId(rutaId);
        rutaExistente.setVendedor(new Vendedor()); // Ya asignada

        when(rutaRepository.findById(rutaId)).thenReturn(Optional.of(rutaExistente));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            rutaService.seleccionarRuta(rutaId, new Vendedor(), new Impulsador());
        });

        assertEquals("Esta ruta ya está asignada", exception.getMessage());
        verify(rutaRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debe lanzar excepción si la ruta a asignar no existe")
    void deberiaLanzarExcepcion_CuandoRutaNoExiste() {
        // Arrange
        when(rutaRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            rutaService.seleccionarRuta(99L, new Vendedor(), new Impulsador());
        });

        assertEquals("Ruta no encontrada", exception.getMessage());
    }
}
