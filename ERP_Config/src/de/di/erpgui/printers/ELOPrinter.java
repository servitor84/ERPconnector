package de.di.erpgui.printers;

import de.di.erp.gui.Config;
import de.di.erp.gui.Config.Property;
import org.jdesktop.application.ResourceMap;

/**
 *
 * @author A. Sopicki
 */
public class ELOPrinter extends Printer {

  public ELOPrinter() {
    displayName = "ERP-Connect/Virtual-Printer";

    ResourceMap map = org.jdesktop.application.Application.
            getInstance(de.di.erp.gui.ERPGUIApp.class).getContext().
            getResourceMap(Printer.class);

    try {
      displayName = map.getString("printer.ELO");
    } catch (Exception ex) {
    }
  }

  @Override
  public void doConfig(Config config) {
    config.setProperty(Property.BasicPrinter, getClass().getName());
    config.setProperty(Property.TriggerTriggerExtendsion, ".grp");
    config.setProperty(Property.TriggerCheckTriggerContent, "true");
    config.setProperty(Property.TriggerCheckTriggerSection, "Blice Printer");
    config.setProperty(Property.TriggerCheckSectionValue, "Total Page Number");
    config.setProperty(Property.TriggerMetadataFileExtension, ".txt");
    config.setProperty(Property.TriggerDocumentFileExtension, ".tif");
    config.setProperty(Property.ParsingMatchCountNeeded, "3");
  }
}
