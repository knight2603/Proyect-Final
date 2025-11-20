package dao;

import control.ConnBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import modelo.Producto;
import modelo.Entradainventario;

public class EntradasInventarioDAO {

    public boolean agregarEntrada(Entradainventario entrada) {

        String sqlEntrada = "INSERT INTO entrada_inventario (id_producto, cantidad, precio, fecha_entrada, cc_usuarios) "
                + "VALUES (?, ?, ?, ?, ?)";

        String sqlStock = "UPDATE producto SET stock = stock + ? WHERE id = ?";

        Connection con = null;

        try {
            con = ConnBD.conectar();
            con.setAutoCommit(false);

            // Registrar entrada
            try (PreparedStatement ps = con.prepareStatement(sqlEntrada)) {

                ps.setInt(1, entrada.getIdProducto());
                ps.setInt(2, entrada.getCantidad());
                ps.setDouble(3, entrada.getPrecio());

                // Permite fecha manual
                ps.setTimestamp(4, new java.sql.Timestamp(entrada.getFechaEntrada().getTime()));

                ps.setInt(5, entrada.getCcUsuario());

                if (ps.executeUpdate() <= 0) {
                    con.rollback();
                    return false;
                }
            }

            // Sumar stock
            try (PreparedStatement ps2 = con.prepareStatement(sqlStock)) {
                ps2.setInt(1, entrada.getCantidad());
                ps2.setInt(2, entrada.getIdProducto());

                if (ps2.executeUpdate() <= 0) {
                    con.rollback();
                    return false;
                }
            }

            con.commit();
            return true;

        } catch (SQLException e) {
            System.out.println("❌ Error agregando entrada: " + e.getMessage());
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

    public List<Entradainventario> listarEntradas() {
        List<Entradainventario> lista = new ArrayList<>();

        String sql = "SELECT ei.*, p.nombre AS nombreProducto, p.stock AS stockActual "
                + "FROM entrada_inventario ei "
                + "JOIN producto p ON ei.id_producto = p.id "
                + "ORDER BY ei.id DESC";

        try (Connection con = ConnBD.conectar(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Entradainventario e = new Entradainventario();

                e.setId(rs.getInt("id"));
                e.setIdProducto(rs.getInt("id_producto"));
                e.setCantidad(rs.getInt("cantidad"));
                e.setPrecio(rs.getDouble("precio"));
                e.setFechaEntrada(rs.getTimestamp("fecha_entrada"));
                e.setCcUsuario(rs.getInt("cc_usuarios"));

                // Campos del JOIN
                e.setNombreProducto(rs.getString("nombreProducto"));
                e.setStockActual(rs.getInt("stockActual"));

                lista.add(e);
            }

        } catch (SQLException e) {
            System.out.println("❌ Error listando entradas: " + e.getMessage());
        }

        return lista;
    }

}
