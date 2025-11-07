package control;

import dao.CategoriasDAO;
import modelo.Categorias;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.List;

@ManagedBean(name = "categoriasBean")
@ViewScoped
public class CategoriasBean implements Serializable {

    private Categorias nuevaCategoria;
    private List<Categorias> listaCategorias;

    public CategoriasBean() {
        nuevaCategoria = new Categorias();
        cargarCategorias();
    }

    public Categorias getNuevaCategoria() {
        return nuevaCategoria;
    }

    public void setNuevaCategoria(Categorias nuevaCategoria) {
        this.nuevaCategoria = nuevaCategoria;
    }

    public List<Categorias> getListaCategorias() {
        return listaCategorias;
    }

    public void cargarCategorias() {
        try {
            CategoriasDAO dao = new CategoriasDAO();
            listaCategorias = dao.listar();
        } catch (Exception e) {
            mostrarError("Error al cargar categorías: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public String agregarCategoria() {
        try {
            // Validaciones
            if (nuevaCategoria.getNombre() == null || nuevaCategoria.getNombre().trim().isEmpty()) {
                mostrarError("El nombre de la categoría es obligatorio");
                return null;
            }

            CategoriasDAO dao = new CategoriasDAO();
            boolean exito = dao.agregar(nuevaCategoria);

            if (exito) {
                mostrarExito("Categoría agregada correctamente");
                nuevaCategoria = new Categorias(); // Limpiar formulario
                cargarCategorias(); // Recargar lista
                return "categorias.xhtml?faces-redirect=true";
            } else {
                mostrarError("No se pudo agregar la categoría");
                return null;
            }

        } catch (Exception e) {
            mostrarError("Error al agregar categoría: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public void eliminarCategoria(int id) {
        try {
            CategoriasDAO dao = new CategoriasDAO();
            boolean eliminado = dao.eliminar(id);
            if (eliminado) {
                mostrarExito("Categoría eliminada correctamente");
                cargarCategorias();
            } else {
                mostrarError("No se pudo eliminar la categoría");
            }
        } catch (Exception e) {
            mostrarError("Error al eliminar categoría: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void mostrarExito(String mensaje) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", mensaje));
    }

    private void mostrarError(String mensaje) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", mensaje));
    }
    // En CategoriasBean - agregar este método

    public int getTotalCategorias() {
        return listaCategorias != null ? listaCategorias.size() : 0;
    }
}
