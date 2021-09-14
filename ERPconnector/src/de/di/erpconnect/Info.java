package de.di.erpconnect;

import de.di.utils.Utils;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Properties;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Rahman
 */
public class Info {

    private static String serviceStartup = new Timestamp(new java.util.Date().getTime()).toString();
    private static Timestamp lastRun = new Timestamp(new java.util.Date().getTime());
    private static int counterOK = 0;
    private static int counterError = 0;
    private static String configPath;
    private static Properties properties;

    private static Properties getProperties() {
        if (properties == null) {
            InputStream inputStream;
            try {
                inputStream = new FileInputStream(configPath);
                properties = Utils.transferToProperties(inputStream);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(Info.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(Info.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        return properties;
    }

    public static String getPollingIntervall() {
        try {

            return getProperties().getProperty("Trigger.PollTime");
        } catch (Exception ex) {
            return ex.getMessage();
        }
    }

    public static void setConfigPath(String path) {
        configPath = path;
    }

    public static String getConfigPath() {
        return configPath;
    }

    public static int getCounterOK() {
        return counterOK;
    }

    public static void incCounterOK() {
        counterOK++;
    }

    public static int getCounterError() {
        return counterError;
    }

    public static void incCounterError() {
        counterError++;
    }

    public static String getServiceStartup() {
        return serviceStartup.substring(0, serviceStartup.length() -4);
    }

    public static String getLogLevel() {
        Properties conf = new Properties();
        try {
            FileInputStream inputStream = new FileInputStream(configPath);
            conf.load(inputStream);
            inputStream.close();
        } catch (Exception ex) {
            ex.getStackTrace();
        }
        return conf.getProperty("Basic.LogLevel");
    }

    public static void setLastRun() {
        lastRun = new Timestamp(new java.util.Date().getTime());
    }

    public static String getLastRun() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(lastRun);
    }

    public static String getNextRun() {
        Properties conf = new Properties();
        try {
            FileInputStream inputStream = new FileInputStream(configPath);
            conf.load(inputStream);
            inputStream.close();
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Timestamp(lastRun.getTime() + Integer.valueOf(conf.getProperty("Trigger.PollTime"))));
        } catch (Exception ex) {
            return ex.getMessage();
        }
    }
}
