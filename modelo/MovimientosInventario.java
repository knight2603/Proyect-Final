package modelo;

import java.util.Date;

public class MovimientosInventario {

    private int id;
    private int idProducto;
    private String tipoMovimiento; // ENTRADA, SALIDA, DEVOLUCION, DESCUENTO
    private int cantidad;
    private Date fechaMovimiento;
    private int idUsuario;
    private int idProveedor;
    private String observaciones;

    // Constructor vacío
    public MovimientosInventario() {
    }

    // Constructor con parámetros (opcional)
    public MovimientosInventario(int id, int idProducto, String tipoMovimiento, int cantidad,
                                Date fechaMovimiento, int idUsuario, int idProveedor, String observaciones) {
        this.id = id;
        this.idProducto = idProducto;
        this.tipoMovimiento = tipoMovimiento;
        this.cantidad = cantidad;
        this.fechaMovimiento = fechaMovimiento;
        this.idUsuario = idUsuario;
        this.idProveedor = idProveedor;
        this.observaciones = observaciones;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public String getTipoMovimiento() {
        return tipoMovimiento;
    }

    public void setTipoMovimiento(String tipoMovimiento) {
        this.tipoMovimiento = tipoMovimiento;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public Date getFechaMovimiento() {
        return fechaMovimiento;
    }

    public void setFechaMovimiento(Date fechaMovimiento) {
        this.fechaMovimiento = fechaMovimiento;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getIdProveedor() {
        return idProveedor;
    }

    public void setIdProveedor(int idProveedor) {
        this.idProveedor = idProveedor;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}
