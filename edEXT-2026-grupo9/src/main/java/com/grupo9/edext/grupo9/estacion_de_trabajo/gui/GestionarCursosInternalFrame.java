package com.grupo9.edext.grupo9.estacion_de_trabajo.gui;

import com.grupo9.edext.grupo9.estacion_de_trabajo.cliente.CursoPres;
import com.grupo9.edext.grupo9.estacion_de_trabajo.cliente.InstitutoPres;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataCurso;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataInstituto;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.HashSet;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

public class GestionarCursosInternalFrame extends JInternalFrame {

    private final CursoPres cursoPres = new CursoPres();
    private final InstitutoPres institutoPres = new InstitutoPres();
    private final boolean modoCrear;
    private final JTextField campoNombre = new JTextField();
    private final JTextArea campoDescripcion = new JTextArea(4, 20);
    private final JTextField campoDuracion = new JTextField();
    private final JTextField campoHoras = new JTextField();
    private final JTextField campoCreditos = new JTextField();
    private final JTextField campoUrl = new JTextField();
    private final JComboBox<DataInstituto> comboInstituto = new JComboBox<>();
    private final JList<DataCurso> listaPrevias = new JList<>();
    private final HashSet<DataCurso> previasSeleccionadas = new HashSet<>();
    private final JTable tablaCursos = new JTable();

    public GestionarCursosInternalFrame(boolean modoCrear) {
        super(modoCrear ? "Crear Curso" : "Consultar Cursos", true, true, true, true);
        this.modoCrear = modoCrear;
        initComponents();
        if (modoCrear) {
            cargarInstitutos();
        } else {
            cargarCursos();
        }
    }

    private void initComponents() {
        setDefaultCloseOperation(JInternalFrame.DISPOSE_ON_CLOSE);
        setSize(820, 600);
        setLocation(20, 20);
        if (modoCrear) {
            setContentPane(crearFormulario());
        } else {
            setContentPane(crearConsulta());
        }
    }

    private JPanel crearFormulario() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JPanel campos = new JPanel(new GridLayout(0, 2, 8, 8));
        campos.add(new JLabel("Nombre"));
        campos.add(campoNombre);
        campos.add(new JLabel("Duración"));
        campos.add(campoDuracion);
        campos.add(new JLabel("Cantidad de horas"));
        campos.add(campoHoras);
        campos.add(new JLabel("Créditos"));
        campos.add(campoCreditos);
        campos.add(new JLabel("URL"));
        campos.add(campoUrl);
        campos.add(new JLabel("Instituto"));
        campos.add(comboInstituto);
        panel.add(new JLabel("Crear Curso"), BorderLayout.NORTH);
        panel.add(campos, BorderLayout.NORTH);

        campoDescripcion.setLineWrap(true);
        campoDescripcion.setWrapStyleWord(true);
        JPanel descripcion = new JPanel(new BorderLayout(4, 4));
        descripcion.add(new JLabel("Descripción"), BorderLayout.NORTH);
        descripcion.add(new JScrollPane(campoDescripcion), BorderLayout.CENTER);

