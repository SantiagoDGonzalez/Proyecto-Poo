package proyectopoo2025;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.*;
import java.util.*;
import java.util.List;

public class Calendario extends JFrame {
    private JPanel calendarPanel;
    private JLabel mesAnioLabel;
    private int mesActual;
    private int anioActual;

    private Map<LocalDate, List<Tarea>> tareasPorFecha = new HashMap<>();

    public Calendario() {
        setTitle("Calendario de Tareas");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        mesActual = LocalDate.now().getMonthValue();
        anioActual = LocalDate.now().getYear();

        JPanel topPanel = new JPanel();
        JButton anteriorBtn = new JButton("<");
        JButton siguienteBtn = new JButton(">");
        mesAnioLabel = new JLabel("", SwingConstants.CENTER);

        anteriorBtn.addActionListener(e -> {
            mesActual--;
            if (mesActual < 1) {
                mesActual = 12;
                anioActual--;
            }
            actualizarCalendario();
        });

        siguienteBtn.addActionListener(e -> {
            mesActual++;
            if (mesActual > 12) {
                mesActual = 1;
                anioActual++;
            }
            actualizarCalendario();
        });

        topPanel.add(anteriorBtn);
        topPanel.add(mesAnioLabel);
        topPanel.add(siguienteBtn);

        add(topPanel, BorderLayout.NORTH);

        calendarPanel = new JPanel(new GridLayout(0, 7));
        add(calendarPanel, BorderLayout.CENTER);

        actualizarCalendario();
    }

