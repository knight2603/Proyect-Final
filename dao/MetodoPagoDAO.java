package dao;

import control.ConnBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import modelo.MetodoPago;


public class MetodoPagoDAO {
    
    
    public List<MetodoPago> listarMetodosPago() {

    List<MetodoPago> lista = new ArrayList<>();

    String sql = "SELECT id, nombre, descripcion FROM metodo_pago ORDER BY nombre ASC";

    try (Connection con = ConnBD.conectar();
         PreparedStatement ps = con.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {

        while (rs.next()) {

            MetodoPago m = new MetodoPago();

            m.setId(rs.getInt("id"));
            m.setNombre(rs.getString("nombre"));
            m.setDescripcion(rs.getString("descripcion"));

            lista.add(m);
        }

    } catch (SQLException e) {
        System.out.println("❌ Error listando métodos de pago: " + e.getMessage());
    }

    return lista;
}

    
}