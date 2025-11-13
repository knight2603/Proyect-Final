package modelo;


public class Categorias {
   
   
    private int id;
    private String nombre, subcategorias;

    public Categorias() {
    }

    public Categorias(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public String getSubcategorias() {
        return subcategorias;
    }

    public void setSubcategorias(String subcategorias) {
        this.subcategorias = subcategorias;
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


   
   
}