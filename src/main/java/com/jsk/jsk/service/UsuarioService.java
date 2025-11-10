package com.jsk.jsk.service;

import org.springframework.stereotype.Service;

import java.util.List;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.jsk.jsk.dtos.CoordinadorRequest;
import com.jsk.jsk.entity.*;
import com.jsk.jsk.repository.*;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
@Service
@RequiredArgsConstructor
@Transactional
public class UsuarioService {
    
    private final UsuarioRepository usuarioRepository;
    private final VendedorRepository vendedorRepository;
    private final ImpulsadorRepository impulsadorRepository;
    private final CoordinadorRepository coordinadorRepository;
    private final RolRepository rolRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public Coordinador crearCoordinador(Usuario creador, Coordinador nuevo){
        if(!creador.getRol().getNombre().equals("ADMIN")){
            throw new RuntimeException("Solo un ADMIN puede crear Coordinadores");
        }
        Rol rolCoord = rolRepository.findByNombre("COORDINADOR")
            .orElseThrow(() -> new RuntimeException("Rol no encontrado"));

        nuevo.setRol(rolCoord);
        nuevo.setPassword(passwordEncoder.encode(nuevo.getPassword()));
        nuevo.setActivo(true);
        return coordinadorRepository.save(nuevo);
        
    }

    public Impulsador crearImpulsador(Usuario creador, Impulsador nuevo){
        if(!creador.getRol().getNombre().equals("COORDINADOR")){
            throw new RuntimeException("Solo un COORDINADOR puede crear Impulsadores");
        }
        
        Rol rol = rolRepository.findByNombre("IMPULSADOR")
        .orElseThrow(() -> new RuntimeException("Rol no encontrado"));
        nuevo.setEmpresa(creador.getEmpresa());
        nuevo.setRol(rol);
        nuevo.setCoordinador((Coordinador) creador);
        nuevo.setPassword(passwordEncoder.encode(nuevo.getPassword()));
        nuevo.setCreatedBy(creador);
        return impulsadorRepository.save(nuevo);
    }

    public Vendedor crearVendedor(Usuario creador, Vendedor nuevo){
        if(!creador.getRol().getNombre().equals("IMPULSADOR")){
            throw new RuntimeException("Solo un IMPULSADOR puede crear Vendedores");
        }
        
        nuevo.setImpulsador((Impulsador) creador);
        nuevo.setRol(rolRepository.findByNombre("VENDEDOR")
        .orElseThrow(() -> new RuntimeException("Rol VENDEDOR no encontrado")));
        nuevo.setCreatedBy(creador);
        nuevo.setActivo(true);
        return usuarioRepository.save(nuevo);
    }
   
    public Usuario buscarPorId(Long id) {
        return usuarioRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }
    
    public Coordinador crearCoordinadorDesdeDTO(Usuario creador, CoordinadorRequest dto) {
    
    Coordinador nuevo = new Coordinador();

    nuevo.setNombre(dto.getNombre());
    nuevo.setPassword(passwordEncoder.encode(dto.getPassword())); 
    nuevo.setEmpresa(dto.getEmpresa());
    nuevo.setActivo(true);

    return crearCoordinador(creador, nuevo);
    }
    // en UsuarioService.java

    @Transactional
    public void cambiarEstadoUsuario(Long userId, Long coordId, boolean estado) {
        Usuario u = usuarioRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Seguridad: el coordinador solo puede modificar sus propios impulsadores
        if (u.getCreatedBy() == null || !u.getCreatedBy().getId().equals(coordId)) {
            throw new RuntimeException("No autorizado para modificar este usuario");
        }

        u.setActivo(estado);
        usuarioRepository.save(u);
    }

    // --- NUEVO: sobrecarga para Admin (sin coordId) ---
    @Transactional
    public void cambiarEstadoUsuario(Long userId, boolean estado) {
        Usuario u = usuarioRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Aquí podrías agregar comprobaciones extra si quieres (ej. rol ADMIN)
        u.setActivo(estado);
        usuarioRepository.save(u);
}


    public List<Usuario> listarCoordinadores(){
        List<Coordinador> coordinadores = usuarioRepository.findAllCoordinadores();
        return coordinadores.stream()
                .map(coord -> (Usuario) coord)
                .toList();
    }   
    
    public List<Usuario> listarImpulsadoresDeCoordinador(Long coordId){
    return usuarioRepository.findByCreatedBy_Id(coordId)
            .stream()
            .filter(u -> u.getRol().getNombre().equals("IMPULSADOR"))
            .toList();
    } 
    
    public List<Usuario> listarVendedoresDeCoordinador(Long coordId){
        return usuarioRepository.findByCreatedBy_Id(coordId)
                .stream()
                .filter(u -> u.getRol().getNombre().equals("VENDEDOR"))
                .toList();
    }
    public List<Usuario> listarVendedoresDeImpulsador(Long impulsadorId) {
    return usuarioRepository.findByCreatedBy_Id(impulsadorId)
            .stream()
            .filter(u -> u.getRol().getNombre().equals("VENDEDOR"))
            .toList();
    }
    
    public Usuario buscarPorDocumentoIdentidad(String documentoIdentidad) {
    return usuarioRepository.findByDocumentoIdentidad(documentoIdentidad)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

}

