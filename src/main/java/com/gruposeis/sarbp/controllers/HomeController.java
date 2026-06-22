package com.gruposeis.sarbp.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.gruposeis.sarbp.services.AlertaService;

import java.util.List;
import com.gruposeis.sarbp.models.Alerta;

@Controller
public class HomeController {

    private final AlertaService alertaService;

    public HomeController(AlertaService alertaService) {
        this.alertaService = alertaService;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("title", "SAR-BP");
        // show public active alerts
        List<Alerta> alertas = alertaService.listActiveAlerts();
        model.addAttribute("alertas", alertas);
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/dashboard")
    public String dashboard() {
        return "dashboard";
    }
}