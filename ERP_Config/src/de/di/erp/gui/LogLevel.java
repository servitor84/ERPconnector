
package de.di.erp.gui;

import org.jdesktop.application.ResourceMap;

/**
 *
 * @author A. Sopicki
 */
public class LogLevel {
  private org.apache.log4j.Level level;

  private String displayName;

  enum Type {
    STANDARD, WARN, INFO, ALL, EXPERT;
  }

  LogLevel(org.apache.log4j.Level l, Type type) {
    level = l;

    ResourceMap map = org.jdesktop.application.Application.
            getInstance(de.di.erp.gui.ERPGUIApp.class).getContext().
            getResourceMap(getClass());

    try {
      displayName = map.getString("level."+type.toString().toLowerCase());
    } catch (Exception ex) {
      displayName = l.toString();
    }
  }

  org.apache.log4j.Level getLevel() {
    return level;
  }

  @Override
  public String toString() {
    return displayName;
  }
}
