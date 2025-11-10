package com.jsk.jsk.controller;

import com.jsk.jsk.entity.Impulsador;
import com.jsk.jsk.entity.Objetivo;
import com.jsk.jsk.entity.Ruta;
import com.jsk.jsk.entity.Vendedor;
import com.jsk.jsk.service.ObjetivoService;
import com.jsk.jsk.service.RutaService;
import com.jsk.jsk.service.UsuarioService;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/vendedor")
@RequiredArgsConstructor
public class VendedorController {

    private final UsuarioService usuarioService;
    private final RutaService rutaService;
    private final ObjetivoService objetivoService;
    // ❌ ELIMINAR el "/vendedor" extra - la clase ya tiene @RequestMapping("/vendedor")
    @GetMapping("/registrar-venta")  // ✅ CORRECTO: /vendedor/registrar-venta
    public String mostrarFormularioRegistrarVenta(Authentication authentication, Model model) {
        String documentoIdentidad = authentication.getName();
        Vendedor vendedor = (Vendedor) usuarioService.buscarPorDocumentoIdentidad(documentoIdentidad);
        model.addAttribute("vendedor", vendedor);
        return "vendedor/registrar-venta";
    }

    @GetMapping("/rutas")
    public String mostrarRutas(Authentication authentication, Model model) {
        String username = authentication.getName();
        Vendedor vendedor = (Vendedor) usuarioService.buscarPorDocumentoIdentidad(username);
    
        List<Ruta> misRutas = rutaService.obtenerRutasPorVendedor(vendedor.getId());
        List<Ruta> rutasDisponibles = rutaService.obtenerRutasDisponibles();
    
        model.addAttribute("vendedor", vendedor);
        model.addAttribute("misRutas", misRutas);
        model.addAttribute("rutasDisponibles", rutasDisponibles);
    
        return "vendedor/rutas";
    }

    @PostMapping("/rutas/seleccionar")
    public String seleccionarRuta(@RequestParam Long rutaId, Authentication authentication) {
        String username = authentication.getName();
        Vendedor vendedor = (Vendedor) usuarioService.buscarPorDocumentoIdentidad(username);
    
        Impulsador impulsador = vendedor.getImpulsador();
    
        rutaService.asignarRutaAVendedor(rutaId, impulsador, vendedor.getId());
    return "redirect:/vendedor/rutas?selected=true";
    }

    @GetMapping("/historial")  // ✅ CORRECTO: /vendedor/historial
    public String mostrarHistorial(Authentication authentication, Model model) {
        String documentoIdentidad = authentication.getName();
        Vendedor vendedor = (Vendedor) usuarioService.buscarPorDocumentoIdentidad(documentoIdentidad);
        model.addAttribute("vendedor", vendedor);
        return "vendedor/historial";
    }

    @GetMapping("/objetivos")  // ✅ CORRECTO: /vendedor/objetivos
    public String mostrarObjetivos(Authentication authentication, Model model) {
        String documentoIdentidad = authentication.getName();
        Vendedor vendedor = (Vendedor) usuarioService.buscarPorDocumentoIdentidad(documentoIdentidad);
        model.addAttribute("vendedor", vendedor);
        return "vendedor/objetivos";
    }

    @GetMapping("/vendedor/objetivos") 
    public String verMisObjetivos(Authentication authentication, Model model) {
    String username = authentication.getName();
    Vendedor vendedor = (Vendedor) usuarioService.buscarPorDocumentoIdentidad(username);
    List<Objetivo> misObjetivos = objetivoService.obtenerObjetivosPorVendedor(vendedor.getId());
    
    model.addAttribute("vendedor", vendedor);
    model.addAttribute("misObjetivos", misObjetivos);
    
    return "vendedor/objetivos";
    }
    
}