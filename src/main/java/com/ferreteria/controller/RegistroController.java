package com.ferreteria.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.ferreteria.entity.Cliente;
import com.ferreteria.entity.Usuario;
import com.ferreteria.persistence.ClienteRepository;
import com.ferreteria.persistence.UsuarioRepository;
import com.ferreteria.usecase.RegistroUseCase;

import jakarta.validation.Valid;

@Controller
public class RegistroController {
	
	@Autowired
    private RegistroUseCase registroUseCase;

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("usuario") Usuario usuario,
                               BindingResult result,
                               Model model) {
        if (result.hasErrors()) {
            return "register";  
        }

        try {
            registroUseCase.registrarUsuario(usuario);
            return "redirect:/login?registroExitoso";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "register";
        }
    }



}
