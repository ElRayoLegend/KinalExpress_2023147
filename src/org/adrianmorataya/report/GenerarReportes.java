package org.adrianmorataya.report;

import java.io.InputStream;
import java.util.Map;
import org.adrianmorataya.dao.Conexion;

public class GenerarReportes {
    public static void mostrarReportes(String nombreReporte, String titulo, Map parametro){
        InputStream reporte = GenerarReportes.class.getResourceAsStream(nombreReporte);
        try{
            JasperReport reporteMaestro = (JasperReport)JRLoader.loadObject(reporte);
            JasperPrint reporteImpreso = JasperFillManager.fillReport(reporteMaestro, parametro, Conexion.getInstance().getConexion());
            JasperViewer visor = new JasperViewer(reporteImpreso, false)
        }catch(){
        
        }
    }
}
