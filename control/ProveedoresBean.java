package control;

import dao.ProveedoresDAO;
import modelo.Proveedores;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.List;

@ManagedBean(name = "proveedoresBean")
@ViewScoped
public class ProveedoresBean implements Serializable {

    private Proveedores nuevoProveedor;
    private Proveedores proveedorSeleccionado;
    private List<Proveedores> listaProveedores;
    private transient ProveedoresDAO dao;

    // ✅ Constructor
    public ProveedoresBean() {
        nuevoProveedor = new Proveedores();
        proveedorSeleccionado = new Proveedores();
        cargarProveedores();
    }

    // ✅ Inicializar DAO si es null (para serialización)
    private ProveedoresDAO getDao() {
        if (dao == null) {
            dao = new ProveedoresDAO();
        }
        return dao;
    }

    // ✅ Getters y Setters
    public Proveedores getNuevoProveedor() {
        return nuevoProveedor;
    }

    public void setNuevoProveedor(Proveedores nuevoProveedor) {
        this.nuevoProveedor = nuevoProveedor;
    }

    public Proveedores getProveedorSeleccionado() {
        return proveedorSeleccionado;
    }

    public void setProveedorSeleccionado(Proveedores proveedorSeleccionado) {
        this.proveedorSeleccionado = proveedorSeleccionado;
    }

    public List<Proveedores> getListaProveedores() {
        return listaProveedores;
    }

    public void setListaProveedores(List<Proveedores> listaProveedores) {
        this.listaProveedores = listaProveedores;
    }

    // ✅ Cargar todos los proveedores desde la base de datos
    public void cargarProveedores() {
        try {
            listaProveedores = getDao().listar();
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Error", "No se pudieron cargar los proveedores"));
            e.printStackTrace();
        }
    }

    // ✅ Agregar un nuevo proveedor
    public String agregarProveedor() {
        try {
            // Validaciones básicas
            if (nuevoProveedor.getNombre() == null || nuevoProveedor.getNombre().trim().isEmpty()) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_WARN,
                                "Advertencia", "El nombre del proveedor es obligatorio"));
                return null;
            }

            boolean exito = getDao().agregar(nuevoProveedor);

            if (exito) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO,
                                "Éxito", "Proveedor agregado correctamente"));

                // 🧹 Limpia el formulario
                nuevoProveedor = new Proveedores();

                // 🔄 Refresca la tabla
                cargarProveedores();

                return "proveedores?faces-redirect=true";
            } else {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                "Error", "No se pudo agregar el proveedor"));
                return null;
            }

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_FATAL,
                            "Error Fatal", "Error al agregar proveedor: " + e.getMessage()));
            e.printStackTrace();
            return null;
        }
    }

    // ✅ Eliminar proveedor
    public void eliminarProveedor(int id) {
        try {
            boolean eliminado = getDao().eliminar(id);
            if (eliminado) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO,
                                "Eliminado", "Proveedor eliminado correctamente"));
                cargarProveedores();
            } else {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                "Error", "No se pudo eliminar el proveedor"));
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_FATAL,
                            "Error Fatal", "Error al eliminar proveedor: " + e.getMessage()));
            e.printStackTrace();
        }
    }

    // ✅ Cargar proveedor para edición
    public String cargarParaEditar(int id) {
        try {
            proveedorSeleccionado = getDao().buscarPorId(id);
            if (proveedorSeleccionado != null) {
                return "editarprov?faces-redirect=true";
            } else {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                "Error", "Proveedor no encontrado"));
                return null;
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_FATAL,
                            "Error Fatal", "Error al cargar proveedor: " + e.getMessage()));
            e.printStackTrace();
            return null;
        }
    }

    // ✅ Método para limpiar formulario
    public void limpiarFormulario() {
        nuevoProveedor = new Proveedores();
    }
    // En ProveedoresBean - agregar este método

    public int getTotalProveedores() {
        return listaProveedores != null ? listaProveedores.size() : 0;
    }
}
