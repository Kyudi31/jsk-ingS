package com.jsk.jsk.service;

import com.jsk.jsk.entity.Objetivo;
import com.jsk.jsk.repository.ObjetivoRepository;
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
class ObjetivoServiceTest {

    @Mock
    private ObjetivoRepository objetivoRepository;

    @Mock
    private UsuarioService usuarioService;

    @InjectMocks
    private ObjetivoService objetivoService;

    @Test
    @DisplayName("Debe marcar el objetivo como COMPLETADO cuando el progreso alcanza o supera el 100%")
    void deberiaCompletarObjetivo_CuandoAlcanzaMeta() {
        // Arrange
        Long objetivoId = 1L;
        Objetivo objetivo = new Objetivo();
        objetivo.setId(objetivoId);
        objetivo.setTipo(Objetivo.TipoObjetivo.VENTAS_CANTIDAD);
        objetivo.setMetaVentas(10);
        objetivo.setProgresoVentas(8); // Faltan 2 para el 100%
        objetivo.setEstado(Objetivo.EstadoObjetivo.ACTIVO);

        when(objetivoRepository.findById(objetivoId)).thenReturn(Optional.of(objetivo));
        when(objetivoRepository.save(any(Objetivo.class))).thenAnswer(i -> i.getArgument(0));

        // Act: sumamos 3 ventas adicionales (8 + 3 = 11 >= 10 meta)
        Objetivo resultado = objetivoService.actualizarProgreso(objetivoId, 3, null);

        // Assert
        assertEquals(11, resultado.getProgresoVentas());
        assertEquals(Objetivo.EstadoObjetivo.COMPLETADO, resultado.getEstado());
        verify(objetivoRepository, times(1)).save(objetivo);
    }

    @Test
    @DisplayName("Debe mantener el estado ACTIVO cuando el progreso aún no alcanza el 100%")
    void deberiaMantenerEstadoActivo_CuandoNoAlcanzaMeta() {
        // Arrange
        Long objetivoId = 1L;
        Objetivo objetivo = new Objetivo();
        objetivo.setId(objetivoId);
        objetivo.setTipo(Objetivo.TipoObjetivo.VENTAS_CANTIDAD);
        objetivo.setMetaVentas(100);
        objetivo.setProgresoVentas(20);
        objetivo.setEstado(Objetivo.EstadoObjetivo.ACTIVO);

        when(objetivoRepository.findById(objetivoId)).thenReturn(Optional.of(objetivo));
        when(objetivoRepository.save(any(Objetivo.class))).thenAnswer(i -> i.getArgument(0));

        // Act: sumamos 30 ventas (20 + 30 = 50 < 100)
        Objetivo resultado = objetivoService.actualizarProgreso(objetivoId, 30, null);

        // Assert
        assertEquals(50, resultado.getProgresoVentas());
        assertEquals(Objetivo.EstadoObjetivo.ACTIVO, resultado.getEstado());
    }

    @Test
    @DisplayName("Debe lanzar excepción si el objetivo a actualizar no existe")
    void deberiaLanzarExcepcion_CuandoObjetivoNoExiste() {
        // Arrange
        when(objetivoRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            objetivoService.actualizarProgreso(99L, 5, 100.0);
        });

        assertEquals("Objetivo no encontrado", exception.getMessage());
    }
}
