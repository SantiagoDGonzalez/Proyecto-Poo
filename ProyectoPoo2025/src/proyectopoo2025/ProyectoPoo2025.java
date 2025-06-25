package proyectopoo2025;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
public class ProyectoPoo2025 {

    public static void main(String[] args) {
        List<Tarea> tareas = new ArrayList<>();

        Tarea tarea1 = new Tarea(1, "Estudiar Java", "Repasar enums y colecciones",
                LocalDate.of(2025, 6, 30), LocalTime.of(18, 0),
                Prioridad.ALTA, false, "Estudios", 101);

        Tarea tarea2 = new Tarea(2, "Reunion equipo", "Planificacion del sprint",
                LocalDate.of(2025, 6, 28), LocalTime.of(10, 0),
                Prioridad.MEDIA, false, "Trabajo", 102);

        Tarea tarea3 = new Tarea(3, "Comprar mercado", "Lista: arroz, leche, pan",
                LocalDate.of(2025, 6, 26), LocalTime.of(17, 0),
                Prioridad.BAJA, false, "Personal", 0);

        tareas.add(tarea1);
        tareas.add(tarea2);
        tareas.add(tarea3);

        // Mostrar todas las tareas
        for (Tarea tarea : tareas) {
            tarea.mostrar();
        }
    }
}

