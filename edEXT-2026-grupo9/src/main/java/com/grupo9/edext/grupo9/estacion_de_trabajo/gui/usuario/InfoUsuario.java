package com.grupo9.edext.grupo9.estacion_de_trabajo.gui.usuario;

import com.grupo9.edext.grupo9.estacion_de_trabajo.gui.ConsultarEdicionJInternalFrame;
import com.grupo9.edext.grupo9.estacion_de_trabajo.gui.MainJFrame;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataDocente;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataEstudiante;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataUsuario;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

public class InfoUsuario extends javax.swing.JFrame {

    private final DataUsuario usuarioData;
    private final MainJFrame parentFrame;

    public InfoUsuario(DataUsuario usuarioData, MainJFrame parentFrame) {
        this.usuarioData = usuarioData;
        this.parentFrame = parentFrame;
        initComponents();
        setTitle("Información de Usuario");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(parentFrame);
        cargarDatos();
    }

    public InfoUsuario() {
        this.usuarioData = null;
        this.parentFrame = null;
        initComponents();
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    private void cargarDatos() {
        if (usuarioData == null) return;

        jTextFieldNick.setText(usuarioData.getNickname());
        jTextFieldNombre.setText(usuarioData.getNombre());
        jTextFieldApellido.setText(usuarioData.getApellido());
        jTextFieldEmail.setText(usuarioData.getEmail());
        jTextFieldFechaNac.setText(usuarioData.getFechaNac() != null ? usuarioData.getFechaNac().toString() : "");

        DefaultListModel<String> modelEdiciones = new DefaultListModel<>();

        if (usuarioData instanceof DataDocente doc) {
            jTextFieldTipo.setText("Docente");
            jTextFieldInstituto.setText(doc.getNombreInst() != null ? doc.getNombreInst() : "");
            jLabelEdiciones.setText("Ediciones que dicta:");
            if (doc.getEdiciones() != null) {
                for (String ed : doc.getEdiciones()) {
                    modelEdiciones.addElement(ed);
                }
            }
        } else if (usuarioData instanceof DataEstudiante est) {
            jTextFieldTipo.setText("Estudiante");
            jTextFieldInstituto.setText(""); // Instituto vacío para estudiantes
            jLabelEdiciones.setText("Ediciones a las que está inscripto:");
            if (est.getEdicionesInscriptas() != null) {
                for (String ed : est.getEdicionesInscriptas()) {
                    modelEdiciones.addElement(ed);
                }
            }
        } else {
            jTextFieldTipo.setText("Usuario");
            jTextFieldInstituto.setText("");
        }

        jListEdiciones.setModel(modelEdiciones);

        // Cargar Foto de Perfil
        if (usuarioData.getImagen() != null && !usuarioData.getImagen().trim().isEmpty()) {
            File imgFile = new File(usuarioData.getImagen());
            if (imgFile.exists()) {
                ImageIcon original = new ImageIcon(usuarioData.getImagen());
                Image scaled = original.getImage().getScaledInstance(140, 140, Image.SCALE_SMOOTH);
                jLabelImagen.setIcon(new ImageIcon(scaled));
                jLabelImagen.setText("");
            } else {
                jLabelImagen.setIcon(null);
                jLabelImagen.setText("Sin foto de perfil");
            }
        } else {
            jLabelImagen.setIcon(null);
            jLabelImagen.setText("Sin foto de perfil");
        }

        // Hacer todos los campos no editables
        jTextFieldNick.setEditable(false);
        jTextFieldNombre.setEditable(false);
        jTextFieldApellido.setEditable(false);
        jTextFieldEmail.setEditable(false);
        jTextFieldFechaNac.setEditable(false);
        jTextFieldTipo.setEditable(false);
        jTextFieldInstituto.setEditable(false);

        // Evento de doble clic en las ediciones
        jListEdiciones.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && jListEdiciones.getSelectedValue() != null) {
                    String nombreEdicion = jListEdiciones.getSelectedValue();
                    if (parentFrame != null) {
                        dispose();
                        ConsultarEdicionJInternalFrame frame = new ConsultarEdicionJInternalFrame(nombreEdicion);
                        parentFrame.mostrarInternalFrame(frame);
                    }
                }
            }
        });
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {

        jLabelNick = new javax.swing.JLabel();
        jTextFieldNick = new javax.swing.JTextField();
        jLabelNombre = new javax.swing.JLabel();
        jTextFieldNombre = new javax.swing.JTextField();
        jLabelApellido = new javax.swing.JLabel();
        jTextFieldApellido = new javax.swing.JTextField();
        jLabelEmail = new javax.swing.JLabel();
        jTextFieldEmail = new javax.swing.JTextField();
        jLabelFechaNac = new javax.swing.JLabel();
        jTextFieldFechaNac = new javax.swing.JTextField();
        jLabelTipo = new javax.swing.JLabel();
        jTextFieldTipo = new javax.swing.JTextField();
        jLabelInstituto = new javax.swing.JLabel();
        jTextFieldInstituto = new javax.swing.JTextField();
        jLabelImagen = new javax.swing.JLabel();
        jLabelEdiciones = new javax.swing.JLabel();
        jScrollPaneEdiciones = new javax.swing.JScrollPane();
        jListEdiciones = new javax.swing.JList<>();
        jButtonCerrar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabelNick.setText("Nickname:");

        jLabelNombre.setText("Nombre:");

        jLabelApellido.setText("Apellido:");

        jLabelEmail.setText("Email:");

        jLabelFechaNac.setText("Fecha Nacimiento:");

        jLabelTipo.setText("Tipo de Usuario:");

        jLabelInstituto.setText("Instituto:");

        jLabelImagen.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelImagen.setText("Sin foto de perfil");
        jLabelImagen.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jLabelImagen.setPreferredSize(new java.awt.Dimension(140, 140));

        jScrollPaneEdiciones.setViewportView(jListEdiciones);

        jButtonCerrar.setText("Cerrar");
        jButtonCerrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCerrarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButtonCerrar)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabelEdiciones)
                        .addComponent(jScrollPaneEdiciones)
                        .addGroup(layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabelNick)
                                .addComponent(jLabelNombre)
                                .addComponent(jLabelApellido)
                                .addComponent(jLabelEmail)
                                .addComponent(jLabelFechaNac)
                                .addComponent(jLabelTipo)
                                .addComponent(jLabelInstituto))
                            .addGap(18, 18, 18)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jTextFieldNick, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
                                .addComponent(jTextFieldNombre)
                                .addComponent(jTextFieldApellido)
                                .addComponent(jTextFieldEmail)
                                .addComponent(jTextFieldFechaNac)
                                .addComponent(jTextFieldTipo)
                                .addComponent(jTextFieldInstituto))
                            .addGap(25, 25, 25)
                            .addComponent(jLabelImagen, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(20, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabelNick)
                            .addComponent(jTextFieldNick, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabelNombre)
                            .addComponent(jTextFieldNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabelApellido)
                            .addComponent(jTextFieldApellido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabelEmail)
                            .addComponent(jTextFieldEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabelFechaNac)
                            .addComponent(jTextFieldFechaNac, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabelTipo)
                            .addComponent(jTextFieldTipo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabelInstituto)
                            .addComponent(jTextFieldInstituto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jLabelImagen, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jLabelEdiciones)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPaneEdiciones, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButtonCerrar)
                .addContainerGap(15, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>

    private void jButtonCerrarActionPerformed(java.awt.event.ActionEvent evt) {
        dispose();
    }

    // Variables declaration - do not modify
    private javax.swing.JButton jButtonCerrar;
    private javax.swing.JLabel jLabelApellido;
    private javax.swing.JLabel jLabelEdiciones;
    private javax.swing.JLabel jLabelEmail;
    private javax.swing.JLabel jLabelFechaNac;
    private javax.swing.JLabel jLabelImagen;
    private javax.swing.JLabel jLabelInstituto;
    private javax.swing.JLabel jLabelNick;
    private javax.swing.JLabel jLabelNombre;
    private javax.swing.JLabel jLabelTipo;
    private javax.swing.JList<String> jListEdiciones;
    private javax.swing.JScrollPane jScrollPaneEdiciones;
    private javax.swing.JTextField jTextFieldApellido;
    private javax.swing.JTextField jTextFieldEmail;
    private javax.swing.JTextField jTextFieldFechaNac;
    private javax.swing.JTextField jTextFieldInstituto;
    private javax.swing.JTextField jTextFieldNick;
    private javax.swing.JTextField jTextFieldNombre;
    private javax.swing.JTextField jTextFieldTipo;
    // End of variables declaration
}
