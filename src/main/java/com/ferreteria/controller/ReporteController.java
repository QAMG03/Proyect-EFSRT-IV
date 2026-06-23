package com.ferreteria.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ferreteria.entity.MovimientoStock;
import com.ferreteria.entity.Venta;
import com.ferreteria.service.ReporteService;
import com.ferreteria.usecase.MovimientoStockUseCase;
import com.ferreteria.usecase.VentaUseCase;

@Controller
@RequestMapping("/ventas")
public class ReporteController {

    @Autowired
    private VentaUseCase ventaUseCase;
    
    @Autowired
    private MovimientoStockUseCase movimientoUseCase;

    @Autowired
    private ReporteService reporteService;
    

    @GetMapping("/boleta/{id}")
    public ResponseEntity<byte[]> reporteBoleta(@PathVariable Long id) {
        try {
            Venta venta = ventaUseCase.obtenerPorId(id);
            byte[] pdf = reporteService.generarReporteBoleta(venta);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.add("Content-Disposition", "inline; filename=boleta_" + id + ".pdf");

            return new ResponseEntity<>(pdf, headers, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/reporte-movimientos")
    public ResponseEntity<byte[]> reporteMovimientos() {
        try {
            List<MovimientoStock> lista = movimientoUseCase.obtenerTodos();
            byte[] pdf = reporteService.generarReporteMovimientos(lista);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.add("Content-Disposition", "inline; filename=reporte_movimientos.pdf");

            return new ResponseEntity<>(pdf, headers, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}

