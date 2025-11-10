package com.jsk.jsk.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
public class Objetivo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String titulo;
    private String descripcion;
    private Integer metaVentas;
    private Double metaValor;
    private Integer progresoVentas = 0;
    private Double progresoValor = 0.0;
    
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private LocalDateTime fechaCreacion = LocalDateTime.now();
    
    @Enumerated(EnumType.STRING)
    private TipoObjetivo tipo;
    
    @Enumerated(EnumType.STRING) 
    private EstadoObjetivo estado = EstadoObjetivo.ACTIVO;
    
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
    
    public enum TipoObjetivo {
        VENTAS_CANTIDAD, VENTAS_VALOR, NUEVOS_CLIENTES, PRODUCTO_ESPECIFICO
    }
    
    public enum EstadoObjetivo {
        ACTIVO, COMPLETADO, CANCELADO, VENCIDO
    }
    
    // Métodos de utilidad
    public Double getPorcentajeCompletado() {
        if (tipo == TipoObjetivo.VENTAS_CANTIDAD && metaVentas != null && metaVentas > 0) {
            return (progresoVentas.doubleValue() / metaVentas) * 100;
        } else if (tipo == TipoObjetivo.VENTAS_VALOR && metaValor != null && metaValor > 0) {
            return (progresoValor / metaValor) * 100;
        }
        return 0.0;
    }
    
    public boolean estaVencido() {
        return LocalDate.now().isAfter(fechaFin);
    }
}