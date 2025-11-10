package com.jsk.jsk.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

public class HomeController {
    
    @GetMapping("/dashboard")
    public String dashboard(Model model){
        model.addAttribute("title", "Dashboard");
        model.addAttribute("content", "dashboard :: content");
        return "layout";
    }
}
