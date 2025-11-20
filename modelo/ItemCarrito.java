package modelo;

public class ItemCarrito {

    private Producto producto;
    private int cantidad;
    private double subtotal;

    // ───────────────────────────────────
    // CONSTRUCTORES
    // ───────────────────────────────────
    public ItemCarrito() {
    }

    public ItemCarrito(Producto producto, int cantidad) {
        this.producto = producto;
        this.cantidad = cantidad;
        calcularSubtotal();
    }

    // ───────────────────────────────────
    // GETTERS Y SETTERS
    // ───────────────────────────────────
    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
        calcularSubtotal();
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
        calcularSubtotal();
    }

    public double getSubtotal() {
        return subtotal;
    }

    // ───────────────────────────────────
    // MÉTODOS DE LÓGICA
    // ───────────────────────────────────
    public void aumentar() {
        this.cantidad++;
        calcularSubtotal();
    }

    public void reducir() {
        if (this.cantidad > 1) {
            this.cantidad--;
        }
        calcularSubtotal();
    }

    // Recalcular subtotal cuando cambie cantidad o producto
    private void calcularSubtotal() {
        if (producto != null) {
            this.subtotal = producto.getPrecioFinal() * cantidad;
        }
    }
}
