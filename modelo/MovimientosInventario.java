
package modelo;

import java.util.Date;


public class MovimientosInventario {
    
    
    private int id, id_producto, cantidad, id_usuario, id_proveedor;
    private String tipo_movimiento, observaciones;
    private Date fecha_movimiento;

    public MovimientosInventario() {
    }

    public MovimientosInventario(int id, int id_producto, int cantidad, int id_usuario, int id_proveedor, String tipo_movimiento, String observaciones, Date fecha_movimiento) {
        this.id = id;
        this.id_producto = id_producto;
        this.cantidad = cantidad;
        this.id_usuario = id_usuario;
        this.id_proveedor = id_proveedor;
        this.tipo_movimiento = tipo_movimiento;
        this.observaciones = observaciones;
        this.fecha_movimiento = fecha_movimiento;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_producto() {
        return id_producto;
    }

    public void setId_producto(int id_producto) {
        this.id_producto = id_producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public int getId_proveedor() {
        return id_proveedor;
    }

    public void setId_proveedor(int id_proveedor) {
        this.id_proveedor = id_proveedor;
    }

    public String getTipo_movimiento() {
        return tipo_movimiento;
    }

    public void setTipo_movimiento(String tipo_movimiento) {
        this.tipo_movimiento = tipo_movimiento;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Date getFecha_movimiento() {
        return fecha_movimiento;
    }

    public void setFecha_movimiento(Date fecha_movimiento) {
        this.fecha_movimiento = fecha_movimiento;
    }
    
    
      
    
}
