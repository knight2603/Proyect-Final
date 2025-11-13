package modelo;

import java.util.Date;

public class Entradainventario {
    
    private int id, cantidad, id_producto, cc_usurios;
    private double precio;
    private Date fecha_entrada;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public int getId_producto() {
        return id_producto;
    }

    public void setId_producto(int id_producto) {
        this.id_producto = id_producto;
    }

    public int getCc_usurios() {
        return cc_usurios;
    }

    public void setCc_usurios(int cc_usurios) {
        this.cc_usurios = cc_usurios;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public Date getFecha_entrada() {
        return fecha_entrada;
    }

    public void setFecha_entrada(Date fecha_entrada) {
        this.fecha_entrada = fecha_entrada;
    }
    
    
    
    
    
}