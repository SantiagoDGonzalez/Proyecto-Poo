package proyectopoo2025;

public class Evento {
    private int id;
    private String titulo;
    private String categoria;
    private String duration;

    // Constructor
    public Evento(int id, String titulo, String categoria, String duration) {
        this.id = id;
        this.titulo = titulo;
        this.categoria = categoria;
        this.duration = duration;
    }

    // Métodos getters y setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    // Métodos funcionales simulados
    public void crear() {
        System.out.println("Evento creado: " + titulo);
    }

    public void actualizar(String nuevoTitulo, String nuevaCategoria, String nuevaDuracion) {
        this.titulo = nuevoTitulo;
        this.categoria = nuevaCategoria;
        this.duration = nuevaDuracion;
        System.out.println("Evento actualizado.");
    }

    public void eliminar() {
        System.out.println("Evento eliminado.");
        // Aquí podrías poner lógica para eliminarlo de una lista o base de datos
    }

    public void establecerRecordatorio() {
        System.out.println("Recordatorio establecido para el evento: " + titulo);
    }
}
