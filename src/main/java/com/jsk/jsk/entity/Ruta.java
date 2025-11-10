package com.jsk.jsk.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ruta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String nombre;
    private String descripcion;
    private String zona;
    
    @Enumerated(EnumType.STRING)
    private EstadoRuta estado = EstadoRuta.DISPONIBLE;
    
    private LocalDateTime fechaCreacion = LocalDateTime.now();
    private LocalDateTime fechaAsignacion;
    
    // Relaciones
    @ManyToOne
    @JoinColumn(name = "coordinador_id")
    private Coordinador coordinador;
    
    @ManyToOne
    @JoinColumn(name = "impulsador_id")
    private Impulsador impulsador;
    
    @ManyToOne
    @JoinColumn(name = "vendedor_id")
    private Vendedor vendedor;

    private Boolean activa = true;
    
    public enum EstadoRuta {
        DISPONIBLE, ASIGNADA, EN_PROGRESO, COMPLETADA
    }
    
    // 🆕 CONSTRUCTOR PERSONALIZADO QUE FALTABA
    public Ruta(String nombre, String descripcion, String zona, Coordinador coordinador) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.zona = zona;
        this.coordinador = coordinador;
        this.estado = EstadoRuta.DISPONIBLE;
        this.fechaCreacion = LocalDateTime.now();
    }
}