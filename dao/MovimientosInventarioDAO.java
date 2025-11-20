package dao;

import modelo.MovimientosInventario;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import control.ConnBD;
import java.util.Date;

public class MovimientosInventarioDAO {

    public MovimientosInventarioDAO() {
        // Constructor vacío
    }

    private Connection getConnection() {
        return ConnBD.conectar();
    }

    /**
     * ✅ Listar todos los movimientos de inventario con información relacionada
     */
    public List<MovimientosInventario> listar() {
        List<MovimientosInventario> lista = new ArrayList<>();
        String sql = "SELECT mi.id, mi.id_producto, p.nombre as nombre_producto, "
                + "mi.tipo_movimiento, mi.cantidad, mi.fecha_movimiento, "
                + "mi.id_usuario, u.nombre as nombre_usuario, "
                + "mi.id_proveedor, prov.nombre as nombre_proveedor, "
                + "mi.observaciones "
                + "FROM movimientos_inventario mi "
                + "LEFT JOIN productos p ON mi.id_producto = p.id "
                + "LEFT JOIN usuarios u ON mi.id_usuario = u.id "
                + "LEFT JOIN proveedores prov ON mi.id_proveedor = prov.id "
                + "ORDER BY mi.fecha_movimiento DESC";

        try (Connection con = getConnection(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                MovimientosInventario movimiento = mapearMovimientoCompleto(rs);
                lista.add(movimiento);
            }

        } catch (SQLException e) {
            System.out.println("❌ Error al listar movimientos: " + e.getMessage());
            e.printStackTrace();
        }

        return lista;
    }

