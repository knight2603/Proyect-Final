package control;

import dao.UsuarioDAO;
import java.io.File;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.component.UIComponent;
import javax.faces.validator.ValidatorException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import modelo.Usuario;
import net.sf.jasperreports.engine.*;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.primefaces.model.file.UploadedFile;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@ManagedBean
public class UsuarioBean {

    public void exportarPDF() {
        try {
            String path = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reporteusuarios.jasper");
            File jasper = new File(path);
            UsuarioDataSource uds = new UsuarioDataSource();

            JasperPrint jprint = JasperFillManager.fillReport(jasper.getPath(), null, uds);

            HttpServletResponse resp = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

            resp.addHeader("Content-disposition", "atachment; filename=Usuarios.pdf");

            try (ServletOutputStream stream = resp.getOutputStream()) {
                JasperExportManager.exportReportToPdfStream(jprint, stream);

                stream.flush();
                stream.close();

            }

        } catch (JRException | IOException e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Error creando reporte"));
        }

    }

    private UploadedFile excel;

    Usuario usuario = new Usuario();
    private List<Usuario> listaUsuarios;

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public List<Usuario> getListaUsuarios() {
        return listaUsuarios;
    }

    public void setListaUsuarios(List<Usuario> listaUsuarios) {
        this.listaUsuarios = listaUsuarios;
    }

    @PostConstruct
    public void init() {
        listar();
    }

    public String authLogin() {
        try {
            Connection con = ConnBD.conectar();

            String sql = "SELECT * FROM usuarios WHERE correo = ? AND pswd = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, usuario.getCorreo());

            String pass = Utilidades.encriptar(usuario.getPswd());
            ps.setString(2, pass);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                // guardar usuario en sesión
                FacesContext.getCurrentInstance().getExternalContext()
                        .getSessionMap()
                        .put("user", rs.getString("nombre"));

                String tipo = rs.getString("tipo");

// SI VIENE DEL CARRITO
                String redirect = (String) FacesContext.getCurrentInstance()
                        .getExternalContext()
                        .getSessionMap()
                        .get("redireccionDespuesLogin");

                if (redirect != null) {
                    FacesContext.getCurrentInstance().getExternalContext()
                            .getSessionMap().remove("redireccionDespuesLogin"); // limpiar
                    return redirect + "?faces-redirect=true";
                }

// LOGIN NORMAL
                if (tipo.equals("C")) {
                    return "cliente.xhtml?faces-redirect=true";
                } else {
                    return "admin.xhtml?faces-redirect=true";
                }

            } else {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_WARN,
                                "Aviso", "Correo y/o contraseña no válidos"));
                return null;
            }

        } catch (SQLException e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_FATAL,
                            "Error", "Error en conexión a base de datos"));
            return null;
        }
    }

    public void auth() {
        String res = authLogin();
        if (res != null) {
            try {
                FacesContext.getCurrentInstance().getExternalContext().redirect(res);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String cerrarSesion() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "/login.xhtml?faces-redirect=true";
    }

    public void verifSesion() {
        FacesContext fc = FacesContext.getCurrentInstance();
        String nom = (String) fc.getExternalContext().getSessionMap().get("user");

        if (nom == null) {
            try {
                // Redirige a noacceso.xhtml usando la ruta completa del contexto
                fc.getExternalContext().redirect(fc.getExternalContext().getRequestContextPath() + "/noacceso.xhtml");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void registrar() {
        UsuarioDAO dao = new UsuarioDAO();
        dao.agregar(usuario);
        usuario = new Usuario();
    }

    public void listar() {
        UsuarioDAO dao = new UsuarioDAO();
        listaUsuarios = dao.listar();
    }

    public void migrar() {
        if (excel == null) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se ha seleccionado archivo Excel"));
            return;
        }

        try (InputStream is = excel.getInputStream(); Workbook libro = WorkbookFactory.create(is)) {

            if (libro.getNumberOfSheets() == 0) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "El archivo Excel no contiene hojas"));
                return;
            }

            Sheet hoja = libro.getSheetAt(0);
            DataFormatter formatter = new DataFormatter();
            UsuarioDAO usuarioDAO = new UsuarioDAO();

            int firstRow = hoja.getFirstRowNum() + 1;
            int lastRow = hoja.getLastRowNum();

            for (int r = firstRow; r <= lastRow; r++) {
                Row fila = hoja.getRow(r);
                if (fila == null) {
                    continue;
                }

                try {
                    String sCc = formatter.formatCellValue(fila.getCell(0, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK)).trim();
                    String nombre = formatter.formatCellValue(fila.getCell(1, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK)).trim();
                    String lastname = formatter.formatCellValue(fila.getCell(2, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK)).trim();
                    String correo = formatter.formatCellValue(fila.getCell(3, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK)).trim();
                    String pswd = formatter.formatCellValue(fila.getCell(4, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK)).trim();

                    if (sCc.isEmpty() && nombre.isEmpty() && lastname.isEmpty() && correo.isEmpty() && pswd.isEmpty()) {
                        continue;
                    }

                    Usuario usu = new Usuario();

                    if (!sCc.isEmpty()) {
                        try {
                            double d = Double.parseDouble(sCc);
                            usu.setCc((int) d);
                        } catch (NumberFormatException nf) {
                            usu.setCc(0);
                        }
                    } else {
                        usu.setCc(0);
                    }

                    usu.setNombre(nombre);
                    usu.setLastname(lastname);
                    usu.setCorreo(correo);
                    usu.setPswd(pswd);

                    usuarioDAO.agregar(usu);

                } catch (Exception filaEx) {
                    System.err.println("Error al procesar fila " + (r + 1) + ": " + filaEx.getMessage());
                    filaEx.printStackTrace();

                    FacesContext.getCurrentInstance().addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
                                    "Error procesando fila " + (r + 1) + ". Revisa el formato del archivo."));
                    return;
                }
            }

            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "Usuarios migrados exitosamente"));

        } catch (IOException | EncryptedDocumentException | InvalidFormatException e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Error al leer el archivo Excel: " + e.getMessage()));
        }
    }

    public UploadedFile getExcel() {
        return excel;
    }

    public void setExcel(UploadedFile excel) {
        this.excel = excel;
    }

    public void eliminar(Usuario usu) {
        UsuarioDAO dao = new UsuarioDAO();
        dao.eliminar(usu);

        listaUsuarios.remove(usu);

        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso",
                        "Usuario eliminado correctamente"));
    }

    public void actualizar() {
        UsuarioDAO dao = new UsuarioDAO();
        dao.actualizar(usuario);
    }

    public void editar(Usuario u) {
        UsuarioDAO dao = new UsuarioDAO();
        usuario = dao.buscar(u.getCc());
    }

    public void validarGmail(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        String correo = (String) value;
        if (correo == null || !correo.endsWith("@gmail.com")) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "El correo debe ser de Gmail",
                    "El correo debe terminar con @gmail.com");
            throw new ValidatorException(msg);
        }
    }

    public void validarContrasena(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        String pswd = (String) value;

        String pattern = "^(?=.*[A-Z])(?=.*\\d).{8,}$";

        if (pswd == null || !pswd.matches(pattern)) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Contraseña inválida",
                    "La contraseña debe tener mínimo 8 caracteres, incluir una mayúscula y un número");
            throw new ValidatorException(msg);
        }
    }

    public int getTotalUsuarios() {
        int total = 0;
        try (Connection con = ConnBD.conectar(); PreparedStatement ps = con.prepareStatement("SELECT COUNT(*) FROM usuarios")) {

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                total = rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return total;
    }

}
