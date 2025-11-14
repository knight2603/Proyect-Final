package control;

import javax.faces.bean.SessionScoped;
import javax.faces.bean.ManagedBean;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import modelo.Productos;

@ManagedBean(name="catalogoBean")
@SessionScoped
public class CatalogoBean implements Serializable {

    private List<Productos> productos;     // Todos los productos
    private List<Productos> carrito;       // Carrito
    private String filtro = "todos";       // Categoría actual

    public CatalogoBean() {
        productos = new ArrayList<>();
        carrito = new ArrayList<>();

        // Útiles
        productos.add(new Productos("Cuaderno Argollado", "utiles", 8000, "resources/img/cuaderno.jpg", "Cuaderno con argollas y hojas rayadas."));
        productos.add(new Productos("Lápiz HB N°2", "utiles", 1000, "resources/img/lapiz.jpg", "Lápiz de grafito HB ideal para escribir y dibujar."));
        productos.add(new Productos("Caja de Colores", "utiles", 7500, "resources/img/colores.jpg", "Caja con 12 lápices de colores para pintar."));
        productos.add(new Productos("Regla 30 cm", "utiles", 2500, "resources/img/regla.jpg", "Regla de 30 cm de plástico transparente."));

        // Medicamentos
        productos.add(new Productos("Acetaminofén 500mg", "med", 3000, "resources/img/acetaminofen.jpg", "Tabletas de acetaminofén 500mg para dolor y fiebre."));
        productos.add(new Productos("Ibuprofeno 400mg", "med", 4000, "resources/img/ibuprofeno.jpg", "Tabletas de ibuprofeno 400mg antiinflamatorio."));
        productos.add(new Productos("Alcohol Antiséptico", "med", 6500, "resources/img/alcohol.jpg", "Alcohol para limpieza y desinfección."));
        productos.add(new Productos("Gasa Estéril", "med", 2000, "resources/img/gasas.jpg", "Gasa estéril para primeros auxilios."));

        // Decoración
        productos.add(new Productos("Florero Decorativo", "deco", 25000, "resources/img/florero.jpg", "Florero de vidrio para decoración."));
        productos.add(new Productos("Vela Aromática", "deco", 10000, "resources/img/vela.jpg", "Vela aromática con fragancia relajante."));
        productos.add(new Productos("Cuadro Minimalista", "deco", 35000, "resources/img/cuadro.jpg", "Cuadro minimalista enmarcado."));
        productos.add(new Productos("Planta Artificial", "deco", 18000, "resources/img/planta.jpg", "Planta artificial decorativa."));
    }

    // --- FILTRO ---
    public List<Productos> getProductosFiltrados() {
        if (filtro.equals("todos")) return productos;

        List<Productos> filtrados = new ArrayList<>();
        for (Productos p : productos) {
            if (p.getCategoria().equals(filtro)) {
                filtrados.add(p);
            }
        }
        return filtrados;
    }

    public void aplicarFiltro(String cat) {
        this.filtro = cat;
    }

    // --- CARRITO ---
    public void agregarCarrito(Productos p) {
        carrito.add(p);
    }

    public void eliminarCarrito(Productos p) {
        carrito.remove(p);
    }

    public List<Productos> getCarrito() {
        return carrito;
    }

    public int getTotal() {
        return carrito.stream().mapToInt(Productos::getPrecio).sum();
    }

    public int getCantidadCarrito() {
        return carrito.size();
    }

    // --- NAVEGAR AL FORMULARIO DE PAGO ---
    public String irAlPago() {
        return "pago?faces-redirect=true";
    }
}
