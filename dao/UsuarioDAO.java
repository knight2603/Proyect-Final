
package dao;

//Importanciones
import control.ConnBD;
import control.Utilidades;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import modelo.Usuario;

public class UsuarioDAO {

    //Declaramos los private
    private PreparedStatement ps;
    private ResultSet rs;

    //Agregamos el metodo agregar nuevo usuario
    public void agregar(Usuario usu) {
        try {
            String sql = "INSERT INTO usuarios(cc, nombre, lastname, correo, pswd, tipo, estado) VALUES(?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = ConnBD.conectar().prepareStatement(sql);

            ps.setInt(1, usu.getCc());
            ps.setString(2, usu.getNombre());
            ps.setString(3, usu.getLastname());
            ps.setString(4, usu.getCorreo());

            // Encriptar contraseña con MD5 antes de guardar
            String pass = Utilidades.encriptar(usu.getPswd());
            ps.setString(5, pass);

            // Siempre será "C"
            ps.setString(6, "C");

            ps.setString(7, "Activo");

            ps.executeUpdate();

            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "Usuario creado correctamente"));
        } catch (SQLException e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Error creando usuario: " + e.getMessage()));
        }
    }

//Agregamos el metodo listar
    public List<Usuario> listar() {
        List<Usuario> listaUsuarios = new ArrayList<>();

        try {
            String sql = "SELECT * FROM usuarios";
            ps = ConnBD.conectar().prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Usuario usu = new Usuario();
                usu.setCc(rs.getInt("cc"));
                usu.setNombre(rs.getString("nombre"));
                usu.setLastname(rs.getString("lastname"));
                usu.setCorreo(rs.getString("correo"));
                usu.setTipo(rs.getString("tipo"));
                usu.setEstado(rs.getString("Estado"));
                listaUsuarios.add(usu);
            }

        } catch (SQLException e) {
            e.printStackTrace(); // importante para depuración
        }

        return listaUsuarios;
    }

    //Agregamos el metodo eliminar
    public void eliminar(Usuario usu) {
        String sql = "DELETE FROM usuarios WHERE cc = ?";
        try (Connection conn = ConnBD.conectar(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, usu.getCc()); // pasa CC correctamente
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //Agregamos el metodo actualizar usuario, en el que se podra actualizar el estado entre activo y inactivo
    public void actualizar(Usuario usu) {
        String sql = "UPDATE usuarios SET nombre = ?, lastname = ?, correo = ?, pswd = ?, tipo = ?, estado = ? WHERE cc = ?";

        try (Connection conn = ConnBD.conectar(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, usu.getNombre());
            ps.setString(2, usu.getLastname());
            ps.setString(3, usu.getCorreo());

            // Encriptar la contraseña antes de actualizar
            String passEncriptada = Utilidades.encriptar(usu.getPswd());
            ps.setString(4, passEncriptada);

            ps.setString(5, usu.getTipo());  
            ps.setString(6, usu.getEstado()); 

            ps.setInt(7, usu.getCc()); 

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace(); 
        }
    }
    
    
    //Agregamos el metodo buscar 
    public Usuario buscar(int cc) {
        Usuario usu = null;
        String sql = "SELECT * FROM usuarios WHERE cc = ?";

        try (Connection conn = ConnBD.conectar(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, cc);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    usu = new Usuario();
                    usu.setCc(rs.getInt("cc"));           
                    usu.setNombre(rs.getString("nombre"));
                    usu.setLastname(rs.getString("lastname"));
                    usu.setPswd(rs.getString("pswd"));  
                    usu.setCorreo(rs.getString("correo"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); 
        }

        return usu;
    }

}



