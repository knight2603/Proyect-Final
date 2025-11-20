package dao;

import control.ConnBD;
import modelo.Compra;
import modelo.DetalleCompra;
import java.sql.*;
import java.util.List;

public class CompraDAO {

    /**
     * Inserta compra + detalles y actualiza stock en una transacción.
     * Retorna true si OK, false si falla (y hace rollback).
     */
    public boolean agregarCompraConDetalles(Compra compra, List<DetalleCompra> detalles) {
        String sqlInsertCompra = "INSERT INTO compra (cc_usuario, fecha_compra, estado, id_metodo_pago, total) VALUES (?, ?, ?, ?, ?)";
        String sqlInsertDetalle = "INSERT INTO detalle_compra (id_compra, id_producto, cantidad, costo_unitario, subtotal) VALUES (?, ?, ?, ?, ?)";
        String sqlUpdateStock = "UPDATE producto SET stock = stock - ? WHERE id = ? AND stock >= ?";

        Connection con = null;
        try {
            con = ConnBD.conectar();
            con.setAutoCommit(false);

            // 1) Insert compra y obtener id generado
            int idCompra;
            try (PreparedStatement ps = con.prepareStatement(sqlInsertCompra, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, compra.getCcUsuario());
                ps.setTimestamp(2, new Timestamp(compra.getFechaCompra().getTime()));
                ps.setString(3, compra.getEstado());
                ps.setInt(4, compra.getIdMetodoPago());
                ps.setDouble(5, compra.getTotal());
                int r = ps.executeUpdate();
                if (r <= 0) {
                    con.rollback();
                    return false;
                }
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) {
                        idCompra = keys.getInt(1);
                    } else {
                        con.rollback();
                        return false;
                    }
                }
            }

            // 2) Insertar cada detalle y decrementar stock (verificando existencia)
            try (PreparedStatement psDetalle = con.prepareStatement(sqlInsertDetalle);
                 PreparedStatement psStock = con.prepareStatement(sqlUpdateStock)) {

                for (DetalleCompra d : detalles) {
                    // Insert detalle
                    psDetalle.setInt(1, idCompra);
                    psDetalle.setInt(2, d.getIdProducto());
                    psDetalle.setInt(3, d.getCantidad());
                    psDetalle.setDouble(4, d.getCostoUnitario());
                    psDetalle.setDouble(5, d.getSubtotal());
                    if (psDetalle.executeUpdate() <= 0) {
                        con.rollback();
                        return false;
                    }

                    // Update stock (evita stock negativo)
                    psStock.setInt(1, d.getCantidad());
                    psStock.setInt(2, d.getIdProducto());
                    psStock.setInt(3, d.getCantidad());
                    if (psStock.executeUpdate() <= 0) {
                        // no hay stock suficiente -> rollback
                        con.rollback();
                        return false;
                    }
                }
            }

            con.commit();
            return true;

        } catch (SQLException ex) {
            ex.printStackTrace();
            try { if (con != null) con.rollback(); } catch (SQLException e) { e.printStackTrace(); }
            return false;
        } finally {
            try { if (con != null) con.setAutoCommit(true); } catch (SQLException e) { e.printStackTrace(); }
        }
    }
}
