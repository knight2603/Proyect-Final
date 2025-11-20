package modelo;

import java.io.Serializable;
import java.util.Date;

public class SalidaInventario implements Serializable {

    private int id;
    private int idProducto;
    private int cantidad;
    private String estado;
    private Date fechaSalida;
    private int ccUsuario;
    private String observaciones;
    private String nombreProducto;

    public SalidaInventario() {
        // evita NullPointerException al registrar salida sin fecha manual
        this.fechaSalida = new Date();
    }

    public SalidaInventario(int id, int idProducto, int cantidad, String estado, Date fechaSalida, int ccUsuario, String observaciones) {
        this.id = id;
        this.idProducto = idProducto;
        this.cantidad = cantidad;
        this.estado = estado;
        this.fechaSalida = fechaSalida != null ? fechaSalida : new Date();
        this.ccUsuario = ccUsuario;
        this.observaciones = observaciones;
    }

    // ============================
    //  GETTERS & SETTERS
    // ============================
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

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Date getFechaSalida() {
        return fechaSalida;
    }

    public void setFechaSalida(Date fechaSalida) {
        this.fechaSalida = fechaSalida != null ? fechaSalida : new Date();
    }

    public int getCcUsuario() {
        return ccUsuario;
    }

    public void setCcUsuario(int ccUsuario) {
        this.ccUsuario = ccUsuario;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

}
