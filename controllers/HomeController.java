package com.gruposeis.sarbp.controllers;

import com.gruposeis.sarbp.services.AlertaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final AlertaService alertaService;

    public HomeController(AlertaService alertaService) {
        this.alertaService = alertaService;
    }

    // ── Tablón público: vecinos ven alertas activas ───────────────
    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("alertas", alertaService.obtenerAlertasActivas());
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    // Dashboard intermedio: redirige según rol
    @GetMapping("/dashboard")
    public String dashboard() {
        return "dashboard";
    }
}
