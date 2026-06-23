package com.ferreteria.controller;

import com.ferreteria.entity.MovimientoStock;
import com.ferreteria.model.MovimientoStockModel;
import com.ferreteria.persistence.ProductoRepository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/movimientos")
public class MovimientoStockController {

    @Autowired
    private MovimientoStockModel movimientoStockModel;

    @Autowired
    private ProductoRepository productoRepository;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("movimientos", movimientoStockModel.listar());
        return "movimiento/index";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("movimiento", new MovimientoStock());
        model.addAttribute("productos", productoRepository.findAll());
        return "movimiento/form";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute MovimientoStock movimiento, Model model) {
        try {
            movimientoStockModel.registrar(movimiento);
            return "redirect:/movimientos";
        } catch (RuntimeException e) {
            model.addAttribute("movimiento", movimiento);
            model.addAttribute("productos", productoRepository.findAll());
            model.addAttribute("error", e.getMessage());
            return "movimiento/form";
        }
    }
    
    @GetMapping("/buscar-fechas")
    public String buscarPorFechas(
            @RequestParam(name = "desde", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam(name = "hasta", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta,
            Model model
    ) {
        try {
            List<MovimientoStock> movimientos = movimientoStockModel.buscarPorRangoFechas(desde, hasta);
            if (movimientos.isEmpty()) {
                model.addAttribute("mensaje", "No se encontraron movimientos en el rango indicado.");
            }
            model.addAttribute("movimientos", movimientos);
            model.addAttribute("desde", desde);
            model.addAttribute("hasta", hasta);
        } catch (IllegalArgumentException ex) {
            model.addAttribute("error", ex.getMessage());
            model.addAttribute("movimientos", movimientoStockModel.listar());
        }
        return "movimiento/index";
    }
}
