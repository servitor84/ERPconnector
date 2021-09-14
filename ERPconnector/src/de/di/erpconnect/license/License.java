package de.di.erpconnect.license;

import de.di.erpconnect.exceptions.StartUpException;
import de.di.erpconnect.license.check.ELOLicenseChecker;
import de.di.license.check.LicenseException;
import de.di.license.check.LicenseKey;
import java.io.File;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;

/**
 *
 * @author Rahman
 */
public class License {
    public static boolean check(InputStream licenseStream, java.util.Map<String, String> status, java.util.Properties settings, org.apache.log4j.Logger logger)throws StartUpException {
        
        boolean result = false;
        try {
            ELOLicenseChecker checker = new ELOLicenseChecker(status.get("product"));

            LicenseKey key;

            if (licenseStream != null) {
                key = LicenseKey.readFromFile(licenseStream);
            } else {
                key = LicenseKey.readFromFile(new File("conf/license.txt"));
            }

            checker.setSettings(settings);
            checker.check(key, checker.getRounds());

            status.put("client", key.getAttribute("client").getValue());
            status.put("ELO_version", key.getAttribute("ELO-version").getValue());
            status.put("ERP_system", key.getAttribute("ERP-system").getValue());
            status.put("license_type", key.getAttribute("license-type").getValue());
            status.put("expiration_date", (new SimpleDateFormat("yyyy-MM-dd")).format(
                    key.getExpirationDate()));
            status.put("license_key", key.getAttribute("bytes2").getValue());
            checker = null;
            result = true;
        } catch (LicenseException lex) {
            logger.fatal("License violation detected: " + lex.getMessage());
            throw new StartUpException();
        } catch (java.io.IOException ioex) {
            logger.fatal("License check file corrupted", ioex);
            throw new StartUpException();
        } catch (NoSuchAlgorithmException ex) {
            logger.fatal("License check not possible " + ex.getMessage());
            throw new StartUpException();
        }
        return result;
    }
    
    
    
}
