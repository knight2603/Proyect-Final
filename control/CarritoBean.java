package control;

import dao.ProductoDAO;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import modelo.ItemCarrito;
import modelo.Producto;
import javax.inject.Named;

@Named("carritoBean")
@SessionScoped
public class CarritoBean implements Serializable {

    private List<ItemCarrito> carrito = new ArrayList<>();
    private double total;

    public List<ItemCarrito> getCarrito() {
        return carrito;
    }

    public double getTotal() {
        return total;
    }

    // AGREGAR DIRECTO
    public void agregar(Producto p) {
        for (ItemCarrito item : carrito) {
            if (item.getProducto().getId() == p.getId()) {
                item.setCantidad(item.getCantidad() + 1);
                calcularTotal();
                return;
            }
        }
        carrito.add(new ItemCarrito(p, 1));
        calcularTotal();
    }

    // AGREGAR POR ID
    public void agregarPorId(Object idProducto) {
        try {
            if (idProducto == null) {
                System.out.println("ID nulo");
                return;
            }

            int id = Integer.parseInt(idProducto.toString());

            ProductoDAO dao = new ProductoDAO();
            Producto p = dao.obtenerPorId(id);

            if (p == null) {
                System.out.println("Producto no encontrado: " + id);
                return;
            }

            agregar(p);

        } catch (Exception e) {
            System.out.println("Error agregarPorId: " + e.getMessage());
        }
    }

    // ELIMINAR
    public void eliminar(int idProducto) {
        carrito.removeIf(i -> i.getProducto().getId() == idProducto);
        calcularTotal();
    }

    // TOTAL
    private void calcularTotal() {
        total = carrito.stream().mapToDouble(ItemCarrito::getSubtotal).sum();
    }

    public String finalizarCompra() {

        // Revisar si existe "user" en sesión (login correcto)
        String userSession = (String) FacesContext.getCurrentInstance()
                .getExternalContext()
                .getSessionMap()
                .get("user");

        // NO está logueado → enviar al login2 y guardar a dónde debe volver
        if (userSession == null) {

            FacesContext.getCurrentInstance().getExternalContext()
                    .getSessionMap()
                    .put("redireccionDespuesLogin", "metodopago.xhtml");

            return "login.xhtml?faces-redirect=true";
        }

        // YA está logueado → continuar a método de pago
        return "metodopago.xhtml?faces-redirect=true";
    }

    public String irMetodoPago() {

        String userSession = (String) FacesContext.getCurrentInstance()
                .getExternalContext()
                .getSessionMap()
                .get("user");

        if (userSession == null) {
            // No logueado → enviarlo al login2
            return "login.xhtml?faces-redirect=true";
        }

        // Logueado → permitir acceso
        return "metodopago.xhtml?faces-redirect=true";
    }

}
