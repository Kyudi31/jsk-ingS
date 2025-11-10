package com.jsk.jsk.dtos;

import com.jsk.jsk.entity.enums.Empresa;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class ImpulsadorRequest {
    private String nombre;
    private String password;
    private Empresa empresa;
    private String puntoDeVenta;
}
