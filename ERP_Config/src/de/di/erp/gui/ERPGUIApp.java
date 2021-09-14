package de.di.erp.gui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;

/**
 * The main class of the application.
 */
public class ERPGUIApp extends SingleFrameApplication {

    Config config = null;

    /**
     * At startup create and show the main frame of the application.
     */
    @Override
    protected void startup() {
        Logger.getRootLogger().setLevel(Level.OFF);
        config = loadConfig();
        show(new ERPGUIView(this));
        this.getMainFrame().setResizable(false);

    }

    /**
     * This method is to initialize the specified window by injecting resources.
     * Windows shown in our application come fully initialized from the GUI
     * builder, so this additional configuration is not needed.
     */
    @Override
    protected void configureWindow(java.awt.Window root) {
        String bundleName = "de/di/erp/gui/resources/ERPGUIApp";
        String bundleName1 = "de/di/erp/gui/resources/product";
        getMainFrame().setTitle(java.util.ResourceBundle.getBundle(bundleName).getString("Application.title") + ' '
                + java.util.ResourceBundle.getBundle(bundleName1).getString("app.version") + " (build "
                + java.util.ResourceBundle.getBundle(bundleName1).getString("app.build") + ")");
        root.setIconImage(getApplication().getContext().getResourceMap(ERPGUIView.class).getImageIcon(
                "fav.icon").getImage());

    }

    /**
     * A convenient static getter for the application instance.
     *
     * @return the instance of ERPGUIApp
     */
    public static ERPGUIApp getApplication() {
        return Application.getInstance(ERPGUIApp.class);
    }

    public Config getConfig() {
        return config;
    }

    public static void main(String[] args) {
        launch(ERPGUIApp.class, args);

    }

    void saveConfig() throws Exception {
        File configFile = null;
        if (!this.config.containsKey("Basic.MaxFilesPerRun")) {
            config.setProperty("Basic.MaxFilesPerRun", "100");
        }
        if (!this.config.containsKey("Basic.ServiceName") || this.config.getProperty("Basic.ServiceName").isEmpty()) {
            File serviceDir = new File("..");
            if (serviceDir.exists()) {
                try {
                    serviceDir = serviceDir.getCanonicalFile();
                    config.setProperty("Basic.ServiceName", serviceDir.getName());
                } catch (IOException ioe) {
                    config.setProperty("Basic.ServiceName", "SERVICE-NAME-HERE");
                } catch (SecurityException se) {
                    config.setProperty("Basic.ServiceName", "SERVICE-NAME-HERE");
                }
            }
        }
        try {
            configFile = new File(config.getProperty(Config.Property.DirectoriesTemplate) + "\\" + config.getProperty(Config.Property.DefaultTemplate));

            if (!configFile.exists() || !configFile.canRead()) {
                throw new Exception("ConfigFile not found");
            }
        } catch (Exception ex) {
            throw new Exception(java.util.ResourceBundle.getBundle(
                    "de/di/erp/gui/resources/ERPGUIApp").
                    getString("missingTemplatefile.text"));
        }

        OutputStream configStream = null;
        try {
            configFile = new File("../conf/config.properties");

            if (configFile.canWrite() == false) {
                throw new Exception(java.util.ResourceBundle.getBundle(
                        "de/di/erp/gui/resources/ERPGUIApp").
                        getString("noWriteAccess.text"));
            }
        } catch (Exception ex) {
            throw new Exception(java.util.ResourceBundle.getBundle(
                    "de/di/erp/gui/resources/ERPGUIApp").
                    getString("unableToWriteFile.text"));
        }

        try {
            configStream = new FileOutputStream(configFile);
        } catch (FileNotFoundException fex) {
            throw new Exception(java.util.ResourceBundle.getBundle(
                    "de/di/erp/gui/resources/ERPGUIApp").
                    getString("canNotWriteFile.text"));
        }

        try {
            this.config.store(configStream, "Configuration file");
        } catch (java.io.IOException ioex) {
            throw new Exception(java.util.ResourceBundle.getBundle(
                    "de/di/erp/gui/resources/ERPGUIApp").
                    getString("errorOnWritingFile.text"));
        }
    }

    private Config loadConfig() {
        Config props = new Config();

        File configFile = null;

        InputStream configStream = null;
        try {
            configFile = new File("../conf/config.properties");

            if (configFile.canRead() == false) {
                //TODO: Handle error
                return props;
            }
        } catch (Exception ex) {
            //TODO: Handle exception
            return props;
        }

        try {
            configStream = new FileInputStream(configFile);
        } catch (FileNotFoundException fex) {
            //TODO: Handle exception
            return props;
        }

        try {
            props.load(configStream);
        } catch (java.io.IOException ioex) {
            //TODO: Handle exception
        }

        return props;
    }
}
