package org.adrianmorataya.bean;

import java.sql.Time;
import java.time.LocalDate;

public class Facturas {
    private int facturaId;
    private LocalDate fecha;
    private Time hora;
    private double total;
    private int codigoCliente;
    private int empleadoId;

    public Facturas() {
    }

    
    
    public Facturas(int facturaId, LocalDate fecha, Time hora, double total, int codigoCliente, int empleadoId) {
        this.facturaId = facturaId;
        this.fecha = fecha;
        this.hora = hora;
        this.total = total;
        this.codigoCliente = codigoCliente;
        this.empleadoId = empleadoId;
    }

    public int getFacturaId() {
        return facturaId;
    }

    public void setFacturaId(int facturaId) {
        this.facturaId = facturaId;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public Time getHora() {
        return hora;
    }

    public void setHora(Time hora) {
        this.hora = hora;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public int getCodigoCliente() {
        return codigoCliente;
    }

    public void setCodigoCliente(int codigoCliente) {
        this.codigoCliente = codigoCliente;
    }

    public int getEmpleadoId() {
        return empleadoId;
    }

    public void setEmpleadoId(int empleadoId) {
        this.empleadoId = empleadoId;
    }

    @Override
    public String toString() {
        return facturaId + " - " + total;
    }
    
    
}
