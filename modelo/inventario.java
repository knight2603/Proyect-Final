
package modelo;

import java.util.Date;



public class inventario {
    
    private int id, id_producto, stock;
    private String ubicacion; 
    private Date ultima_actualizacion;

    public inventario() {
    }

    public inventario(int id, int id_producto, int stock, String ubicacion, Date ultima_actualizacion) {
        this.id = id;
        this.id_producto = id_producto;
        this.stock = stock;
        this.ubicacion = ubicacion;
        this.ultima_actualizacion = ultima_actualizacion;
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

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public Date getUltima_actualizacion() {
        return ultima_actualizacion;
    }

    public void setUltima_actualizacion(Date ultima_actualizacion) {
        this.ultima_actualizacion = ultima_actualizacion;
    }
    
    
    

}
