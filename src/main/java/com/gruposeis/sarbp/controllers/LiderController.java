package com.gruposeis.sarbp.controllers;

import com.gruposeis.sarbp.models.Usuario;
import com.gruposeis.sarbp.services.AlertaService;
import com.gruposeis.sarbp.services.UsuarioService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/lider")
@PreAuthorize("hasRole('LIDER')")
public class LiderController {

    private final AlertaService alertaService;
    private final UsuarioService usuarioService;

    public LiderController(AlertaService alertaService, UsuarioService usuarioService) {
        this.alertaService = alertaService;
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public String panel(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        try {
            Usuario lider = usuarioService.buscarPorUsername(userDetails.getUsername());
            model.addAttribute("lider", lider);
            model.addAttribute("alertas", alertaService.obtenerAlertasDelLider(lider));
            model.addAttribute("categorias", alertaService.obtenerCategoriasActivas());
        } catch (Exception e) {
            model.addAttribute("error", "Error al cargar los datos: " + e.getMessage());
        }
        return "lider";
    }

    @PostMapping("/nueva")
    public String crearAlerta(@AuthenticationPrincipal UserDetails userDetails,
                              @RequestParam String titulo,
                              @RequestParam String descripcion,
                              @RequestParam Long categoriaId,
                              RedirectAttributes redirectAttrs) {
        try {
            if (titulo.trim().isEmpty() || descripcion.trim().isEmpty()) {
                redirectAttrs.addFlashAttribute("error", "Título y descripción son requeridos.");
                return "redirect:/lider";
            }
            Usuario lider = usuarioService.buscarPorUsername(userDetails.getUsername());
            alertaService.crearAlerta(titulo, descripcion, categoriaId, lider);
            redirectAttrs.addFlashAttribute("exito", "Alerta publicada correctamente.");
        } catch (IllegalArgumentException e) {
            redirectAttrs.addFlashAttribute("error", "Categoría no válida: " + e.getMessage());
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("error", "Error al publicar la alerta: " + e.getMessage());
        }
        return "redirect:/lider";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminarAlerta(@AuthenticationPrincipal UserDetails userDetails,
                                 @PathVariable Long id,
                                 RedirectAttributes redirectAttrs) {
        try {
            Usuario lider = usuarioService.buscarPorUsername(userDetails.getUsername());
            alertaService.eliminarAlerta(id, lider);
            redirectAttrs.addFlashAttribute("exito", "Alerta eliminada correctamente.");
        } catch (SecurityException e) {
            redirectAttrs.addFlashAttribute("error", "No puedes eliminar una alerta que no es tuya.");
        } catch (IllegalArgumentException e) {
            redirectAttrs.addFlashAttribute("error", "Alerta no encontrada.");
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("error", "Error al eliminar la alerta.");
        }
        return "redirect:/lider";
    }
}
