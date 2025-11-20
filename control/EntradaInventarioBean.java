package control;

import java.io.Serializable;
import java.util.Date;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import dao.EntradasInventarioDAO;
import dao.ProductoDAO;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import modelo.Entradainventario;
import modelo.Producto;

@Named(value = "entradaInventarioBean")
@ViewScoped
public class EntradaInventarioBean implements Serializable {

    private Entradainventario entrada;
    private EntradasInventarioDAO entradaDAO = new EntradasInventarioDAO();

    private List<Producto> listaProductos;
    private List<Entradainventario> listaEntradas;

    public EntradaInventarioBean() {
        entrada = new Entradainventario();
        entrada.setFechaEntrada(new Date()); 
    }

    @PostConstruct
    public void init() {
        listaProductos = new ProductoDAO().listarProductos();
        listarEntradas(); // carga inicial de entradas
    }

    // ============================================
    //     MÉTODO PRINCIPAL: REGISTRAR ENTRADA
    // ============================================
public void registrarEntrada() {
    FacesContext fc = FacesContext.getCurrentInstance();

    try {
        // === VALIDACIONES ===

        if (entrada.getCcUsuario() <= 0) {
            fc.addMessage(null, new FacesMessage(
                    FacesMessage.SEVERITY_WARN,
                    "Validación",
                    "Ingrese la cédula del usuario."
            ));
            return;
        }

        if (entrada.getIdProducto() == 0) {
            fc.addMessage(null, new FacesMessage(
                    FacesMessage.SEVERITY_WARN,
                    "Validación",
                    "Seleccione un producto."
            ));
            return;
        }

        if (entrada.getCantidad() <= 0) {
            fc.addMessage(null, new FacesMessage(
                    FacesMessage.SEVERITY_WARN,
                    "Validación",
                    "La cantidad debe ser mayor que cero."
            ));
            return;
        }

        if (entrada.getFechaEntrada() == null) {
            entrada.setFechaEntrada(new Date());
        }

        // === GUARDAR ENTRADA (DAO maneja la transacción) ===
        boolean ok = entradaDAO.agregarEntrada(entrada);

        if (ok) {
            fc.addMessage(null, new FacesMessage(
                    FacesMessage.SEVERITY_INFO,
                    "Éxito",
                    "Entrada registrada correctamente."
            ));

            listarEntradas(); // refrescar tabla

            // === LIMPIAR FORMULARIO ===
            entrada = new Entradainventario();
            entrada.setFechaEntrada(new Date());

        } else {
            fc.addMessage(null, new FacesMessage(
                    FacesMessage.SEVERITY_ERROR,
                    "Error",
                    "No se pudo registrar la entrada."
            ));
        }

    } catch (Exception ex) {
        ex.printStackTrace();
        fc.addMessage(null, new FacesMessage(
                FacesMessage.SEVERITY_ERROR,
                "Error inesperado",
                ex.getMessage()
        ));
    }
}


    // ============================================
    //     LISTAR ENTRADAS (FUNCIONAL)
    // ============================================
    private void listarEntradas() {
        listaEntradas = entradaDAO.listarEntradas();
    }

    // ============================================
    //                 GETTERS / SETTERS
    // ============================================
    public Entradainventario getEntrada() {
        return entrada;
    }

    public void setEntrada(Entradainventario entrada) {
        this.entrada = entrada;
    }

    public List<Producto> getListaProductos() {
        return listaProductos;
    }

    public List<Entradainventario> getListaEntradas() {
        return listaEntradas;
    }
}
