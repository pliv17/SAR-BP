package com.gruposeis.sarbp.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LiderController {

    @GetMapping("/lider")
    @PreAuthorize("hasRole('LIDER')")
    public String liderPanel() {
        return "lider";
    }
}