/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.grupo9.edext.grupo9.estacion_de_trabajo.gui.usuario;

import com.grupo9.edext.grupo9.estacion_de_trabajo.cliente.InstitutoPres;
import com.grupo9.edext.grupo9.estacion_de_trabajo.cliente.UsuarioPres;
import com.grupo9.edext.grupo9.estacion_de_trabajo.gui.MainJFrame;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataUsuario;
import java.awt.Image;
import java.io.File;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.logging.Logger;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author Usuario
 */
public class ModificarUsuarioFrame extends JFrame {
    
    private static final Logger logger = Logger.getLogger(ModificarUsuarioFrame.class.getName());
    private File archivoImagenSeleccionado;
    private MainJFrame parentFrame;
    private DataUsuario usuarioActual;
    private final UsuarioPres usuarioPres = new UsuarioPres();
    private final InstitutoPres institutoPres = new InstitutoPres();
    private ButtonGroup buttonGroupEstYDocMod = new ButtonGroup();

    /**
     * Creates new form ModificarUsuarioFrame
     */
    public ModificarUsuarioFrame() {
        initComponents();
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    public ModificarUsuarioFrame(DataUsuario du, MainJFrame parent) {
        this.usuarioActual = du;
        this.parentFrame = parent;
        initComponents();
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setTitle("Modificar Usuario - " + (du != null ? du.getNickname() : ""));

        jSpinnerFechaNac.setModel(new javax.swing.SpinnerDateModel(new Date(), null, null, java.util.Calendar.DAY_OF_MONTH));
        JSpinner.DateEditor editor = new JSpinner.DateEditor(jSpinnerFechaNac, "dd/MM/yyyy");
        jSpinnerFechaNac.setEditor(editor);

        jButtonGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonGuardarActionPerformed(evt);
            }
        });

        jButtonElim.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonEliminarActionPerformed(evt);
            }
        });

        jButtonCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCancelarActionPerformed(evt);
            }
        });

        if (usuarioActual != null) {
            cargarDatosUsuario();
        }
    }

    private void cargarDatosUsuario() {
        jTextFieldNickMod.setText(usuarioActual.getNickname());
        jTextFieldNickMod.setEnabled(false);
        jTextFieldNickMod.setEditable(false);

        jTextFieldEmailMod.setText(usuarioActual.getEmail());
        jTextFieldEmailMod.setEnabled(false);
        jTextFieldEmailMod.setEditable(false);

        jTextFieldNomMod.setText(usuarioActual.getNombre());
        jTextFieldApelMod.setText(usuarioActual.getApellido());

        if (usuarioActual.getFechaNac() != null) {
            java.util.Date fechaUtil = java.sql.Date.valueOf(usuarioActual.getFechaNac());
            jSpinnerFechaNac.setValue(fechaUtil);
        }

        if (usuarioActual.getImagen() != null && !usuarioActual.getImagen().isEmpty()) {
            this.archivoImagenSeleccionado = new File(usuarioActual.getImagen());
            if (this.archivoImagenSeleccionado.exists()) {
                javax.swing.ImageIcon iconoOriginal = new javax.swing.ImageIcon(usuarioActual.getImagen());
                int ancho = jLabelImagen.getWidth() > 0 ? jLabelImagen.getWidth() : 160;
                int alto = jLabelImagen.getHeight() > 0 ? jLabelImagen.getHeight() : 160;
                java.awt.Image imagenEscalada = iconoOriginal.getImage().getScaledInstance(ancho, alto, java.awt.Image.SCALE_SMOOTH);
                jLabelImagen.setIcon(new javax.swing.ImageIcon(imagenEscalada));
                jLabelImagen.setText("");
            } else {
                jLabelImagen.setIcon(null);
                jLabelImagen.setText("Sin Imagen");
            }
        } else {
            jLabelImagen.setIcon(null);
            jLabelImagen.setText("Sin Imagen");
        }
    }

    private void jButtonGuardarActionPerformed(java.awt.event.ActionEvent evt) {
        String nick = jTextFieldNickMod.getText().trim();
        String nombre = jTextFieldNomMod.getText().trim();
        String apellido = jTextFieldApelMod.getText().trim();

        if (nombre.isEmpty() || apellido.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor complete los campos de Nombre y Apellido.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Date fechaUtil = (Date) jSpinnerFechaNac.getValue();
        LocalDate fechaNac = fechaUtil.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        String rutaImg = (this.archivoImagenSeleccionado != null) ? this.archivoImagenSeleccionado.getAbsolutePath() :
                         (usuarioActual != null ? usuarioActual.getImagen() : null);

        String passNueva = new String(jPasswordFieldPassMod.getPassword()).trim();
        String passActual = null;
        if (!passNueva.isEmpty()) {
            passActual = (usuarioActual != null) ? usuarioActual.getPassword() : null;
        } else {
            passNueva = null;
        }

        try {
            usuarioPres.modificarUsuario(nick, nombre, apellido, fechaNac, rutaImg, passActual, passNueva);
            JOptionPane.showMessageDialog(this, "¡Usuario actualizado exitosamente!", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            if (parentFrame != null) {
                parentFrame.refreshTablaUsuarios();
            }
            this.dispose();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al guardar los cambios: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void jButtonEliminarActionPerformed(java.awt.event.ActionEvent evt) {
        String nick = jTextFieldNickMod.getText().trim();
        int confirm = JOptionPane.showConfirmDialog(this,
            "¿Está seguro que desea eliminar al usuario '" + nick + "'?",
            "Confirmar Eliminación",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                usuarioPres.eliminarUsuario(nick);
                JOptionPane.showMessageDialog(this, "¡Usuario eliminado exitosamente!", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                if (parentFrame != null) {
                    parentFrame.refreshTablaUsuarios();
                }
                this.dispose();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error al eliminar usuario: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void jButtonCancelarActionPerformed(java.awt.event.ActionEvent evt) {
        this.dispose();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabelVistaDeMod = new javax.swing.JLabel();
        jLabelNickMod = new javax.swing.JLabel();
        jLabelEmailMod = new javax.swing.JLabel();
        jLabelNomMod = new javax.swing.JLabel();
        jLabelApelMod = new javax.swing.JLabel();
        jLabelPassMod = new javax.swing.JLabel();
        jLabelFechaNacMod = new javax.swing.JLabel();
        jSpinnerFechaNac = new javax.swing.JSpinner();
        jButtonGuardar = new javax.swing.JButton();
        jButtonElim = new javax.swing.JButton();
        jButtonCancel = new javax.swing.JButton();
        jLabelImagen = new javax.swing.JLabel();
        jLabelSeleccionarFoto = new javax.swing.JLabel();
        jTextFieldNickMod = new javax.swing.JTextField();
        jTextFieldEmailMod = new javax.swing.JTextField();
        jTextFieldNomMod = new javax.swing.JTextField();
        jTextFieldApelMod = new javax.swing.JTextField();
        jPasswordFieldPassMod = new javax.swing.JPasswordField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabelVistaDeMod.setText("Vista de Modificación de Usuario");

        jLabelNickMod.setText("Nickname");

        jLabelEmailMod.setText("Email");

        jLabelNomMod.setText("Nombre");

        jLabelApelMod.setText("Apellido");

        jLabelPassMod.setText("Contraseña");

        jLabelFechaNacMod.setText("Fecha de Nacimiento");

        jSpinnerFechaNac.addChangeListener(this::jSpinnerFechaNacStateChanged);

        jButtonGuardar.setText("Guardar");

        jButtonElim.setText("Eliminar");

        jButtonCancel.setText("Cancelar");

        jLabelImagen.setText("Imagen");

        jLabelSeleccionarFoto.setForeground(new java.awt.Color(51, 51, 255));
        jLabelSeleccionarFoto.setText("Seleccionar Foto de Perfil");
        jLabelSeleccionarFoto.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jLabelSeleccionarFoto.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabelSeleccionarFotoMouseClicked(evt);
            }
        });

        jTextFieldNickMod.setEnabled(false);

        jTextFieldEmailMod.setEnabled(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(jButtonGuardar)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jButtonElim)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jButtonCancel))
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(jLabelNickMod)
                                    .addGap(18, 18, 18)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jPasswordFieldPassMod, javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jTextFieldApelMod, javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jTextFieldNomMod, javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jTextFieldEmailMod, javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jTextFieldNickMod))))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabelFechaNacMod)
                                    .addComponent(jSpinnerFechaNac, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(jLabelPassMod, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabelApelMod, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabelNomMod, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabelEmailMod, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(109, 109, 109)))
                        .addGap(68, 68, 68)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(15, 15, 15)
                                .addComponent(jLabelSeleccionarFoto))
                            .addComponent(jLabelImagen, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(37, 37, 37))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabelVistaDeMod)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabelVistaDeMod)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jLabelImagen, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabelSeleccionarFoto))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabelNickMod)
                            .addComponent(jTextFieldNickMod, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(6, 6, 6)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabelEmailMod)
                            .addComponent(jTextFieldEmailMod, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(8, 8, 8)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabelNomMod)
                            .addComponent(jTextFieldNomMod, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabelApelMod)
                            .addComponent(jTextFieldApelMod, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(8, 8, 8)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabelPassMod)
                            .addComponent(jPasswordFieldPassMod, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(48, 48, 48)
                .addComponent(jLabelFechaNacMod)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSpinnerFechaNac, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 43, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonGuardar)
                    .addComponent(jButtonElim)
                    .addComponent(jButtonCancel))
                .addGap(51, 51, 51))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jSpinnerFechaNacStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinnerFechaNacStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_jSpinnerFechaNacStateChanged

    private void jLabelSeleccionarFotoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelSeleccionarFotoMouseClicked
        JFileChooser fileChooser = new JFileChooser();

        // Filtrar para que solo permita elegir imágenes JPG o PNG
        FileNameExtensionFilter filtro = new FileNameExtensionFilter("Imágenes JPG y PNG", "jpg", "png");
        fileChooser.setFileFilter(filtro);

        int resultado = fileChooser.showOpenDialog(this);

        if (resultado == JFileChooser.APPROVE_OPTION) {
            this.archivoImagenSeleccionado = fileChooser.getSelectedFile();
            String rutaImagen = this.archivoImagenSeleccionado.getAbsolutePath();

            ImageIcon iconoOriginal = new ImageIcon(rutaImagen);

            int ancho = jLabelImagen.getWidth() > 0 ? jLabelImagen.getWidth() : 160;
            int alto = jLabelImagen.getHeight() > 0 ? jLabelImagen.getHeight() : 160;

            Image imagenEscalada = iconoOriginal.getImage().getScaledInstance(
                ancho,
                alto,
                Image.SCALE_SMOOTH
            );

            jLabelImagen.setIcon(new ImageIcon(imagenEscalada));
            jLabelImagen.setText(""); // Quita el texto "Imagen"
        }
    }//GEN-LAST:event_jLabelSeleccionarFotoMouseClicked

    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonCancel;
    private javax.swing.JButton jButtonElim;
    private javax.swing.JButton jButtonGuardar;
    private javax.swing.JLabel jLabelApelMod;
    private javax.swing.JLabel jLabelEmailMod;
    private javax.swing.JLabel jLabelFechaNacMod;
    private javax.swing.JLabel jLabelImagen;
    private javax.swing.JLabel jLabelNickMod;
    private javax.swing.JLabel jLabelNomMod;
    private javax.swing.JLabel jLabelPassMod;
    private javax.swing.JLabel jLabelSeleccionarFoto;
    private javax.swing.JLabel jLabelVistaDeMod;
    private javax.swing.JPasswordField jPasswordFieldPassMod;
    private javax.swing.JSpinner jSpinnerFechaNac;
    private javax.swing.JTextField jTextFieldApelMod;
    private javax.swing.JTextField jTextFieldEmailMod;
    private javax.swing.JTextField jTextFieldNickMod;
    private javax.swing.JTextField jTextFieldNomMod;
    // End of variables declaration//GEN-END:variables
}
