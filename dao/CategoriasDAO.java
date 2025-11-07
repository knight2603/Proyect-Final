package dao;

import modelo.Categorias;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import control.ConnBD;

public class CategoriasDAO {

    public CategoriasDAO() {
        // Constructor vacío
    }

    private Connection getConnection() {
        return ConnBD.conectar();
    }

    public List<Categorias> listar() {
        List<Categorias> lista = new ArrayList<>();
        String sql = "SELECT id, nombre, descripcion FROM categorias ORDER BY nombre";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Categorias cat = new Categorias();
                cat.setId(rs.getInt("id"));
                cat.setNombre(rs.getString("nombre"));
                cat.setDescripcion(rs.getString("descripcion"));
                lista.add(cat);
            }

        } catch (SQLException e) {
            System.out.println("❌ Error al listar categorías: " + e.getMessage());
            e.printStackTrace();
        }

        return lista;
    }

    public boolean agregar(Categorias categoria) {
        String sql = "INSERT INTO categorias (nombre, descripcion) VALUES (?, ?)";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, categoria.getNombre());
            
            // Manejar descripción nula
            if (categoria.getDescripcion() != null) {
                ps.setString(2, categoria.getDescripcion());
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

    public boolean eliminar(int id) {
        String sql = "DELETE FROM categorias WHERE id = ?";
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            System.out.println("❌ Error al eliminar categoría: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public Categorias buscarPorId(int id) {
        String sql = "SELECT id, nombre, descripcion FROM categorias WHERE id = ?";
        Categorias cat = null;
        
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                cat = new Categorias();
                cat.setId(rs.getInt("id"));
                cat.setNombre(rs.getString("nombre"));
                cat.setDescripcion(rs.getString("descripcion"));
            }
            
        } catch (SQLException e) {
            System.out.println("❌ Error al buscar categoría: " + e.getMessage());
            e.printStackTrace();
        }
        
        return cat;
    }
}