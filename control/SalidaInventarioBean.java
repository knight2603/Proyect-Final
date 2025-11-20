package control;

import dao.ProductoDAO;
import dao.SalidasInventarioDAO;
import modelo.SalidaInventario;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import modelo.Producto;
import org.primefaces.PrimeFaces;

@ManagedBean(name = "salidaInventarioBean")
@ViewScoped
public class SalidaInventarioBean implements Serializable {

    private SalidasInventarioDAO salidaDAO = new SalidasInventarioDAO();
    private SalidaInventario salida = new SalidaInventario();
    private List<SalidaInventario> listaSalidas = new ArrayList<>();
    private List<Producto> listaProductos;

    @PostConstruct
    public void init() {
        listaProductos = new ProductoDAO().listarProductos();
        listar(); // cargar salidas aquí
    }

    // ==========================
    //     REGISTRAR SALIDA
    // ==========================
    public void registrar() {

        try {
            salida.setFechaSalida(new Date());

            boolean ok = salidaDAO.agregarSalida(salida);

            if (ok) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO,
                                "Éxito", "Salida registrada correctamente."));

                salida = new SalidaInventario();
                listar();

                PrimeFaces.current().ajax().update("formSalida", "tablaSalidas");

            } else {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                "Error", "No se pudo registrar la salida. Revise stock disponible."));
            }

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_FATAL,
                            "Error", "Ocurrió un problema: " + e.getMessage()));
        }
    }

    // ==========================
    //          LISTAR
    // ==========================
    public void listar() {
        listaSalidas = salidaDAO.listarSalidas();
    }

    // ==========================
    //     GETTERS & SETTERS
    // ==========================
    public SalidaInventario getSalida() {
        return salida;
    }

    public void setSalida(SalidaInventario salida) {
        this.salida = salida;
    }

    public List<SalidaInventario> getListaSalidas() {
        return listaSalidas;
    }

    public void setListaSalidas(List<SalidaInventario> listaSalidas) {
        this.listaSalidas = listaSalidas;
    }

    public List<Producto> getListaProductos() {
        return listaProductos;
    }

}
