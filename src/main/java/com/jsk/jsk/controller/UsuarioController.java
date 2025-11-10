package com.jsk.jsk.controller;

import java.util.List;
import org.springframework.web.bind.annotation.*;


import com.jsk.jsk.entity.*;

import com.jsk.jsk.service.UsuarioService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/usuarios")
@RequiredArgsConstructor

public class UsuarioController {
    
    private final UsuarioService usuarioService;
    
    @PostMapping("/coordinador")
    public Coordinador crearCoordinador(@RequestBody Coordinador nuevo, @RequestParam Long creadorId){
        Usuario creador = usuarioService.buscarPorId(creadorId);
        if (!(creador instanceof Admin)) {
            throw new RuntimeException("Solo admin puede crear coordinadores");
        }
        return usuarioService.crearCoordinador(creador, nuevo);
    }

    @PostMapping("/impulsador")
    public Impulsador crearImpulsador(@RequestBody Impulsador nuevo, @RequestParam Long creadorId){
        Usuario creador = usuarioService.buscarPorId(creadorId);
        return usuarioService.crearImpulsador(creador, nuevo);
    }
    
    @PostMapping("/vendedor")
    public Vendedor crearVendedor(@RequestBody Vendedor nuevo, @RequestParam Long creadorId) {
        Usuario creador = usuarioService.buscarPorId(creadorId);
        return usuarioService.crearVendedor(creador, nuevo);
    }
    @GetMapping("/coordinadores")
    public List<Usuario> getCoordinadores(){
        return usuarioService.listarCoordinadores();
    }

    @GetMapping("/impulsadores")
    public List<Usuario> getImpulsadores(@RequestParam Long coordId){
        return usuarioService.listarImpulsadoresDeCoordinador(coordId);
    }

    @GetMapping("/vendedores")
    public List<Usuario> getVendedores(@RequestParam Long coordId){
        return usuarioService.listarVendedoresDeCoordinador(coordId);
    }

}
