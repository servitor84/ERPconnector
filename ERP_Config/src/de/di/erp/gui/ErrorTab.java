package de.di.erp.gui;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author Rahman
 */
public class ErrorTab extends javax.swing.JPanel implements ConfigTab {

    private String title = "License";
    private Config config = null;
    private org.jdesktop.application.ResourceMap resourceMap = null;
    private DefaultListModel mod = new DefaultListModel();
    private Properties prop = new Properties();
    private final String configPath = "../conf/config.properties";

    /**
     * Creates new form Error
     */
    public ErrorTab() {
        resourceMap = org.jdesktop.application.Application.getInstance(de.di.erp.gui.ERPGUIApp.class).getContext().
                getResourceMap(ErrorTab.class);
        title = resourceMap.getString("tabTitle.text");
        initComponents();
        refreshJList();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();

        setName("Form"); // NOI18N

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("de/di/erp/gui/resources/ErrorTab"); // NOI18N
        jButton1.setText(bundle.getString("launch.again")); // NOI18N
        jButton1.setName("jButton1"); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText(bundle.getString("open.in.text.editor")); // NOI18N
        jButton2.setName("jButton2"); // NOI18N
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        jList1.setModel(mod);
        jList1.setName("jList1"); // NOI18N
        jScrollPane1.setViewportView(jList1);

        jButton3.setText(bundle.getString("Error.Get.File")); // NOI18N
        jButton3.setName("jButton3"); // NOI18N
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setText(bundle.getString("Error.Delete")); // NOI18N
        jButton4.setName("jButton4"); // NOI18N
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setText(bundle.getString("Error.Refresh")); // NOI18N
        jButton5.setName("jButton5"); // NOI18N
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jLabel1.setText(bundle.getString("Error.Document.Number")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N

        jLabel2.setName("jLabel2"); // NOI18N

        jLabel3.setText(bundle.getString("Error.Document.After.Filter")); // NOI18N
        jLabel3.setName("jLabel3"); // NOI18N

        jLabel4.setName("jLabel4"); // NOI18N

        jTextField1.setName("jTextField1"); // NOI18N
        jTextField1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField1FocusLost(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 617, Short.MAX_VALUE)
                        .addGap(31, 31, 31)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(19, 19, 19))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(66, 66, 66)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3)
                        .addGap(28, 28, 28)
                        .addComponent(jLabel4)
                        .addGap(182, 182, 182))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap(13, Short.MAX_VALUE)
                        .addComponent(jButton1)
                        .addGap(18, 18, 18)
                        .addComponent(jButton2)
                        .addGap(18, 18, 18)
                        .addComponent(jButton3)
                        .addGap(18, 18, 18)
                        .addComponent(jButton4)
                        .addGap(256, 256, 256))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 407, Short.MAX_VALUE)
                        .addGap(8, 8, 8)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel2)
                    .addComponent(jLabel4)
                    .addComponent(jButton5)
                    .addComponent(jLabel1)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        openTextFile();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        retry();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        openFile();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        deleteFile();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        if (!jTextField1.getText().equals("")) {
            if (jTextField1.getText().length() == 1) {
                java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("de/di/erp/gui/resources/ErrorTab");
                java.util.ResourceBundle bundle1 = java.util.ResourceBundle.getBundle("de/di/erp/gui/resources/ERPGUIApp");
                JOptionPane.showConfirmDialog(this, bundle.getString("Error.Min"), bundle1.getString("Application.title"), JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE);
            } else {
                filtering();
            }
        } else {
            refreshJList();
        }
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jTextField1FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField1FocusLost
//        filtering();
    }//GEN-LAST:event_jTextField1FocusLost
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JList jList1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables

    @Override
    public JPanel getJPanel() {
        return this;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public void setConfig(Config c) {
        config = c;
    }

    private void refreshJList() {
        try {
            prop.load(new FileInputStream(configPath));
            String var = prop.getProperty("Directories.ErrorOutput");
            File folder = new File(var);
            File[] listOfFiles = folder.listFiles();
            int ducumetnsNumber = 0;
            mod.clear();
            for (File f : listOfFiles) {
                if (prop.getProperty("Trigger.DocumentFileExtension").contains(f.getName().substring(f.getName().length() - 4))) {
                    mod.addElement(f.getName());
                    ducumetnsNumber++;
                }
            }
            jLabel2.setText(String.valueOf(ducumetnsNumber));
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error.java cannot find config.properties " + ex.getMessage());
        }
        jList1.setSelectedIndex(0);
    }

    private void openTextFile() {
        int position = 0;
        try {
            position = jList1.getSelectedIndex();
            if (position == -1) {
                JOptionPane.showMessageDialog(this, "Either the list of documents is empty or non ducument is selected");
            } else {
                File text = new File(prop.getProperty("Directories.ErrorOutput") + "\\" + mod.elementAt(position).toString().substring(0, mod.elementAt(position).toString().length() - 4) + prop.getProperty("Cleanup.DeleteByExtension"));
                java.awt.Desktop.getDesktop().edit(text);
            }
        } catch (IOException ioex) {
            JOptionPane.showMessageDialog(this, "Error.openTextFile() cannot open " + mod.elementAt(position).toString().substring(mod.elementAt(position).toString().length() - 4) + prop.getProperty("Cleanup.DeleteByExtension") + ioex.getMessage());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Excepiton in Error.openTextFile() " + ex.getMessage());
        }
    }

    private void openFile() {
        int position = 0;
        try {
            position = jList1.getSelectedIndex();
            if (position == -1) {
                JOptionPane.showMessageDialog(this, "Either the list of documents is empty or non ducument is selected");
            } else {
                File pdfFile = new File(prop.getProperty("Directories.ErrorOutput") + "\\" + mod.elementAt(position));
                if (java.awt.Desktop.isDesktopSupported()) {
                    java.awt.Desktop.getDesktop().open(pdfFile);
                } else {
                    JOptionPane.showMessageDialog(this, "Awt Desktop is not supported!");
                }
            }
        } catch (IOException ioex) {
            JOptionPane.showMessageDialog(this, "Error.openTextFile() cannot open " + mod.elementAt(position).toString().substring(mod.elementAt(position).toString().length() - 4) + prop.getProperty("Cleanup.DeleteByExtension") + ioex.getMessage());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Excepiton in Error.openTextFile() " + ex.getMessage());
        }
    }

    private void deleteFile() {
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("de/di/erp/gui/resources/ErrorTab");
        int delete = JOptionPane.showConfirmDialog(this, bundle.getString("Error.Delete.Message"), bundle.getString("Error.Delete.Titel"), JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (delete == 0) {
            int position = 0;
            int[] positions;
            try {
                positions = jList1.getSelectedIndices();
                if (positions.length <= 1) {
                    position = jList1.getSelectedIndex();
                    if (position == -1) {
                        JOptionPane.showMessageDialog(this, "Either the list of documents is empty or non ducument is selected");
                    } else {
                        File textFile = new File(prop.getProperty("Directories.ErrorOutput") + "\\" + mod.elementAt(position).toString().substring(0, mod.elementAt(position).toString().length() - 4) + prop.getProperty("Cleanup.DeleteByExtension"));
                        File pdfFile = new File(prop.getProperty("Directories.ErrorOutput") + "\\" + mod.elementAt(position));
                        File triggerFile = new File(prop.getProperty("Directories.ErrorOutput") + "\\" + mod.elementAt(position).toString().substring(0, mod.elementAt(position).toString().length() - 4) + prop.getProperty("Trigger.TriggerExtension"));
                        if (!pdfFile.delete()) {
                            JOptionPane.showMessageDialog(this, "Document file cannot be removed");
                        }
                        if (!textFile.delete()) {
                            JOptionPane.showMessageDialog(this, "Text file cannot be removed");
                        }
                        if (!triggerFile.delete()) {
                            JOptionPane.showMessageDialog(this, "Trigger file cannot be removed");
                        }
                    }
                } else {
                    for (int i = 0; i < positions.length; i++) {
                        File textFile = new File(prop.getProperty("Directories.ErrorOutput") + "\\" + mod.elementAt(i).toString().substring(0, mod.elementAt(position).toString().length() - 4) + prop.getProperty("Cleanup.DeleteByExtension"));
                        File pdfFile = new File(prop.getProperty("Directories.ErrorOutput") + "\\" + mod.elementAt(i));
                        File triggerFile = new File(prop.getProperty("Directories.ErrorOutput") + "\\" + mod.elementAt(i).toString().substring(0, mod.elementAt(position).toString().length() - 4) + prop.getProperty("Trigger.TriggerExtension"));
                        if (!pdfFile.delete()) {
                            JOptionPane.showMessageDialog(this, "Document file cannot be removed");
                        }
                        if (!textFile.delete()) {
                            JOptionPane.showMessageDialog(this, "Text file cannot be removed");
                        }
                        if (!triggerFile.delete()) {
                            JOptionPane.showMessageDialog(this, "Trigger file cannot be removed");
                        }
                    }
                }
                refreshJList();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Excepiton in Error.deleteFile() " + ex.getMessage());
            }
        }
    }

    private void retry() {
        int[] positions;
        DefaultListModel modTemp = new DefaultListModel();
        try {
            positions = jList1.getSelectedIndices();
            Arrays.sort(positions);
            for (int position : positions) {
                String path = prop.getProperty("Directories.ErrorOutput") + "\\" + mod.elementAt(position).toString().substring(0, mod.elementAt(position).toString().length() - 4);
                deleteFromBackUpDirectory((new File(path)).getName());
                shiftToInputDirectory(path, position);
                modTemp.addElement(mod.elementAt(position));
            }
            for (int i = 0; i < modTemp.getSize(); i++) {
                for (int j = 0; j < mod.getSize(); j++) {
                    if (modTemp.elementAt(i) == mod.elementAt(j)) {
                        mod.remove(j);
                    }
                }
            }
            jList1.setModel(mod);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Exception in Error.retry() " + ex.getMessage());
        }
    }

    private void shiftToInputDirectory(String path, int position) {
        try {
            String inputDirectory = prop.getProperty("Directories.Input");
            for (String ext : prop.getProperty("Trigger.DocumentFileExtension").split(",")) {
                File originalFile = new File(path + ext);
                if (originalFile.exists()) {
                    String newOriginalFile = mod.elementAt(position).toString();
                    originalFile.renameTo(new File(inputDirectory + "\\" + newOriginalFile));
                    originalFile.delete();
                }
            }
            File textFile = new File(path + prop.getProperty("Cleanup.DeleteByExtension"));
            File sigFile = new File(path + prop.getProperty("Trigger.TriggerExtension"));
            if (textFile.exists()) {
                String newTextFile = textFile.getName().substring(0, textFile.getName().indexOf(".")) + prop.getProperty("Cleanup.DeleteByExtension");
                textFile.renameTo(new File(inputDirectory + "\\" + newTextFile));
                textFile.delete();
            }
            if (sigFile.exists()) {
                String newSigFile = sigFile.getName().substring(0, sigFile.getName().indexOf(".")) + prop.getProperty("Trigger.TriggerExtension");
                sigFile.renameTo(new File(inputDirectory + "\\" + newSigFile));
                sigFile.delete();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Exception in Error.shiftToInputDirectory() " + ex.getMessage());
        }

    }

    private void deleteFromBackUpDirectory(String name) {
        try {
            String inputDirectory = prop.getProperty("Directories.Backup", "");
            File originalFile = new File(inputDirectory + "\\" + name + prop.getProperty("Trigger.DocumentFileExtension"));
            File textFile = new File(inputDirectory + "\\" + name + prop.getProperty("Cleanup.DeleteByExtension"));
            File sigFile = new File(inputDirectory + "\\" + name + prop.getProperty("Trigger.TriggerExtension"));
            originalFile.delete();
            textFile.delete();
            sigFile.delete();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Exception in Error.deleteFromBackUpDirectory(). Cannot delete file " + name + prop.getProperty("Trigger.DocumentFileExtension") + " in Backup folder " + ex.getMessage());
        }
    }

    private void filtering() {
        String pattern = jTextField1.getText();
        try {
            prop.load(new FileInputStream(configPath));
            String var = prop.getProperty("Directories.ErrorOutput");
            File folder = new File(var);
            File[] listOfFiles = folder.listFiles();
            int ducumetnsNumber = 0;
            mod.clear();
            for (File f : listOfFiles) {
                if (prop.getProperty("Trigger.DocumentFileExtension").contains(f.getName().substring(f.getName().length() - 4))) {
                    if (f.getName().contains(pattern)) {
                        mod.addElement(f.getName());
                        ducumetnsNumber++;
                    }
                }
            }
            jLabel4.setText(String.valueOf(ducumetnsNumber));
            jList1.setModel(mod);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error.java cannot find config.properties " + ex.getMessage());
        }
        jList1.setSelectedIndex(0);
    }
}
