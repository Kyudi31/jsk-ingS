package com.jsk.jsk.controller;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import com.jsk.jsk.entity.enums.Empresa;
import com.jsk.jsk.entity.Coordinador;

import com.jsk.jsk.service.UsuarioService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class AdminController {
    private final UsuarioService usuarioService;

    @GetMapping("/admin/coordinadores")
    public String listarCoordinadores(Model model) {
        model.addAttribute("coordinadores", usuarioService.listarCoordinadores());
        return "admin/coordinadores";
    }
    
    @GetMapping("/admin/coordinadores/desactivar/{id}")
    public String desactivar(@PathVariable Long id) {
        usuarioService.cambiarEstadoUsuario(id, false);
        return "redirect:/admin/coordinadores";
    }

    @GetMapping("/admin/coordinadores/activar/{id}")
    public String activar(@PathVariable Long id) {
        usuarioService.cambiarEstadoUsuario(id, true);
        return "redirect:/admin/coordinadores";
    }
    
    @PostMapping("/admin/coordinadores/guardar")
    public String guardarCoordinador(Coordinador nuevo) {

        Long adminId = 1L; // de momento fijo
        usuarioService.crearCoordinador(
            usuarioService.buscarPorId(adminId),
            nuevo
    );

    return "redirect:/admin/coordinadores";
    }

    
    @GetMapping("/admin/coordinadores/nuevo")
    public String mostrarFormularioCrear(Model model) {
        model.addAttribute("coordinador", new Coordinador());
        model.addAttribute("empresas", Empresa.values());    // <-- aquí pasas el enum
        return "admin/nuevo-coordinador"; // plantilla Thymeleaf
    }


}
