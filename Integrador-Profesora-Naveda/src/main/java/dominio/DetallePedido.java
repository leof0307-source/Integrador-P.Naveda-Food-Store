package dominio;

public class DetallePedido extends Base {
    private int cantidad;
    private Double subtotal;
    private Producto producto; // Relación asociativa al producto

    public DetallePedido() {
        super();
    }

    public DetallePedido(String id, int cantidad, Producto producto) {
        super();
        setId(id);
        this.cantidad = cantidad;
        this.producto = producto;
        this.subtotal = producto.getPrecio() * cantidad; // El subtotal se calcula solo
    }

    // Getters y Setters
    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }
    public Double getSubtotal() { return subtotal; }
    public void setSubtotal(Double subtotal) { this.subtotal = subtotal; }
    public Producto getProducto() { return producto; }
    public void setProducto(Producto producto) { this.producto = producto; }
}