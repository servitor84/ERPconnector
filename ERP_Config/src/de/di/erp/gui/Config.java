package de.di.erp.gui;

import java.util.LinkedList;
import java.util.Properties;

public class Config extends Properties {

    public enum Property {

        BasicQueueSize,
        BasicLogLevel,
        BasicMaxLogFiles,
        BasicMaxLogSize,
        BasicMaxRecoveryLogSize,
        BasicPrinter,
        BasicServiceName,
        BasicTomcatURL,
        BasicNewInstall,
        DirectoriesInput,
        DirectoriesOutput,
        DirectoriesBackup,
        DirectoriesErrorOutput,
        DirectoriesWorking,
        DirectoriesLogging,
        DirectoriesRecovery,
        DirectoriesTemplate,
        TriggerPollTime,
        TriggerTriggerExtendsion,
        TriggerCheckTriggerContent,
        TriggerCheckTriggerSection,
        TriggerCheckSectionValue,
        TriggerMetadataFileExtension,
        TriggerDocumentFileExtension,
        ParsingMakeBackup,
        ParsingThreadCount,
        ParsingMatchCountNeeded,
        DefaultTemplate,
        IndexServerURL;

        @Override
        public String toString() {
            String s = name();
            if (s.equals("IndexServerURL")) {
                return "IndexServer.URL";
            } else if (s.startsWith("Basic")) {
                return "Basic." + s.substring(5);
            } else if (s.startsWith("Trigger")) {
                return "Trigger." + s.substring(
                        7);
            } else if (s.startsWith("Default")) {
                return "Default." + s.substring(
                        7);
            } else if (s.startsWith("Parsing")) {
                return "Parsing." + s.substring(7);
            } else if (s.startsWith("Directories")) {
                return "Directories."
                        + s.substring(11);
            } else if (s.startsWith("Index")) {

                return "Index." + s.substring(5);
            } else if (s.startsWith("Cleanup")) {
                return "Cleanup."
                        + s.substring(7);
            } else {
                return s;
            }

        }

        public static Property fromString(
                String prop) {
            String name = prop.replace(".", "");

            for (Property p : values()) {
                if (p.name().equals(name)) {
                    return p;
                }

            }

            throw new IllegalArgumentException("Unknown property: " + prop);
        }
    }

    private LinkedList<ConfigChangeListener> listeners = new LinkedList<ConfigChangeListener>();

    public Config() {
        super();
    }

    public Config(Properties defaults) {
        super(defaults);
    }

    public void addConfigChangeListener(ConfigChangeListener listener) {
        listeners.add(listener);
    }

    public void removeConfigChangeListener(ConfigChangeListener listener) {
        listeners.remove(listener);
    }

    @Override
    public Object setProperty(String key, String value) {
        Object o = super.setProperty(key, value);

//        log.info(key + ": " + value);
        ConfigChangeEvent event = new ConfigChangeEvent();
        for (ConfigChangeListener l : listeners) {
            l.configChange(event);
        }

        return o;
    }

    public Object setProperty(
            Property prop, String value) {
        Object o = super.setProperty(prop.toString(), value);

//        log.info(prop.toString() + ": " + value);
        ConfigChangeEvent event = new ConfigChangeEvent();
        for (ConfigChangeListener l : listeners) {
            l.configChange(event);
        }

        return o;
    }

    public String getProperty(
            Property prop) {
        return super.getProperty(prop.toString());
    }

    public String getProperty(
            Property prop, String defaultValue) {
        return super.getProperty(prop.toString(), defaultValue);
    }

    public int indexOf(String key) {
        int i = 0;

        for (Object k : this.keySet()) {
            if (k.equals(key)) {
                return i;
            }

            i++;
        }

        throw new IllegalArgumentException();
    }
}
