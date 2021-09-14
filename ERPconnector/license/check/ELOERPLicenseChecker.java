package de.di.license.check;

import java.util.Properties;
import org.apache.log4j.Logger;

/**
 *
 * @author A. Sopicki
 */
public class ELOERPLicenseChecker { //extends BasicLicenseChecker {

    private Logger logger = null;
    private String product = null;
    private Properties settings = null;
    private boolean aborted = false;

    public ELOERPLicenseChecker(String product) throws java.security.NoSuchAlgorithmException {
        super();

        logger = Logger.getLogger(getClass());
        this.product = product;
    }
//
//    @Override
//    public void check(LicenseKey key, int rounds) throws LicenseException {
//        LicenseAttribute client = key.getAttribute("client");
//        LicenseAttribute eloVersion = key.getAttribute("ELO-version");
//
//        if (client == null) {
//            throw new LicenseException("client attribute missing");
//        }
//
//        if (eloVersion == null) {
//            throw new LicenseException("ELO-version attribute missing");
//        }
//
//        if (key.getAttribute("ERP-system") == null) {
//            throw new LicenseException("ERP-system attribute missing");
//        }
//
//        if (key.getAttribute("license-type") == null) {
//            throw new LicenseException("license-type attribute missing");
//        }
//
//        if (!eloVersion.getValue().equals("Office")) {
//            if (!eloVersion.getValue().equals("Professional")
//                    && !eloVersion.getValue().equals("Enterprise")) {
//                throw new LicenseException("Illegal ELO-version");
//            }
//
//            ELOERPClient eloClient = null;
//            License license = null;
//            ServerInfo serverInfo = null;
//            int count = 0;
//
//            try {
//                //
//                while (serverInfo == null && count < 20 && !aborted) {
//                    try {
//                        eloClient = new ELOERPClient(settings);
//                        serverInfo = eloClient.getServerInfo();
//                        license = serverInfo.getLicense();
//                    } catch (Exception ex) {
//                        logger.trace("An exception occured while contacting ELO", ex);
//                        if (count == 20) {
//                            throw ex;
//                        } else {
//                            count++;
//                            try {
//                                Thread.sleep(10000);
//                            } catch (InterruptedException iex) {
//                            }
//                        }
//                    } finally {
//                        if (eloClient != null) {
//                            eloClient.close();
//                        }
//                        eloClient = null;
//                    }
//                }
//
//                if (license == null) {
//                    throw new Exception("Unable to retrieve license info from ELO");
//                }
//            } catch (Exception ex) {
//                logger.debug("An exception occured while retrieving license info from ELO", ex);
//                throw new ELOException("ELO not available");
//            }
//            String serno = license.getSerno();
//
//            if (serno.indexOf("\\n") != -1) {
//                serno = license.getSerno().substring(0, license.getSerno().indexOf("\\n"));
//            }
//
//            serno = serno.trim();
//
//            if (!serno.equals(client.getValue())) {
//                throw new LicenseException("client attribute missmatch (got '"
//                        + serno + "' from ELO instead of '" + client.getValue() + "')");
//            }
//
//            super.check(key, rounds);
//
//            String version = serverInfo.getVersion();
//
//            if (version != null) {
//                eloVersion.setValue("ELO " + eloVersion.getValue() + " " + version);
//            }
//
//            serverInfo = null;
//            license = null;
//        } else {
//
//            super.check(key, rounds);
//
//            eloVersion.setValue("ELO " + eloVersion.getValue());
//
//        }
//
//        if (key.getExpirationDate() == null) {
//            throw new LicenseException("Illegal license expiration date!");
//        }
//
//        if (key.getExpirationDate().compareTo(new java.util.Date()) < 0) {
//            throw new LicenseExpiredException("License expired on: "
//                    + String.format(java.util.Locale.getDefault(), "%1$tc", key.getExpirationDate()));
//        }
//
//        try {
//            //no xml support for ELO Office
//            if (key.getAttribute("ELO-version").getValue().toLowerCase().contains("office")) {
//                settings.setProperty("Index.GenerateXML", "");
//            }
//
//            if (!key.getAttribute("product").getValue().equals(product)) {
//                logger.fatal("License violation detected!");
//                throw new LicenseException("Illegal product value detected.");
//            }
//        } catch (NullPointerException ex) {
//            logger.fatal("Missing license attribute. License not valid!");
//            throw new LicenseException("Missing license attribute detected.");
//        }
//    }

    public Properties getSettings() {
        return settings;
    }

    public void setSettings(Properties settings) {
        this.settings = settings;
    }

    public void abort() {
        aborted = true;
    }
}
