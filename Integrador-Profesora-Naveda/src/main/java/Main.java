
import DAO.CategoriaCrud;
import DAO.ProductoCrud;
import DAO.UsuarioCrud;
import DAO.PedidoCrud;
import dominio.*;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.UUID;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        int opcion;
        do {
            System.out.println("\n========== FOOD STORE - MENÚ PRINCIPAL ==========");
            System.out.println("1. Gestionar Categorías (HU-CAT)");
            System.out.println("2. Gestionar Productos (HU-PROD)");
            System.out.println("3. Gestionar Usuarios (HU-USR)");
            System.out.println("4. Gestionar Pedidos (HU-PED)");
            System.out.println("0. Salir del Sistema");
            System.out.print("Seleccione una opción: ");

            while (!scanner.hasNextInt()) {
                System.out.print("Por favor, ingrese un número válido: ");
                scanner.next();
            }
            opcion = scanner.nextInt();
            scanner.nextLine(); // Limpiar el búfer

            switch (opcion) {
                case 1 -> menuCategorias();
                case 2 -> menuProductos();
                case 3 -> menuUsuarios();
                case 4 -> menuPedidos();
                case 0 -> System.out.println("¡Gracias por utilizar Food Store!");
                default -> System.out.println("Opción no válida. Intente nuevamente.");
            }
        } while (opcion != 0);
    }

    // ==========================================
    // 1. SECCIÓN CATEGORÍAS (HU-CAT)
    // ==========================================
    private static void menuCategorias() {
        System.out.println("\n--- GESTIÓN DE CATEGORÍAS ---");
        System.out.println("1. Registrar Categoría (Alta)");
        System.out.println("2. Listar Categorías");
        System.out.print("Opción: ");
        int op = scanner.nextInt();
        scanner.nextLine();

        if (op == 1) {
            System.out.print("Nombre de la categoría: ");
            String nombre = scanner.nextLine();
            System.out.print("Descripción: ");
            String desc = scanner.nextLine();

            Categoria cat = new Categoria();
            cat.setId(UUID.randomUUID().toString().substring(0, 8)); // Genera un ID corto único
            cat.setNombre(nombre);
            cat.setDescripcion(desc);

            CategoriaCrud.insertarCategoria(cat);
        } else if (op == 2) {
            ArrayList<Categoria> lista = CategoriaCrud.obtenerTodas();
            System.out.println("\n--- LISTADO DE CATEGORÍAS EN BD ---");
            for (Categoria c : lista) {
                System.out.println("[" + c.getId() + "] " + c.getNombre() + " - " + c.getDescripcion());
            }
        }
    }

    // ==========================================
    // 2. SECCIÓN PRODUCTOS (HU-PROD)
    // ==========================================
    private static void menuProductos() {
        System.out.println("\n--- GESTIÓN DE PRODUCTOS ---");
        System.out.println("1. Registrar Producto (Alta)");
        System.out.println("2. Listar Productos");
        System.out.print("Opción: ");
        int op = scanner.nextInt();
        scanner.nextLine();

        if (op == 1) {
            System.out.print("Nombre del producto: ");
            String nombre = scanner.nextLine();
            System.out.print("Precio: ");
            double precio = scanner.nextDouble();
            scanner.nextLine();
            System.out.print("Descripción: ");
            String desc = scanner.nextLine();
            System.out.print("Stock inicial: ");
            int stock = scanner.nextInt();
            scanner.nextLine();

            // Listar categorías existentes para asociar la FK
            ArrayList<Categoria> categorias = CategoriaCrud.obtenerTodas();
            if (categorias.isEmpty()) {
                System.out.println("[Error]: No puedes crear productos sin categorías previas.");
                return;
            }
            System.out.println("Seleccione el ID de la Categoría para este producto:");
            for (Categoria c : categorias) {
                System.out.println(" -> " + c.getId() + ": " + c.getNombre());
            }
            System.out.print("Escriba el ID elegido: ");
            String idCat = scanner.nextLine();

            Categoria catSeleccionada = new Categoria();
            catSeleccionada.setId(idCat);

            Producto prod = new Producto();
            prod.setId(UUID.randomUUID().toString().substring(0, 8));
            prod.setNombre(nombre);
            prod.setPrecio(precio);
            prod.setDescripcion(desc);
            prod.setStock(stock);
            prod.setImagen("default.png");
            prod.setDisponible(true);
            prod.setCategoria(catSeleccionada); // Asigna la relación

            ProductoCrud.insertarProducto(prod);
        } else if (op == 2) {
            ArrayList<Producto> lista = ProductoCrud.obtenerTodos();
            System.out.println("\n--- LISTADO DE PRODUCTOS EN BD ---");
            for (Producto p : lista) {
                System.out.println("[" + p.getId() + "] " + p.getNombre() + " | Precio: $" + p.getPrecio() + " | Stock: " + p.getStock() + " | Cat_FK: " + p.getCategoria().getId());
            }
        }
    }

    // ==========================================
    // 3. SECCIÓN USUARIOS (HU-USR)
    // ==========================================
    private static void menuUsuarios() {
        System.out.println("\n--- GESTIÓN DE USUARIOS ---");
        System.out.println("1. Registrar Usuario (Alta)");
        System.out.println("2. Listar Usuarios");
        System.out.print("Opción: ");
        int op = scanner.nextInt();
        scanner.nextLine();

        if (op == 1) {
            System.out.print("Nombre: ");
            String nom = scanner.nextLine();
            System.out.print("Apellido: ");
            String ape = scanner.nextLine();
            System.out.print("Mail: ");
            String mail = scanner.nextLine();
            System.out.print("Celular: ");
            String cel = scanner.nextLine();
            System.out.print("Contraseña: ");
            String pass = scanner.nextLine();

            System.out.print("Rol (1 = ADMIN, 2 = USUARIO): ");
            int rolSel = scanner.nextInt();
            scanner.nextLine();
            Rol rol = (rolSel == 1) ? Rol.ADMIN : Rol.USUARIO;

            Usuario usr = new Usuario();
            usr.setId(UUID.randomUUID().toString().substring(0, 8));
            usr.setNombre(nom);
            usr.setApellido(ape);
            usr.setMail(mail);
            usr.setCelular(cel);
            usr.setContrasenia(pass);
            usr.setRol(rol);

            UsuarioCrud.insertarUsuario(usr);
        } else if (op == 2) {
            ArrayList<Usuario> lista = UsuarioCrud.obtenerTodos();
            System.out.println("\n--- LISTADO DE USUARIOS EN BD ---");
            for (Usuario u : lista) {
                System.out.println("[" + u.getId() + "] " + u.getApellido() + ", " + u.getNombre() + " (" + u.getRol() + ") - " + u.getMail());
            }
        }
    }

    // ==========================================
    // 4. SECCIÓN PEDIDOS (HU-PED)
    // ==========================================
    private static void menuPedidos() {
        System.out.println("\n--- GESTIÓN DE PEDIDOS ---");
        System.out.println("1. Crear Nuevo Pedido");
        System.out.println("2. Listar Historial de Pedidos");
        System.out.print("Opción: ");
        int op = scanner.nextInt();
        scanner.nextLine();

        if (op == 1) {
            ArrayList<Usuario> usuarios = UsuarioCrud.obtenerTodos();
            if (usuarios.isEmpty()) {
                System.out.println("[Error]: No se pueden registrar pedidos sin usuarios en el sistema.");
                return;
            }
            System.out.println("Seleccione el ID del Usuario que realiza la compra:");
            for (Usuario u : usuarios) {
                System.out.println(" -> " + u.getId() + ": " + u.getNombre() + " " + u.getApellido());
            }
            System.out.print("Escriba el ID del usuario: ");
            String idUsr = scanner.nextLine();

            Usuario comprador = new Usuario();
            comprador.setId(idUsr);

            Pedido pedido = new Pedido();
            pedido.setId(UUID.randomUUID().toString().substring(0, 8));
            pedido.setUsuario(comprador);
            pedido.setEstado(Estado.PENDIENTE);

            System.out.print("Forma de Pago (1=EFECTIVO, 2=TARJETA, 3=TRANSFERENCIA): ");
            int fp = scanner.nextInt();
            scanner.nextLine();
            if (fp == 2) pedido.setFormaPago(FormaPago.TARJETA);
            else if (fp == 3) pedido.setFormaPago(FormaPago.TRANSFERENCIA);
            else pedido.setFormaPago(FormaPago.EFECTIVO);

            ArrayList<Producto> productosDisponibles = ProductoCrud.obtenerTodos();
            if (productosDisponibles.isEmpty()) {
                System.out.println("[Error]: No hay alimentos en el menú para vender.");
                return;
            }

            String agregarMas;
            do {
                System.out.println("\n--- MENÚ DE COMIDAS ---");
                for (Producto p : productosDisponibles) {
                    System.out.println(" -> ID: " + p.getId() + " | " + p.getNombre() + " | Precio: $" + p.getPrecio());
                }
                System.out.print("Ingrese el ID del producto que desea añadir: ");
                String idProd = scanner.nextLine();

                Producto prodElegido = null;
                for (Producto p : productosDisponibles) {
                    if (p.getId().equals(idProd)) {
                        prodElegido = p;
                        break;
                    }
                }

                if (prodElegido != null) {
                    System.out.print("Cantidad: ");
                    int cant = scanner.nextInt();
                    scanner.nextLine();

                    pedido.addDetallePedido(cant, prodElegido.getPrecio(), prodElegido);
                    System.out.println("¡Agregado con éxito al carrito temporal!");
                } else {
                    System.out.println("ID de producto inexistente.");
                }

                System.out.print("¿Desea agregar otro artículo a este pedido? (s/n): ");
                agregarMas = scanner.nextLine();
            } while (agregarMas.equalsIgnoreCase("s"));

            System.out.println("\nGuardando pedido completo con un total de: $" + pedido.getTotal());
            PedidoCrud.insertarPedido(pedido);

        } else if (op == 2) {
            ArrayList<Pedido> lista = PedidoCrud.obtenerTodos();
            System.out.println("\n--- HISTORIAL GENERAL DE PEDIDOS EN BASE DE DATOS ---");
            for (Pedido p : lista) {
                System.out.println("Pedido ID: [" + p.getId() + "] | Fecha: " + p.getFecha() + " | Estado: " + p.getEstado() + " | Total: $" + p.getTotal() + " | Comprador FK: " + p.getUsuario().getId());
            }
        }
    }
}