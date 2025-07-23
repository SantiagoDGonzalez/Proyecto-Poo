package proyectopoo2025;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
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
                    if (SwingUtilities.isRightMouseButton(e)) {
                        List<Tarea> tareasDia = tareasPorFecha.get(fecha);
                        if (tareasDia != null && !tareasDia.isEmpty()) {
                            mostrarEditorDeTareas(fecha, tareasDia);
                        } else {
                            JOptionPane.showMessageDialog(null, "No hay tareas o eventos para " + fecha);
                        }
                    } else if (e.getClickCount() == 1) {
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
    private void mostrarEditorDeTareas(LocalDate fecha, List<Tarea> tareas) {
        JDialog dialogo = new JDialog(this, "Editar/Borrar tareas o eventos para " + fecha, true);
        dialogo.setSize(500, 400);
        dialogo.setLayout(new BorderLayout());

        DefaultListModel<Tarea> model = new DefaultListModel<>();
        for (Tarea t : tareas) {
            model.addElement(t);
        }

        JList<Tarea> lista = new JList<>(model);
        lista.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                Tarea t = (Tarea) value;
                label.setText(String.format("%s (%s - %s)", t.getTitulo(), t.getHoraLimite(), t.getPrioridad()));
                return label;
            }
        });

        JScrollPane scrollPane = new JScrollPane(lista);
        dialogo.add(scrollPane, BorderLayout.CENTER);

        JPanel botones = new JPanel();

        JButton editarBtn = new JButton("Editar");
        JButton eliminarBtn = new JButton("Eliminar");
        JButton cerrarBtn = new JButton("Cerrar");

        editarBtn.addActionListener(e -> {
            Tarea seleccionada = lista.getSelectedValue();
            if (seleccionada != null) {
                mostrarDialogoEditarTarea(seleccionada);
                actualizarCalendario(); // Actualiza la vista después de editar
            }
        });

        eliminarBtn.addActionListener(e -> {
            Tarea seleccionada = lista.getSelectedValue();
            if (seleccionada != null) {
                int confirm = JOptionPane.showConfirmDialog(dialogo, "¿Eliminar esta tarea/evento?",
                        "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    tareas.remove(seleccionada);
                    model.removeElement(seleccionada);
                    actualizarCalendario();
                }
            }
        });

        cerrarBtn.addActionListener(e -> dialogo.dispose());

        botones.add(editarBtn);
        botones.add(eliminarBtn);
        botones.add(cerrarBtn);

        dialogo.add(botones, BorderLayout.SOUTH);
        dialogo.setLocationRelativeTo(this);
        dialogo.setVisible(true);
    }
    private void mostrarDialogoEditarTarea(Tarea tarea) {
        JTextField tituloField = new JTextField(tarea.getTitulo());
        JTextField descripcionField = new JTextField(tarea.getDescripcion());
        JTextField horaField = new JTextField(tarea.getHoraLimite().toString());
        JComboBox<Prioridad> prioridadBox = new JComboBox<>(Prioridad.values());
        prioridadBox.setSelectedItem(tarea.getPrioridad());
        JTextField categoriaField = new JTextField(tarea.getCategoria());

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

        int result = JOptionPane.showConfirmDialog(this, panel, "Editar tarea/evento",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                tarea.setTitulo(tituloField.getText());
                tarea.setDescripcion(descripcionField.getText());
                tarea.setHoraLimite(LocalTime.parse(horaField.getText()));
                tarea.setPrioridad((Prioridad) prioridadBox.getSelectedItem());
                tarea.setCategoria(categoriaField.getText());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        }
    }
    public void guardarTareasEnArchivo(String nombreArchivo) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(nombreArchivo))) {
            for (Map.Entry<LocalDate, List<Tarea>> entrada : tareasPorFecha.entrySet()) {
                LocalDate fecha = entrada.getKey();
                for (Tarea tarea : entrada.getValue()) {
                    writer.printf("%s|%s|%s|%s|%s|%s|%s%n",
                            fecha,
                            tarea.getHoraLimite(),
                            tarea.getTitulo(),
                            tarea.getDescripcion(),
                            tarea.getPrioridad(),
                            tarea.getCategoria(),
                            tarea.estaCompletada() ? "1" : "0"
                    );
                }
            }
            JOptionPane.showMessageDialog(this, "Tareas guardadas en: " + nombreArchivo);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al guardar tareas: " + e.getMessage());
        }
    }
    public void cargarTareasDesdeArchivo(String nombreArchivo) {
        File archivo = new File(nombreArchivo);
        if (!archivo.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] partes = linea.split("\\|");
                if (partes.length != 7) continue;

                LocalDate fecha = LocalDate.parse(partes[0]);
                LocalTime hora = LocalTime.parse(partes[1]);
                String titulo = partes[2];
                String descripcion = partes[3];
                Prioridad prioridad = Prioridad.valueOf(partes[4]);
                String categoria = partes[5];
                boolean completada = partes[6].equals("1");

                Tarea tarea = new Tarea(
                        new Random().nextInt(10000), // puedes usar el ID si lo guardas
                        titulo,
                        descripcion,
                        fecha,
                        hora,
                        prioridad,
                        completada,
                        categoria
                );
                agregarTarea(tarea);
            }
        } catch (IOException | RuntimeException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar tareas: " + e.getMessage());
        }
    }
}
