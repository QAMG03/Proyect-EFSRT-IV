package com.ferreteria.controller;

import com.ferreteria.entity.Producto;
import com.ferreteria.usecase.ProductoUseCase;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/productos")
public class ProductoController {

    @Autowired
    private ProductoUseCase productoUseCase;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("productos", productoUseCase.listar());
        return "producto/index";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("producto", new Producto());
        return "producto/form";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute Producto producto, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("producto", producto);
            return "producto/form";
        }

        productoUseCase.guardar(producto);
        return "redirect:/productos/lista";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("producto", productoUseCase.obtenerPorId(id));
        return "producto/form";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            productoUseCase.eliminar(id);
            redirectAttributes.addFlashAttribute("mensaje", "✅ Producto eliminado correctamente.");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", "❌ No se puede eliminar el producto: " + e.getMessage());
        }
        return "redirect:/productos";
    }
    
    @GetMapping("/lista")
    public String listarProductos(Model model) {
        model.addAttribute("productos", productoUseCase.listar());
        return "producto/index";
    }
    
    @GetMapping("/buscar")
    public String buscar(@RequestParam("busqueda") String texto, Model model) {
        model.addAttribute("productos", productoUseCase.buscarPorNombre(texto.trim()));
        model.addAttribute("busqueda", texto.trim());
        return "producto/index";
    }
}
