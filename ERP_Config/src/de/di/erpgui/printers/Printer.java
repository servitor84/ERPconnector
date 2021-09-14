package de.di.erpgui.printers;

import de.di.erp.gui.Config;

/**
 *
 * @author A. Sopicki
 */
public abstract class Printer {

    protected String displayName;

    public abstract void doConfig(Config config);

    @Override
    public String toString() {
        return displayName;
    }
}
