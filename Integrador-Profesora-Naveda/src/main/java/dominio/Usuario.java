package dominio;

import java.time.LocalDateTime;

public class Usuario extends Base {
    private String nombre;
    private String apellido;
    private String mail;
    private String celular;
    private String contrasenia;
    private Rol rol;

    public Usuario() {
        super();
    }

    public Usuario(String id, boolean eliminado, LocalDateTime createdAt, String nombre, String apellido, String mail, String celular, String contrasenia, Rol rol) {
        super(id, eliminado, createdAt);
        this.nombre = nombre;
        this.apellido = apellido;
        this.mail = mail;
        this.celular = celular;
        this.contrasenia = contrasenia;
        this.rol = rol;
    }

    // Getters y Setters
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }
    public String getMail() { return mail; }
    public void setMail(String mail) { this.mail = mail; }
    public String getCelular() { return celular; }
    public void setCellular(String celular) { this.celular = celular; }
    public String getContrasenia() { return contrasenia; }
    public void setContrasenia(String contrasenia) { this.contrasenia = contrasenia; }
    public Rol getRol() { return rol; }
    public void setRol(Rol rol) { this.rol = rol; }

    public void setCelular(String celular) {
        this.celular = celular;
    }
}