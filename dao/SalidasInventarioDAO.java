package dao;

import control.ConnBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import modelo.SalidaInventario;

public class SalidasInventarioDAO {

    public boolean agregarSalida(SalidaInventario salida) {

        String sqlSalida = "INSERT INTO salida_inventario (id_producto, cantidad, estado, fecha_salida, cc_usuarios, observaciones) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        String sqlStock = "UPDATE producto SET stock = stock - ? WHERE id = ? AND stock >= ?";

        Connection con = null;

        try {
            con = ConnBD.conectar();
            con.setAutoCommit(false);

            // Registrar salida
            try (PreparedStatement ps = con.prepareStatement(sqlSalida)) {

                ps.setInt(1, salida.getIdProducto());
                ps.setInt(2, salida.getCantidad());
                ps.setString(3, salida.getEstado());
                ps.setTimestamp(4, new java.sql.Timestamp(salida.getFechaSalida().getTime()));
                ps.setInt(5, salida.getCcUsuario());
                ps.setString(6, salida.getObservaciones());

                if (ps.executeUpdate() <= 0) {
                    con.rollback();
                    return false;
                }
            }

            // Restar stock
            try (PreparedStatement ps2 = con.prepareStatement(sqlStock)) {

                ps2.setInt(1, salida.getCantidad());
                ps2.setInt(2, salida.getIdProducto());
                ps2.setInt(3, salida.getCantidad());

                int filas = ps2.executeUpdate();

                if (filas <= 0) {
                    con.rollback();
                    System.out.println("❌ No hay stock suficiente para el producto ID: " + salida.getIdProducto());
                    return false;
                }
            }

            con.commit();
            return true;

        } catch (SQLException e) {

            System.out.println("❌ Error agregando salida: " + e.getMessage());

            try {
                if (con != null) {
                    con.rollback();
                }
            } catch (SQLException ex) {
                System.out.println("❌ Error en rollback: " + ex.getMessage());
            }

            return false;

        } finally {

            try {
                if (con != null) {
                    con.setAutoCommit(true);
                }
            } catch (SQLException e) {
                System.out.println("❌ Error restaurando AutoCommit: " + e.getMessage());
            }
        }
    }

    // =====================================
    //              LISTAR
    // =====================================
    public List<SalidaInventario> listarSalidas() {

        List<SalidaInventario> lista = new ArrayList<>();

        String sql = "SELECT s.id, p.nombre AS nombre_producto, s.cantidad, s.estado, "
                + "s.fecha_salida, s.cc_usuarios, s.observaciones "
                + "FROM salida_inventario s "
                + "INNER JOIN producto p ON s.id_producto = p.id "
                + "ORDER BY s.fecha_salida DESC";

        try (Connection con = ConnBD.conectar(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                SalidaInventario s = new SalidaInventario();

                s.setId(rs.getInt("id"));
                s.setNombreProducto(rs.getString("nombre_producto")); // ← NUEVO
                s.setCantidad(rs.getInt("cantidad"));
                s.setEstado(rs.getString("estado"));
                s.setFechaSalida(rs.getTimestamp("fecha_salida"));
                s.setCcUsuario(rs.getInt("cc_usuarios"));
                s.setObservaciones(rs.getString("observaciones"));

                lista.add(s);
            }

        } catch (SQLException e) {
            System.out.println("❌ Error listando salidas: " + e.getMessage());
        }

        return lista;
    }

}
