package control;

import java.io.Serializable;
import modelo.Producto;
import dao.ProductoDAO;
import dao.CategoriasDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import modelo.Categorias;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

@ManagedBean(name = "productoBean")
@ViewScoped
public class ProductoBean implements Serializable {

    private Producto producto = new Producto();
    private List<Categorias> listaCategorias;

    private ProductoDAO productoDAO = new ProductoDAO();
    private CategoriasDAO categoriasDAO = new CategoriasDAO();
    private List<Object[]> listaVistaProductos;
    private List<Producto> listaProductosImagen;

    // ✅ Método para cargar la lista de productos con inventario y categoría
    public void cargarVistaProductos() {
        listaVistaProductos = productoDAO.listarVistaProductos();
    }

    public List<Object[]> getListaVistaProductos() {
        if (listaVistaProductos == null) {
            cargarVistaProductos(); // carga automática al renderizar la vista
        }
        return listaVistaProductos;
    }

    public ProductoBean() {
        cargarProductosConImagen();
    }

    @PostConstruct
    public void init() {
        // Cargar listas de categorías y proveedores al iniciar el bean
        listaCategorias = categoriasDAO.listar();
        listaVistaProductos = productoDAO.obtenerVistaProductos();
    }

    private String mensaje;

// Método para agregar un nuevo producto y registrar su stock en inventario
    public void agregarProducto() {
        try {
            boolean agregado = productoDAO.agregarProducto(producto);

            if (agregado) {
                FacesMessage mensajeExito = new FacesMessage(
                        FacesMessage.SEVERITY_INFO,
                        "Éxito",
                        "Producto agregado correctamente y vinculado al inventario."
                );
                FacesContext.getCurrentInstance().addMessage(null, mensajeExito);
                producto = new Producto(); // limpia el formulario
            } else {
                FacesMessage mensajeError = new FacesMessage(
                        FacesMessage.SEVERITY_ERROR,
                        "Error",
                        "No se pudo agregar el producto. Verifica los datos ingresados."
                );
                FacesContext.getCurrentInstance().addMessage(null, mensajeError);
            }

        } catch (Exception e) {
            FacesMessage mensajeExcepcion = new FacesMessage(
                    FacesMessage.SEVERITY_FATAL,
                    "Error inesperado",
                    "Ocurrió un error al intentar agregar el producto: " + e.getMessage()
            );
            FacesContext.getCurrentInstance().addMessage(null, mensajeExcepcion);
            e.printStackTrace();
        }
    }
    // Método para eliminar un producto

    public void eliminarProducto(int idProducto) {
        boolean eliminado = productoDAO.eliminarProducto(idProducto);

        if (eliminado) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Producto eliminado correctamente."));
            cargarVistaProductos(); // refresca lista
        } else {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo eliminar el producto."));
        }
    }

    public void prepararEdicion(Object idProducto) {
        try {
            int id = Integer.parseInt(idProducto.toString()); // ✅ convierte el Object a int
            ProductoDAO dao = new ProductoDAO();
            producto = dao.obtenerPorId(id);

            if (producto == null) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_WARN, "Advertencia", "No se encontró el producto seleccionado."));
            }

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo cargar el producto para editar."));
            e.printStackTrace();
        }
    }

    public void guardarEdicion() {
        try {
            ProductoDAO dao = new ProductoDAO();

            if (producto == null || producto.getId() <= 0) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Producto inválido o sin ID."));
                return;
            }

            System.out.println("=== DEBUG GUARDAR EDICION ===");
            System.out.println("ID: " + producto.getId());
            System.out.println("Nombre: " + producto.getNombre());
            System.out.println("Descripcion: " + producto.getDescripcion());
            System.out.println("Precio: " + producto.getPrecioFinal());
            System.out.println("Stock: " + producto.getStock());
            System.out.println("Categoria: " + producto.getIdCategoria());
            System.out.println("Estado: " + producto.getEstado());
            System.out.println("Imagen: " + producto.getImagen());
            System.out.println("Fecha Caducidad: " + producto.getFechaCaducidad());

            boolean actualizado = dao.actualizarProducto(producto);

            if (actualizado) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Producto actualizado correctamente."));

                // Refrescar lista
                listaVistaProductos = dao.obtenerVistaProductos();

            } else {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo actualizar el producto."));
            }

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Ocurrió un error al actualizar el producto."));
            e.printStackTrace();
        }
    }

    public void listarProductos() {
        ProductoDAO productoDAO = new ProductoDAO();
        try {
            listaVistaProductos = productoDAO.obtenerVistaProductos();
        } catch (Exception e) {
            System.out.println("Error al listar productos: " + e.getMessage());
        }
    }

    public void cargarProductosConImagen() {
        ProductoDAO dao = new ProductoDAO();
        listaProductosImagen = dao.listarProductosConImagen();
    }

    public int getTotalProductos() {
        int total = 0;
        try (Connection con = ConnBD.conectar(); PreparedStatement ps = con.prepareStatement("SELECT COUNT(*) FROM producto")) {

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                total = rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return total;
    }

    // Getters y Setters
    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public String getMensaje() {
        return mensaje;
    }

    public List<Producto> getListaProductosImagen() {
        return listaProductosImagen;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public List<Categorias> getListaCategorias() {
        return listaCategorias;
    }

    public void setListaCategorias(List<Categorias> listaCategorias) {
        this.listaCategorias = listaCategorias;
    }

    public void setListaVistaProductos(List<Object[]> listaVistaProductos) {
        this.listaVistaProductos = listaVistaProductos;
    }

}
