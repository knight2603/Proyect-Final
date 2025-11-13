
package modelo;


public class DetalleCompra {
    
    private int id, id_compra, id_producto, cantidad;
    private double precio_unitario, descuento_aplicado, subtotal;

    public DetalleCompra() {
    }

    public DetalleCompra(int id, int id_compra, int id_producto, int cantidad, double precio_unitario, double descuento_aplicado, double subtotal) {
        this.id = id;
        this.id_compra = id_compra;
        this.id_producto = id_producto;
        this.cantidad = cantidad;
        this.precio_unitario = precio_unitario;
        this.descuento_aplicado = descuento_aplicado;
        this.subtotal = subtotal;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_compra() {
        return id_compra;
    }

    public void setId_compra(int id_compra) {
        this.id_compra = id_compra;
    }

    public int getId_producto() {
        return id_producto;
    }

    public void setId_producto(int id_producto) {
        this.id_producto = id_producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getPrecio_unitario() {
        return precio_unitario;
    }

    public void setPrecio_unitario(double precio_unitario) {
        this.precio_unitario = precio_unitario;
    }

    public double getDescuento_aplicado() {
        return descuento_aplicado;
    }

    public void setDescuento_aplicado(double descuento_aplicado) {
        this.descuento_aplicado = descuento_aplicado;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }
    
    
    
}
