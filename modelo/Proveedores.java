package modelo;

import java.io.Serializable;

public class Proveedores implements Serializable {

    private int id;
    private String nombre;
    private String almacen;
    private String cadena;
    private String telefono;
    private String correo;
    private String direccion;

    public Proveedores() {
    }

    public Proveedores(int id, String nombre, String almacen, String cadena, String telefono, String correo, String direccion) {
        this.id = id;
        this.nombre = nombre;
        this.almacen = almacen;
        this.cadena = cadena;
        this.telefono = telefono;
        this.correo = correo;
        this.direccion = direccion;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getAlmacen() {
        return almacen;
    }

    public void setAlmacen(String almacen) {
        this.almacen = almacen;
    }

    public String getCadena() {
        return cadena;
    }

    public void setCadena(String cadena) {
        this.cadena = cadena;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
}
