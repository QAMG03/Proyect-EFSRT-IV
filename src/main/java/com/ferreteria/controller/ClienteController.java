package com.ferreteria.controller;

import com.ferreteria.entity.Cliente;
import com.ferreteria.usecase.ClienteUseCase;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private ClienteUseCase clienteUseCase;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("clientes", clienteUseCase.listar());
        return "cliente/index";
    }

    @GetMapping("/lista")
    public String listarClientes(Model model) {
        model.addAttribute("clientes", clienteUseCase.listar());
        return "cliente/index";
    }
    
    @GetMapping("/buscar")
    public String buscar(@RequestParam("busqueda") String texto, Model model) {
        String textoLimpio = texto.trim();
        model.addAttribute("clientes", clienteUseCase.buscarPorNombreODni(textoLimpio));
        model.addAttribute("busqueda", textoLimpio);
        return "cliente/index";
    }
    
    
    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("cliente", new Cliente());
        return "cliente/form";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute Cliente cliente, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("cliente", cliente);
            return "cliente/form";
        }

        try {
            clienteUseCase.guardar(cliente);
            return "redirect:/clientes/lista";
        } catch (RuntimeException e) {
            model.addAttribute("cliente", cliente);
            model.addAttribute("error", e.getMessage());
            return "cliente/form";
        }
    }


    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("cliente", clienteUseCase.obtenerPorId(id));
        return "cliente/form";
    }

    @GetMapping("/eliminar/{id}")
    public String suspender(@PathVariable("id") Long id, RedirectAttributes redirect) {
        try {
            clienteUseCase.suspender(id);
            redirect.addFlashAttribute("mensaje", "Cliente suspendido correctamente.");
        } catch (DataIntegrityViolationException e) {
            redirect.addFlashAttribute("error", "No se puede eliminar el cliente porque tiene ventas registradas.");
        }
        return "redirect:/clientes";
    }
    
    @GetMapping("/reactivar/{id}")
    public String reactivar(@PathVariable Long id, RedirectAttributes redirect) {
        Cliente cliente = clienteUseCase.obtenerPorId(id);
        if (cliente != null && cliente.getEstado().equalsIgnoreCase("INACTIVO")) {
            cliente.setEstado("ACTIVO");
            clienteUseCase.guardar(cliente);
            redirect.addFlashAttribute("mensaje", "Cliente reactivado correctamente.");
        } else {
            redirect.addFlashAttribute("error", "No se pudo reactivar el cliente.");
        }
        return "redirect:/clientes/lista";
    }
    
    @GetMapping("/eliminar-definitivo/{id}")
    public String eliminarDefinitivo(@PathVariable Long id, RedirectAttributes redirect) {
        boolean eliminado = clienteUseCase.eliminarDefinitivo(id);
        
        if (eliminado) {
            redirect.addFlashAttribute("mensaje", "Cliente eliminado correctamente.");
        } else {
            redirect.addFlashAttribute("error", "No se puede eliminar. El cliente está asociado a una venta.");
        }

        return "redirect:/clientes/lista";
    }
    
}
