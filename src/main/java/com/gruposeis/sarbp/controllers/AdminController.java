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

    @GetMapping
    public String panel(Model model) {
        try {
            model.addAttribute("lideres", usuarioService.listarLideres());
        } catch (Exception e) {
            model.addAttribute("lideres", java.util.Collections.emptyList());
            model.addAttribute("error", "Error al cargar los líderes");
        }
        return "admin";
    }

    @PostMapping("/nuevo")
    public String crearLider(@RequestParam String nombres,
                             @RequestParam String apellidos,
                             @RequestParam String docIdentificacion,
                             @RequestParam String username,
                             @RequestParam String password,
                             RedirectAttributes redirectAttrs) {
        try {
            if (nombres.trim().isEmpty() || apellidos.trim().isEmpty() ||
                username.trim().isEmpty() || password.trim().isEmpty()) {
                redirectAttrs.addFlashAttribute("error", "Todos los campos son requeridos.");
                return "redirect:/admin";
            }
            usuarioService.crearLider(nombres, apellidos, docIdentificacion, username, password);
            redirectAttrs.addFlashAttribute("exito", "Líder creado correctamente.");
        } catch (IllegalArgumentException e) {
            redirectAttrs.addFlashAttribute("error", e.getMessage());
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("error", "Error al crear el líder: " + e.getMessage());
        }
        return "redirect:/admin";
    }

    @PostMapping("/toggle/{id}")
    public String toggleEstado(@PathVariable Long id, RedirectAttributes redirectAttrs) {
        try {
            usuarioService.toggleEstado(id);
            redirectAttrs.addFlashAttribute("exito", "Estado del usuario actualizado correctamente.");
        } catch (IllegalArgumentException e) {
            redirectAttrs.addFlashAttribute("error", "Usuario no encontrado.");
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("error", "Error al cambiar el estado.");
        }
        return "redirect:/admin";
    }
}
