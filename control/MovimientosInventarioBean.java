package control;

import dao.MovimientosInventarioDAO;
import dao.ProveedoresDAO;
import modelo.MovimientosInventario;
import modelo.Proveedores;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpSession;

@ManagedBean(name = "movimientosBean")
@ViewScoped
public class MovimientosInventarioBean implements Serializable {

    private MovimientosInventario nuevoMovimiento;
    private MovimientosInventario movimientoSeleccionado;
    private List<MovimientosInventario> listaMovimientos;
    private List<Proveedores> listaProveedores;

    // Para filtros
    private String filtroTipo;
    private Integer filtroProducto;
    private Date filtroFechaInicio;
    private Date filtroFechaFin;

    // Tipos de movimiento disponibles
    private static final String[] TIPOS_MOVIMIENTO = {
        "ENTRADA", "SALIDA", "PROMOCION", "DEVOLUCION", "AJUSTE"
    };

    public MovimientosInventarioBean() {
        nuevoMovimiento = new MovimientosInventario();
        // Establecer fecha actual del momento exacto
        nuevoMovimiento.setFechaMovimiento(new Date());
        cargarMovimientos();
        cargarProveedores();
    }



    // ✅ Getters y Setters
    public MovimientosInventario getNuevoMovimiento() {
        return nuevoMovimiento;
    }

    public void setNuevoMovimiento(MovimientosInventario nuevoMovimiento) {
        this.nuevoMovimiento = nuevoMovimiento;
    }

    public MovimientosInventario getMovimientoSeleccionado() {
        return movimientoSeleccionado;
    }

    public void setMovimientoSeleccionado(MovimientosInventario movimientoSeleccionado) {
        this.movimientoSeleccionado = movimientoSeleccionado;
    }

    public List<MovimientosInventario> getListaMovimientos() {
        return listaMovimientos;
    }

    public void setListaMovimientos(List<MovimientosInventario> listaMovimientos) {
        this.listaMovimientos = listaMovimientos;
    }

    public List<Proveedores> getListaProveedores() {
        return listaProveedores;
    }

    public void setListaProveedores(List<Proveedores> listaProveedores) {
        this.listaProveedores = listaProveedores;
    }

    public String getFiltroTipo() {
        return filtroTipo;
    }

    public void setFiltroTipo(String filtroTipo) {
        this.filtroTipo = filtroTipo;
    }

    public Integer getFiltroProducto() {
        return filtroProducto;
    }

    public void setFiltroProducto(Integer filtroProducto) {
        this.filtroProducto = filtroProducto;
    }

    public Date getFiltroFechaInicio() {
        return filtroFechaInicio;
    }

    public void setFiltroFechaInicio(Date filtroFechaInicio) {
        this.filtroFechaInicio = filtroFechaInicio;
    }

    public Date getFiltroFechaFin() {
        return filtroFechaFin;
    }

    public void setFiltroFechaFin(Date filtroFechaFin) {
        this.filtroFechaFin = filtroFechaFin;
    }

    public String[] getTIPOS_MOVIMIENTO() {
        return TIPOS_MOVIMIENTO;
    }

    // ✅ Obtener usuario logueado desde la sesión
    private int obtenerUsuarioLogueado() {
        try {
            HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
                    .getExternalContext().getSession(false);
            if (session != null) {
                // Asumiendo que guardas el ID del usuario en la sesión como "userId"
                Integer userId = (Integer) session.getAttribute("userId");
                if (userId != null) {
                    return userId;
                }

                // Si no está como "userId", intenta obtenerlo de "user"
                Object user = session.getAttribute("user");
                if (user != null) {
                    // Aquí deberías adaptar según cómo guardes el usuario en tu sesión
                    // Por ahora retornamos 1 como valor por defecto
                    return 1;
                }
            }
        } catch (Exception e) {
            System.out.println("❌ Error al obtener usuario logueado: " + e.getMessage());
        }

        // Valor por defecto si no se puede obtener el usuario
        return 1;
    }

