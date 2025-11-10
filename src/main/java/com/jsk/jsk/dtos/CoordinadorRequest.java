package com.jsk.jsk.dtos;
import com.jsk.jsk.entity.enums.*;

import lombok.Data;;
@Data
public class CoordinadorRequest {
    private String nombre;
    private String password;
    private Empresa empresa;
}
