package integrador.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConfig {

    public static Connection conectar() {
        Connection con = null;
        try {
            con = DriverManager.getConnection("jdbc:sqlite:productos_database.db");
            System.out.println("- conexion exitosa -");
        } catch (SQLException e) {
            System.err.println("[ERROR]: error al conectar a la base de datos: " + e.getMessage());
        }
        return con;
    }

    public static void crearTablasIniciales() {
        // ACTIVAR CLAVES FORÁNEAS EN SQLITE (Súper importante para las relaciones)
        String pragmaSql = "PRAGMA foreign_keys = ON;";

        // 1. Tabla Categorias
        String sqlCategorias = """
                CREATE TABLE IF NOT EXISTS categorias (
                    id_categoria TEXT PRIMARY KEY,
                    nombre TEXT NOT NULL,
                    descripcion TEXT,
                    eliminado INTEGER NOT NULL DEFAULT 0,
                    created_at TEXT NOT NULL
                );
                """;

        // 2. Tabla Usuarios
        String sqlUsuarios = """
                CREATE TABLE IF NOT EXISTS usuarios (
                    id_usuario TEXT PRIMARY KEY,
                    nombre TEXT NOT NULL,
                    apellido TEXT NOT NULL,
                    mail TEXT NOT NULL UNIQUE,
                    celular TEXT,
                    contrasenia TEXT NOT NULL,
                    rol TEXT NOT NULL, -- Guardaremos el ENUM como TEXT (ADMIN, OPERADOR, etc.)
                    eliminado INTEGER NOT NULL DEFAULT 0,
                    created_at TEXT NOT NULL
                );
                """;

        // 3. Tabla Productos (Depende de categorias por la FK)
        String sqlProductos = """
                CREATE TABLE IF NOT EXISTS productos (
                    id_producto TEXT PRIMARY KEY,
                    nombre TEXT NOT NULL,
                    precio REAL NOT NULL,
                    descripcion TEXT,
                    stock INTEGER NOT NULL,
                    imagen TEXT,
                    disponible INTEGER NOT NULL DEFAULT 1,
                    eliminado INTEGER NOT NULL DEFAULT 0,
                    created_at TEXT NOT NULL,
                    id_categoria TEXT,
                    FOREIGN KEY (id_categoria) REFERENCES categorias(id_categoria)
                );
                """;

        // 4. Tabla Pedidos
        String sqlPedidos = """
                CREATE TABLE IF NOT EXISTS pedidos (
                    id_pedido TEXT PRIMARY KEY,
                    fecha TEXT NOT NULL, -- LocalDate guardado como TEXT "YYYY-MM-DD"
                    estado TEXT NOT NULL, -- ENUM como TEXT
                    total REAL NOT NULL,
                    forma_pago TEXT NOT NULL, -- ENUM como TEXT
                    eliminado INTEGER NOT NULL DEFAULT 0,
                    created_at TEXT NOT NULL,
                    id_usuario TEXT,
                    FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario)
                );
                """;

        // 5. Tabla DetallePedidos
        String sqlDetalles = """
                CREATE TABLE IF NOT EXISTS detalles_pedido (
                    id_detalle TEXT PRIMARY KEY,
                    cantidad INTEGER NOT NULL,
                    subtotal REAL NOT NULL,
                    eliminado INTEGER NOT NULL DEFAULT 0,
                    created_at TEXT NOT NULL,
                    id_pedido TEXT,
                    id_producto TEXT,
                    FOREIGN KEY (id_pedido) REFERENCES pedidos(id_pedido) ON DELETE CASCADE,
                    FOREIGN KEY (id_producto) REFERENCES productos(id_producto)
                );
                """;

        try (Connection con = conectar();
             Statement stmt = con.createStatement()) {

            // Ejecutamos el pragma y cada una de las tablas en orden
            stmt.execute(pragmaSql);
            stmt.execute(sqlCategorias);
            stmt.execute(sqlUsuarios);
            stmt.execute(sqlProductos);
            stmt.execute(sqlPedidos);
            stmt.execute(sqlDetalles);

            System.out.println("¡Todas las tablas del sistema Food Store fueron verificadas/creadas con éxito!");

        } catch (SQLException e) {
            System.err.println("[ERROR]: Error al estructurar la base de datos: " + e.getMessage());
        }
    }
}