    // ✅ Cargar todos los proveedores
    private void cargarProveedores() {
        try {
            ProveedoresDAO proveedoresDAO = new ProveedoresDAO();
            listaProveedores = proveedoresDAO.listar();
        } catch (Exception e) {
            mostrarError("Error al cargar proveedores: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ✅ Cargar todos los movimientos
    public void cargarMovimientos() {
        try {
            MovimientosInventarioDAO dao = new MovimientosInventarioDAO();
            listaMovimientos = dao.listar();
        } catch (Exception e) {
            mostrarError("Error al cargar movimientos: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ✅ Agregar nuevo movimiento
    public String agregarMovimiento() {
        try {
            // Validaciones
            if (nuevoMovimiento.getIdProducto() <= 0) {
                mostrarError("Debe seleccionar un producto válido");
                return null;
            }

            if (nuevoMovimiento.getTipoMovimiento() == null || nuevoMovimiento.getTipoMovimiento().trim().isEmpty()) {
                mostrarError("Debe seleccionar un tipo de movimiento");
                return null;
            }

            if (nuevoMovimiento.getCantidad() <= 0) {
                mostrarError("La cantidad debe ser mayor a cero");
                return null;
            }

            // ✅ Establecer usuario logueado
            int usuarioLogueado = obtenerUsuarioLogueado();
            nuevoMovimiento.setIdUsuario(usuarioLogueado);

            // ✅ Establecer fecha actual del momento exacto
            nuevoMovimiento.setFechaMovimiento(new Date());

            MovimientosInventarioDAO dao = new MovimientosInventarioDAO();
            boolean exito = dao.agregar(nuevoMovimiento);

            if (exito) {
                mostrarExito("Movimiento registrado correctamente");

                // Limpiar formulario
                limpiarFormulario();

                // Recargar lista
                cargarMovimientos();

                return "movimientos.xhtml?faces-redirect=true";
            } else {
                mostrarError("No se pudo registrar el movimiento");
                return null;
            }

        } catch (Exception e) {
            mostrarError("Error al registrar movimiento: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    // ✅ Filtrar movimientos
    public void filtrarMovimientos() {
        try {
            MovimientosInventarioDAO dao = new MovimientosInventarioDAO();

            if (filtroTipo != null && !filtroTipo.isEmpty()) {
                listaMovimientos = dao.listarPorTipo(filtroTipo);
            } else if (filtroProducto != null && filtroProducto > 0) {
                listaMovimientos = dao.listarPorProducto(filtroProducto);
            } else if (filtroFechaInicio != null && filtroFechaFin != null) {
                listaMovimientos = dao.listarPorRangoFechas(filtroFechaInicio, filtroFechaFin);
            } else {
                listaMovimientos = dao.listar();
            }

            mostrarExito("Filtro aplicado correctamente");

        } catch (Exception e) {
            mostrarError("Error al aplicar filtro: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ✅ Limpiar filtros
    public void limpiarFiltros() {
        filtroTipo = null;
        filtroProducto = null;
        filtroFechaInicio = null;
        filtroFechaFin = null;
        cargarMovimientos();
        mostrarExito("Filtros limpiados");
    }

    // ✅ Eliminar movimiento
    public void eliminarMovimiento(int id) {
        try {
            MovimientosInventarioDAO dao = new MovimientosInventarioDAO();
            boolean eliminado = dao.eliminar(id);
            if (eliminado) {
                mostrarExito("Movimiento eliminado correctamente");
                cargarMovimientos();
            } else {
                mostrarError("No se pudo eliminar el movimiento");
            }
        } catch (Exception e) {
            mostrarError("Error al eliminar movimiento: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ✅ Métodos auxiliares para mensajes
    private void mostrarExito(String mensaje) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", mensaje));
    }

    private void mostrarError(String mensaje) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", mensaje));
    }

    // ✅ Método para obtener el total de movimientos (para dashboard)
    public int getTotalMovimientos() {
        return listaMovimientos != null ? listaMovimientos.size() : 0;
    }

    // ✅ Método para limpiar formulario
    public void limpiarFormulario() {
        nuevoMovimiento = new MovimientosInventario();
        nuevoMovimiento.setFechaMovimiento(new Date());
    }

    // ✅ Método para obtener el nombre del usuario logueado (para mostrar en la vista)
    public String getNombreUsuarioLogueado() {
        try {
            HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
                    .getExternalContext().getSession(false);
            if (session != null) {
                return (String) session.getAttribute("user");
            }
        } catch (Exception e) {
            System.out.println("❌ Error al obtener nombre de usuario: " + e.getMessage());
        }
        return "Administrador";
    }

}
