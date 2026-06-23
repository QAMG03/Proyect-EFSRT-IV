package com.ferreteria.controller;

import com.ferreteria.entity.CarritoItem;
import com.ferreteria.model.CarritoModel;
import com.ferreteria.persistence.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/carrito")
public class CarritoController {

    @Autowired
    private CarritoModel carritoModel;

    @Autowired
    private ProductoRepository productoRepository;

    @GetMapping("/catalogo")
    public String catalogo(Model model) {
        model.addAttribute("productos", productoRepository.findAll());
        return "carrito/catalogo";
    }

    @PostMapping("/agregar")
    public String agregar(@RequestParam Long productoId,
                          @RequestParam int cantidad,
                          Authentication auth) {
        String username = auth.getName();
        carritoModel.agregarProducto(username, productoId, cantidad);
        return "redirect:/carrito/ver";
    }

    @GetMapping("/ver")
    public String verCarrito(Model model, Authentication auth) {
        String username = auth.getName();
        model.addAttribute("items", carritoModel.listarCarrito(username));
        return "carrito/index";
    }

    @PostMapping("/eliminar")
    public String eliminarItem(@RequestParam Long id, Authentication auth) {
        String username = auth.getName();
        carritoModel.eliminarItem(username, id);
        return "redirect:/carrito/ver";
    }

    @PostMapping("/vaciar")
    public String vaciarCarrito(Authentication auth) {
        String username = auth.getName();
        carritoModel.vaciarCarrito(username);
        return "redirect:/carrito/ver";
    }
}
