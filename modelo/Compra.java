package modelo;

import java.util.Date;

public class Compra{
    
    private int id, cc, id_metodo_pagar;
    private double total_pagar;
    private String estado;
    private Date fecha_compra;

    public Compra() {
    }

    public Compra(int id, int cc, int id_metodo_pagar, double total_pagar, String estado, Date fecha_compra) {
        this.id = id;
        this.cc = cc;
        this.id_metodo_pagar = id_metodo_pagar;
        this.total_pagar = total_pagar;
        this.estado = estado;
        this.fecha_compra = fecha_compra;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCc() {
        return cc;
    }

    public void setCc(int cc) {
        this.cc = cc;
    }

    public int getId_metodo_pagar() {
        return id_metodo_pagar;
    }

    public void setId_metodo_pagar(int id_metodo_pagar) {
        this.id_metodo_pagar = id_metodo_pagar;
    }

    public double getTotal_pagar() {
        return total_pagar;
    }

    public void setTotal_pagar(double total_pagar) {
        this.total_pagar = total_pagar;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Date getFecha_compra() {
        return fecha_compra;
    }

    public void setFecha_compra(Date fecha_compra) {
        this.fecha_compra = fecha_compra;
    }
    
    
}
