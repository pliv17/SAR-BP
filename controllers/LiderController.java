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

    // ── GET /lider → panel con lista de alertas del líder ─────────
    @GetMapping
    public String panel(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        Usuario lider = usuarioService.buscarPorUsername(userDetails.getUsername());
        model.addAttribute("lider", lider);
        model.addAttribute("alertas", alertaService.obtenerAlertasDelLider(lider));
        model.addAttribute("categorias", alertaService.obtenerCategoriasActivas());
        return "lider";
    }

    // ── POST /lider/nueva → crear alerta ──────────────────────────
    @PostMapping("/nueva")
    public String crearAlerta(@AuthenticationPrincipal UserDetails userDetails,
                              @RequestParam String titulo,
                              @RequestParam String descripcion,
                              @RequestParam Long categoriaId,
                              RedirectAttributes redirectAttrs) {
        try {
            Usuario lider = usuarioService.buscarPorUsername(userDetails.getUsername());
            alertaService.crearAlerta(titulo, descripcion, categoriaId, lider);
            redirectAttrs.addFlashAttribute("exito", "Alerta publicada correctamente.");
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("error", "No se pudo publicar la alerta.");
        }
        return "redirect:/lider";
    }

    // ── POST /lider/eliminar/{id} → eliminar alerta propia ────────
    @PostMapping("/eliminar/{id}")
    public String eliminarAlerta(@AuthenticationPrincipal UserDetails userDetails,
                                 @PathVariable Long id,
                                 RedirectAttributes redirectAttrs) {
        try {
            Usuario lider = usuarioService.buscarPorUsername(userDetails.getUsername());
            alertaService.eliminarAlerta(id, lider);
            redirectAttrs.addFlashAttribute("exito", "Alerta eliminada.");
        } catch (SecurityException e) {
            redirectAttrs.addFlashAttribute("error", "No puedes eliminar una alerta que no es tuya.");
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("error", "Error al eliminar la alerta.");
        }
        return "redirect:/lider";
    }
}
