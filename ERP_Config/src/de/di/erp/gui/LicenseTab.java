package de.di.erp.gui;

import de.di.license.check.ELOERPLicenseChecker;
import de.di.license.check.LicenseException;
import de.di.license.check.LicenseKey;
import de.di.license.check.ELOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ResourceBundle;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;
import org.apache.commons.codec.binary.Base64;

/**
 *
 * @author A. Sopicki
 */
public class LicenseTab extends javax.swing.JPanel implements ConfigTab {

    private String title = "License";
    private static final String bundleName = "de/di/erp/gui/resources/LicenseTab";
    private Config config = null;
    private java.util.Map<String, String> productInfo = null;
    private ResourceBundle bundle = ResourceBundle.getBundle(bundleName);
    private boolean setup = false;
    private volatile LicenseChecker checker = null;
    private org.jdesktop.application.ResourceMap resourceMap = null;

    /**
     * Creates new form LicenseTab
     */
    public LicenseTab() {

        resourceMap = org.jdesktop.application.Application.getInstance(de.di.erp.gui.ERPGUIApp.class).getContext().
                getResourceMap(LicenseTab.class);

        title = resourceMap.getString("tabTitle.text");

        initComponents();
        loadProductInfo();
        bundle = ResourceBundle.getBundle(bundleName);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        licensePanel = new javax.swing.JPanel();
        licenseLabel = new javax.swing.JLabel();
        changeLicenseButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        setName("Form"); // NOI18N
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }
        });
        addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                formFocusGained(evt);
            }
        });

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("de/di/erp/gui/resources/LicenseTab"); // NOI18N
        licensePanel.setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("licenseLabel.text"))); // NOI18N
        licensePanel.setName("licensePanel"); // NOI18N

        licenseLabel.setName("licenseLabel"); // NOI18N

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance().getContext().getResourceMap(LicenseTab.class);
        changeLicenseButton.setText(resourceMap.getString("changeLicenseButton.text")); // NOI18N
        changeLicenseButton.setName("changeLicenseButton"); // NOI18N
        changeLicenseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                changeLicenseButtonActionPerformed(evt);
            }
        });

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Id", "User/PC"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.setName("jTable1"); // NOI18N
        jScrollPane1.setViewportView(jTable1);
        jTable1.getColumnModel().getColumn(0).setMinWidth(30);
        jTable1.getColumnModel().getColumn(0).setPreferredWidth(30);
        jTable1.getColumnModel().getColumn(0).setMaxWidth(30);
        jTable1.getColumnModel().getColumn(0).setHeaderValue(resourceMap.getString("jTable1.columnModel.title0")); // NOI18N
        jTable1.getColumnModel().getColumn(1).setHeaderValue(resourceMap.getString("jTable1.columnModel.title1")); // NOI18N

        javax.swing.GroupLayout licensePanelLayout = new javax.swing.GroupLayout(licensePanel);
        licensePanel.setLayout(licensePanelLayout);
        licensePanelLayout.setHorizontalGroup(
            licensePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(licensePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(licensePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(changeLicenseButton)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 487, Short.MAX_VALUE)
                    .addComponent(licenseLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 487, Short.MAX_VALUE))
                .addContainerGap())
        );
        licensePanelLayout.setVerticalGroup(
            licensePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(licensePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(changeLicenseButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(licenseLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 303, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(licensePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(licensePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

private void changeLicenseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_changeLicenseButtonActionPerformed
    java.io.File f = getSelectedFile();
    if (f != null) {
        try {
            if (!f.exists()) {
                throw new Exception(bundle.getString("missingFile.text"));
            }

            if (!f.canRead()) {
                throw new Exception(bundle.getString("noReadAccess.text"));
            }

            java.io.File licenseFile = new java.io.File("../conf/license.txt");

            java.io.File configDir = new java.io.File(licenseFile.getParent());

            if (licenseFile.exists() && !licenseFile.canWrite()) {
                String msg = bundle.getString("noWriteAccess.text");
                msg = java.text.MessageFormat.format(msg, licenseFile.getAbsolutePath());
                throw new Exception(msg);
            } else if (!licenseFile.exists() && !configDir.canWrite()) {
                String msg = bundle.getString("noWriteAccess.text");
                msg = java.text.MessageFormat.format(msg, licenseFile.getAbsolutePath());
                throw new Exception(msg);
            }

            try {
                java.io.FileInputStream ins = null;
                java.io.FileOutputStream outs = null;

                java.nio.channels.FileChannel iChannel = null;
                java.nio.channels.FileChannel oChannel = null;

                ins = new java.io.FileInputStream(f);
                outs = new java.io.FileOutputStream(licenseFile);

                iChannel = ins.getChannel();
                oChannel = outs.getChannel();

                oChannel.transferFrom(iChannel, 0, iChannel.size());

                iChannel.close();
                oChannel.close();
                checker = null;
                showLicenseInformation();
            } catch (java.io.IOException ioex) {
                throw new Exception(bundle.getString("copyFailed.text"));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(),
                    bundle.getString("errorDialogTitle.text"),
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}//GEN-LAST:event_changeLicenseButtonActionPerformed

    private void formMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseClicked
        try {
            addClients();
        } catch (IOException ex) {
            Logger.getLogger(LicenseTab.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_formMouseClicked

    private void formFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_formFocusGained
//        System.out.println("Focus gained");
        try {
            addClients();
        } catch (IOException ex) {
            Logger.getLogger(LicenseTab.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_formFocusGained

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
        setup = true;
        showLicenseInformation();
        setup = false;
    }

    private java.io.File getSelectedFile() {
        java.io.File f = null;

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int result = fileChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile();
        }

        return null;
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton changeLicenseButton;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JLabel licenseLabel;
    private javax.swing.JPanel licensePanel;
    // End of variables declaration//GEN-END:variables

    private void showLicenseInformation() {

        if (config.getProperty(Config.Property.IndexServerURL, "").length() == 0) {
            licenseLabel.setText(bundle.getString("warning.elo.text"));
            return;
        }

        if (checker == null) {
            licenseLabel.setText(bundle.getString("check.license.text"));
            changeLicenseButton.setEnabled(false);

            checker = new LicenseChecker(!setup);
//            System.out.println("Starting license chech");
            checker.start();

        }
    }

    private void loadProductInfo() {
        productInfo = new java.util.HashMap<String, String>();

        InputStream in = this.getClass().getResourceAsStream("resources/product.properties");

        if (in != null) {
            ProductInfo.readProductInfo(in, productInfo);

//            System.out.println("product: "+productInfo.get("product"));
        }
    }

    private class LicenseChecker extends Thread {

        private boolean warnOnLicenseChange = false;
        private ELOERPLicenseChecker checker = null;

        LicenseChecker(boolean warn) {
            warnOnLicenseChange = warn;
        }

        @Override
        public void run() {
            try {
                java.io.File licenseFile = new java.io.File("../conf/license.txt");

                if (!licenseFile.exists() || licenseFile.length() == 0) {
                    throw new Exception();
                }

                LicenseKey key = LicenseKey.readFromFile(licenseFile);
                checker = new ELOERPLicenseChecker(productInfo.get("product"));
                checker.setSettings(config);
                checker.check(key, checker.getRounds());
//                System.out.println("product: " + productInfo.get("product"));
                int currentClientCount = 0;
                int maxClientCount = 0;
                String clients = "0";

                java.io.File inputFile = new java.io.File("../conf/extra.properties");

                if (inputFile.canRead()) {
                    ClientCountFile countFile = ClientCountFile.readFromFile(inputFile);
                    currentClientCount = countFile.getClientCount();
                }

                Pattern pattern = Pattern.compile("^(\\d+)\\s.*");
                Matcher matcher = pattern.matcher(key.getAttribute("license-type").getValue());

                if (matcher.matches()) {
                    clients = matcher.group(1);
                }

                try {
                    maxClientCount = Integer.parseInt(clients);
                } catch (NumberFormatException nfe) {
                }

                String builder = "";

                builder += "<html>";
                //"Lizenznehmer: ");
                builder += resourceMap.getString("license.licensee.text");
                builder += " ";
                builder += key.getAttribute("client").getValue();
                builder += "<br />";
                //Gültig bis: ");
                builder += resourceMap.getString("license.validUntil.text");
                builder += " ";
                builder += java.text.DateFormat.getDateInstance().format(key.getExpirationDate());
                builder += "<br />";
                //ERP-System: ");
                builder += resourceMap.getString("license.erpSystem.text");
                builder += " ";
                builder += key.getAttribute("ERP-system").getValue();
                builder += "<br />";
                //Lizenzart: ");
                builder += resourceMap.getString("license.licenseType.text");
                builder += " ";
                builder += key.getAttribute("license-type").getValue();
                builder += "<br />";
                //Dokumenten Management System:
                builder += resourceMap.getString("license.dms.text");
                builder += " ";
                builder += key.getAttribute("ELO-version").getValue();
                java.io.File dataFile = new java.io.File("classes/de/di/erpconnect/web/data.pkg");
//                 java.io.File dataFile = new java.io.File("data.pkg");
//                System.out.println(dataFile.getAbsolutePath());
                if (!dataFile.exists()) {
                    dataFile.createNewFile();
                }
                BufferedReader br = new BufferedReader(new FileReader(dataFile));
                String encodedLines = br.readLine();
                if (encodedLines != null) {
                    String numberOfClients = new String(Base64.decodeBase64(encodedLines)).substring(2);
                    currentClientCount = Integer.parseInt(numberOfClients);
                    //20% extra to have some extra clients for migrating clients
                    builder += "<br /><br /><b>";
                    if (currentClientCount > maxClientCount * 1.2) {
                        builder += "<br /><font color=\"#ff0000\">";
                        //Anzahl der Clients überschritten. Maximal "+maxClientCount+" Clients erlaubt.");
                        String msg = resourceMap.getString("license.clientsExceeded.text");
                        builder += java.text.MessageFormat.format(msg, maxClientCount);
                        builder += "</font>";
                    } else {
                        builder += "<br />";
                        String msg = resourceMap.getString("license.clients.text");
                        builder += java.text.MessageFormat.format(msg, currentClientCount);
                        //currentClientCount+" Clients zur Zeit in Verwendung");
                    }
                    builder += "</b>";

                    addClients();
                }
                builder += "</html>";
                licenseLabel.setText(builder);
            } catch (ELOException eloex) {
                licenseLabel.setText(bundle.getString("warning.elo.text"));
                changeLicenseButton.setEnabled(true);
                checker = null;
                return;
            } catch (LicenseException lex) {
//            System.out.println(lex.getMessage());
                String msg = bundle.getString("warning.license.text");
                licenseLabel.setText(bundle.getString("license.violation.text"));
                if (warnOnLicenseChange) {
                    JOptionPane.showMessageDialog(LicenseTab.this, msg);
                }
                checker = null;
                changeLicenseButton.setEnabled(true);
                return;
//            lex.printStackTrace();
            } catch (Exception ex) {
                licenseLabel.setText(bundle.getString("license.violation.text"));
                changeLicenseButton.setEnabled(true);
                checker = null;
                ex.printStackTrace(System.err);
                return;
            }

            if (warnOnLicenseChange) {
                String msg = bundle.getString("warning.restart.text");
                JOptionPane.showMessageDialog(LicenseTab.this, msg);
            }

            changeLicenseButton.setEnabled(true);
            checker = null;
        }
    }

    public void addClients() throws IOException {
        java.io.File dataFile = new java.io.File("classes/de/di/erpconnect/web/data.pkg");
//        java.io.File dataFile = new java.io.File("data.pkg");
        if (dataFile.exists()) {
            DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
            int rowCount = model.getRowCount();
            for (int i = rowCount - 1; i >= 0; i--) {
                model.removeRow(i);
            }
            BufferedReader br = new BufferedReader(new FileReader(dataFile));
            br.readLine();
            String encodedComputers = br.readLine();
            if (encodedComputers != null) {
                String computersAvailable = new String(Base64.decodeBase64(encodedComputers)).substring(2);
                StringTokenizer tk = new StringTokenizer(computersAvailable, "|");
                int count = 1;
                while (tk.hasMoreTokens()) {

                    model.addRow(new Object[]{count, tk.nextToken()});
                    count++;
                }
            }
        }

    }
}