package com.jsk.jsk.entity;

import com.jsk.jsk.entity.enums.Empresa;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EmpresaTest {

    @Test
    @DisplayName("Debe formatear nombres legibles correctamente reemplazando guiones bajos y con mayúscula inicial")
    void deberiaRetornarNombreLegibleCorrecto() {
        assertEquals("Jsk", Empresa.JSK.getNombreLegible());
        assertEquals("Coca cola", Empresa.COCA_COLA.getNombreLegible());
        assertEquals("Nestle", Empresa.NESTLE.getNombreLegible());
    }
}
