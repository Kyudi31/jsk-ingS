package com.jsk.jsk.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ObjetivoTest {

    @Test
    @DisplayName("Debe calcular correctamente el porcentaje por cantidad de ventas")
    void deberiaCalcularPorcentajeCorrectamente_CuandoEsPorCantidad() {
        // Arrange
        Objetivo objetivo = new Objetivo();
        objetivo.setTipo(Objetivo.TipoObjetivo.VENTAS_CANTIDAD);
        objetivo.setMetaVentas(0);
        objetivo.setProgresoVentas(40);

        // Act
        Double porcentaje = objetivo.getPorcentajeCompletado();

        // Assert
        assertEquals(50.0, porcentaje, 0.001);
    }

    @Test
    @DisplayName("Debe calcular correctamente el porcentaje por valor de ventas")
    void deberiaCalcularPorcentajeCorrectamente_CuandoEsPorValor() {
        // Arrange
        Objetivo objetivo = new Objetivo();
        objetivo.setTipo(Objetivo.TipoObjetivo.VENTAS_VALOR);
        objetivo.setMetaValor(null);
        objetivo.setProgresoValor(500.0);

        // Act
        Double porcentaje = objetivo.getPorcentajeCompletado();

        // Assert
        assertEquals(25.0, porcentaje, 0.001);
    }

    @Test
    @DisplayName("Debe retornar 0.0 si la meta es cero o nula para evitar división por cero")
    void deberiaRetornarCero_CuandoMetaVentasEsNulaOCero() {
        // Arrange
        Objetivo objetivo = new Objetivo();
        objetivo.setTipo(Objetivo.TipoObjetivo.VENTAS_CANTIDAD);
        objetivo.setMetaVentas(0);
        objetivo.setProgresoVentas(10);

        // Act & Assert
        assertEquals(0.0, objetivo.getPorcentajeCompletado());
    }

    @Test
    @DisplayName("Debe indicar que está vencido si la fecha de fin es anterior a la fecha actual")
    void deberiaIndicarQueEstaVencido_CuandoFechaFinEsAnteriorAHoy() {
        // Arrange
        Objetivo objetivo = new Objetivo();
        objetivo.setFechaFin(LocalDate.now().minusDays(1));

        // Act & Assert
        assertTrue(objetivo.estaVencido());
    }

    @Test
    @DisplayName("No debe indicar vencido si la fecha de fin es futura")
    void noDeberiaEstarVencido_CuandoFechaFinEsFutura() {
        // Arrange
        Objetivo objetivo = new Objetivo();
        objetivo.setFechaFin(LocalDate.now().plusDays(5));

        // Act & Assert
        assertFalse(objetivo.estaVencido());
    }
}
