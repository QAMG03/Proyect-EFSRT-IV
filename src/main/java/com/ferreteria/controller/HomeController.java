package com.ferreteria.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String inicio() {
        return "inicio"; 
    }
    
    @GetMapping("/admin-home")
    public String adminHome() {
        return "inicio";  
    }

    @GetMapping("/user-home")
    public String userHome() {
        return "user-home";   
    }
}
