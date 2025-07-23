package proyectopoo2025;

public class Usuario {
    private int id;
    private String nombreUsuario;
    private String contraseña;
    private String email;

    // Constructor
    public Usuario(int id, String nombreUsuario, String contraseña, String email) {
        this.id = id;
        this.nombreUsuario = nombreUsuario;
        this.contraseña = contraseña;
        this.email = email;
    }

    // Método para autenticar (ejemplo básico)
    public boolean autentificar(String nombreIngresado, String contraseñaIngresada) {
        return this.nombreUsuario.equals(nombreIngresado) && this.contraseña.equals(contraseñaIngresada);
    }

    // Método para registrar (en una app real debería estar en un gestor)
    public boolean registrar() {
        // Aquí normalmente se verificaría si ya existe en una base de datos o lista
        System.out.println("Usuario registrado: " + nombreUsuario);
        return true; // suposición de que fue exitoso
    }

    // Método para actualizar el perfil (aquí podrías pedir datos nuevos como parámetros)
    public void actualizarPerfil(String nuevoNombre, String nuevaContraseña, String nuevoEmail) {
        this.nombreUsuario = nuevoNombre;
        this.contraseña = nuevaContraseña;
        this.email = nuevoEmail;
        System.out.println("Perfil actualizado.");
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
