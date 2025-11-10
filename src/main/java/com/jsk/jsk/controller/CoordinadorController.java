package com.jsk.jsk.controller;

import com.jsk.jsk.entity.Coordinador;
import com.jsk.jsk.entity.Impulsador;
import com.jsk.jsk.entity.Objetivo;
import com.jsk.jsk.entity.Ruta;
import com.jsk.jsk.entity.Usuario;
import com.jsk.jsk.entity.Vendedor;
import com.jsk.jsk.entity.enums.Empresa;
import com.jsk.jsk.service.ObjetivoService;
import com.jsk.jsk.service.RutaService;
import com.jsk.jsk.service.UsuarioService;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.jsk.jsk.security.CustomUserDetails;


@Controller
@RequiredArgsConstructor
public class CoordinadorController {

    private final UsuarioService usuarioService;
    private final RutaService rutaService;
    private final ObjetivoService objetivoService;
    
    @GetMapping("/coordinador/impulsadores")
    public String listarImpulsadores(Model model, @AuthenticationPrincipal CustomUserDetails user) {
        Usuario coordinador = user.getUsuario();
        List<Usuario> impulsadores = usuarioService.listarImpulsadoresDeCoordinador(coordinador.getId());
        model.addAttribute("impulsadores", impulsadores);
        return "coordinador/impulsadores";
    }   


    @GetMapping("/coordinador/impulsadores/activar/{id}")
    public String activarImpulsador(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails user) {

    usuarioService.cambiarEstadoUsuario(id, user.getUsuario().getId(), true);
    return "redirect:/coordinador/impulsadores";
    }

    @GetMapping("/coordinador/impulsadores/desactivar/{id}")
    public String desactivarImpulsador(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails user) {

    usuarioService.cambiarEstadoUsuario(id, user.getUsuario().getId(), false);
    return "redirect:/coordinador/impulsadores";
    }

    @GetMapping("/coordinador/impulsadores/nuevo")
    public String formNuevoImpulsador(Model model) {
        model.addAttribute("impulsador", new Impulsador());
        model.addAttribute("empresas", Empresa.values());
        return "coordinador/nuevo-impulsador";
    }

    @PostMapping("/coordinador/impulsadores/guardar")
    public String guardarImpulsador(Impulsador nuevo, @AuthenticationPrincipal CustomUserDetails user) {
        usuarioService.crearImpulsador(user.getUsuario(), nuevo);
        return "redirect:/coordinador/impulsadores";
    }

    @GetMapping("/coordinador/rutas")
    public String gestionarRutas(@AuthenticationPrincipal CustomUserDetails user, Model model) {
        Coordinador coordinador = (Coordinador) user.getUsuario();
        List<Ruta> rutas = rutaService.obtenerRutasPorCoordinador(coordinador.getId());
    
    // DEBUG: Ver qué datos tenemos
        System.out.println("Número de rutas: " + rutas.size());
        for (Ruta ruta : rutas) {
        System.out.println("Ruta: " + ruta.getNombre() + " - Estado: " + ruta.getEstado());
        }
        model.addAttribute("totalRutas", rutas.size());
        model.addAttribute("rutasAsignadas", rutas.stream().filter(r -> r.getVendedor() != null).count());
        model.addAttribute("rutasDisponibles", rutas.stream().filter(r -> r.getVendedor() == null).count());
        model.addAttribute("coordinador", coordinador);
        model.addAttribute("rutas", rutas);
        return "coordinador/rutas";
    }
    
    @GetMapping("/coordinador/rutas/nueva")
    public String formNuevaRuta(Model model, @AuthenticationPrincipal CustomUserDetails user) {
        Coordinador coordinador = (Coordinador) user.getUsuario();
        model.addAttribute("coordinador", coordinador);
        model.addAttribute("ruta", new Ruta()); // Para el formulario
        return "coordinador/nueva-ruta"; // Crearemos este template después
    }
   
    @GetMapping("/coordinador/objetivos")
public String gestionarObjetivos(Model model, @AuthenticationPrincipal CustomUserDetails user) {
    Coordinador coordinador = (Coordinador) user.getUsuario();
    List<Objetivo> objetivos = objetivoService.obtenerObjetivosPorCoordinador(coordinador.getId());
    List<Usuario> impulsadores = usuarioService.listarImpulsadoresDeCoordinador(coordinador.getId());
    
    model.addAttribute("coordinador", coordinador);
    model.addAttribute("objetivos", objetivos);
    model.addAttribute("impulsadores", impulsadores);
    
    return "coordinador/objetivos";
    }

    @GetMapping("/coordinador/objetivos/nuevo")
    public String formNuevoObjetivo(Model model, @AuthenticationPrincipal CustomUserDetails user) {
    Coordinador coordinador = (Coordinador) user.getUsuario();
    List<Usuario> impulsadores = usuarioService.listarImpulsadoresDeCoordinador(coordinador.getId());
    
    model.addAttribute("coordinador", coordinador);
    model.addAttribute("impulsadores", impulsadores);
    return "coordinador/crear-objetivo";
    }

    @PostMapping("/coordinador/objetivos/guardar")
    public String guardarObjetivo(@RequestParam String titulo,
                             @RequestParam Objetivo.TipoObjetivo tipo,
                             @RequestParam Long destinatarioId,
                             @RequestParam(required = false) Integer metaVentas,
                             @RequestParam(required = false) Double metaValor,
                             @RequestParam String fechaInicio,
                             @RequestParam String fechaFin,
                             @RequestParam String descripcion,
                             @AuthenticationPrincipal CustomUserDetails user) {
    
    Coordinador coordinador = (Coordinador) user.getUsuario();
    LocalDate inicio = LocalDate.parse(fechaInicio);
    LocalDate fin = LocalDate.parse(fechaFin);
    
    try {
        // Buscar el impulsador
        Impulsador impulsador = (Impulsador) usuarioService.buscarPorId(destinatarioId);
        
        // Crear el objetivo
        objetivoService.crearObjetivoImpulsador(coordinador, impulsador, titulo, descripcion, 
                                               tipo, metaVentas, metaValor, inicio, fin);
        
        return "redirect:/coordinador/objetivos?created=true";
    } catch (Exception e) {
        e.printStackTrace();
        return "redirect:/coordinador/objetivos/nuevo?error=true";
    }
    }
    @PostMapping("/coordinador/rutas/guardar")
    public String guardarRuta(@RequestParam String nombre,@RequestParam String descripcion, @RequestParam String zona, @AuthenticationPrincipal CustomUserDetails user) {
    Coordinador coordinador = (Coordinador) user.getUsuario();
    rutaService.crearRuta(coordinador, nombre, descripcion, zona);
    return "redirect:/coordinador/rutas?success=true";
    }

    @GetMapping("/coordinador/rutas/eliminar/{id}")
    public String eliminarRuta(@PathVariable Long id) {
    rutaService.eliminarRuta(id);
    return "redirect:/coordinador/rutas?deleted=true";
    }

}