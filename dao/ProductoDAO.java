package dao;

//Importaciones
import modelo.Producto;
import control.ConnBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAO {

    //Agregamos el metodo para agregar un nuevo producto
    public boolean agregarProducto(Producto p) {
        String sql = "INSERT INTO producto (nombre, descripcion, stock, precio_final, id_categoria, fecha_caducidad, estado, imagen) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = ConnBD.conectar(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, p.getNombre());
            ps.setString(2, p.getDescripcion());
            ps.setInt(3, p.getStock());
            ps.setDouble(4, p.getPrecioFinal());
            ps.setInt(5, p.getIdCategoria());

            if (p.getFechaCaducidad() != null) {
                ps.setTimestamp(6, new java.sql.Timestamp(p.getFechaCaducidad().getTime()));
            } else {
                ps.setNull(6, java.sql.Types.TIMESTAMP);
            }

            ps.setString(7, p.getEstado()); // 'Activo', 'Privado' o 'Agotado'
            ps.setString(8, p.getImagen());

            int filas = ps.executeUpdate();
            return filas > 0;

        } catch (SQLException e) {
            System.err.println("❌ Error SQL al agregar producto: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("❌ Error inesperado al agregar producto: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    //Creamos una lista para traer los productos a la vista
    public List<Object[]> listarVistaProductos() {
        List<Object[]> lista = new ArrayList<>();

        String sql = "SELECT p.id, p.nombre, p.descripcion, p.precio_final, "
                + "c.nombre AS categoria, p.stock, p.fecha_caducidad, p.estado "
                + "FROM producto p "
                + "LEFT JOIN categorias c ON p.id_categoria = c.id";

        try (Connection con = ConnBD.conectar(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Object[] fila = new Object[8];
                fila[0] = rs.getInt("id");
                fila[1] = rs.getString("nombre");
                fila[2] = rs.getString("descripcion");
                fila[3] = rs.getDouble("precio_final");
                fila[4] = rs.getString("categoria");
                fila[5] = rs.getInt("stock");
                fila[6] = rs.getTimestamp("fecha_caducidad");
                fila[7] = rs.getString("estado");
                lista.add(fila);
            }

        } catch (SQLException e) {
            System.err.println("❌ Error al listar productos: " + e.getMessage());
            e.printStackTrace();
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
                    p.setStock(rs.getInt("stock"));
                    p.setPrecioFinal(rs.getDouble("precio_final"));
                    p.setIdCategoria(rs.getInt("id_categoria"));
                    p.setFechaCaducidad(rs.getTimestamp("fecha_caducidad")); // datetime → Timestamp
                    p.setEstado(rs.getString("estado"));
                    p.setImagen(rs.getString("imagen"));
                }
            }

        } catch (SQLException e) {
            System.err.println("❌ Error al obtener producto: " + e.getMessage());
            e.printStackTrace();
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

    //Agregamos el metodo actualizar un producto ya existente en la base de datos 
    public boolean actualizarProducto(Producto p) {
        String sql = "UPDATE producto SET nombre = ?, descripcion = ?, stock = ?, precio_final = ?, "
                + "id_categoria = ?, fecha_caducidad = ?, estado = ?, imagen = ? WHERE id = ?";

        try (Connection con = ConnBD.conectar(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, p.getNombre());
            ps.setString(2, p.getDescripcion());
            ps.setInt(3, p.getStock());
            ps.setDouble(4, p.getPrecioFinal());

            if (p.getIdCategoria() > 0) {
                ps.setInt(5, p.getIdCategoria());
            } else {
                ps.setNull(5, java.sql.Types.INTEGER);
            }

            if (p.getFechaCaducidad() != null) {
                ps.setTimestamp(6, new java.sql.Timestamp(p.getFechaCaducidad().getTime()));
            } else {
                ps.setNull(6, java.sql.Types.TIMESTAMP);
            }

            ps.setString(7, p.getEstado());
            ps.setString(8, p.getImagen());
            ps.setInt(9, p.getId());

            int filas = ps.executeUpdate();
            System.out.println("✅ Filas actualizadas: " + filas);
            return filas > 0;

        } catch (SQLException e) {
            System.err.println("❌ Error al actualizar producto: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // 🔹 Método que obtiene la vista completa del producto con JOIN
public List<Object[]> obtenerVistaProductos() {
    List<Object[]> lista = new ArrayList<>();

    String sql = "SELECT "
               + "p.id, "
               + "p.nombre, "
               + "p.descripcion, "
               + "p.precio_final, "
               + "c.nombre AS categoria, "
               + "p.stock, "
               + "p.fecha_caducidad, "
               + "p.estado "
               + "FROM producto p "
               + "LEFT JOIN categorias c ON p.id_categoria = c.id "
               + "ORDER BY p.id DESC";

    try (Connection con = ConnBD.conectar();
         PreparedStatement ps = con.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {

        while (rs.next()) {
            Object[] fila = new Object[8];
            fila[0] = rs.getInt("id");
            fila[1] = rs.getString("nombre");
            fila[2] = rs.getString("descripcion");
            fila[3] = rs.getDouble("precio_final");
            fila[4] = rs.getString("categoria");
            fila[5] = rs.getInt("stock");
            fila[6] = rs.getTimestamp("fecha_caducidad"); // DATETIME → Timestamp
            fila[7] = rs.getString("estado");
            lista.add(fila);
        }
    } catch (SQLException e) {
        System.err.println("❌ Error al obtener vista de productos: " + e.getMessage());
        e.printStackTrace();
    }
    return lista;
}
}