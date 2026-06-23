package com.ferreteria.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {
	
	@GetMapping("/login")
	public String login(@RequestParam(value = "registroExitoso", required = false) String registroExitoso,
	                    @RequestParam(value = "errorMsg", required = false) String errorMsg,
	                    @RequestParam(value = "logout", required = false) String logout,
	                    Model model) {

	    if (registroExitoso != null) {
	        model.addAttribute("success", "¡Registro exitoso! Ahora puedes iniciar sesión.");
	    }

	    if (errorMsg != null) {
	        model.addAttribute("error", errorMsg);
	    }

	    if (logout != null) {
	        model.addAttribute("logout", "Has cerrado sesión correctamente.");
	    }

	    return "login";
	}




    @GetMapping("/home")
    public String home() {
        return "/"; 
    }
}
