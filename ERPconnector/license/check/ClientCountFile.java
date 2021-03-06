package de.di.license.check;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import org.apache.log4j.Logger;

public class ClientCountFile {

    private static Logger logger = null;
    private int count;

    public ClientCountFile() {
        this.count = 0;
    }

    public int getClientCount() {
        return this.count;
    }

    public void setClientCount(int c) {
        this.count = c;
    }

    void save(File outputFile) throws IOException {
        Properties p = new Properties();
        p.setProperty("count", Integer.toString(this.count));
        p.store(new FileOutputStream(outputFile), "WEBaccess PRO - do not modify");
    }

    public static ClientCountFile readFromFile(File inputFile) throws IOException {
        ClientCountFile countFile = new ClientCountFile();

        Properties p = new Properties();
        p.load(new FileInputStream(inputFile));
        String count = p.getProperty("count", "0");
        try {
            countFile.count = Integer.parseInt(count);
        } catch (NumberFormatException nfe) {
            countFile.count = 0;
        }
        return countFile;
    }
}
