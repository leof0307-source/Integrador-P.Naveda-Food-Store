package DAO;

import integrador.config.DatabaseConfig;
import dominio.Usuario;
import dominio.Rol;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class UsuarioCrud {

    public static boolean insertarUsuario(Usuario usuario) {
        String query = """
                INSERT INTO usuarios (id_usuario, nombre, apellido, mail, celular, contrasenia, rol, created_at)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?);
                """;

        try (Connection con = DatabaseConfig.conectar();
             PreparedStatement pstmt = con.prepareStatement(query)) {

            pstmt.setString(1, usuario.getId());
            pstmt.setString(2, usuario.getNombre());
            pstmt.setString(3, usuario.getApellido());
            pstmt.setString(4, usuario.getMail());
            pstmt.setString(5, usuario.getCelular());
            pstmt.setString(6, usuario.getContrasenia());
            pstmt.setString(7, usuario.getRol().name()); // Guardamos el nombre del Enum (ADMIN, USUARIO)
            pstmt.setString(8, usuario.getCreatedAt().toString());

            pstmt.executeUpdate();
            System.out.println("[BD]: Usuario " + usuario.getNombre() + " registrado con éxito.");
            return true;
        } catch (SQLException err) {
            System.err.println("[Error al insertar usuario]: " + err.getMessage());
            return false;
        }
    }

    public static ArrayList<Usuario> obtenerTodos() {
        ArrayList<Usuario> lista = new ArrayList<>();
        String query = "SELECT id_usuario, nombre, apellido, mail, celular, contrasenia, rol, eliminado, created_at FROM usuarios";

        try (Connection con = DatabaseConfig.conectar();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Usuario u = new Usuario();
                u.setId(rs.getString("id_usuario"));
                u.setNombre(rs.getString("nombre"));
                u.setApellido(rs.getString("apellido"));
                u.setMail(rs.getString("mail"));
                u.setCelular(rs.getString("celular"));
                u.setContrasenia(rs.getString("contrasenia"));
                u.setRol(Rol.valueOf(rs.getString("rol"))); // Convertimos el texto de vuelta a Enum
                u.setEliminado(rs.getBoolean("eliminado"));
                u.setCreatedAt(LocalDateTime.parse(rs.getString("created_at")));

                lista.add(u);
            }
        } catch (SQLException err) {
            System.err.println("[Error al obtener usuarios]: " + err.getMessage());
        }
        return lista;
    }
}