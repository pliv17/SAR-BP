package com.gruposeis.sarbp.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.gruposeis.sarbp.models.Alerta;
import com.gruposeis.sarbp.models.Usuario;
import com.gruposeis.sarbp.services.AlertaService;
import com.gruposeis.sarbp.services.UsuarioService;

import java.security.Principal;
import java.util.List;

@Controller
public class LiderController {
    private final AlertaService alertaService;
    private final UsuarioService usuarioService;

    public LiderController(AlertaService alertaService, UsuarioService usuarioService) {
        this.alertaService = alertaService;
        this.usuarioService = usuarioService;
    }

    @GetMapping("/lider")
    @PreAuthorize("hasRole('LIDER')")
    public String liderPanel(Model model, Principal principal) {
        Usuario usuario = usuarioService.findByUsername(principal.getName()).orElse(null);
        List<Alerta> alerts = usuario != null ? alertaService.listByUsuario(usuario.getId()) : List.of();
        model.addAttribute("alerts", alerts);
        return "lider";
    }

    @GetMapping("/lider/alertas/new")
    @PreAuthorize("hasRole('LIDER')")
    public String newAlertaForm(Model model) {
        model.addAttribute("alerta", new Alerta());
        return "lider_alerta_form";
    }

    @PostMapping("/lider/alertas")
    @PreAuthorize("hasRole('LIDER')")
    public String createAlerta(@ModelAttribute Alerta alerta, Principal principal) {
        Usuario usuario = usuarioService.findByUsername(principal.getName())
            .orElseThrow();
        alertaService.create(alerta, usuario.getId());
        return "redirect:/lider";
    }

    @PostMapping("/lider/alertas/{id}/delete")
    @PreAuthorize("hasRole('LIDER')")
    public String deleteAlerta(@PathVariable Long id, Principal principal) {
        // basic ownership check could be added
        alertaService.delete(id);
        return "redirect:/lider";
    }

    @PostMapping("/lider/alertas/{id}/resolver")
    @PreAuthorize("hasRole('LIDER')")
    public String resolveAlerta(@PathVariable Long id) {
        alertaService.findById(id).ifPresent(a -> {
            a.setEstado("RESUELTA");
            alertaService.update(a);
        });
        return "redirect:/lider";
    }
}