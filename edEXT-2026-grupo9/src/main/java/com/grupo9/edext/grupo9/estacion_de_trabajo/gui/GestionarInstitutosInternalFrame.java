package com.grupo9.edext.grupo9.estacion_de_trabajo.gui;

import com.grupo9.edext.grupo9.estacion_de_trabajo.cliente.InstitutoPres;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataInstituto;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.HashSet;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class GestionarInstitutosInternalFrame extends JInternalFrame {

    private final InstitutoPres institutoPres = new InstitutoPres();
    private final JTextField campoNombre = new JTextField(25);
    private final JLabel etiquetaError = new JLabel("* Instituto ya existente.");
    private final JTable tablaInstitutos = new JTable();

    public GestionarInstitutosInternalFrame() {
        super("Gestión de Institutos", true, true, true, true);
        initComponents();
        cargarInstitutos();
    }

    private void initComponents() {
        JPanel contenido = new JPanel(new BorderLayout(8, 8));
        contenido.setBorder(javax.swing.BorderFactory.createEmptyBorder(8, 8, 8, 8));

        JPanel formulario = new JPanel(new FlowLayout(FlowLayout.LEFT));
        formulario.add(new JLabel("Nombre"));
        formulario.add(campoNombre);

        JButton botonCancelar = new JButton("Cancelar");
        botonCancelar.addActionListener(event -> limpiarFormulario());

        JButton botonGuardar = new JButton("Aceptar");
        botonGuardar.addActionListener(event -> guardarInstituto());

        etiquetaError.setForeground(new java.awt.Color(255, 0, 0));
        etiquetaError.setVisible(false);
        formulario.add(etiquetaError);
        formulario.add(botonCancelar);
        formulario.add(botonGuardar);

        DefaultTableModel modelo = new DefaultTableModel(new Object[][] {}, new String[] {"Nombre"}) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaInstitutos.setModel(modelo);

        JButton botonRefrescar = new JButton("Refrescar");
        botonRefrescar.addActionListener(event -> cargarInstitutos());
        JPanel encabezadoTabla = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        encabezadoTabla.add(botonRefrescar);

        JPanel listado = new JPanel(new BorderLayout(0, 4));
        listado.add(new JLabel("Lista de Institutos"), BorderLayout.NORTH);
        listado.add(new JScrollPane(tablaInstitutos), BorderLayout.CENTER);
        listado.add(encabezadoTabla, BorderLayout.SOUTH);

        contenido.add(new JLabel("Ingresar nuevo Instituto"), BorderLayout.NORTH);
        contenido.add(formulario, BorderLayout.CENTER);
        contenido.add(listado, BorderLayout.SOUTH);

        setContentPane(contenido);
        setDefaultCloseOperation(JInternalFrame.DISPOSE_ON_CLOSE);
        setSize(680, 500);
        setLocation(20, 20);
    }

    private void guardarInstituto() {
        String nombre = campoNombre.getText().trim();
        etiquetaError.setVisible(false);

        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe ingresar el nombre del instituto.", "Dato requerido", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (Boolean.TRUE.equals(institutoPres.existeInstituto(nombre))) {
            etiquetaError.setVisible(true);
            JOptionPane.showMessageDialog(this, "Ya existe un instituto con ese nombre. Prueba con otro nombre.", "Instituto duplicado", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!institutoPres.guardarNuevoInstituto(nombre)) {
            JOptionPane.showMessageDialog(this, "No se pudo guardar el instituto.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        limpiarFormulario();
        cargarInstitutos();
        JOptionPane.showMessageDialog(this, "Instituto registrado y guardado con éxito", "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }

    private void cargarInstitutos() {
        HashSet<DataInstituto> institutos = institutoPres.cargarInstitutos();
        DefaultTableModel modelo = (DefaultTableModel) tablaInstitutos.getModel();
        modelo.setRowCount(0);

        if (institutos != null) {
            for (DataInstituto instituto : institutos) {
                modelo.addRow(new Object[] {instituto.nombreI()});
            }
        }
    }

    private void limpiarFormulario() {
        campoNombre.setText("");
        etiquetaError.setVisible(false);
    }
}
