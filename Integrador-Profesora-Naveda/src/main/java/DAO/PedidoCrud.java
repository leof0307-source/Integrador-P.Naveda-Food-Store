package DAO;

import integrador.config.DatabaseConfig;
import dominio.Pedido;
import dominio.DetallePedido;
import dominio.Estado;
import dominio.FormaPago;
import dominio.Usuario;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.ArrayList;

public class PedidoCrud {

    public static boolean insertarPedido(Pedido pedido) {
        String queryPedido = """
                INSERT INTO pedidos (id_pedido, fecha, estado, total, forma_pago, eliminado, created_at, id_usuario)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?);
                """;

        String queryDetalle = """
                INSERT INTO detalles_pedido (id_detalle, cantidad, subtotal, eliminado, created_at, id_pedido, id_producto)
                VALUES (?, ?, ?, ?, ?, ?, ?);
                """;

        Connection con = null;
        try {
            con = DatabaseConfig.conectar();
            con.setAutoCommit(false); // Desactivamos el auto-commit por seguridad transaccional

            // 1. Guardar la Cabecera del Pedido
            try (PreparedStatement psPedido = con.prepareStatement(queryPedido)) {
                psPedido.setString(1, pedido.getId());
                psPedido.setString(2, pedido.getFecha().toString()); // LocalDate a String "YYYY-MM-DD"
                psPedido.setString(3, pedido.getEstado().name());
                psPedido.setDouble(4, pedido.getTotal());
                psPedido.setString(5, pedido.getFormaPago().name());
                psPedido.setBoolean(6, pedido.isEliminado());
                psPedido.setString(7, pedido.getCreatedAt().toString());
                psPedido.setString(8, pedido.getUsuario().getId()); // Clave foránea al usuario

                psPedido.executeUpdate();
            }

            // 2. Guardar los Detalles (Composición)
            try (PreparedStatement psDetalle = con.prepareStatement(queryDetalle)) {
                for (DetallePedido detalle : pedido.getDetalles()) {
                    psDetalle.setString(1, detalle.getId());
                    psDetalle.setInt(2, detalle.getCantidad());
                    psDetalle.setDouble(3, detalle.getSubtotal());
                    psDetalle.setBoolean(4, detalle.isEliminado());
                    psDetalle.setString(5, detalle.getCreatedAt().toString());

                    // ¡Aquí los vinculamos físicamente en la BD!
                    psDetalle.setString(6, pedido.getId());             // id_pedido (FK de unión)
                    psDetalle.setString(7, detalle.getProducto().getId()); // id_producto (FK al alimento)

                    psDetalle.addBatch(); // Los encolamos para insertarlos juntos de forma veloz
                }
                psDetalle.executeBatch();
            }

            con.commit(); // Si todo salió bien, guardamos definitivamente en el archivo .db
            System.out.println("[BD]: Pedido " + pedido.getId() + " y sus detalles registrados.");
            return true;

        } catch (SQLException err) {
            System.err.println("[Error transaccional en Pedido]: " + err.getMessage());
            if (con != null) {
                try { con.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
            return false;
        } finally {
            if (con != null) { try { con.close(); } catch (SQLException e) { e.printStackTrace(); } }
        }
    }

    public static ArrayList<Pedido> obtenerTodos() {
        ArrayList<Pedido> listaPedidos = new ArrayList<>();
        String query = "SELECT id_pedido, fecha, estado, total, forma_pago, eliminado, created_at, id_usuario FROM pedidos";

        try (Connection con = DatabaseConfig.conectar();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Pedido p = new Pedido();
                p.setId(rs.getString("id_pedido"));
                p.setFecha(LocalDate.parse(rs.getString("fecha")));
                p.setEstado(Estado.valueOf(rs.getString("estado")));
                p.setTotal(rs.getDouble("total"));
                p.setFormaPago(FormaPago.valueOf(rs.getString("forma_pago")));
                p.setEliminado(rs.getBoolean("eliminado"));
                p.setCreatedAt(LocalDateTime.parse(rs.getString("created_at")));

                // Vinculamos al Usuario de forma temporal usando el ID
                Usuario usrTemporal = new Usuario();
                usrTemporal.setId(rs.getString("id_usuario"));
                p.setUsuario(usrTemporal);

                listaPedidos.add(p);
            }
        } catch (SQLException err) {
            System.err.println("[Error al obtener pedidos]: " + err.getMessage());
        }
        return listaPedidos;
    }
    public static boolean actualizarEstadoYPago(String idPedido, String nuevoEstado, String nuevaFormaPago) {
        String query = "UPDATE pedidos SET estado = ?, forma_pago = ? WHERE id_pedido = ? AND eliminado = 0;";
        try (Connection con = DatabaseConfig.conectar();
             PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setString(1, nuevoEstado);
            pstmt.setString(2, nuevaFormaPago);
            pstmt.setString(3, idPedido);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException err) {
            System.err.println("[Error al actualizar pedido]: " + err.getMessage());
            return false;
        }
    }
}
