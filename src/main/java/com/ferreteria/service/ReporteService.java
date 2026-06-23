package com.ferreteria.service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.ferreteria.entity.DetalleVenta;
import com.ferreteria.entity.MovimientoStock;
import com.ferreteria.entity.Venta;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Service
public class ReporteService {

	
	public byte[] generarReporteBoleta(Venta venta) throws JRException {
	    
	    InputStream jasperStream = this.getClass().getResourceAsStream("/reportes/boleta.jasper");

	    
	    double igv = Math.round(venta.getTotal() * 0.18 * 100.0) / 100.0;
	    double totalConIgv = Math.round((venta.getTotal() + igv) * 100.0) / 100.0;

	   
	    Map<String, Object> parametros = new HashMap<>();
	    parametros.put("idVenta", venta.getId());
	    parametros.put("fecha", venta.getFecha().toLocalDate().toString()); // solo fecha
	    parametros.put("nombreCliente", venta.getCliente().getNombres() + " " + venta.getCliente().getApellidos());
	    parametros.put("dniCliente", venta.getCliente().getDni());
	    parametros.put("direccionCliente", venta.getCliente().getDireccion());
	    parametros.put("total", venta.getTotal());
	    parametros.put("igv", igv);
	    parametros.put("totalConIgv", totalConIgv);

	    
	    List<Map<String, Object>> detalleData = new ArrayList<>();
	    for (DetalleVenta d : venta.getDetalles()) {
	        Map<String, Object> fila = new HashMap<>();
	        fila.put("producto", d.getProducto().getNombre());
	        fila.put("cantidad", d.getCantidad());
	        fila.put("precioUnitario", d.getProducto().getPrecio());
	        detalleData.add(fila);
	    }

	    
	    JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(detalleData);

	    
	    JasperPrint jasperPrint = JasperFillManager.fillReport(jasperStream, parametros, dataSource);

	    
	    return JasperExportManager.exportReportToPdf(jasperPrint);
	}


    
    public byte[] generarReporteMovimientos(List<MovimientoStock> movimientos) throws JRException {
        InputStream jasperStream = this.getClass().getResourceAsStream("/reportes/reporte_movimientos.jasper");

        List<Map<String, Object>> data = new ArrayList<>();
        for (MovimientoStock m : movimientos) {
            Map<String, Object> fila = new HashMap<>();
            fila.put("id", m.getId());
            fila.put("producto", m.getProducto().getNombre());
            fila.put("tipo", m.getTipo().toString());
            fila.put("cantidad", m.getCantidad());
            fila.put("fecha", m.getFecha().toLocalDate().toString());
            fila.put("observacion", m.getObservacion());
            data.add(fila);
        }

        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(data);

        JasperPrint print = JasperFillManager.fillReport(jasperStream, new HashMap<>(), dataSource);
        return JasperExportManager.exportReportToPdf(print);
    }

}

