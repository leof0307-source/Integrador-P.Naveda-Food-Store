package dominio;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Pedido extends Base implements Calculable {
    private LocalDate fecha;
    private Estado estado;
    private Double total;
    private FormaPago formaPago;
    private Usuario usuario; // Relación: Pedido conoce a Usuario
    private List<DetallePedido> detalles; // El rombo negro: Composición 1..m

    public Pedido() {
        super();
        this.fecha = LocalDate.now();
        this.detalles = new ArrayList<>();
        this.total = 0.0;
    }

    // MÉTODOS DEL DIAGRAMA UML EXIGIDOS POR LA CONSIGNA

    public void addDetallePedido(int cantidad, Double precio, Producto producto) {
        // Generamos un ID provisorio para el detalle usando el tamaño de la lista
        String idDetalle = this.getId() + "-D" + (detalles.size() + 1);
        DetallePedido nuevoDetalle = new DetallePedido(idDetalle, cantidad, producto);
        detalles.add(nuevoDetalle);
        calcularTotal(); // Recalculamos el total del pedido automáticamente
    }

    public DetallePedido findDetallePedidoByProducto(Producto producto) {
        for (DetallePedido d : detalles) {
            if (d.getProducto().getId().equals(producto.getId())) {
                return d;
            }
        }
        return null; // No se encontró
    }

    public void deleteDetallePedidoByProducto(Producto producto) {
        DetallePedido encontrado = findDetallePedidoByProducto(producto);
        if (encontrado != null) {
            detalles.remove(encontrado);
            calcularTotal(); // Actualizamos el total de la orden
        }
    }

    // Getters y Setters básicos
    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }
    public Estado getEstado() { return estado; }
    public void setEstado(Estado estado) { this.estado = estado; }
    public Double getTotal() { return total; }
    public void setTotal(Double total) { this.total = total; }
    public FormaPago getFormaPago() { return formaPago; }
    public void setFormaPago(FormaPago formaPago) { this.formaPago = formaPago; }
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    public List<DetallePedido> getDetalles() { return detalles; }
    public void setDetalles(List<DetallePedido> detalles) { this.detalles = detalles; }
    @Override
    public void calcularTotal() {
        double suma = 0.0;
        for (DetallePedido d : detalles) {
            suma += d.getSubtotal();
        }
        this.total = suma; // Guarda el resultado en el atributo de Pedido
    }
}