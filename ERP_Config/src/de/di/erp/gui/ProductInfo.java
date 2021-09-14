package de.di.erp.gui;

import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

public class ProductInfo {

    public static void readProductInfo(InputStream in, Map<String, String> status) {
        Properties p = new Properties();
        try {
            p.load(in);
            String version = p.getProperty("app.version");
            String productName = p.getProperty("app.product_name");
            String product = p.getProperty("app.product");
            in.close();

            if (version != null) {
                status.put("version", version);
            } else {
                status.put("version", "");
            }
            if (productName != null) {
                status.put("product_name", productName);
            } else {
                status.put("product_name", "");
            }
            if (product != null) {
                status.put("product", product);
            } else {
                status.put("product", "Illegal product value");
            }
        } catch (Exception e) {
            status.put("product_name", "");
            status.put("version", "");
            status.put("product", "Illegal product value");
        }
    }
}