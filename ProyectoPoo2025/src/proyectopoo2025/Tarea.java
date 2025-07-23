package proyectopoo2025;

import java.time.LocalDate;
import java.time.LocalTime;

public class Tarea {
  
    private int id;
    private String titulo;
    private String descripcion;
    private LocalDate fechaLimite;
    private LocalTime horaLimite;
    private Prioridad prioridad;
    private boolean estaCompletada;
    private String categoria;
    private int idProyecto;
    
    public Tarea(int id, String titulo, String descripcion, LocalDate fechaLimite, LocalTime horaLimite,
                 Prioridad prioridad, boolean estaCompletada, String categoria, int idProyecto) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.fechaLimite = fechaLimite;
        this.horaLimite = horaLimite;
        this.prioridad = prioridad;
        this.estaCompletada = estaCompletada;
        this.categoria = categoria;
        this.idProyecto = idProyecto;
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
    public void AsignarPrioridad(Prioridad p){
        this.prioridad = p;
    }
    public void asociarAproyecto(){
         this.idProyecto = idProyecto;
    }
    public void mostrar() {
        System.out.println("----- TAREA -----");
        System.out.println("ID: " + id);
        System.out.println("Titulo: " + titulo);
        System.out.println("Descripcion: " + descripcion);
        System.out.println("Fecha limite: " + fechaLimite);
        System.out.println("Hora limite: " + horaLimite);
        System.out.println("Prioridad: " + prioridad);
        System.out.println("Completada: " + (estaCompletada ? "Sí" : "No"));
        System.out.println("Categoria: " + categoria);
        System.out.println("ID Proyecto: " + idProyecto);
        System.out.println();   
    }
}
