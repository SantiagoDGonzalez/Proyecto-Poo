package proyectopoo2025;

import java.time.LocalDate;
import java.time.LocalTime;

public class Tarea {
  
    private int id;
    private String titulo;
    private String descripcion;
    private LocalDate fechaLimite;
    private LocalTime horaLimite;
    private boolean estaCompletada;
    private String categoria;
    private Prioridad prioridad; // Usa el enum externo

    public Tarea(int id, String titulo, String descripcion, LocalDate fechaLimite, LocalTime horaLimite,
                 Prioridad prioridad, boolean estaCompletada, String categoria) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.fechaLimite = fechaLimite;
        this.horaLimite = horaLimite;
        this.prioridad = prioridad;
        this.estaCompletada = estaCompletada;
        this.categoria = categoria;
    }
    
    public String getTitulo() {
        return titulo;
    }
    public LocalDate getFechaLimite() {
        return fechaLimite;
    }
    public Prioridad getPrioridad() {
        return prioridad;
    }
    public void crear(){
        System.out.println("Tarea creada: " + titulo);
    }
    public void actualizar(String nuevoTitulo){
        this.titulo = nuevoTitulo;
        System.out.println("Tarea actualizada: " + titulo);
    }
    public void eliminar(){
        System.out.println("Tarea eliminada: " + titulo);
    }
    public void marcarComoCompletada(){
        this.estaCompletada = true;
    }
    public void asignarPrioridad(Prioridad nueva) {
        this.prioridad = nueva;
    }
    //public void asociarAproyecto(){
    //     this.idProyecto = idProyecto;
    //}  
    public String toString() {
        return titulo + " (" + prioridad + ")";
    }
    public String getDescripcion() {
    return descripcion;
}

    public LocalTime getHoraLimite() {
    return horaLimite;
}

    public String getCategoria() {
    return categoria;
}

    public boolean estaCompletada() {
    return estaCompletada;
}

}
