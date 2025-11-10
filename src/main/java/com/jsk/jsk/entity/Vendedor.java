package com.jsk.jsk.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@Entity
@Table(name = "vendedores")
public class Vendedor extends Usuario {
    private String ruta;
    @ManyToOne
    @JoinColumn(name = "impulsador_id")
    private Impulsador impulsador;
    
}
