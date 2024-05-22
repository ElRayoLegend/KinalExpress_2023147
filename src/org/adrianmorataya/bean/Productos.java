package org.adrianmorataya.bean;

public class Productos {
    String productoId;
    String descripcionProducto;
    String nombreProducto;
    byte[] imagenProducto;
    double precioVentaUnitario;
    double precioCompra;
    double precioVentaMayor;
    int cantidadStock;
    int codigoTipoProducto;
    int codigoProveedor;

    public Productos() {
    }

    public Productos(String productoId, String descripcionProducto, String nombreProducto, byte[] imagenProducto, double precioVentaUnitario, double precioCompra, double precioVentaMayor, int cantidadStock, int codigoTipoProducto, int codigoProveedor) {
        this.productoId = productoId;
        this.descripcionProducto = descripcionProducto;
        this.nombreProducto = nombreProducto;
        this.imagenProducto = imagenProducto;
        this.precioVentaUnitario = precioVentaUnitario;
        this.precioCompra = precioCompra;
        this.precioVentaMayor = precioVentaMayor;
        this.cantidadStock = cantidadStock;
        this.codigoTipoProducto = codigoTipoProducto;
        this.codigoProveedor = codigoProveedor;
    }

    public String getProductoId() {
        return productoId;
    }

    public void setProductoId(String productoId) {
        this.productoId = productoId;
    }

    public String getDescripcionProducto() {
        return descripcionProducto;
    }

    public void setDescripcionProducto(String descripcionProducto) {
        this.descripcionProducto = descripcionProducto;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public byte[] getImagenProducto() {
        return imagenProducto;
    }

    public void setImagenProducto(byte[] imagenProducto) {
        this.imagenProducto = imagenProducto;
    }

    public double getPrecioVentaUnitario() {
        return precioVentaUnitario;
    }

    public void setPrecioVentaUnitario(double precioVentaUnitario) {
        this.precioVentaUnitario = precioVentaUnitario;
    }

    public double getPrecioCompra() {
        return precioCompra;
    }

    public void setPrecioCompra(double precioCompra) {
        this.precioCompra = precioCompra;
    }

    public double getPrecioVentaMayor() {
        return precioVentaMayor;
    }

    public void setPrecioVentaMayor(double precioVentaMayor) {
        this.precioVentaMayor = precioVentaMayor;
    }

    public int getCantidadStock() {
        return cantidadStock;
    }

    public void setCantidadStock(int cantidadStock) {
        this.cantidadStock = cantidadStock;
    }

    public int getCodigoTipoProducto() {
        return codigoTipoProducto;
    }

    public void setCodigoTipoProducto(int codigoTipoProducto) {
        this.codigoTipoProducto = codigoTipoProducto;
    }

    public int getCodigoProveedor() {
        return codigoProveedor;
    }

    public void setCodigoProveedor(int codigoProveedor) {
        this.codigoProveedor = codigoProveedor;
    }

    
}
