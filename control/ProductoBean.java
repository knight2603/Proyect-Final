package control;

import java.io.Serializable;
import modelo.Producto;
import dao.ProductoDAO;
import dao.CategoriasDAO;
import dao.ProveedoresDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import modelo.Categorias;
import modelo.Proveedores;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

@ManagedBean(name = "productoBean")
@ViewScoped
public class ProductoBean implements Serializable {

    private Producto producto = new Producto();
    private List<Categorias> listaCategorias;
    private List<Proveedores> listaProveedores;

    private ProductoDAO productoDAO = new ProductoDAO();
    private CategoriasDAO categoriasDAO = new CategoriasDAO();
    private ProveedoresDAO proveedoresDAO = new ProveedoresDAO();
    private List<Object[]> listaVistaProductos;

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

    // Método para preparar la edición de un producto
    public void prepararEdicion(int idProducto) {
        producto = productoDAO.obtenerPorId(idProducto);
    }

    @PostConstruct
    public void init() {
        // Cargar listas de categorías y proveedores al iniciar el bean
        listaCategorias = categoriasDAO.listar();
        listaProveedores = proveedoresDAO.listar();
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

            if (producto != null && producto.getId() > 0) {
                boolean actualizado = dao.actualizarProducto(producto);

                if (actualizado) {
                    FacesContext.getCurrentInstance().addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Producto actualizado correctamente."));

                    // Refresca la lista después de guardar los cambios
                    listaVistaProductos = dao.obtenerVistaProductos();

                } else {
                    FacesContext.getCurrentInstance().addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo actualizar el producto."));
                }
            } else {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Producto inválido o sin ID."));
            }

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", e.getMessage()));
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

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public List<Categorias> getListaCategorias() {
        return listaCategorias;
    }

    public void setListaCategorias(List<Categorias> listaCategorias) {
        this.listaCategorias = listaCategorias;
    }

    public List<Proveedores> getListaProveedores() {
        return listaProveedores;
    }

    public void setListaProveedores(List<Proveedores> listaProveedores) {
        this.listaProveedores = listaProveedores;
    }

    public void setListaVistaProductos(List<Object[]> listaVistaProductos) {
        this.listaVistaProductos = listaVistaProductos;
    }

}
