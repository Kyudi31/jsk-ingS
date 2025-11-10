package com.jsk.jsk.entity;
import com.jsk.jsk.entity.enums.Empresa;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@Entity
@SuperBuilder
@NoArgsConstructor
@Table(name = "usuarios")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Usuario {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; 
    @Column(length = 100, nullable = false)
    private String nombre;
    @Column(nullable = false)
    private String password;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Empresa empresa;
    @Column(nullable = false)
    private boolean activo;
    @ManyToOne
    @JoinColumn(name = "rol_id", nullable = false)
    private Rol rol;
    @ManyToOne
    @JoinColumn(name = "coordinador_id")
    private Coordinador coordinador;
    @ManyToOne
    @JoinColumn(name = "created_by")
    private Usuario createdBy;
    @Column(unique = true, nullable = false)
    private String documentoIdentidad;
}
