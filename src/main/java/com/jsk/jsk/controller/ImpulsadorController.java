package com.jsk.jsk.controller;

import com.jsk.jsk.entity.Impulsador;
import com.jsk.jsk.entity.Objetivo;
import com.jsk.jsk.entity.Ruta;
import com.jsk.jsk.entity.Usuario;
import com.jsk.jsk.entity.Vendedor;
import com.jsk.jsk.service.ObjetivoService;
import com.jsk.jsk.service.RutaService;
import com.jsk.jsk.service.UsuarioService;
import com.jsk.jsk.entity.enums.Empresa;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.jsk.jsk.security.CustomUserDetails;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/impulsador")
public class ImpulsadorController {

    private final UsuarioService usuarioService;
    private final RutaService rutaService;
    private final ObjetivoService objetivoService;
    
    @GetMapping("/vendedores")
    public String listarVendedores(Model model, @AuthenticationPrincipal CustomUserDetails user) {

        Long impulsadorId = user.getUsuario().getId();
        List<Usuario> vendedores = usuarioService.listarVendedoresDeImpulsador(impulsadorId);

        model.addAttribute("vendedores", vendedores);
        return "impulsador/vendedores";
    }

    @GetMapping("/vendedores/nuevo")
    public String formNuevoVendedor(Model model) {
        model.addAttribute("vendedor", new Vendedor());
        model.addAttribute("empresas", Empresa.values());
        return "impulsador/nuevo-vendedor";
    }

    @PostMapping("/vendedores/guardar")
    public String guardarVendedor(Vendedor nuevo, @AuthenticationPrincipal CustomUserDetails user) {
        usuarioService.crearVendedor(user.getUsuario(), nuevo);
        return "redirect:/impulsador/vendedores";
    }

    @GetMapping("/vendedores/activar/{id}")
    public String activarVendedor(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails user) {
        usuarioService.cambiarEstadoUsuario(id, user.getUsuario().getId(), true);
        return "redirect:/impulsador/vendedores";
    }

    @GetMapping("/vendedores/desactivar/{id}")
    public String desactivarVendedor(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails user) {
        usuarioService.cambiarEstadoUsuario(id, user.getUsuario().getId(), false);
        return "redirect:/impulsador/vendedores";
    }

    // 🆕 MÉTODOS RÁPIDOS
    @GetMapping("/objetivos")
    public String verObjetivos() {  
    return "impulsador/objetivos";
    }

    @GetMapping("/ventas")
    public String verVentas() {
    return "impulsador/ventas";
    }

    @GetMapping("/supervision-rutas")
    public String supervisionRutas(Model model, @AuthenticationPrincipal CustomUserDetails user) {
        Impulsador impulsador = (Impulsador) user.getUsuario();
        
        List<Ruta> rutasDeMiEquipo = rutaService.obtenerRutasPorImpulsador(impulsador.getId());
        List<Usuario> misVendedores = usuarioService.listarVendedoresDeImpulsador(impulsador.getId());
        
        model.addAttribute("impulsador", impulsador);
        model.addAttribute("rutasDeMiEquipo", rutasDeMiEquipo);
        model.addAttribute("misVendedores", misVendedores);
        
        return "impulsador/supervision-rutas";
    }

    @GetMapping("/impulsador/rutas")
    public String gestionarRutasImpulsador(Model model, @AuthenticationPrincipal CustomUserDetails user) {
        Impulsador impulsador = (Impulsador) user.getUsuario();
        
        List<Ruta> rutasDisponibles = rutaService.obtenerRutasDisponibles();
        List<Ruta> misRutasAsignadas = rutaService.obtenerRutasPorImpulsador(impulsador.getId());
        List<Usuario> misVendedores = usuarioService.listarVendedoresDeImpulsador(impulsador.getId());
        
        model.addAttribute("impulsador", impulsador);
        model.addAttribute("rutasDisponibles", rutasDisponibles);
        model.addAttribute("misRutasAsignadas", misRutasAsignadas);
        model.addAttribute("vendedores", misVendedores);
        
        return "impulsador/gestion-rutas";
    }

    @PostMapping("/impulsador/rutas/asignar")
    public String asignarRutaAVendedor(@RequestParam Long rutaId, @RequestParam Long vendedorId, @AuthenticationPrincipal CustomUserDetails user) {
    Impulsador impulsador = (Impulsador) user.getUsuario();
    rutaService.asignarRutaAVendedor(rutaId, impulsador, vendedorId);
    return "redirect:/impulsador/rutas?asigned=true";
    }

    @GetMapping("/impulsador/objetivos")
    public String verObjetivos(Model model, @AuthenticationPrincipal CustomUserDetails user) {
    Impulsador impulsador = (Impulsador) user.getUsuario();
    List<Objetivo> misObjetivos = objetivoService.obtenerObjetivosPorImpulsador(impulsador.getId());
    
    // Obtener objetivos de mis vendedores - CORREGIDO
    List<Usuario> misVendedores = usuarioService.listarVendedoresDeImpulsador(impulsador.getId());
    List<Objetivo> objetivosVendedores = misVendedores.stream()
            .map(v -> objetivoService.obtenerObjetivosPorVendedor(v.getId()))
            .flatMap(List::stream)
            .collect(Collectors.toList()); // Usar collect en lugar de toList()
    
    model.addAttribute("impulsador", impulsador);
    model.addAttribute("misObjetivos", misObjetivos);
    model.addAttribute("objetivosVendedores", objetivosVendedores);
    
    return "impulsador/objetivos";
    }
}
