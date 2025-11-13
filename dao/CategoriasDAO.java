package dao;

//Importaciones
import modelo.Categorias;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import control.ConnBD;

public class CategoriasDAO {

    public CategoriasDAO() {
        // Constructor vacío
    }

    //El metodo a la conexion a la base de datos 
    private Connection getConnection() {
        return ConnBD.conectar();
    }

    //Creamos el metodo y listamos las categorias
    public List<Categorias> listar() {
        List<Categorias> lista = new ArrayList<>();
        String sql = "SELECT id, nombre, subcategoria FROM categorias ORDER BY nombre";

        try (Connection con = getConnection(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Categorias cat = new Categorias();
                cat.setId(rs.getInt("id"));
                cat.setNombre(rs.getString("nombre"));
                cat.setSubCategorias(rs.getString("subcategoria"));
                lista.add(cat);
            }

        } catch (SQLException e) {
            System.err.println("❌ Error al listar categorías: " + e.getMessage());
            e.printStackTrace();
        }

        return lista;
    }

    //Agregamos el metodo agregar para nuevas categorias
    public boolean agregar(Categorias categoria) {
        String sql = "INSERT INTO categorias (nombre, subcategoria) VALUES (?, ?)";

        try (Connection con = getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, categoria.getNombre());

            // Manejar descripción nula
            if (categoria.getSubCategorias() != null) {
                ps.setString(2, categoria.getSubCategorias());
            } else {
                ps.setString(2, "");
            }

            int filasAfectadas = ps.executeUpdate();
            System.out.println("✅ Filas afectadas al agregar categoría: " + filasAfectadas);
            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.out.println("❌ Error al agregar categoría: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    //Agregamos el metodo eliminar para eliminar las categorias
    public boolean eliminar(int id) {
        String sql = "DELETE FROM categorias WHERE id = ?";
        try (Connection con = getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            System.out.println("❌ Error al eliminar categoría: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    //Agregamos el metodo buscar por ID las categorias
    public Categorias buscarPorId(int id) {
        String sql = "SELECT id, nombre, subcategorias FROM categorias WHERE id = ?";
        Categorias cat = null;

        try (Connection con = getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                cat = new Categorias();
                cat.setId(rs.getInt("id"));
                cat.setNombre(rs.getString("nombre"));
                cat.setSubCategorias(rs.getString("Subcategorias"));
            }

        } catch (SQLException e) {
            System.out.println("❌ Error al buscar categoría: " + e.getMessage());
            e.printStackTrace();
        }

        return cat;
    }
}
