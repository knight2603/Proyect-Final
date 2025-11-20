package dao;

import modelo.Producto;
import control.ConnBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class CarritoDAO {

    // Verificar stock del producto
    public int obtenerStock(int idProducto) {
        String sql = "SELECT stock FROM producto WHERE id=?";
        try (Connection con = ConnBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idProducto);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("stock");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    // Obtener info producto para agregar al carrito
    public Producto obtenerProducto(int idProducto) {
        String sql = "SELECT * FROM producto WHERE id=?";
        try (Connection con = ConnBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idProducto);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Producto p = new Producto();
                p.setId(rs.getInt("id"));
                p.setNombre(rs.getString("nombre"));
                p.setPrecioFinal(rs.getDouble("precio"));
                return p;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
