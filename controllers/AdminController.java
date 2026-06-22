package com.gruposeis.sarbp.controllers;

import com.gruposeis.sarbp.services.UsuarioService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final UsuarioService usuarioService;

    public AdminController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    // ── GET /admin → panel con lista de líderes ───────────────────
    @GetMapping
    public String panel(Model model) {
        model.addAttribute("lideres", usuarioService.listarLideres());
        return "admin";
    }

    // ── POST /admin/nuevo → crear líder ───────────────────────────
    @PostMapping("/nuevo")
    public String crearLider(@RequestParam String nombres,
                             @RequestParam String apellidos,
                             @RequestParam String docIdentificacion,
                             @RequestParam String username,
                             @RequestParam String password,
                             RedirectAttributes redirectAttrs) {
        try {
            usuarioService.crearLider(nombres, apellidos, docIdentificacion, username, password);
            redirectAttrs.addFlashAttribute("exito", "Líder creado correctamente.");
        } catch (IllegalArgumentException e) {
            redirectAttrs.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin";
    }

    // ── POST /admin/toggle/{id} → activar o desactivar líder ──────
    @PostMapping("/toggle/{id}")
    public String toggleEstado(@PathVariable Long id, RedirectAttributes redirectAttrs) {
        try {
            usuarioService.toggleEstado(id);
            redirectAttrs.addFlashAttribute("exito", "Estado del usuario actualizado.");
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("error", "Error al cambiar el estado.");
        }
        return "redirect:/admin";
    }
}
