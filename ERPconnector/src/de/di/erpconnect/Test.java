/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.di.erpconnect;

import de.di.utils.FileUtils;
import java.io.File;
import java.util.HashMap;

/**
 *
 * @author Stefan.Cioaba
 */
public class Test {

    public static final String dataFilePath = "/WEB-INF/classes/de/di/erpconnect/web/data.pkg";
    public static final String dataBkpPath = "/conf/data.pkg";
    public static final String extraFilePath = "../conf/extra.properties";

    /**
     * The method verifies the existence of files for the web application to run
     *
     * @return hashMap of String Object with File objects for folders and
     * InputSream for files
     * @throws Exception - when one of the files cannot be found. The message of
     * the exception also contains the fileName
     */
    public static HashMap<String, Object> verifyFilesExist() throws Exception {
        HashMap<String, Object> files = new HashMap<String, Object>();
        String dataBkpRealpath = dataBkpPath;
        if (dataBkpRealpath != null) {
            files.put(dataBkpPath, FileUtils.openFileRead(dataBkpRealpath, dataBkpPath, true));
        }
        String dataFileRealpath = dataFilePath;
        if (dataFileRealpath != null) {
            try {
                files.put(dataFilePath, FileUtils.openFileRead(dataFileRealpath, dataFilePath, false));
            } catch (RuntimeException ex) {
                files.put(dataFilePath, FileUtils.openFileRead(dataFilePath, dataFilePath, true));
                FileUtils.copyFileUsingStream((File) files.get(dataBkpPath), (File) files.get(dataFilePath));
                files.put(dataFilePath, FileUtils.openFileRead(dataFileRealpath, dataFilePath, true));
            }

        }
        String extraFileRealPath = extraFilePath;
        if (extraFileRealPath != null) {
            files.put(extraFilePath, FileUtils.openFileRead(extraFileRealPath, extraFilePath, true));
        }
        return files;
    }

//    public static void main(String[] args) throws Exception {
//    }

}
