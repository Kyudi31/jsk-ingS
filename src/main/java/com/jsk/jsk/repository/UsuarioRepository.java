package com.jsk.jsk.repository;

import com.jsk.jsk.entity.Coordinador;
import com.jsk.jsk.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    List<Usuario> findByRol_Nombre(String rol);
    List<Usuario> findByCreatedBy_Id(Long id);
    Optional<Usuario> findByNombre(String nombre);
    Optional<Usuario> findByDocumentoIdentidad(String documentoIdentidad);
    List<Usuario> findByActivoTrue();
    @Query("SELECT c FROM Coordinador c")
    List<Coordinador> findAllCoordinadores();
}
