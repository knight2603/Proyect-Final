
package dao;

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


    private PreparedStatement ps;
    private ResultSet rs;
    
public void agregar(Usuario usu) {
    try {
        String sql = "INSERT INTO usuarios(cc, nombre, lastname, correo, pswd, tipo) VALUES(?, ?, ?, ?, ?, ?)";
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

        ps.executeUpdate();

        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "Usuario creado correctamente"));
    } catch (SQLException e) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Error creando usuario: " + e.getMessage()));
    }
}

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

            listaUsuarios.add(usu);
        }

    } catch (SQLException e) {
        e.printStackTrace(); 
    }

    return listaUsuarios;
}

public void eliminar(Usuario usu) {
    String sql = "DELETE FROM usuarios WHERE cc = ?"; 
    try (Connection conn = ConnBD.conectar();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        
        ps.setInt(1, usu.getCc()); // pasa CC correctamente
        ps.executeUpdate();
        
    } catch (SQLException e) {
        e.printStackTrace(); 
    }
}
public void actualizar(Usuario usu){
    String sql = "UPDATE usuarios SET cc = ?, nombre = ?, lastname = ?, pswd = ? WHERE cc = ?";
    
    try (Connection conn = ConnBD.conectar();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setInt(1, usu.getCc()); // cc es int
        ps.setString(2, usu.getNombre());
        ps.setString(3, usu.getLastname());

        // Encriptar la contraseña antes de actualizar
        String passEncriptada = Utilidades.encriptar(usu.getPswd());
        ps.setString(4, passEncriptada);

        ps.setInt(5, usu.getCc()); // cc del usuario a actualizar

        ps.executeUpdate();

    } catch (SQLException e) {
        e.printStackTrace(); // para depuración
    }
}
public Usuario buscar(int cc) {
    Usuario usu = null;
    String sql = "SELECT * FROM usuarios WHERE cc = ?";

    try (Connection conn = ConnBD.conectar();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setInt(1, cc);
        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                usu = new Usuario();
                usu.setCc(rs.getInt("cc"));           
                usu.setNombre(rs.getString("nombre"));
                usu.setLastname(rs.getString("lastname"));
                usu.setPswd(rs.getString("pswd"));   // Contraseña encriptada
                usu.setCorreo(rs.getString("correo"));
            }
        }
    } catch (SQLException e) {
        e.printStackTrace(); // loguear el error
    }

    return usu;
}



}
