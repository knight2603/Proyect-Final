package dao;

import modelo.Producto;
import control.ConnBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAO {

    public boolean agregarProducto(Producto p) {
        String sqlProducto = "INSERT INTO producto (nombre, descripcion, precio, id_categoria, fecha_caducidad, estado, foto) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        String sqlInventario = "INSERT INTO inventario (id_producto, stock) VALUES (?, ?)";

        try (Connection con = ConnBD.conectar(); PreparedStatement psProducto = con.prepareStatement(sqlProducto, PreparedStatement.RETURN_GENERATED_KEYS)) {

            // Inserta producto
            psProducto.setString(1, p.getNombre());
            psProducto.setString(2, p.getDescripcion());
            psProducto.setDouble(3, p.getPrecio());
            psProducto.setInt(4, p.getIdCategoria());
            psProducto.setDate(5, p.getFechaCaducidad() != null ? new java.sql.Date(p.getFechaCaducidad().getTime()) : null);
            psProducto.setString(6, p.getEstado());
            psProducto.setString(7, p.getFoto());

            int filas = psProducto.executeUpdate();

            if (filas > 0) {
                // Obtener ID generado
                ResultSet rs = psProducto.getGeneratedKeys();
                if (rs.next()) {
                    int idGenerado = rs.getInt(1);

                    // Insertar en inventario
                    try (PreparedStatement psInventario = con.prepareStatement(sqlInventario)) {
                        psInventario.setInt(1, idGenerado);
                        psInventario.setInt(2, p.getStockInicial());
                        psInventario.executeUpdate();
                    }
                }
                return true;
            }
        } catch (Exception e) {
            System.out.println("Error al agregar producto e inventario: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public List<Object[]> listarVistaProductos() {
        List<Object[]> lista = new ArrayList<>();
        String sql = "SELECT p.id, p.nombre, p.descripcion, p.precio, "
                + "c.nombre AS categoria, i.stock, p.fecha_caducidad, p.estado "
                + "FROM producto p "
                + "LEFT JOIN categorias c ON p.id_categoria = c.id "
                + "LEFT JOIN inventario i ON p.id = i.id_producto";

        try (Connection con = ConnBD.conectar(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Object[] fila = new Object[8];
                fila[0] = rs.getInt("id");
                fila[1] = rs.getString("nombre");
                fila[2] = rs.getString("descripcion");
                fila[3] = rs.getDouble("precio");
                fila[4] = rs.getString("categoria");
                fila[5] = rs.getInt("stock");
                fila[6] = rs.getDate("fecha_caducidad");
                fila[7] = rs.getString("estado");
                lista.add(fila);
            }

        } catch (SQLException e) {
            System.out.println("❌ Error al listar productos con inventario: " + e.getMessage());
        }

        return lista;
    }

    // Obtener un producto por su ID
    public Producto obtenerPorId(int id) {
        Producto p = null;
        String sql = "SELECT * FROM producto WHERE id = ?";
        try (Connection con = ConnBD.conectar(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    p = new Producto();
                    p.setId(rs.getInt("id"));
                    p.setNombre(rs.getString("nombre"));
                    p.setDescripcion(rs.getString("descripcion"));
                    p.setPrecio(rs.getDouble("precio"));
                    p.setIdCategoria(rs.getInt("id_categoria"));
                    p.setFechaCaducidad(rs.getDate("fecha_caducidad"));
                    p.setEstado(rs.getString("estado"));
                    p.setFoto(rs.getString("foto"));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener producto: " + e.getMessage());
        }
        return p;
    }

    // Eliminar producto por ID
    public boolean eliminarProducto(int id) {
        String sql = "DELETE FROM producto WHERE id = ?";
        try (Connection con = ConnBD.conectar(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al eliminar producto: " + e.getMessage());
            return false;
        }
    }

    public boolean actualizarProducto(Producto p) {
        String sql = "UPDATE producto SET nombre=?, descripcion=?, precio=?, id_categoria=?, estado=? WHERE id=?";

        try (Connection con = ConnBD.conectar(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, p.getNombre());
            ps.setString(2, p.getDescripcion());
            ps.setDouble(3, p.getPrecio());

            if (p.getIdCategoria() > 0) {
                ps.setInt(4, p.getIdCategoria());
            } else {
                ps.setNull(4, java.sql.Types.INTEGER);
            }

            ps.setString(5, p.getEstado());
            ps.setInt(6, p.getId());

            int filas = ps.executeUpdate();
            System.out.println("✅ Filas actualizadas: " + filas);
            return filas > 0;

        } catch (SQLException e) {
            System.out.println("❌ Error al actualizar producto: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // 🔹 Método que obtiene la vista completa del producto con JOIN
    public List<Object[]> obtenerVistaProductos() {
        List<Object[]> lista = new ArrayList<>();

        String sql = "          SELECT \n"
                + "                p.id, \n"
                + "                p.nombre, \n"
                + "                p.descripcion, \n"
                + "                p.precio, \n"
                + "                c.nombre AS categoria, \n"
                + "                i.stock, \n"
                + "                p.fecha_caducidad, \n"
                + "                p.estado\n"
                + "            FROM producto p\n"
                + "            LEFT JOIN categorias c ON p.id_categoria = c.id\n"
                + "            LEFT JOIN inventario i ON p.id = i.id_producto\n"
                + "            ORDER BY p.id DESC";

        try (Connection con = ConnBD.conectar(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Object[] fila = new Object[8];
                fila[0] = rs.getInt("id");
                fila[1] = rs.getString("nombre");
                fila[2] = rs.getString("descripcion");
                fila[3] = rs.getDouble("precio");
                fila[4] = rs.getString("categoria");
                fila[5] = rs.getInt("stock");
                fila[6] = rs.getDate("fecha_caducidad");
                fila[7] = rs.getString("estado");
                lista.add(fila);
            }

        } catch (Exception e) {
            System.out.println("Error al obtener vista de productos: " + e.getMessage());
            e.printStackTrace();
        }

        return lista;
    }

}
