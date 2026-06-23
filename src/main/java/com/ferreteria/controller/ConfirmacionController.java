package com.ferreteria.controller;

import com.ferreteria.entity.CarritoItem;
import com.ferreteria.model.CarritoModel;
import com.ferreteria.model.VentaModel;
import com.ferreteria.entity.Venta;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/carrito")
public class ConfirmacionController {

    @Autowired
    private CarritoModel carritoModel;

    @Autowired
    private VentaModel ventaModel;

    @GetMapping("/confirmar")
    public String confirmarCompra(Authentication auth, Model model) {
        String username = auth.getName();

        try {
            List<CarritoItem> items = carritoModel.listarCarrito(username);

            Venta venta = ventaModel.registrarVenta(username);

            
            model.addAttribute("ventaId", venta.getId());
            model.addAttribute("mensaje", "Compra confirmada con éxito. Código de venta: " + venta.getId());


        } catch (RuntimeException ex) {
            model.addAttribute("error", ex.getMessage());
        }

        return "carrito/confirmacion";
    }
}
