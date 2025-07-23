package proyectopoo2025;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import javax.swing.SwingUtilities;
public class ProyectoPoo2025 {

    public static void main(String[] args) {        
        Calendario calendario = new Calendario();
        calendario.setVisible(true);

        // Cargar tareas desde archivo al iniciar
        calendario.cargarTareasDesdeArchivo("tareas.txt");

        // Guardar al cerrar la ventana
        calendario.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent e) {
                calendario.guardarTareasEnArchivo("tareas.txt");
            }
        });
    }
}
