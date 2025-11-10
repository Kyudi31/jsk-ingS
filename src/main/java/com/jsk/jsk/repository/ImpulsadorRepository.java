package com.jsk.jsk.repository;

import com.jsk.jsk.entity.Impulsador;
import com.jsk.jsk.entity.enums.Empresa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ImpulsadorRepository extends JpaRepository<Impulsador, Long> {
    List<Impulsador> findByEmpresa(Empresa empresa);

}
