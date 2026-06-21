package dominio;

import java.time.LocalDateTime;

public class Categoria extends Base {
    private String nombre;
    private String descripcion;

    public Categoria() {
        super();
    }

    public Categoria(String id, boolean eliminado, LocalDateTime createdAt, String nombre, String descripcion) {
        super(id, eliminado, createdAt);
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
}