        listaPrevias.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaPrevias.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public java.awt.Component getListCellRendererComponent(JList<?> list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof DataCurso curso) {
                    setText((previasSeleccionadas.contains(curso) ? "[x] " : "[ ] ") + curso.nombreCurso());
                }
                return this;
            }
        });
        listaPrevias.addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting() && listaPrevias.getSelectedValue() != null) {
                DataCurso curso = listaPrevias.getSelectedValue();
                if (!previasSeleccionadas.add(curso)) {
                    previasSeleccionadas.remove(curso);
                }
                listaPrevias.clearSelection();
                listaPrevias.repaint();
            }
        });
        JPanel previas = new JPanel(new BorderLayout(4, 4));
        previas.add(new JLabel("Previas"), BorderLayout.NORTH);
        previas.add(new JScrollPane(listaPrevias), BorderLayout.CENTER);

        JPanel centro = new JPanel(new GridLayout(1, 2, 8, 8));
        centro.add(descripcion);
        centro.add(previas);
        panel.add(centro, BorderLayout.CENTER);

        JButton cancelar = new JButton("Cancelar");
        cancelar.addActionListener(event -> limpiarFormulario());
        JButton guardar = new JButton("Aceptar");
        guardar.addActionListener(event -> guardarCurso());
        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        acciones.add(cancelar);
        acciones.add(guardar);
        panel.add(acciones, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel crearConsulta() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        DefaultTableModel modelo = new DefaultTableModel(new Object[][] {},
                new String[] {"Nombre", "Instituto", "Duración", "Horas", "Créditos", "Fecha registro", "URL", "Descripción"}) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaCursos.setModel(modelo);
        panel.add(new JLabel("Consultar Cursos"), BorderLayout.NORTH);
        panel.add(new JScrollPane(tablaCursos), BorderLayout.CENTER);
        JButton refrescar = new JButton("Refrescar");
        refrescar.addActionListener(event -> cargarCursos());
        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        acciones.add(refrescar);
        panel.add(acciones, BorderLayout.SOUTH);
        return panel;
    }

    private void cargarInstitutos() {
        DefaultComboBoxModel<DataInstituto> modelo = new DefaultComboBoxModel<>();
        HashSet<DataInstituto> institutos = institutoPres.cargarInstitutos();
        if (institutos != null) {
            institutos.forEach(modelo::addElement);
        }
        comboInstituto.setModel(modelo);
        comboInstituto.setRenderer(new DefaultListCellRenderer() {
            @Override
            public java.awt.Component getListCellRendererComponent(JList<?> list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof DataInstituto instituto) {
                    setText(instituto.nombreI());
                }
                return this;
            }
        });
        DefaultListModel<DataCurso> modeloPrevias = new DefaultListModel<>();
        HashSet<DataCurso> cursos = cursoPres.cargarCursos();
        if (cursos != null) {
            cursos.forEach(modeloPrevias::addElement);
        }
        listaPrevias.setModel(modeloPrevias);
    }

    private void guardarCurso() {
        String nombre = campoNombre.getText().trim();
        String descripcion = campoDescripcion.getText().trim();
        if (nombre.isEmpty() || descripcion.isEmpty() || campoUrl.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe ingresar nombre, descripción y URL.", "Datos requeridos", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int duracion;
        int horas;
        int creditos;
        try {
            duracion = Integer.parseInt(campoDuracion.getText().trim());
            horas = Integer.parseInt(campoHoras.getText().trim());
            creditos = Integer.parseInt(campoCreditos.getText().trim());
        } catch (NumberFormatException exception) {
            JOptionPane.showMessageDialog(this, "La duración, las horas y los créditos deben ser números enteros.", "Datos inválidos", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (duracion <= 0 || horas <= 0 || creditos <= 0) {
            JOptionPane.showMessageDialog(this, "Los valores numéricos deben ser mayores que cero.", "Datos inválidos", JOptionPane.WARNING_MESSAGE);
            return;
        }
        DataInstituto instituto = (DataInstituto) comboInstituto.getSelectedItem();
        if (instituto == null) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un instituto.", "Dato requerido", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (Boolean.TRUE.equals(cursoPres.existeCurso(nombre))) {
            JOptionPane.showMessageDialog(this, "Ya existe un curso con ese nombre.", "Curso duplicado", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (cursoPres.guardarNuevoCurso(instituto, nombre, descripcion, duracion, horas, creditos,
                campoUrl.getText().trim(), previasSeleccionadas) != null) {
            limpiarFormulario();
            JOptionPane.showMessageDialog(this, "Curso registrado y guardado con éxito", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void cargarCursos() {
        HashSet<DataCurso> cursos = cursoPres.cargarCursos();
        DefaultTableModel modelo = (DefaultTableModel) tablaCursos.getModel();
        modelo.setRowCount(0);
        if (cursos != null) {
            for (DataCurso curso : cursos) {
                modelo.addRow(new Object[] {curso.nombreCurso(), curso.instituto() == null ? "N/A" : curso.instituto().nombreI(),
                    curso.duracion(), curso.cantHoras(), curso.cantCred(), curso.fechaReg(), curso.url(), curso.descCurso()});
            }
        }
    }

    private void limpiarFormulario() {
        campoNombre.setText("");
        campoDescripcion.setText("");
        campoDuracion.setText("");
        campoHoras.setText("");
        campoCreditos.setText("");
        campoUrl.setText("");
        previasSeleccionadas.clear();
        listaPrevias.repaint();
    }
}
