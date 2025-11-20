package modelo;

public class DetalleCompra {

    private int idDetalle;
    private int idCompra;
    private int idProducto;
    private int cantidad;
    private double costoUnitario;
    private double subtotal;

    // (Opcional) para mostrar el nombre del producto en tablas
    private String nombreProducto;

    public DetalleCompra() {
    }

    public DetalleCompra(int idDetalle, int idCompra, int idProducto, int cantidad,
                         double costoUnitario, double subtotal) {
        this.idDetalle = idDetalle;
        this.idCompra = idCompra;
        this.idProducto = idProducto;
        this.cantidad = cantidad;
        this.costoUnitario = costoUnitario;
        this.subtotal = subtotal;
    }

    // =======================
    // GETTERS & SETTERS
    // =======================

    public int getIdDetalle() {
        return idDetalle;
    }

    public void setIdDetalle(int idDetalle) {
        this.idDetalle = idDetalle;
    }

    public int getIdCompra() {
        return idCompra;
    }

    public void setIdCompra(int idCompra) {
        this.idCompra = idCompra;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getCostoUnitario() {
        return costoUnitario;
    }

    public void setCostoUnitario(double costoUnitario) {
        this.costoUnitario = costoUnitario;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }
}
