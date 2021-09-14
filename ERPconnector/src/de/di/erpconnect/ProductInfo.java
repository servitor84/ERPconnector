package de.di.erpconnect;

import de.di.utils.Utils;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;
import org.apache.log4j.Logger;

public class ProductInfo {

    private static final Logger logger = Logger.getLogger(ProductInfo.class);

    /*Transfers in a formatted way the values that it reads from the input config file*/
    public static void readProductInfo(InputStream in, Map<String, String> status) {
        /*obtaining the properties from the InputStream*/
        logger.debug("Started reading the product info");
        Properties properties = Utils.transferToProperties(in);
        try {
            /*getting the formatted info from the properties*/
            String version = properties.getProperty("app.version"); //+ " (Build " + properties.getProperty("app.build") + ")";
            String productName = properties.getProperty("app.product_name");
            String product = (properties.getProperty("app.product") == null) ? "Illegal product value" : properties.getProperty("app.product");
            /*adding the values to the map*/
            Utils.addValue(status, "version", version);
            Utils.addValue(status, "product_name", productName);
            Utils.addValue(status, "product", product);
        } catch (Exception e) {
            /*unexpected exception code putting in the map the default values*/
            logger.error("Error on reading properties ", e);
            Utils.addValue(status, "product_name", "");
            Utils.addValue(status, "version", "");
            Utils.addValue(status, "product", "Illegal product value");
        }
        logger.debug("Finished reading the product info");
    }
}
