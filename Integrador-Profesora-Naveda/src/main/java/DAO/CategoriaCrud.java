package DAO;

import integrador.config.DatabaseConfig; 
import dominio.Categoria;               
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class CategoriaCrud {
public static boolean insertarCategoria(Categoria categoria){
    // query incompleta

    String query = "INSERT INTO categorias (id_categoria, nombre, descripcion, eliminado, created_at) VALUES (?, ?, ?, ?, ?)";


    // 1] conexion y statment
    try (Connection con = DatabaseConfig.conectar();
         PreparedStatement pstmt = con.prepareStatement(query)) {


        // 2] completamos la query con los datos del obejto categoria
        pstmt.setString(1, categoria.getId());

        pstmt.setString(2, categoria.getNombre());
        pstmt.setString(3, categoria.getDescripcion());
        pstmt.setBoolean(4, categoria.isEliminado());
        pstmt.setString(5, categoria.getCreatedAt().toString()); //Convertimos el objeto fecha a string

        // 3] ejecutar query
        pstmt.executeUpdate();

        System.out.println("[BD]: Subida de categoria "+categoria.getNombre()+" exitosa!");
        return true;

    } catch (SQLException err) {
        System.err.println("[Error al insertar]: " + err.getMessage());
        return false;
    }
}
public static ArrayList<Categoria> obtenerTodas() {

    ArrayList<Categoria> listaCategorias = new ArrayList<>();

    String query = "SELECT id_categoria,created_at, eliminado,nombre,descripcion FROM categorias";

    try (Connection con = DatabaseConfig.conectar();
         Statement stmt = con.createStatement();
         ResultSet resultado = stmt.executeQuery(query)) {


        while (resultado.next()) {

            // Extraemos los datos de la fila actual
            String id = resultado.getString("id_categoria");

            String fechaStr = resultado.getString("created_at");
            LocalDateTime createdAt = LocalDateTime.parse(fechaStr); // Parseamos de String a LocalDateTime

            boolean eliminado = resultado.getBoolean("eliminado");

            String nombre = resultado.getString("nombre");
            String descripcion = resultado.getString("descripcion");



            // 4. Creamos el objeto Categoria usando el constructor completo
            Categoria categoria = new Categoria(id,eliminado,createdAt, nombre, descripcion );

            // 5. Agregamos el objeto a nuestra lista
            listaCategorias.add(categoria);
        }

    } catch (SQLException err) {
        System.err.println("[Error al obtener todas las categorías]: " + err.getMessage());
    }

    return listaCategorias;
}
    public static boolean modificarCategoria(Categoria cat) {
        String query = "UPDATE categorias SET nombre = ?, descripcion = ? WHERE id_categoria = ? AND eliminado = 0;";
        try (Connection con = DatabaseConfig.conectar();
             PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setString(1, cat.getNombre());
            pstmt.setString(2, cat.getDescripcion());
            pstmt.setString(3, cat.getId());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException err) {
            System.err.println("[Error al modificar categoría]: " + err.getMessage());
            return false;
        }
    }
}
