package com.gruposeis.sarbp.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.gruposeis.sarbp.models.Usuario;
import com.gruposeis.sarbp.services.UsuarioService;

import jakarta.validation.Valid;

import java.util.List;

@Controller
public class AdminController {

    private final UsuarioService usuarioService;

    public AdminController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminPanel(Model model) {
        List<Usuario> leaders = usuarioService.listLeaders();
        model.addAttribute("leaders", leaders);
        return "admin";
    }

    @GetMapping("/admin/usuarios/new")
    @PreAuthorize("hasRole('ADMIN')")
    public String newLeaderForm(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "admin_new_user";
    }

    @PostMapping("/admin/usuarios")
    @PreAuthorize("hasRole('ADMIN')")
    public String createLeader(@ModelAttribute @Valid Usuario usuario) {
        usuario.setRol(Usuario.Rol.LIDER);
        usuarioService.save(usuario);
        return "redirect:/admin";
    }

    @PostMapping("/admin/usuarios/{id}/deactivate")
    @PreAuthorize("hasRole('ADMIN')")
    public String deactivateLeader(@PathVariable Long id) {
        usuarioService.deactivate(id);
        return "redirect:/admin";
    }
}