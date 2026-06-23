package com.ferreteria.controller;

import com.ferreteria.entity.Usuario;
import com.ferreteria.usecase.UsuarioUseCase;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/usuarios")
public class UsuarioAdminController {

	@Autowired
    private UsuarioUseCase usuarioUseCase;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("usuarios", usuarioUseCase.listarUsuarios());
        return "usuario/index";
    }

    @GetMapping("/buscar")
    public String buscarPorId(@RequestParam(required = false) Long id, Model model) {
        if (id == null) {
            model.addAttribute("usuarios", usuarioUseCase.listarUsuarios());
            return "usuario/index";
        }

        Optional<Usuario> resultado = usuarioUseCase.obtenerUsuarioPorId(id);
        if (resultado.isPresent()) {
            model.addAttribute("usuarios", List.of(resultado.get()));
        } else {
            model.addAttribute("error", "No se encontró el usuario con ID: " + id);
            model.addAttribute("usuarios", usuarioUseCase.listarUsuarios());
        }

        return "usuario/index";
    }

    
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        usuarioUseCase.obtenerUsuarioPorId(id)
                      .ifPresent(usuario -> model.addAttribute("usuario", usuario));
        return "usuario/edit";
    }


    @PostMapping("/guardar")
    public String guardar(@ModelAttribute("usuario") Usuario usuario) {
        usuarioUseCase.actualizarUsuario(usuario);
        return "redirect:/admin/usuarios";
    }

    @GetMapping("/estado/{id}")
    public String cambiarEstado(@PathVariable Long id) {
        usuarioUseCase.cambiarEstado(id);
        return "redirect:/admin/usuarios";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            usuarioUseCase.eliminarUsuario(id);
            redirectAttributes.addFlashAttribute("success", "Usuario eliminado correctamente.");
        } catch (RuntimeException ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
        }
        return "redirect:/admin/usuarios";
    }

}