    /**
     * ✅ Agregar nuevo movimiento de inventario
     */
    public boolean agregar(MovimientosInventario movimiento) {
        String sql = "INSERT INTO movimientos_inventario (id_producto, tipo_movimiento, cantidad, "
                + "id_usuario, id_proveedor, observaciones) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        System.out.println("=== 🚨 DEBUG COMPLETO MOVIMIENTO ===");
        System.out.println("🔍 Producto ID: " + movimiento.getIdProducto());
        System.out.println("🔍 Tipo Movimiento: " + movimiento.getTipoMovimiento());
        System.out.println("🔍 Cantidad: " + movimiento.getCantidad());
        System.out.println("🔍 Usuario ID: " + movimiento.getIdUsuario());
        System.out.println("🔍 Proveedor ID: " + movimiento.getIdProveedor());
        System.out.println("🔍 Observaciones: " + movimiento.getObservaciones());
        System.out.println("🔍 SQL: " + sql);

        Connection con = null;
        PreparedStatement ps = null;

        try {
            con = getConnection();
            System.out.println("🔍 Conexión obtenida: " + (con != null && !con.isClosed()));

            ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            // Setear parámetros con validación
            ps.setInt(1, movimiento.getIdProducto());
            System.out.println("🔍 Parámetro 1 (id_producto): " + movimiento.getIdProducto());

            ps.setString(2, movimiento.getTipoMovimiento());
            System.out.println("🔍 Parámetro 2 (tipo_movimiento): " + movimiento.getTipoMovimiento());

            ps.setInt(3, movimiento.getCantidad());
            System.out.println("🔍 Parámetro 3 (cantidad): " + movimiento.getCantidad());

            ps.setInt(4, movimiento.getIdUsuario());
            System.out.println("🔍 Parámetro 4 (id_usuario): " + movimiento.getIdUsuario());

            // Proveedor
            if (movimiento.getIdProveedor() > 0) {
                ps.setInt(5, movimiento.getIdProveedor());
                System.out.println("🔍 Parámetro 5 (id_proveedor): " + movimiento.getIdProveedor());
            } else {
                ps.setNull(5, Types.INTEGER);
                System.out.println("🔍 Parámetro 5 (id_proveedor): NULL");
            }

            // Observaciones
            if (movimiento.getObservaciones() != null && !movimiento.getObservaciones().trim().isEmpty()) {
                ps.setString(6, movimiento.getObservaciones().trim());
                System.out.println("🔍 Parámetro 6 (observaciones): " + movimiento.getObservaciones().trim());
            } else {
                ps.setNull(6, Types.VARCHAR);
                System.out.println("🔍 Parámetro 6 (observaciones): NULL");
            }

            System.out.println("🔍 Ejecutando INSERT...");
            int filasAfectadas = ps.executeUpdate();
            System.out.println("🔍 Filas afectadas: " + filasAfectadas);

            if (filasAfectadas > 0) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int idGenerado = generatedKeys.getInt(1);
                        movimiento.setId(idGenerado);
                        System.out.println("✅ MOVIMIENTO AGREGADO EXITOSAMENTE - ID: " + idGenerado);
                    }
                }
                return true;
            } else {
                System.out.println("❌ ERROR: No se afectaron filas en la inserción");
                return false;
            }

        } catch (SQLException e) {
            System.out.println("❌ ERROR SQL DETALLADO:");
            System.out.println("❌ Mensaje: " + e.getMessage());
            System.out.println("❌ SQL State: " + e.getSQLState());
            System.out.println("❌ Error Code: " + e.getErrorCode());
            System.out.println("❌ Stack Trace:");
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            System.out.println("❌ ERROR GENERAL:");
            System.out.println("❌ Mensaje: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            // Cerrar recursos manualmente
            try {
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
                System.out.println("🔍 Recursos cerrados");
            } catch (SQLException e) {
                System.out.println("❌ Error cerrando recursos: " + e.getMessage());
            }
            System.out.println("=== 🏁 FIN DEBUG MOVIMIENTO ===");
        }
    }

    /**
     * ✅ Buscar movimiento por ID
     */
    public MovimientosInventario buscarPorId(int id) {
        String sql = "SELECT mi.id, mi.id_producto, p.nombre as nombre_producto, "
                + "mi.tipo_movimiento, mi.cantidad, mi.fecha_movimiento, "
                + "mi.id_usuario, u.nombre as nombre_usuario, "
                + "mi.id_proveedor, prov.nombre as nombre_proveedor, "
                + "mi.observaciones "
                + "FROM movimientos_inventario mi "
                + "LEFT JOIN productos p ON mi.id_producto = p.id "
                + "LEFT JOIN usuarios u ON mi.id_usuario = u.id "
                + "LEFT JOIN proveedores prov ON mi.id_proveedor = prov.id "
                + "WHERE mi.id = ?";

        MovimientosInventario movimiento = null;

        try (Connection con = getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                movimiento = mapearMovimientoCompleto(rs);
            }

        } catch (SQLException e) {
            System.out.println("❌ Error al buscar movimiento: " + e.getMessage());
            e.printStackTrace();
        }

        return movimiento;
    }

    /**
     * ✅ Listar movimientos por producto
     */
    public List<MovimientosInventario> listarPorProducto(int idProducto) {
        List<MovimientosInventario> lista = new ArrayList<>();
        String sql = "SELECT mi.id, mi.id_producto, p.nombre as nombre_producto, "
                + "mi.tipo_movimiento, mi.cantidad, mi.fecha_movimiento, "
                + "mi.id_usuario, u.nombre as nombre_usuario, "
                + "mi.id_proveedor, prov.nombre as nombre_proveedor, "
                + "mi.observaciones "
                + "FROM movimientos_inventario mi "
                + "LEFT JOIN productos p ON mi.id_producto = p.id "
                + "LEFT JOIN usuarios u ON mi.id_usuario = u.id "
                + "LEFT JOIN proveedores prov ON mi.id_proveedor = prov.id "
                + "WHERE mi.id_producto = ? ORDER BY mi.fecha_movimiento DESC";

        try (Connection con = getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idProducto);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                MovimientosInventario movimiento = mapearMovimientoCompleto(rs);
                lista.add(movimiento);
            }

        } catch (SQLException e) {
            System.out.println("❌ Error al listar movimientos por producto: " + e.getMessage());
            e.printStackTrace();
        }

        return lista;
    }

    /**
     * ✅ Listar movimientos por tipo
     */
    public List<MovimientosInventario> listarPorTipo(String tipoMovimiento) {
        List<MovimientosInventario> lista = new ArrayList<>();
        String sql = "SELECT mi.id, mi.id_producto, p.nombre as nombre_producto, "
                + "mi.tipo_movimiento, mi.cantidad, mi.fecha_movimiento, "
                + "mi.id_usuario, u.nombre as nombre_usuario, "
                + "mi.id_proveedor, prov.nombre as nombre_proveedor, "
                + "mi.observaciones "
                + "FROM movimientos_inventario mi "
                + "LEFT JOIN productos p ON mi.id_producto = p.id "
                + "LEFT JOIN usuarios u ON mi.id_usuario = u.id "
                + "LEFT JOIN proveedores prov ON mi.id_proveedor = prov.id "
                + "WHERE mi.tipo_movimiento = ? ORDER BY mi.fecha_movimiento DESC";

        try (Connection con = getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, tipoMovimiento);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                MovimientosInventario movimiento = mapearMovimientoCompleto(rs);
                lista.add(movimiento);
            }

        } catch (SQLException e) {
            System.out.println("❌ Error al listar movimientos por tipo: " + e.getMessage());
            e.printStackTrace();
        }

        return lista;
    }

    /**
     * ✅ Obtener movimientos en un rango de fechas
     */
    public List<MovimientosInventario> listarPorRangoFechas(Date fechaInicio, Date fechaFin) {
        List<MovimientosInventario> lista = new ArrayList<>();
        String sql = "SELECT mi.id, mi.id_producto, p.nombre as nombre_producto, "
                + "mi.tipo_movimiento, mi.cantidad, mi.fecha_movimiento, "
                + "mi.id_usuario, u.nombre as nombre_usuario, "
                + "mi.id_proveedor, prov.nombre as nombre_proveedor, "
                + "mi.observaciones "
                + "FROM movimientos_inventario mi "
                + "LEFT JOIN productos p ON mi.id_producto = p.id "
                + "LEFT JOIN usuarios u ON mi.id_usuario = u.id "
                + "LEFT JOIN proveedores prov ON mi.id_proveedor = prov.id "
                + "WHERE mi.fecha_movimiento BETWEEN ? AND ? "
                + "ORDER BY mi.fecha_movimiento DESC";

        try (Connection con = getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setTimestamp(1, new Timestamp(fechaInicio.getTime()));
            ps.setTimestamp(2, new Timestamp(fechaFin.getTime()));
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                MovimientosInventario movimiento = mapearMovimientoCompleto(rs);
                lista.add(movimiento);
            }

        } catch (SQLException e) {
            System.out.println("❌ Error al listar movimientos por rango de fechas: " + e.getMessage());
            e.printStackTrace();
        }

        return lista;
    }

    /**
     * ✅ Obtener movimientos por usuario
     */
    public List<MovimientosInventario> listarPorUsuario(int idUsuario) {
        List<MovimientosInventario> lista = new ArrayList<>();
        String sql = "SELECT mi.id, mi.id_producto, p.nombre as nombre_producto, "
                + "mi.tipo_movimiento, mi.cantidad, mi.fecha_movimiento, "
                + "mi.id_usuario, u.nombre as nombre_usuario, "
                + "mi.id_proveedor, prov.nombre as nombre_proveedor, "
                + "mi.observaciones "
                + "FROM movimientos_inventario mi "
                + "LEFT JOIN productos p ON mi.id_producto = p.id "
                + "LEFT JOIN usuarios u ON mi.id_usuario = u.id "
                + "LEFT JOIN proveedores prov ON mi.id_proveedor = prov.id "
                + "WHERE mi.id_usuario = ? ORDER BY mi.fecha_movimiento DESC";

        try (Connection con = getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idUsuario);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                MovimientosInventario movimiento = mapearMovimientoCompleto(rs);
                lista.add(movimiento);
            }

        } catch (SQLException e) {
            System.out.println("❌ Error al listar movimientos por usuario: " + e.getMessage());
            e.printStackTrace();
        }

        return lista;
    }

    /**
     * ✅ Obtener el historial de movimientos recientes (últimos 30 días)
     */
    public List<MovimientosInventario> listarMovimientosRecientes() {
        List<MovimientosInventario> lista = new ArrayList<>();
        String sql = "SELECT mi.id, mi.id_producto, p.nombre as nombre_producto, "
                + "mi.tipo_movimiento, mi.cantidad, mi.fecha_movimiento, "
                + "mi.id_usuario, u.nombre as nombre_usuario, "
                + "mi.id_proveedor, prov.nombre as nombre_proveedor, "
                + "mi.observaciones "
                + "FROM movimientos_inventario mi "
                + "LEFT JOIN productos p ON mi.id_producto = p.id "
                + "LEFT JOIN usuarios u ON mi.id_usuario = u.id "
                + "LEFT JOIN proveedores prov ON mi.id_proveedor = prov.id "
                + "WHERE mi.fecha_movimiento >= DATE_SUB(NOW(), INTERVAL 30 DAY) "
                + "ORDER BY mi.fecha_movimiento DESC";

        try (Connection con = getConnection(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                MovimientosInventario movimiento = mapearMovimientoCompleto(rs);
                lista.add(movimiento);
            }

        } catch (SQLException e) {
            System.out.println("❌ Error al listar movimientos recientes: " + e.getMessage());
            e.printStackTrace();
        }

        return lista;
    }

    /**
     * ✅ Eliminar movimiento (solo para administradores)
     */
    public boolean eliminar(int id) {
        String sql = "DELETE FROM movimientos_inventario WHERE id = ?";
        try (Connection con = getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            int filasAfectadas = ps.executeUpdate();
            System.out.println("✅ Movimiento eliminado - ID: " + id + " - Filas afectadas: " + filasAfectadas);
            return filasAfectadas > 0;
        } catch (SQLException e) {
            System.out.println("❌ Error al eliminar movimiento: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * ✅ Método auxiliar para mapear un ResultSet a MovimientosInventario
     */
    private MovimientosInventario mapearMovimientoCompleto(ResultSet rs) throws SQLException {
        MovimientosInventario movimiento = new MovimientosInventario();
        movimiento.setId(rs.getInt("id"));
        movimiento.setIdProducto(rs.getInt("id_producto"));
        movimiento.setTipoMovimiento(rs.getString("tipo_movimiento"));
        movimiento.setCantidad(rs.getInt("cantidad"));
        movimiento.setFechaMovimiento(rs.getTimestamp("fecha_movimiento"));
        movimiento.setIdUsuario(rs.getInt("id_usuario"));

        // Manejar proveedor (puede ser null)
        int idProveedor = rs.getInt("id_proveedor");
        if (!rs.wasNull()) {
            movimiento.setIdProveedor(idProveedor);
        } else {
            movimiento.setIdProveedor(0);
        }

        movimiento.setObservaciones(rs.getString("observaciones"));

        return movimiento;
    }

    /**
     * ✅ Obtener estadísticas de movimientos
     */
    public int obtenerTotalMovimientos() {
        String sql = "SELECT COUNT(*) as total FROM movimientos_inventario";
        try (Connection con = getConnection(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt("total");
            }

        } catch (SQLException e) {
            System.out.println("❌ Error al obtener total de movimientos: " + e.getMessage());
            e.printStackTrace();
        }

        return 0;
    }
}
