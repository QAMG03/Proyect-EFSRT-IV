package com.ferreteria.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

import com.ferreteria.entity.DetalleVenta;
import com.ferreteria.entity.Producto;
import com.ferreteria.entity.Venta;
import com.ferreteria.usecase.ClienteUseCase;
import com.ferreteria.usecase.ProductoUseCase;
import com.ferreteria.usecase.VentaUseCase;

@Controller
@RequestMapping("/ventas")
public class VentaController {

    @Autowired private VentaUseCase ventaUseCase;
    @Autowired private ClienteUseCase clienteUseCase;
    @Autowired private ProductoUseCase productoUseCase;

    @GetMapping("/lista")
    public String listarVentas(Model model) {
        model.addAttribute("ventas", ventaUseCase.listar());
        return "venta/index";
    }

    @GetMapping("/nueva")
    public String nuevaVenta(Model model) {
        Venta venta = new Venta();
        venta.getDetalles().add(new DetalleVenta());

        model.addAttribute("venta", venta);
        model.addAttribute("clientes", clienteUseCase.listar());
        model.addAttribute("productos", productoUseCase.listar());
        return "venta/form";
    }

    @PostMapping("/guardar")
    public String procesarFormulario(@ModelAttribute Venta venta,
                                     @RequestParam String accion,
                                     Model model) {
        if (venta.getDetalles() == null) {
            venta.setDetalles(new ArrayList<>());
        }

        if (accion.equals("agregar")) {
            DetalleVenta nuevo = new DetalleVenta();
            nuevo.setProducto(new Producto());
            venta.getDetalles().add(nuevo);

            model.addAttribute("venta", venta);
            model.addAttribute("clientes", clienteUseCase.listar());
            model.addAttribute("productos", productoUseCase.listar());
            return "venta/form";
        }

        if (venta.getCliente() == null || venta.getCliente().getId() == null) {
            model.addAttribute("venta", venta);
            model.addAttribute("clientes", clienteUseCase.listar());
            model.addAttribute("productos", productoUseCase.listar());
            model.addAttribute("error", "Debe seleccionar un cliente.");
            return "venta/form";
        }

        venta.getDetalles().removeIf(
            d -> d.getProducto() == null || d.getProducto().getId() == null
        );

        if (venta.getDetalles().isEmpty()) {
            model.addAttribute("venta", venta);
            model.addAttribute("clientes", clienteUseCase.listar());
            model.addAttribute("productos", productoUseCase.listar());
            model.addAttribute("error", "Debe seleccionar al menos un producto.");
            return "venta/form";
        }

        try {
            ventaUseCase.registrarVenta(venta);
            return "redirect:/ventas/lista";
        } catch (RuntimeException e) {
            model.addAttribute("venta", venta);
            model.addAttribute("clientes", clienteUseCase.listar());
            model.addAttribute("productos", productoUseCase.listar());
            model.addAttribute("error", e.getMessage());
            return "venta/form";
        }
    }


    @GetMapping("/detalle/{id}")
    public String verDetalleVenta(@PathVariable Long id, Model model) {
        Venta venta = ventaUseCase.obtenerPorId(id);
        if (venta == null) {
            return "redirect:/ventas/lista";
        }

        model.addAttribute("venta", venta);
        return "venta/detalle"; 
    }
    
    
    @PostMapping("/eliminar/{id}")
    public String eliminarVenta(@PathVariable Long id) {
        ventaUseCase.eliminarPorId(id);
        return "redirect:/ventas/lista";
    }
    
    @GetMapping("/buscar")
    public String buscarVentaPorId(@RequestParam(name = "id", required = false) Long id, Model model) {
        if (id == null) {
            model.addAttribute("ventas", ventaUseCase.listar());
            return "venta/index";
        }

        Venta venta = ventaUseCase.obtenerPorId(id);
        if (venta != null) {
            model.addAttribute("ventas", List.of(venta));
        } else {
            model.addAttribute("error", "No se encontró una venta con el ID: " + id);
            model.addAttribute("ventas", ventaUseCase.listar());
        }
        return "venta/index";
    }

    @GetMapping("/mis-compras")
    public String listarComprasUsuario(Authentication auth, Model model) {
        String username = auth.getName();
        List<Venta> ventas = ventaUseCase.obtenerVentasPorUsername(username);

        model.addAttribute("ventas", ventas);
        return "venta/mis-compras"; 
    }

}
