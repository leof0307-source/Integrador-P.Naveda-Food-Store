package DAO;

import integrador.config.DatabaseConfig;
import dominio.Producto;
import dominio.Categoria;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class ProductoCrud {

    public static boolean insertarProducto(Producto producto) {
        String query = """
                INSERT INTO productos (id_producto, nombre, precio, descripcion, stock, imagen, disponible, eliminado, created_at, id_categoria)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);
                """;

        try (Connection con = DatabaseConfig.conectar();
             PreparedStatement pstmt = con.prepareStatement(query)) {

            pstmt.setString(1, producto.getId());
            pstmt.setString(2, producto.getNombre());
            pstmt.setDouble(3, producto.getPrecio());
            pstmt.setString(4, producto.getDescripcion());
            pstmt.setInt(5, producto.getStock());
            pstmt.setString(6, producto.getImagen());
            pstmt.setBoolean(7, producto.isDisponible());
            pstmt.setBoolean(8, producto.isEliminado());
            pstmt.setString(9, producto.getCreatedAt().toString());

            // EXTRAEMOS LA FK: Obtenemos el ID de la categoría vinculada
            pstmt.setString(10, producto.getCategoria().getId());

            pstmt.executeUpdate();
            System.out.println("[BD]: Producto " + producto.getNombre() + " subido con éxito.");
            return true;
        } catch (SQLException err) {
            System.err.println("[Error al insertar producto]: " + err.getMessage());
            return false;
        }
    }

    public static ArrayList<Producto> obtenerTodos() {
        ArrayList<Producto> lista = new ArrayList<>();
        String query = "SELECT id_producto, nombre, precio, descripcion, stock, imagen, disponible, eliminado, created_at, id_categoria FROM productos";

        try (Connection con = DatabaseConfig.conectar();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Producto p = new Producto();
                p.setId(rs.getString("id_producto"));
                p.setNombre(rs.getString("nombre"));
                p.setPrecio(rs.getDouble("precio"));
                p.setDescripcion(rs.getString("descripcion"));
                p.setStock(rs.getInt("stock"));
                p.setImagen(rs.getString("imagen"));
                p.setDisponible(rs.getBoolean("disponible"));
                p.setEliminado(rs.getBoolean("eliminado"));
                p.setCreatedAt(LocalDateTime.parse(rs.getString("created_at")));

                // ARMA RELACIÓN INTERNA: Creamos la categoría temporal usando solo el ID que vino de la base de datos
                Categoria catTemporal = new Categoria();
                catTemporal.setId(rs.getString("id_categoria"));
                p.setCategoria(catTemporal);

                lista.add(p);
            }
        } catch (SQLException err) {
            System.err.println("[Error al obtener productos]: " + err.getMessage());
        }
        return lista;
    }
}