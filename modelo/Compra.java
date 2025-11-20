package modelo;

import java.util.Date;

public class Compra {

    private int idCompra;
    private int ccUsuario;
    private Date fechaCompra;
    private String estado;
    private int idMetodoPago;
    private double total;
    private String metodoPagoNombre;

    public Compra() {
    }

    public Compra(int idCompra, int ccUsuario, Date fechaCompra,
            String estado, int idMetodoPago, double total) {

        this.idCompra = idCompra;
        this.ccUsuario = ccUsuario;
        this.fechaCompra = fechaCompra;
        this.estado = estado;
        this.idMetodoPago = idMetodoPago;
        this.total = total;
    }

    // GETTERS & SETTERS
    public int getIdCompra() {
        return idCompra;
    }

    public void setIdCompra(int idCompra) {
        this.idCompra = idCompra;
    }

    public int getCcUsuario() {
        return ccUsuario;
    }

    public void setCcUsuario(int ccUsuario) {
        this.ccUsuario = ccUsuario;
    }

    public Date getFechaCompra() {
        return fechaCompra;
    }

    public void setFechaCompra(Date fechaCompra) {
        this.fechaCompra = fechaCompra;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public int getIdMetodoPago() {
        return idMetodoPago;
    }

    public void setIdMetodoPago(int idMetodoPago) {
        this.idMetodoPago = idMetodoPago;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getMetodoPagoNombre() {
        return metodoPagoNombre;
    }

    public void setMetodoPagoNombre(String metodoPagoNombre) {
        this.metodoPagoNombre = metodoPagoNombre;
    }
}
