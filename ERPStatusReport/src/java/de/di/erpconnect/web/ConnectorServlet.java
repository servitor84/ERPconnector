package de.di.erpconnect.web;

import de.di.erpconnect.ERPConnect;
import de.di.erpconnect.Info;
import de.di.utils.FileUtils;
import java.io.*;
import java.util.HashMap;
import java.util.Locale;
import java.util.List;
import javax.servlet.*;
import javax.servlet.http.*;

/**
 * The servlet for the ERP. 
 * 1. verifyes that all the files are found in the location hardcoded with the 3 path
 * @author A. Sopicki
 */
public class ConnectorServlet extends HttpServlet {

    public static final String connectorAttribute = "de.di.erpconnector.connector";
    public static final String configFilePath = "/conf/config.properties";
    public static final String configDirPath = "/conf";
    public static final String licenseFilePath = "/conf/license.txt";
    public static final String dataFilePath = "/WEB-INF/classes/de/di/erpconnect/web/data.pkg";
    public static final String dataBkpPath = "/conf/data.pkg";
    public static final String extraFilePath = "/conf/extra.properties";
    public static List<String> errorStatus = null;
    public static final Object erpConnect = null;

    /** The method verifies the existence of files for the web application to run 
     * @return hashMap of String Object with File objects for folders and InputSream for files
     * @throws Exception - when one of the files cannot be found. The message of the exception also contains the fileName
     */
    public HashMap<String, Object> verifyFilesExist() throws Exception {
        HashMap<String, Object> files = new HashMap<String, Object>();
        String configdDirRealPath = getServletContext().getRealPath(configDirPath);
        if (configdDirRealPath == null) {
            throw new Exception("Missing config dir. Used path: " + configDirPath + ". Service start ended in error");
        }
        files.put(configDirPath, new File(configdDirRealPath));
        String configFileRealPath = getServletContext().getRealPath(configFilePath);
        if (configFileRealPath == null) {
            throw new Exception("Missing config file. Used path: " + configFilePath + ". Service start ended in error");
        }
//        files.put(configFilePath, new FileInputStream(new File("E:\\testFiles\\erp\\config.properties")));
        files.put(configFilePath, new FileInputStream(new File(configFileRealPath)));
        Info.setConfigPath(configFileRealPath);
        String licenseFileRealPath = getServletContext().getRealPath(licenseFilePath);
        if (licenseFileRealPath == null) {
            throw new Exception("Missing license file. Used path: " + licenseFilePath + ". Service start ended in error");
        }
//        files.put(licenseFilePath, new FileInputStream(new File("E:\\testFiles\\erp\\license.txt")));
        files.put(licenseFilePath, new FileInputStream(new File(licenseFileRealPath)));
        String appFolder = getServletContext().getClass().getClassLoader().getResource("").getPath();
        String tomcatFolder = new File(appFolder).getParentFile().getAbsolutePath();
        String webApp = tomcatFolder + "/webapps" + getServletContext().getContextPath();

        String dataBkpRealpath = getServletContext().getRealPath(dataBkpPath);
        if (dataBkpRealpath != null) {
            files.put(dataBkpPath, FileUtils.openFileRead(tomcatFolder + dataBkpPath, dataBkpPath, true));
        }
        String dataFileRealpath = getServletContext().getRealPath(dataFilePath);
        if (dataFileRealpath != null) {
            files.put(dataFilePath, FileUtils.openFileRead(webApp + dataFilePath, dataFilePath, true));
        }
        String extraFileRealPath = getServletContext().getRealPath(extraFilePath);
        if (extraFileRealPath != null) {
            files.put(extraFilePath, FileUtils.openFileRead(webApp + extraFilePath, extraFilePath, true));
        }
        return files;
    }

    /** 
     * 
     */
    public void initConnector() {
  
        getServletContext().log(this.getClass().getClassLoader().getResource("").getPath());
        ERPConnect erpConnector = (ERPConnect) getServletContext().getAttribute(connectorAttribute);
        if (erpConnector == null) {
            try {
                /*changed logic*/                
                erpConnector = new ERPConnect(getServletContext().getRealPath(configDirPath));
                getServletContext().setAttribute(connectorAttribute, erpConnector);
                erpConnector.start();
                errorStatus = erpConnector.getErrorStatus();
            } catch (Exception ex) {
                System.out.println(ex);
                getServletContext().log("Startup failed due to Exception. ", ex);
                if (errorStatus == null) {
                    errorStatus = new java.util.ArrayList<String>();
                    errorStatus.add(ex.getMessage());
                }
                System.out.println(ex.getMessage());
                ex.printStackTrace();
            }
        }

    }

    @Override
    public void init() throws ServletException {
        initConnector();
    }

    @Override
    public void destroy() {
        //check if the ERPConnector is active
        ERPConnect connector = (ERPConnect) getServletContext().getAttribute(ConnectorServlet.connectorAttribute);
        if (connector != null) {
            connector.shutdown();
            try {
                connector.join(5000);
            } catch (InterruptedException iex) {
                getServletContext().log("Error shutting down ERPConnector", iex);
            }
            getServletContext().removeAttribute(ConnectorServlet.connectorAttribute);
        }
    }

    protected void processRequest(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {
        if (request.getLocale().equals(Locale.GERMAN)) {
            request.getRequestDispatcher("/index.html").forward(request, response);
        } else {
            request.getRequestDispatcher("/index_en.html").forward(request, response);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     */
    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP
     * <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     */
    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }
    // </editor-fold>
}
