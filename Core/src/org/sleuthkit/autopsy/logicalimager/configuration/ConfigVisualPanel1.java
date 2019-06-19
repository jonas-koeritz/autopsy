/*
 * Autopsy Forensic Browser
 *
 * Copyright 2011-2019 Basis Technology Corp.
 * Contact: carrier <at> sleuthkit <dot> org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.sleuthkit.autopsy.logicalimager.configuration;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.openide.util.NbBundle;

/**
 * Configuration Visual Panel 1
 */
@SuppressWarnings("PMD.SingularField") // UI widgets cause lots of false positives
final class ConfigVisualPanel1 extends JPanel {

    private LogicalImagerConfig config;
    private String configFilename;
    private boolean newFile = true;

    /**
     * Creates new form ConfigVisualPanel1
     */
    ConfigVisualPanel1() {
        initComponents();
        configFileTextField.getDocument().addDocumentListener(new MyDocumentListener(this));
    }

    @NbBundle.Messages({
        "ConfigVisualPanel1.selectConfigurationFile=Select configuration file"
    })
    @Override
    public String getName() {
        return Bundle.ConfigVisualPanel1_selectConfigurationFile();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jLabel1 = new javax.swing.JLabel();
        configFileTextField = new javax.swing.JTextField();
        browseButton = new javax.swing.JButton();

        org.openide.awt.Mnemonics.setLocalizedText(jLabel1, org.openide.util.NbBundle.getMessage(ConfigVisualPanel1.class, "ConfigVisualPanel1.jLabel1.text_1")); // NOI18N

        configFileTextField.setEditable(false);
        configFileTextField.setText(org.openide.util.NbBundle.getMessage(ConfigVisualPanel1.class, "ConfigVisualPanel1.configFileTextField.text_1")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(browseButton, org.openide.util.NbBundle.getMessage(ConfigVisualPanel1.class, "ConfigVisualPanel1.browseButton.text")); // NOI18N
        browseButton.setToolTipText(org.openide.util.NbBundle.getMessage(ConfigVisualPanel1.class, "ConfigVisualPanel1.browseButton.toolTipText")); // NOI18N
        browseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                browseButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(configFileTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 279, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(browseButton)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(116, 116, 116)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(configFileTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(browseButton))
                .addContainerGap(141, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    @NbBundle.Messages({
        "ConfigVisualPanel1.chooseFileTitle=Select a Logical Imager configuration"
    })
    private void browseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_browseButtonActionPerformed
         chooseFile(Bundle.ConfigVisualPanel1_chooseFileTitle());
    }//GEN-LAST:event_browseButtonActionPerformed

    @NbBundle.Messages({
        "ConfigVisualPanel1.fileNameExtensionFilter=Configuration JSON File",
        "ConfigVisualPanel1.invalidConfigJson=Invalid config JSON: ",
        "ConfigVisualPanel1.configurationError=Configuration error",
    })
    private void chooseFile(String title) {
        final String jsonExt = ".json"; // NON-NLS
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle(title);
        fileChooser.setDragEnabled(false);
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        FileFilter filter = new FileNameExtensionFilter(Bundle.ConfigVisualPanel1_fileNameExtensionFilter(), new String[] {"json"}); // NON-NLS
        fileChooser.setFileFilter(filter);
        fileChooser.setSelectedFile(new File("logical-imager-config.json")); // default
        fileChooser.setMultiSelectionEnabled(false);
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            String path = fileChooser.getSelectedFile().getPath();
            if (new File(path).exists()) {
                try {
                    loadConfigFile(path);
                    configFilename = path;
                    configFileTextField.setText(path);
                    newFile = false;
                } catch (JsonIOException | JsonSyntaxException | IOException ex) {
                    JOptionPane.showMessageDialog(this, 
                        Bundle.ConfigVisualPanel1_invalidConfigJson() + ex.getMessage() , 
                        Bundle.ConfigVisualPanel1_configurationError(), 
                        JOptionPane.ERROR_MESSAGE);
                }
            } else {
                if (!path.endsWith(jsonExt)) {
                    path += jsonExt;
                }
                configFilename = path;
                configFileTextField.setText(path);
                config = new LogicalImagerConfig();
                newFile = true;
            }
        }
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton browseButton;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JTextField configFileTextField;
    private javax.swing.JLabel jLabel1;
    // End of variables declaration//GEN-END:variables

    @NbBundle.Messages({
        "# {0} - filename",
        "ConfigVisualPanel1.configFileIsEmpty=Configuration file {0} is empty",
    })
    private void loadConfigFile(String path) throws FileNotFoundException, JsonIOException, JsonSyntaxException, IOException {
        try (FileInputStream is = new FileInputStream(path)) {
            InputStreamReader reader = new InputStreamReader(is, StandardCharsets.UTF_8);
            GsonBuilder gsonBuilder = new GsonBuilder().setPrettyPrinting();
            gsonBuilder.registerTypeAdapter(LogicalImagerConfig.class, new LogicalImagerConfigDeserializer());
            Gson gson = gsonBuilder.create();
            config = gson.fromJson(reader, LogicalImagerConfig.class);
            if (config == null) {
                // This happens if the file is empty. Gson doesn't call the deserializer and doesn't throw any exception.
                throw new IOException(Bundle.ConfigVisualPanel1_configFileIsEmpty(path));
            }
        }
    }

    LogicalImagerConfig getConfig() {
        return config;
    }

    String getConfigFilename() {
        return configFilename;
    }

    boolean isNewFile() {
        return newFile;
    }

    void setConfigFilename(String filename) {
        configFileTextField.setText(filename);
    }

    boolean isPanelValid() {
        return (newFile || !configFileTextField.getText().isEmpty());
    }

    /**
     * Document Listener for textfield
     */
    private static class MyDocumentListener implements DocumentListener {

        private final ConfigVisualPanel1 panel;
        
        MyDocumentListener(ConfigVisualPanel1 aThis) {
            this.panel = aThis;
        }

        @Override
        public void insertUpdate(DocumentEvent e) {
            fireChange();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            fireChange();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            fireChange();
        }

        private void fireChange() {
            panel.firePropertyChange("UPDATE_UI", false, true); // NON-NLS
        }
    }

}