    private void actualizarCalendario() {
        calendarPanel.removeAll();
        mesAnioLabel.setText(Month.of(mesActual) + " " + anioActual);

        String[] dias = {"Dom", "Lun", "Mar", "Mié", "Jue", "Vie", "Sáb"};
        for (String dia : dias) {
            JLabel label = new JLabel(dia, SwingConstants.CENTER);
            label.setFont(new Font("SansSerif", Font.BOLD, 14));
            calendarPanel.add(label);
        }

        LocalDate primerDia = LocalDate.of(anioActual, mesActual, 1);
        int primerDiaSemana = primerDia.getDayOfWeek().getValue() % 7;

        for (int i = 0; i < primerDiaSemana; i++) {
            calendarPanel.add(new JLabel(""));
        }

        int diasEnMes = primerDia.lengthOfMonth();
        for (int dia = 1; dia <= diasEnMes; dia++) {
            LocalDate fecha = LocalDate.of(anioActual, mesActual, dia);

            JTextArea area = new JTextArea();
            area.setEditable(false);
            area.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
            area.setBackground(Color.WHITE);

            area.setText(dia + "\n");

            List<Tarea> tareasDia = tareasPorFecha.getOrDefault(fecha, new ArrayList<>());
            for (Tarea t : tareasDia) {
                area.append("• " + t.getTitulo() + " (" + t.getPrioridad() + ")\n");
            }

            area.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() == 1) {
                        String[] opciones = {"Crear Tarea", "Crear Evento", "Cancelar"};
                        int seleccion = JOptionPane.showOptionDialog(
                                null,
                                "¿Qué deseas crear para el " + fecha + "?",
                                "Selecciona una opción",
                                JOptionPane.DEFAULT_OPTION,
                                JOptionPane.QUESTION_MESSAGE,
                                null,
                                opciones,
                                opciones[0]
                        );

                        if (seleccion == 0) {
                            mostrarDialogoCrearTarea(fecha);
                        } else if (seleccion == 1) {
                            mostrarDialogoCrearEvento(fecha); 
                        }
                    } else if (e.getClickCount() == 2) {
                        mostrarTareasDelDia(fecha);
                    }
                }
            });


            calendarPanel.add(area);
        }

        calendarPanel.revalidate();
        calendarPanel.repaint();
    }
    private void mostrarDialogoCrearEvento(LocalDate fecha) {
        JTextField tituloField = new JTextField();
        JTextField descripcionField = new JTextField();
        JTextField horaInicioField = new JTextField("10:00");
        JTextField horaFinField = new JTextField("11:00");

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Título del evento:"));
        panel.add(tituloField);
        panel.add(new JLabel("Descripción:"));
        panel.add(descripcionField);
        panel.add(new JLabel("Hora inicio (HH:mm):"));
        panel.add(horaInicioField);
        panel.add(new JLabel("Hora fin (HH:mm):"));
        panel.add(horaFinField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Crear evento para " + fecha,
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                LocalTime inicio = LocalTime.parse(horaInicioField.getText());
                LocalTime fin = LocalTime.parse(horaFinField.getText());

                String infoEvento = String.format("[Evento] %s (%s - %s)", tituloField.getText(), inicio, fin);
                Tarea evento = new Tarea(
                        new Random().nextInt(10000),
                        infoEvento,
                        descripcionField.getText(),
                        fecha,
                        fin, 
                        Prioridad.MEDIA,
                        false,
                        "Evento"
                );
                agregarTarea(evento);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al crear evento: " + ex.getMessage());
            }
        }
    }

    private void mostrarDialogoCrearTarea(LocalDate fecha) {
        JTextField tituloField = new JTextField();
        JTextField descripcionField = new JTextField();
        JTextField horaField = new JTextField("14:00");
        JComboBox<Prioridad> prioridadBox = new JComboBox<>(Prioridad.values());
        JTextField categoriaField = new JTextField();

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Título:"));
        panel.add(tituloField);
        panel.add(new JLabel("Descripción:"));
        panel.add(descripcionField);
        panel.add(new JLabel("Hora límite (HH:mm):"));
        panel.add(horaField);
        panel.add(new JLabel("Prioridad:"));
        panel.add(prioridadBox);
        panel.add(new JLabel("Categoría:"));
        panel.add(categoriaField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Crear tarea para " + fecha,
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                LocalTime horaLimite = LocalTime.parse(horaField.getText());
                Tarea nueva = new Tarea(
                        new Random().nextInt(10000),
                        tituloField.getText(),
                        descripcionField.getText(),
                        fecha,
                        horaLimite,
                        (Prioridad) prioridadBox.getSelectedItem(),
                        false,
                        categoriaField.getText()
                );
                agregarTarea(nueva);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        }
    }

    private void agregarTarea(Tarea tarea) {
        tareasPorFecha.computeIfAbsent(tarea.getFechaLimite(), f -> new ArrayList<>()).add(tarea);
        if (tarea.getFechaLimite().getMonthValue() == mesActual && tarea.getFechaLimite().getYear() == anioActual) {
            actualizarCalendario();
        }
    }
    private void mostrarTareasDelDia(LocalDate fecha) {
        List<Tarea> tareasDia = tareasPorFecha.get(fecha);

        if (tareasDia == null || tareasDia.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay tareas para " + fecha);
            return;
        }

        JDialog dialogo = new JDialog(this, "Tareas para " + fecha, true);
        dialogo.setSize(400, 300);
        dialogo.setLayout(new BorderLayout());

        DefaultListModel<String> model = new DefaultListModel<>();
        for (Tarea t : tareasDia) {
            String info = String.format(
                    "Título: %s\nDescripción: %s\nHora: %s\nPrioridad: %s\nCategoría: %s\nCompletada: %s\n",
                    t.getTitulo(),
                    t.getDescripcion(),
                    t.getHoraLimite(),
                    t.getPrioridad(),
                    t.getCategoria(),
                    t.estaCompletada() ? "Sí" : "No"
            );
            model.addElement(info);
        }

        JList<String> lista = new JList<>(model);
        lista.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(lista);

        dialogo.add(scrollPane, BorderLayout.CENTER);

        JButton cerrar = new JButton("Cerrar");
        cerrar.addActionListener(e -> dialogo.dispose());
        dialogo.add(cerrar, BorderLayout.SOUTH);

        dialogo.setLocationRelativeTo(this);
        dialogo.setVisible(true);
    }
}


