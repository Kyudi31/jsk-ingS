package com.jsk.jsk.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    @GetMapping("/admin/dashboard")
    public String adminDashboard() {
        return "admin/dashboard";
    }
    
    @GetMapping("/coordinador/dashboard")
    public String coordinadorDashboard() {
        return "coordinador/dashboard";
    }

    @GetMapping("/impulsador/dashboard")
    public String impulsadorDashboard() {
        return "impulsador/dashboard";
    }

    @GetMapping("/vendedor/dashboard")
    public String vendedorDashboard() {
        return "vendedor/dashboard";
    }
    
    

}