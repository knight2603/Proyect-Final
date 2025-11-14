package control;

import javax.faces.bean.SessionScoped;
import javax.faces.bean.ManagedBean;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import modelo.Productos;

@ManagedBean(name = "carritoBean")
@SessionScoped
public class CarritoBean implements Serializable {

    private List<Productos> carrito = new ArrayList<>();

    // Datos del cliente
    private String nombre;
    private String correo;
    private String direccion;
    private String metodoPago;

    // Total guardado para mostrar en confirmación
    private double totalPagado;

    // ------------------------------
    // GETTERS Y SETTERS
    // ------------------------------

    public List<Productos> getCarrito() {
        return carrito;
    }

    public void setCarrito(List<Productos> carrito) {
        this.carrito = carrito;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }

    public double getTotalPagado() {
        return totalPagado;
    }

    // ------------------------------
    // FUNCIONES DEL CARRITO
    // ------------------------------

    public void agregarProducto(Productos p) {
        carrito.add(p);
    }

    public void eliminarProducto(Productos p) {
        carrito.remove(p);
    }

    public double getTotal() {
        return carrito.stream().mapToDouble(Productos::getPrecio).sum();
    }

    // Cantidad total (badge del header)
    public int getCantidadCarrito() {
        return carrito.size();
    }

    // ------------------------------
    // PROCESAR PAGO
    // ------------------------------
    public String procesarPago() {

        // Guardamos el total antes de limpiar el carrito
        totalPagado = getTotal();

        System.out.println("----- PAGO PROCESADO -----");
        System.out.println("Cliente: " + nombre);
        System.out.println("Correo: " + correo);
        System.out.println("Dirección: " + direccion);
        System.out.println("Método Pago: " + metodoPago);
        System.out.println("Total Pagado: " + totalPagado);

        // Limpiar carrito después
        carrito.clear();

        return "confirmacion?faces-redirect=true";
    }
}
