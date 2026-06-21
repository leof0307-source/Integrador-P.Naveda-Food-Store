package dominio;

import java.time.LocalDateTime;

public abstract class Base {
    private String id; // Usamos String porque en tu BD pusimos TEXT PRIMARY KEY
    private boolean eliminado;
    private LocalDateTime createdAt;

    public Base() {
        this.createdAt = LocalDateTime.now();
        this.eliminado = false;
    }

    public Base(String id, boolean eliminado, LocalDateTime createdAt) {
        this.id = id;
        this.eliminado = eliminado;
        this.createdAt = createdAt;
    }

    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public boolean isEliminado() { return eliminado; }
    public void setEliminado(boolean eliminado) { this.eliminado = eliminado; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}