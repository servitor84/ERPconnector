package de.di.erpgui.printers;

import de.di.erp.gui.Config;
import org.jdesktop.application.ResourceMap;

/**
 *
 * @author A. Sopicki
 */
public class ExpertPrinter extends Printer {

  public ExpertPrinter() {
    displayName = "Other";

    ResourceMap map = org.jdesktop.application.Application.
            getInstance(de.di.erp.gui.ERPGUIApp.class).getContext().
            getResourceMap(Printer.class);

    try {
      displayName = map.getString("printer.expert");
    } catch (Exception ex) {
    }
  }

    @Override
    public void doConfig(de.di.erp.gui.Config config) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }
}
