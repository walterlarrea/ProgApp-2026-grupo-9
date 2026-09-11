package com.grupo9.edext.grupo9.estacion_de_trabajo.gui;

import com.grupo9.edext.grupo9.estacion_de_trabajo.cliente.CursoPres;
import com.grupo9.edext.grupo9.estacion_de_trabajo.cliente.ProgramaDeFormacionPres;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataCurso;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataProgramaFormacion;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashSet;
import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class GestionarProgramasInternalFrame extends JInternalFrame {

    private final ProgramaDeFormacionPres programaPres = new ProgramaDeFormacionPres();
    private final CursoPres cursoPres = new CursoPres();
    private final boolean modoCrear;
    private final JTextField campoNombre = new JTextField();
    private final JTextArea campoDescripcion = new JTextArea(5, 20);
    private final JTextField campoFechaInicio = new JTextField();
    private final JTextField campoFechaFin = new JTextField();
    private final JTable tablaProgramas = new JTable();
    private final JLabel detalleNombre = new JLabel("-");
    private final JLabel detalleCreacion = new JLabel("-");
    private final JLabel detalleInicio = new JLabel("-");
    private final JLabel detalleFin = new JLabel("-");
    private final JLabel detalleDescripcion = new JLabel("-");
    private final JList<DataCurso> listaCursos = new JList<>();
    private final JList<DataCurso> listaCursosDisponibles = new JList<>();
    private final JCheckBox habilitarAgregar = new JCheckBox("Habilitar edición");
    private final JButton agregarCurso = new JButton("Agregar");
    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("d/M/yy");

    public GestionarProgramasInternalFrame(boolean modoCrear) {
        super(modoCrear ? "Crear Programa" : "Consultar Programas", true, true, true, true);
        this.modoCrear = modoCrear;
        initComponents();
        if (!modoCrear) {
            cargarProgramas();
        }
    }

    private void initComponents() {
        setDefaultCloseOperation(JInternalFrame.DISPOSE_ON_CLOSE);
        setSize(850, 650);
        setLocation(20, 20);
        setContentPane(modoCrear ? crearFormulario() : crearConsulta());
    }

    private JPanel crearFormulario() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JPanel datos = new JPanel(new GridLayout(0, 2, 8, 8));
        datos.add(new JLabel("Nombre"));
        datos.add(campoNombre);
        datos.add(new JLabel("Fecha de inicio (ej: 24/8/26)"));
        datos.add(campoFechaInicio);
        datos.add(new JLabel("Fecha de finalización"));
        datos.add(campoFechaFin);
        panel.add(new JLabel("Crear Programa"), BorderLayout.NORTH);
        panel.add(datos, BorderLayout.CENTER);
        campoDescripcion.setLineWrap(true);
        campoDescripcion.setWrapStyleWord(true);
        JPanel descripcion = new JPanel(new BorderLayout(4, 4));
        descripcion.add(new JLabel("Descripción"), BorderLayout.NORTH);
        descripcion.add(new JScrollPane(campoDescripcion), BorderLayout.CENTER);
        panel.add(descripcion, BorderLayout.SOUTH);
        JButton cancelar = new JButton("Cancelar");
        cancelar.addActionListener(event -> limpiarFormulario());
        JButton guardar = new JButton("Aceptar");
        guardar.addActionListener(event -> guardarPrograma());
        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        acciones.add(cancelar);
        acciones.add(guardar);
        descripcion.add(acciones, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel crearConsulta() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        DefaultTableModel modelo = new DefaultTableModel(new Object[][] {},
                new String[] {"Nombre", "Fecha inicio", "Fecha fin", "Descripción"}) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaProgramas.setModel(modelo);
        tablaProgramas.getSelectionModel().addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting()) {
                mostrarDetalles();
            }
        });
        panel.add(new JLabel("Consultar Programa"), BorderLayout.NORTH);
        panel.add(new JScrollPane(tablaProgramas), BorderLayout.CENTER);
        panel.add(crearDetalles(), BorderLayout.SOUTH);
        return panel;
    }

    private JPanel crearDetalles() {
        JPanel detalles = new JPanel(new BorderLayout(8, 8));
        JPanel datos = new JPanel(new GridLayout(0, 2, 4, 4));
        datos.add(new JLabel("Nombre:"));
        datos.add(detalleNombre);
        datos.add(new JLabel("Fecha de creación:"));
        datos.add(detalleCreacion);
        datos.add(new JLabel("Fecha de inicio:"));
        datos.add(detalleInicio);
        datos.add(new JLabel("Fecha de finalización:"));
        datos.add(detalleFin);
        datos.add(new JLabel("Descripción:"));
        datos.add(detalleDescripcion);
        detalles.add(datos, BorderLayout.NORTH);

        listaCursos.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public java.awt.Component getListCellRendererComponent(JList<?> list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof DataCurso curso) {
                    setText(curso.nombreCurso());
                }
                return this;
            }
        });
        listaCursosDisponibles.setCellRenderer(listaCursos.getCellRenderer());
        JPanel listas = new JPanel(new GridLayout(1, 2, 8, 8));
        listas.add(new JScrollPane(listaCursos));
        listas.add(new JScrollPane(listaCursosDisponibles));
        detalles.add(listas, BorderLayout.CENTER);

        habilitarAgregar.addActionListener(event -> actualizarCursosDisponibles());
        agregarCurso.setEnabled(false);
        agregarCurso.addActionListener(event -> agregarCursoAlPrograma());
        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        acciones.add(habilitarAgregar);
        acciones.add(agregarCurso);
        detalles.add(acciones, BorderLayout.SOUTH);
        return detalles;
    }

    private void guardarPrograma() {
        String nombre = campoNombre.getText().trim();
        String descripcion = campoDescripcion.getText().trim();
        if (nombre.isEmpty() || descripcion.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe ingresar nombre y descripción.", "Datos requeridos", JOptionPane.WARNING_MESSAGE);
            return;
        }
        LocalDate inicio;
        LocalDate fin;
        try {
            inicio = LocalDate.parse(campoFechaInicio.getText().trim(), FORMATO_FECHA);
            fin = LocalDate.parse(campoFechaFin.getText().trim(), FORMATO_FECHA);
        } catch (DateTimeParseException exception) {
            JOptionPane.showMessageDialog(this, "Ingrese fechas válidas con formato d/M/yy.", "Fecha inválida", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (fin.isBefore(inicio)) {
            JOptionPane.showMessageDialog(this, "La fecha de finalización no puede ser anterior a la de inicio.", "Fechas inválidas", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (Boolean.TRUE.equals(programaPres.existeProgramaDeFormacion(nombre))) {
            JOptionPane.showMessageDialog(this, "Ya existe un programa con ese nombre.", "Programa duplicado", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (programaPres.guardarNuevoProgramaDeFormacion(nombre, descripcion, inicio, fin) != null) {
            limpiarFormulario();
            JOptionPane.showMessageDialog(this, "Programa registrado y guardado con éxito", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void cargarProgramas() {
        DefaultTableModel modelo = (DefaultTableModel) tablaProgramas.getModel();
        modelo.setRowCount(0);
        HashSet<DataProgramaFormacion> programas = programaPres.cargarProgramas();
        if (programas != null) {
            for (DataProgramaFormacion programa : programas) {
                modelo.addRow(new Object[] {programa.nombre(), programa.fechaInicio(), programa.fechaFin(), programa.descripcion()});
            }
        }
    }

    private void mostrarDetalles() {
        int fila = tablaProgramas.getSelectedRow();
        if (fila < 0) {
            return;
        }
        String nombre = (String) tablaProgramas.getValueAt(fila, 0);
        DataProgramaFormacion programa = programaPres.buscarPorNombreId(nombre);
        if (programa == null) {
            return;
        }
        detalleNombre.setText(programa.nombre());
        detalleCreacion.setText(programa.fechaDeCreacion().toString());
        detalleInicio.setText(programa.fechaInicio().toString());
        detalleFin.setText(programa.fechaFin().toString());
        detalleDescripcion.setText(programa.descripcion());
        DefaultListModel<DataCurso> modeloCursos = new DefaultListModel<>();
        programa.cursos().forEach(modeloCursos::addElement);
        listaCursos.setModel(modeloCursos);
        actualizarCursosDisponibles();
    }

    private void actualizarCursosDisponibles() {
        boolean activo = habilitarAgregar.isSelected() && tablaProgramas.getSelectedRow() >= 0;
        agregarCurso.setEnabled(activo);
        listaCursosDisponibles.setEnabled(activo);
        DefaultListModel<DataCurso> modelo = new DefaultListModel<>();
        if (activo) {
            String nombre = (String) tablaProgramas.getValueAt(tablaProgramas.getSelectedRow(), 0);
            HashSet<DataCurso> disponibles = cursoPres.cursosNoRelacionadosConUnProgDeFormacion(nombre);
            if (disponibles != null) {
                disponibles.forEach(modelo::addElement);
            }
        }
        listaCursosDisponibles.setModel(modelo);
    }

    private void agregarCursoAlPrograma() {
        DataCurso curso = listaCursosDisponibles.getSelectedValue();
        int fila = tablaProgramas.getSelectedRow();
        if (curso == null || fila < 0) {
            return;
        }
        String programa = (String) tablaProgramas.getValueAt(fila, 0);
        if (Boolean.TRUE.equals(programaPres.agregarCursoAProgramaDeFormacion(programa, curso.nombreCurso()))) {
            mostrarDetalles();
        }
    }

    private void limpiarFormulario() {
        campoNombre.setText("");
        campoDescripcion.setText("");
        campoFechaInicio.setText("");
        campoFechaFin.setText("");
    }
}
