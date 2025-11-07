package dao;

import modelo.Proveedores;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import control.ConnBD;

public class ProveedoresDAO {

    // No mantener conexión como atributo, crear una nueva por operación
    public ProveedoresDAO() {
        // Constructor vacío, conexión se crea por operación
    }

    private Connection getConnection() {
        return ConnBD.conectar();
    }

    /**
     * ✅ Listar todos los proveedores
     */
    public List<Proveedores> listar() {
        List<Proveedores> lista = new ArrayList<>();
        String sql = "SELECT id, nombre, almacen, cadena, telefono, correo, direccion FROM proveedores ORDER BY nombre";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Proveedores p = new Proveedores();
                p.setId(rs.getInt("id"));
                p.setNombre(rs.getString("nombre"));
                p.setAlmacen(rs.getString("almacen"));
                p.setCadena(rs.getString("cadena"));
                p.setTelefono(rs.getString("telefono"));
                p.setCorreo(rs.getString("correo"));
                p.setDireccion(rs.getString("direccion"));
                lista.add(p);
            }

        } catch (SQLException e) {
            System.out.println("❌ Error al listar proveedores: " + e.getMessage());
            e.printStackTrace();
        }

        return lista;
    }

    /**
     * ✅ Agregar nuevo proveedor
     */
    public boolean agregar(Proveedores p) {
        String sql = "INSERT INTO proveedores (nombre, almacen, cadena, telefono, correo, direccion) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, p.getNombre());
            ps.setString(2, p.getAlmacen() != null ? p.getAlmacen() : "");
            ps.setString(3, p.getCadena() != null ? p.getCadena() : "");
            ps.setString(4, p.getTelefono());
            ps.setString(5, p.getCorreo());
            ps.setString(6, p.getDireccion() != null ? p.getDireccion() : "");

            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.out.println("❌ Error al agregar proveedor: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * ✅ Eliminar proveedor por ID
     */
    public boolean eliminar(int id) {
        String sql = "DELETE FROM proveedores WHERE id = ?";
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            System.out.println("❌ Error al eliminar proveedor: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * ✅ Buscar proveedor por ID
     */
    public Proveedores buscarPorId(int id) {
        String sql = "SELECT id, nombre, almacen, cadena, telefono, correo, direccion FROM proveedores WHERE id = ?";
        Proveedores p = null;
        
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                p = new Proveedores();
                p.setId(rs.getInt("id"));
                p.setNombre(rs.getString("nombre"));
                p.setAlmacen(rs.getString("almacen"));
                p.setCadena(rs.getString("cadena"));
                p.setTelefono(rs.getString("telefono"));
                p.setCorreo(rs.getString("correo"));
                p.setDireccion(rs.getString("direccion"));
            }
            
        } catch (SQLException e) {
            System.out.println("❌ Error al buscar proveedor: " + e.getMessage());
            e.printStackTrace();
        }
        
        return p;
    }
}