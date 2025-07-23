package proyectopoo2025;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import javax.swing.SwingUtilities;
public class ProyectoPoo2025 {

    public static void main(String[] args) {
         SwingUtilities.invokeLater(() -> {
            Calendario calendario = new Calendario();
            calendario.setVisible(true);
        });
    }
}

