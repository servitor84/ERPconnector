package de.di.erpconnect;

import de.di.elo.client.ELOERPClient;
import de.di.erpconnect.license.License;
import de.di.utils.FileUtils;
import de.di.utils.Utils;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.*;
import org.apache.log4j.spi.LoggingEvent;

public class ERPConnect extends Thread {

    private static final double LICENSE_FACTOR = 1.2;
    private static Logger logger = null;
    private static Properties settings = null;
    public static volatile boolean running = true;
    private DailyRollingFileAppender handler = null;
    private InputStream configStream = null, licenseStream = null;
    private Map<String, String> status = new Hashtable();
    private BufferHandler logHandler = null;
    private List<String> errorStatus = new ArrayList();
    private List<File> sigArray = new ArrayList<File>();
    private Set<String> openedFiles = new HashSet<String>();
    private String errorFolder, backupFolder, outputFolder, workingFolder, inputFolder;
    private File docFile, txtFile, sigFile, xmlFile, pkgFile, bkpFile, extraProperties, tomcatbkpFile;
    private int basicMaxFilesPerRun;
    private static int currentNbOfLicensesUsed, maxNumber;
    private Set<String> containedIps = new HashSet<String>();
    private Writer output, outputBkp, outputExtra, outputTomcatBkp;
    public static final String dataFilePath = "/WEB-INF/classes/de/di/erpconnect/web/data.pkg";

    public ERPConnect() throws IOException {
//        this(null, null, null, null, null);
    }

    public final String[] readLines(File file) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(file));
        List<String> lines = new ArrayList<String>();
        String line;
        while ((line = br.readLine()) != null) {
            lines.add(line);
        }
        String[] linesArr = new String[lines.size()];
        lines.toArray(linesArr);
        return linesArr;
    }

    public void addIps(String base64EncIps) {
        if (base64EncIps != null) {
            //this.logger.info("Es wird aus der Datei \""+base64EncIps+" \" gelesen");
            String ipsLine = new String(Base64.decodeBase64(base64EncIps)).replaceAll("U=", "");
            try {
                StringTokenizer st = new StringTokenizer(ipsLine, "|");
                while (st.hasMoreTokens()) {
                    containedIps.add(st.nextToken());
                }
            } catch (Exception ex) {
                logger.warn("invalid string format in the data.pkg file found for ips");
                logger.error(ex);
            }
        }
    }

    private void initiateFiles() throws IOException {
        /*We read the lines from the bkpFile. If lines != 2 than the number Of lines is incorect*/
        //String[] linesBkp = readLines(bkpFile);
        String[] linesData = readLines(pkgFile);
        String[] tomcatBkpLines = readLines(tomcatbkpFile);
        if (tomcatBkpLines.length != 2) {
            logger.warn("\t\tInvalid number of lines in bkp.pkg file. Lines.length: " + tomcatBkpLines.length);
        } else {
            addIps(tomcatBkpLines[1]);
        }
//        if (linesBkp.length != 2) {
//            logger.warn("\t\tInvalid number of lines in bkp.pkg file. Lines.length: " + linesBkp.length);
//        } else {
//            addIps(linesBkp[1]);
//        }
        if (linesData.length != 2) {
            logger.warn("\t\tInvalid number of lines in data.pkg file. Lines.length: " + linesData.length);
        } else {
            addIps(linesData[1]);
        }
        currentNbOfLicensesUsed = containedIps.size();
        rewriteFiles();

    }

    public ERPConnect(String dirName) throws FileNotFoundException {        
        if (dirName == null) {
            logger.error("Directory for the application is null!!!!");
        }               
        File configDirectory = new File(dirName);
//        System.out.println("configDirectory: " + configDirectory);
        String configFileRealPath = dirName + File.separator + "config.properties";
        InputStream inConfig = new FileInputStream(new File(configFileRealPath));
        Properties properties = Utils.transferToProperties(inConfig);

        File serverDir = configDirectory.getParentFile().getParentFile().getParentFile();
        File directoryConf = new File(serverDir.getAbsolutePath() + File.separator + "webapps" + File.separator + properties.getProperty("Basic.ServiceName") + File.separator + "conf");
        if (directoryConf.exists()) {
            dirName = directoryConf.getAbsolutePath();
            System.out.println("dirName: " + dirName);
        }

        String licenseRealPath = dirName + File.separator + "license.txt";
        InputStream inLicense = new FileInputStream(new File(licenseRealPath));
        Info.setConfigPath(configFileRealPath);
        this.logger = Logger.getLogger(super.getClass().getName());
        this.logHandler = new BufferHandler();
        this.logger.addAppender(this.logHandler);
        
        /*transforming the configFile stream to Properties*/
        settings = properties;
        licenseStream = inLicense;
        InputStream in = getContextClassLoader().getResourceAsStream("de/di/erpconnect/resources/ERPConnect.properties");

        if (in != null) {
            ProductInfo.readProductInfo(in, this.status);
        }
        /*old commented code was here, that tried to read the two files allready received as params*/
        logger.log(Level.INFO, "##########################################################");
        File extraPropertiesFile = FileUtils.openFileRead(dirName + File.separator + "extra.properties", "extra", true);        
        File pkgFileInit = FileUtils.openFileRead(directoryConf.getParentFile().getAbsolutePath() + File.separator + "WEB-INF/classes/de/di/erpconnect/web/data.pkg", "data.pkg", true);
        //File bkpFileInit = FileUtils.openFileRead(dirName + File.separator + "data.pkg", "bkp.pkg", true);
        File tomcatbkpFile = FileUtils.openFileRead(serverDir.getAbsolutePath() + File.separator + "conf" + File.separator + "data.pkg", "tomcatbkp.pkg", true);
        this.pkgFile = pkgFileInit;
        //this.bkpFile = bkpFileInit;
        this.tomcatbkpFile = tomcatbkpFile;
        this.extraProperties = extraPropertiesFile;
        try {
            initiateFiles();
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(ERPConnect.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
    }

//    public ERPConnect(InputStream configFile, InputStream licenseFile, File pkgFile, File bkpFile, File extraProperties) throws IOException {
//        this.logger = Logger.getLogger(super.getClass().getName());
//        this.logHandler = new BufferHandler();
//        this.logger.addAppender(this.logHandler);
//
//        /*transforming the configFile stream to Properties*/
//        settings = Utils.transferToProperties(configFile);
//        licenseStream = licenseFile;
//        InputStream in = getContextClassLoader().getResourceAsStream("de/di/erpconnect/resources/ERPConnect.properties");
//
//        if (in != null) {
//            ProductInfo.readProductInfo(in, this.status);
//        }
//        this.pkgFile = pkgFile;
//        //this.bkpFile = bkpFile;
//        this.extraProperties = extraProperties;
//        initiateFiles();
//
//    }

    public static Properties getSettings() {
        return settings;
    }

    public List<String> getErrorStatus() {
        return this.errorStatus;
    }

    private void checkSettings() throws ERPConnect.StartUpException {
        this.logger.trace(super.getClass().getName() + ": checkSettings() enter");
        try {
            File templateFile = new File(settings.getProperty("Directories.Template") + "/" + settings.getProperty("Default.Template"));

            if (!templateFile.exists()) {
                this.logger.fatal("Configuration option Default.Template points to a non-existing file. Please correct that.");

                throw new StartUpException();
            }
        } catch (NullPointerException npe) {
            this.logger.fatal("Default.Template not set in configuration file. Please correct that.");

            throw new StartUpException();
        }

        if (settings.getProperty("Parsing.ExpressionMatching") == null) {
            this.logger.fatal("Parsing.ExpressionMatching not set in configuration file. Please correct that.");

            throw new StartUpException();
        }

        if (!settings.containsKey("Parsing.ESC")) {
            this.logger.fatal("Parsing.ESC not set in configuration file. Please correct that.");

            throw new StartUpException();
        }
        if (settings.getProperty("IndexServer.URL") == null) {
            this.logger.fatal("IndexServerl.URL is not configured. Please configure the indexserver url");
            throw new StartUpException();
        }

        if (settings.getProperty("Cleanup.DeleteByExtension") == null) {
            settings.setProperty("Cleanup.DeleteByExtension", "");
        }
        this.logger.trace(super.getClass().getName() + ": checkSettings() leave");

    }

    private void checkLicense() throws StartUpException {
        try {
            License.check(licenseStream, status, settings, logger);
            maxNumber = Integer.parseInt(status.get("license_type").split(" ")[0]);
            if (maxNumber * LICENSE_FACTOR < currentNbOfLicensesUsed) {
                logger.error(" License violation. This system is licensed for " + maxNumber + " users and is being used by " + currentNbOfLicensesUsed + " users. Service startup not possible. ");
                throw new ERPConnect.StartUpException("Too many licenses used . Product startup failed");
            }
            if (maxNumber < currentNbOfLicensesUsed) {
                logger.warn("License violation. This system is licensed for " + maxNumber + " users and is being used by " + currentNbOfLicensesUsed + " users. Please contact DOKinform for a license extention. Restrictions may apply.");
            }

        } catch (de.di.erpconnect.exceptions.StartUpException ex) {
            throw new ERPConnect.StartUpException(ex.getMessage());
        }
    }

    private void startUp() {
        this.logger.trace(super.getClass().getName() + ": startUp() enter");
        try {
            startLogging();
            loadSettings();

            boolean newInstall = Boolean.parseBoolean(settings.getProperty("Basic.NewInstall", "TRUE"));

            if (newInstall) {
                this.logger.fatal("Service has been deactivated. Aborting.");
                throw new ERPConnect.StartUpException();
            }
            checkDirectories();
            checkSettings();
            checkLicense();

        } catch (ERPConnect.StartUpException sex) {
            this.logger.info("Unable to start the service.");
            this.logger.error(sex, sex);
            this.running = false;
        } catch (Exception e) {
            this.logger.error(e, e);
        }
        this.logger.trace(super.getClass().getName() + ": startUp() leave");
    }

    private void loadSettings()
            throws ERPConnect.StartUpException {

        if (settings.containsKey("Directories.ErrorOutput")) {
            errorFolder = settings.getProperty("Directories.ErrorOutput");
        }
        if (settings.containsKey("Directories.Backup")) {
            backupFolder = settings.getProperty("Directories.Backup");
        }
        if (settings.containsKey("Directories.Output")) {
            outputFolder = settings.getProperty("Directories.Output");
        }
        if (settings.containsKey("Directories.Working")) {
            workingFolder = settings.getProperty("Directories.Working");
        }
        if (settings.containsKey("Directories.Input")) {
            inputFolder = settings.getProperty("Directories.Input");
        }
        if (settings.containsKey("Basic.MaxFilesPerRun") && !settings.getProperty("Basic.MaxFilesPerRun").equals("") && (Integer.valueOf(settings.getProperty("Basic.MaxFilesPerRun")) != 0)) {
            basicMaxFilesPerRun = Integer.valueOf(settings.getProperty("Basic.MaxFilesPerRun"));
            this.logger.debug("MaxFilesPerRun = " + basicMaxFilesPerRun);
        } else {
            this.logger.info("The property MaxFilesPerRun in config.properties is not correct. Set default 100 files per run");
            basicMaxFilesPerRun = 100;
        }

    }

    private void startLogging()
            throws ERPConnect.StartUpException {
        this.logger.trace(super.getClass().getName() + ": startLogging() enter");
        File logDir = null;
        try {
            logDir = new File(settings.getProperty("Directories.Logging"), "");
        } catch (NullPointerException nex) {
            this.logger.log(Level.FATAL, "Logging directory not found!");
            throw new StartUpException();
        }

        if (!logDir.canWrite()) {
            this.logger.log(Level.FATAL, "Unable to write to log directory");
            throw new StartUpException();
        }

        try {
            String logfile = logDir.getCanonicalPath() + File.separator + "erpconnector.log";

            this.handler = new DailyRollingFileAppender();
            this.handler.setDatePattern("'.'yyyy-MM-dd");
            this.handler.setFile(logfile);
            this.handler.setImmediateFlush(true);
            this.handler.setName("ErpconnectAppender");

            String pattern = settings.getProperty("Basic.LoggingPattern", "%d{dd.MM.yyyy HH:mm:ss} %-p [%t]: %m%n");

            this.handler.setLayout(new PatternLayout(pattern));
            this.handler.activateOptions();

            this.logHandler.close();

            Level logLevel = Level.toLevel(settings.getProperty("Basic.LogLevel", "WARN"), Level.WARN);

            Logger baseLogger = Logger.getLogger("de.di");
            baseLogger.addAppender(this.handler);
            baseLogger.setLevel(logLevel);

            baseLogger = Logger.getLogger("org.apache.commons");
            baseLogger.setLevel(Level.WARN);

            for (LoggingEvent event : this.logHandler.getEvents()) {
                if (event.getLevel().isGreaterOrEqual(logLevel)) {
                    this.handler.append(event);
                }
            }

            this.logger.setLevel(logLevel);
        } catch (IOException ioex) {
            this.logger.log(Level.FATAL, "Unable to write to create log file");
            throw new StartUpException();
        } catch (IllegalArgumentException iaex) {
            this.logger.log(Level.FATAL, "Illegal log level set");
            throw new StartUpException();
        }
        this.logger.trace(super.getClass().getName() + ": startLogging() leave");

    }

    private void checkDirectories()
            throws ERPConnect.StartUpException {
        logger.debug("Checking directories");
        File dir = new File(settings.getProperty("Directories.Input", ""));

        if (!dir.canWrite()) {
            this.logger.fatal("Input directory not configured or not writable!\nPlease edit config.properties accordingly or \nmake the  directory writeable for the application.");

            throw new StartUpException();
        }

        if (!dir.canRead()) {
            this.logger.fatal("Input directory not readable! Please make the\ndirectory readable for the application.");

            throw new StartUpException();
        }

        dir = new File(settings.getProperty("Directories.Output", ""));

        if (!dir.canWrite()) {
            this.logger.fatal("Output directory not configured or not writable!\nPlease edit config.properties accordingly or \nmake the  directory writeable for the application.");

            throw new StartUpException();
        }

        dir = new File(settings.getProperty("Directories.Backup", ""));

        if (!dir.canWrite()) {
            this.logger.fatal("Backup directory not configured or not writable!\nPlease edit config.properties accordingly or \nmake the  directory writeable for the application.");

            throw new StartUpException();
        }

        dir = new File(settings.getProperty("Directories.ErrorOutput", ""));

        if (!dir.canWrite()) {
            this.logger.fatal("Error output directory not configured or not writable!\nPlease edit config.properties accordingly or \nmake the  directory writeable for the application.");

            throw new StartUpException();
        }

        dir = new File(settings.getProperty("Directories.Template", ""));

        if (!dir.canRead()) {
            this.logger.fatal("Template directory not configured or not readable!\nPlease edit config.properties accordingly or \nmake the  directory writeable for the application.");

            throw new StartUpException();
        }

        dir = new File(settings.getProperty("Directories.Logging", ""));

        if (!dir.canWrite()) {
            this.logger.fatal("Logging directory not configured or not writable!\nPlease edit config.properties accordingly or \nmake the  directory writeable for the application.");

            throw new StartUpException();
        }

        dir = new File(settings.getProperty("Directories.Working", ""));

        if (!dir.canWrite()) {
            this.logger.fatal("Working directory not configured or not writable!\nPlease edit config.properties accordingly or \nmake the  directory writeable for the application.");

            throw new StartUpException();
        }

        if (!dir.canRead()) {
            this.logger.fatal("Working directory not readable! Please make the\ndirectory readable for the application.");

            throw new StartUpException();
        }
    }

    public void run() {
        this.logger.setLevel(Level.ALL);
        this.logger.log(Level.INFO, "Starting " + (String) this.status.get("product") + " (Version: " + (String) this.status.get("version") + ")");
        startUp();
        this.logger.debug("Startup complete");
        this.logger.debug("Entering main loop");
        while (this.running) {
            Info.setLastRun();
            try {
                logger.info("Start to browse input directory");
                if (triggerFileFound()) {
                    for (int j = 0; j < basicMaxFilesPerRun; j++) {
                        if (j < sigArray.size()) {
                            initialWorkingFile(sigArray.get(j));
                            logger.info("\t\t\t\tWork on file : " + sigFile.getName());
                            if (associateFileExist() && sigFile.getName().split("_").length == 3) {
                                if (filesAreNotOpenedByAnotherProcess()) {
                                    if (moveFileToWorkingDirectory()) {
                                        processFile();
                                    } else {
                                        continue;
                                    }
                                } else {
                                    continue;
                                }
                            } else {
                                moveToErrorDirectory();
                                if (sigFile.getName().split("_").length != 3) {
                                    logger.error("\t\t\t File: " + docFile.getAbsolutePath() + "/" + docFile.getName() + " must have 2 underscores. Moving to error directory");
                                }

                            }
                        } else {
                            break;
                        }
                    }
                    logger.info("Current total amount fo successfull / error actions: " + Info.getCounterOK() + " / " + Info.getCounterError());
                    Thread.sleep(Long.valueOf(settings.getProperty("Trigger.PollTime")));
                } else {
                    logger.info("Current total amount fo successfull / error actions: " + Info.getCounterOK() + " / " + Info.getCounterError());
                    Thread.sleep(Long.valueOf(settings.getProperty("Trigger.PollTime")));
                }
            } catch (FileNotFoundException fnfex) {
                Info.incCounterError();
                this.logger.log(Level.ERROR, "Excpetion : " + fnfex.getMessage());
            } catch (IOException ioex) {
                Info.incCounterError();
                this.logger.log(Level.ERROR, "Exception : " + ioex.getMessage());
            } catch (InterruptedException iex) {
                Info.incCounterError();
                this.logger.log(Level.FATAL, "Unhandled exception occured! Please report the bug \nif possible with all information available\nto reproduce the problem. ", iex);
            } catch (Exception e) {
                Info.incCounterError();
                this.logger.log(Level.FATAL, "Unhandled exception occured! Please report the bug \nif possible with all information available\nto reproduce the problem. ", e);
            }
        }
        logger.debug("Exiting main loop");
        killLogger();
        settings = null;
        shutdown();
    }

    public synchronized Map<String, String> getStatus() {
        this.status.put("Basic.MaxFilesPerRun", settings.getProperty("Basic.MaxFilesPerRun", ""));
        this.status.put("client_count", "" + currentNbOfLicensesUsed);
        return this.status;
    }

    private void rewriteFiles() throws IOException {
        String outputFirstLine = Base64.encodeBase64String(("C=" + currentNbOfLicensesUsed).getBytes());
        String outPutSecondLine = Base64.encodeBase64String(("U=" + StringUtils.join(containedIps, "|")).getBytes());
        output = new BufferedWriter(new FileWriter(pkgFile));
        //outputBkp = new BufferedWriter(new FileWriter(bkpFile));
        outputTomcatBkp = new BufferedWriter(new FileWriter(tomcatbkpFile));
        outputExtra = new BufferedWriter(new FileWriter(extraProperties));
        output.write(outputFirstLine + "\r\n" + outPutSecondLine);
        //outputBkp.write(outputFirstLine + "\r\n" + outPutSecondLine);
        outputTomcatBkp.write(outputFirstLine + "\r\n" + outPutSecondLine);
        outputExtra.write("count=" + currentNbOfLicensesUsed);
        output.flush();
        output.close();
        //outputBkp.flush();
        outputTomcatBkp.flush();
        //outputBkp.close();
        outputExtra.flush();
        outputExtra.close();
        outputTomcatBkp.close();
    }

    private class StartUpException extends Exception {

        StartUpException() {
        }

        StartUpException(String msg) {
            super(msg);
        }
    }

    private boolean triggerFileFound() {
        sigArray.clear();
        File f = new File(settings.getProperty("Directories.Input"));
        File[] fileArray = f.listFiles();
        Set<String> filesName = new HashSet<String>();
        for (int i = 0; i < fileArray.length; i++) {
            String fName = fileArray[i].getName();
            while (fName.contains(".")) {
                fName = fName.substring(fName.indexOf(".") + 1, fName.length());
            }
            fName = "." + fName;
            if (fName.equalsIgnoreCase(settings.getProperty("Trigger.TriggerExtension"))) {
                sigArray.add(fileArray[i]);
                filesName.add(fileArray[i].getName());
            }
        }
        setOpenFiles(filesName);
        f = null;

        if (sigArray.size() > 0) {
            logger.info(sigArray.size() + " " + settings.getProperty("Trigger.TriggerExtension").toUpperCase() + " file(s) found");
            return true;
        } else {
            logger.info(sigArray.size() + " " + settings.getProperty("Trigger.TriggerExtension").toUpperCase() + " file(s) found");
            return false;
        }
    }

    private void processFile() throws IOException, FileNotFoundException { //, FileNameCorruptedException {
        logger.debug("\t\t\t---------------------------------------------------------------------------------------------------");
        logger.debug("\t\t\t---------------------------------------------------------------------------------------------------");
        try {
            if (settings.containsKey("Parsing.MakeBackup") && settings.getProperty("Parsing.MakeBackup").equalsIgnoreCase("true")) {
                try {
                    makeBackUp(docFile);
                    makeBackUp(txtFile);
                    makeBackUp(sigFile);
                } catch (Exception ex) {
                    throw new FileExistingException(ex.getMessage());
                }
            } else {
                logger.info("\t\t\t\t\tBackup function is deactivated");
            }
            logger.debug("\t\t\t---------------------------------------------------------------------------------------------------");
            if (generateXMLFile(txtFile)) {
                logger.debug("\t\t\t Move \'" + docFile.getName() + "\' and " + xmlFile.getName() + " files to output directory : " + outputFolder);
                moveFile(xmlFile.getAbsolutePath(), outputFolder + "/" + xmlFile.getName());
                moveFile(docFile.getAbsolutePath(), outputFolder + "/" + docFile.getName());
                try {
                    if (!containedIps.contains(docFile.getName().split("_")[1])) {
                        containedIps.add(docFile.getName().split("_")[1]);
                        currentNbOfLicensesUsed++;
                        rewriteFiles();
                    }
                } catch (Exception e) {
                    logger.error(e);
                }
                logger.debug("\t\t\t Done");
                String fileNameSignal = sigFile.getName().substring(0, sigFile.getName().length() - settings.getProperty("Trigger.TriggerExtension").length()) + settings.getProperty("Index.SignalFileExtension");
                logger.debug("\t\t\t Create signalFile " + fileNameSignal);
                createSignalFile(sigFile.getName().substring(0, sigFile.getName().length() - settings.getProperty("Trigger.TriggerExtension").length()));
                logger.info("\t\t\t\tAll files moved to output directory");
                deleteInputFile(txtFile, sigFile);
                Info.incCounterOK();
            } else {
                moveToErrorDirectory();
            }
        } catch (FileExistingException fex) {
            logger.warn("\t\t\t Files alredy exist in backup folder: " + fex.getMessage());
            moveToErrorDirectory();
        }
    }

    private void makeBackUp(File srcFile) throws IOException {
        File destFile = new File(backupFolder + "/" + srcFile.getName());
        java.io.FileInputStream ins = null;
        java.io.FileOutputStream outs = null;
        java.nio.channels.FileChannel iChannel = null;
        java.nio.channels.FileChannel oChannel = null;
        logger.debug("\t\t\t Backup file \'" + srcFile.toString() + "\'");
        try {
            if (!destFile.exists()) {
                logger.debug("\t\t\t\tCreating new file \'" + destFile.toString() + "\'");
                destFile.createNewFile();
            } else {
                logger.debug("\t\t\t\t Backupfile \'" + destFile.toString() + "\' already exists");
                throw new IOException("Existing file exception");
            }

            ins = new java.io.FileInputStream(srcFile);
            outs = new java.io.FileOutputStream(destFile);
            iChannel = ins.getChannel();
            oChannel = outs.getChannel();
            oChannel.transferFrom(iChannel, 0, iChannel.size());
            iChannel.close();
            oChannel.close();
        } catch (IOException ex) {
            if (iChannel != null) {
                iChannel.close();
            }
            if (oChannel != null) {
                oChannel.close();
            }
            throw new IOException("Exception in makeBackUp function : " + ex.getMessage());
        }

    }

    private boolean generateXMLFile(File txtFile) throws FileNotFoundException, IOException {
        BufferedWriter bw = null;
        BufferedReader br = null;
        try {
            logger.debug("\t\t\t---------------------------------------------------------------------------------------------------");
            logger.debug("\t\t\t Start to create associate XML file");
            HashMap<String, String> map = new HashMap<String, String>();
            br = new BufferedReader(new FileReader(txtFile.getAbsolutePath()));
            String line = br.readLine();
            String txtFileContentString = "";
            while (line != null) {
                txtFileContentString += line;
                line = br.readLine();
            }
            txtFileContentString = parseExpressionMatching(txtFileContentString);
            txtFileContentString = txtFileContentString + " ##document_filename=" + outputFolder + "\\" + docFile.getName() + "##";
            String parsingESC = settings.getProperty("Parsing.ESC");
            String tmp = txtFileContentString;
            if (txtFileContentString.contains(parsingESC)) {

                while (tmp.contains(parsingESC)) {
                    tmp = tmp.substring(tmp.indexOf(parsingESC) + parsingESC.length(), tmp.length());
                    String valueKeyPair = tmp.substring(0, tmp.indexOf(parsingESC));
                    tmp = tmp.substring(tmp.indexOf(parsingESC) + parsingESC.length(), tmp.length());
                    if (valueKeyPair.isEmpty() || !valueKeyPair.contains("=")) {
                        continue;
                    }
                    String key = valueKeyPair.substring(0, valueKeyPair.indexOf("="));
                    String value = valueKeyPair.substring(valueKeyPair.indexOf("=") + 1, valueKeyPair.length());
                    //logger.info("Adding to map : " + key + " : " + value);
                    map.put(valueKeyPair.substring(0, valueKeyPair.indexOf("=")), valueKeyPair.substring(valueKeyPair.indexOf("=") + 1, valueKeyPair.length()));
                }
            }
            br.close();
            line = "";
            String templateValue = null;
            if (map.containsKey("Template") && !map.get("Template").isEmpty()) {
                templateValue = map.get("Template");
            } else if (map.containsKey("TEMPLATE") && !map.get("TEMPLATE").isEmpty()) {
                templateValue = map.get("TEMPLATE");
            }
            if (templateValue != null) {
                logger.debug("\t\t\t\t Using " + templateValue + " template");
                File templatesFolder = new File(settings.getProperty("Directories.Template"));
                if (templatesFolder.exists() && templatesFolder.isDirectory() && templatesFolder.canRead()) {
                    for (File templateFile : templatesFolder.listFiles()) {
                        if (templateFile.exists() && templateFile.canRead() && templateFile.getName().equalsIgnoreCase(templateValue)) {
                            logger.debug("\t\t\t\t Found template xml file : " + templateFile.getName());
                            br = new BufferedReader(new FileReader(templateFile));
                        }
                    }
                }
            } else {
                logger.debug("\t\t\t\t" + settings.getProperty("Trigger.MetadataFileExtension").toUpperCase() + " file contains any template name. Using default template" + settings.getProperty("Default.Template"));
                try {
                    br = new BufferedReader(new FileReader(settings.getProperty("Directories.Template") + "/" + settings.getProperty("Default.Template")));
                } catch (FileNotFoundException fnfex) {
                    throw new Exception("\t\t\t\tDefault template \'" + settings.getProperty("Default.Template") + "\' does not exist in path \'" + settings.getProperty("Directories.Template") + "\'. " + fnfex.getMessage());
                }
            }
            line = br.readLine();
            String xmlFileContentString = "";
            while (line != null) {
                xmlFileContentString += line;
                line = br.readLine();
            }
            Iterator it = map.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pairs = (Map.Entry) it.next();
                if (xmlFileContentString.contains(parsingESC + pairs.getKey().toString() + parsingESC)) {
                    xmlFileContentString = xmlFileContentString.replace(parsingESC + pairs.getKey().toString() + parsingESC, StringEscapeUtils.escapeXml(pairs.getValue().toString()));
                }
                it.remove();
            }
            xmlFile = new File(workingFolder + "/" + txtFile.getName().substring(0, txtFile.getName().length() - settings.getProperty("Trigger.TriggerExtension").length()) + ".xml");
            bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(xmlFile), "ISO-8859-1"));
            bw.write(XmlFormatter.start(xmlFileContentString));
            bw.close();
            logger.debug("\t\t\t Done");
            return true;
        } catch (Exception ex) {
            logger.warn("\t\t\t Cannot create XML file: " + ex.getMessage());
            if (bw != null) {
                bw.close();
            }
            if (br != null) {
                br.close();
            }
            xmlFile.delete();
            return false;
        }
    }

    private void deleteInputFile(File txtFile, File sigFile) {
        logger.debug("\t\t\t---------------------------------------------------------------------------------------------------");
        logger.debug("\t\t\tStart to delete \'" + txtFile.getName() + "\', \'" + docFile.getName() + "\' and \'" + sigFile.getName() + "\' from working directory : " + workingFolder);
        boolean txt = false, sig = false, doc = false;
        if (docFile.exists()) {
//            logger.warn("\t\t\t\tCannot delete " + docFile.getName());
        } else {
            doc = true;
        }
        if (!txtFile.delete()) {
            logger.warn("\t\t\t\tCannot delete " + txtFile.getAbsolutePath());
        } else {
            txt = true;
        }
        if (!sigFile.delete()) {
            logger.warn("\t\t\t\tCannot delete " + sigFile.getAbsolutePath());
        } else {
            sig = true;
        }
        if (txt && sig && doc) {
            logger.debug("\t\t\tDone");
        }
    }

    private void moveToFolder(File docFile, File txtFile, File sigFile, String folder) {
        String orgFileName = docFile.getName();
        String txtFileName = txtFile.getName();
        String sigFileName = sigFile.getName();
        if (folder.equals(errorFolder) && new File(folder + "/" + sigFileName).exists()) {
            int i = 0;
            String ext = "_00" + i;
            String sigFileExtention = settings.getProperty("Trigger.TriggerExtension");
            sigFileName = sigFileName.substring(0, sigFileName.length() - sigFileExtention.length()) + ext + sigFileExtention;
            while (new File(folder + "/" + sigFileName).exists()) {
                i++;
                if (i >= 10) {
                    ext = "_0" + i;
                } else if (i >= 100) {
                    ext = "_" + i;
                } else {
                    ext = "_00" + i;
                }
                sigFileName = sigFileName.substring(0, sigFileName.indexOf("_00")) + ext + sigFileExtention;
            }
            String docFileExtention = settings.getProperty("Trigger.DocumentFileExtension");
            orgFileName = orgFileName.substring(0, orgFileName.length() - docFileExtention.length()) + ext + docFileExtention;
            String txtFileExtention = settings.getProperty("Trigger.MetadataFileExtension");
            txtFileName = txtFileName.substring(0, txtFileName.length() - txtFileExtention.length()) + ext + txtFileExtention;
        }
        if (moveFile(sigFile.getAbsolutePath(), folder + "/" + sigFileName)) {
            logger.debug("\t\t\t\t" + sigFileName + " is moved");
        } else {
            logger.warn("\t\t\t\tCannot move " + sigFileName + " to " + folder + "/" + sigFileName);
        }
        if (new File(docFile.getAbsolutePath()).exists()) {
            if (moveFile(docFile.getAbsolutePath(), folder + "/" + orgFileName)) {
                logger.debug("\t\t\t\t" + orgFileName + " is moved");
            } else {
                logger.warn("\t\t\t\tCannot move " + orgFileName + " to " + folder + "/" + orgFileName);
            }
        }
        if (new File(txtFile.getAbsolutePath()).exists()) {
            if (moveFile(txtFile.getAbsolutePath(), folder + "/" + txtFileName)) {
                logger.debug("\t\t\t\t" + txtFileName + " is moved");
            } else {
                logger.warn("\t\t\t\tCannot move " + txtFileName + " to " + folder + "/" + txtFileName);
            }
        }
        sigArray.remove(sigFile);
    }

    private boolean moveFile(String oldPath, String newPath) {
        File oldName = new File(oldPath);
        File newName = new File(newPath);
        if (oldName.renameTo(newName)) {
            return true;
        } else {
            return false;
        }
    }

    public synchronized Set<String> getOpenFiles() {
        return openedFiles;
    }

    private void setOpenFiles(Set<String> files) {
        openedFiles = files;
    }

    private void createSignalFile(String fileName) throws IOException {
        logger.debug("\t\t\t---------------------------------------------------------------------------------------------------");
        logger.debug("\t\t\tCreate associate " + settings.getProperty("Index.SignalFileExtension").toUpperCase() + " file");
        File sigFile = new File(outputFolder + "/" + fileName + settings.getProperty("Index.SignalFileExtension"));
        sigFile.createNewFile();
        logger.debug("\t\t\tDone");
        logger.debug("\t\t\t---------------------------------------------------------------------------------------------------");
    }

    private boolean filesAreNotOpenedByAnotherProcess() {
        logger.debug("\t\t\t---------------------------------------------------------------------------------------------------");
        logger.debug("\t\t\t Try to move " + getFilesName() + " to working directory : " + workingFolder);
        return docFile.renameTo(docFile) && txtFile.renameTo(txtFile) && sigFile.renameTo(sigFile);
    }

    private boolean moveFileToWorkingDirectory() {
        if (!moveFile(docFile.getAbsolutePath(), workingFolder + "/" + docFile.getName())) {
            logger.warn("\t\t\t\t Cannot move " + docFile.getName());
            return false;
        }
        if (!moveFile(txtFile.getAbsolutePath(), workingFolder + "/" + txtFile.getName())) {
            logger.warn("\t\t\t\t Cannot move " + txtFile.getName());
            moveFile(workingFolder + "/" + docFile.getName(), inputFolder + "/" + docFile.getName());
            return false;
        }
        if (!moveFile(sigFile.getAbsolutePath(), workingFolder + "/" + sigFile.getName())) {
            logger.warn("\t\t\t\t Cannot move " + sigFile.getName());
            moveFile(workingFolder + "/" + docFile.getName(), inputFolder + "/" + docFile.getName());
            moveFile(workingFolder + "/" + txtFile.getName(), inputFolder + "/" + txtFile.getName());
            return false;
        }
        docFile = new File(workingFolder + "/" + docFile.getName());
        txtFile = new File(workingFolder + "/" + txtFile.getName());
        sigFile = new File(workingFolder + "/" + sigFile.getName());
        logger.debug("\t\t\t Done");
        return true;
    }

    private boolean associateFileExist() {
        return docFile.exists() && txtFile.exists();
    }

    private void moveToErrorDirectory() {
        logger.debug("\t\t\t---------------------------------------------------------------------------------------------------");
        logger.debug("\t\t\t---------------------------------------------------------------------------------------------------");
        if (docFile.exists() && txtFile.exists() && sigFile.exists()) {
            logger.warn("\t\t\tShift " + docFile.getName() + ", " + txtFile.getName() + " and " + sigFile.getName() + " to error directory");
            moveToFolder(docFile, txtFile, sigFile, errorFolder);
        }
        if (!docFile.exists()) {
            logger.warn("\t\t\tThe " + FilenameUtils.getExtension(docFile.getName()).toUpperCase() + " file " + docFile.getName() + " cannot be found ");
        }
        if (!txtFile.exists()) {
            logger.warn("\t\t\tThe " + FilenameUtils.getExtension(txtFile.getName()).toUpperCase() + " file " + txtFile.getName() + " cannot be found ");
        }
        if (!sigFile.exists()) {
            logger.warn("\t\t\tThe " + FilenameUtils.getExtension(sigFile.getName()).toUpperCase() + " file " + sigFile.getName() + " cannot be found ");
        }
        moveToFolder(docFile, txtFile, sigFile, errorFolder);
        Info.incCounterError();
    }

    private void initialWorkingFile(File sFile) {
        sigFile = sFile;
        String path = sigFile.getAbsolutePath();
        while (!path.substring(path.length() - 1, path.length()).equals(".")) {
            path = path.substring(0, path.length() - 1);
        }
        path = path.substring(0, path.length() - 1);
        setDocFileName(path);
        txtFile = new File(path + settings.getProperty("Trigger.MetadataFileExtension"));
    }

    private String getFilesName() {
        return docFile.getName() + ", " + txtFile.getName() + " and " + sigFile.getName();
    }

    private class BufferHandler extends AppenderSkeleton {

        private ArrayList<LoggingEvent> events = new ArrayList();
        private boolean append = true;

        BufferHandler() {
            super.setName(super.getClass().getName());
        }

        protected void append(LoggingEvent event) {
            if (this.append) {
                this.events.add(event);
            }
        }

        public void close() {
            this.append = false;
        }

        public boolean requiresLayout() {
            return false;
        }

        public List<LoggingEvent> getEvents() {
            return this.events;
        }
    }

    public void shutdown() {
        if (!this.running) {
            return;
        }
        if (this.logger != null) {
            this.logger.info("Shutting down...");
        }
        try {
            output.close();
        } catch (IOException ex) {
            logger.error(ex);
        }
        this.running = false;
    }

    private void killLogger() {
        if (this.logger != null) {
            this.logger.trace(super.getClass().getName() + ": run() leave");
        }
        if (this.handler != null) {
            this.logger.removeAppender(this.handler);
            this.handler = null;
        }
        this.logger = null;
    }

    private String parseExpressionMatching(String fileAsString) {
        String result = "";
        String regex = settings.getProperty("Parsing.ExpressionMatching");
        Pattern p = Pattern.compile(regex);
        Matcher matcher = p.matcher(fileAsString);
        //logger.info("To parse : " + fileAsString);
        while (matcher.find()) {
            String mg = matcher.group();
            result += mg;
            // logger.info("Adding to result  : " + mg);
        }
        // logger.info("Result : " + result);
        return result;
    }

    private void setDocFileName(String path) {
        String docFileExt = settings.getProperty("Trigger.DocumentFileExtension");
        String tmp;
        while (docFileExt.contains(",")) {
            tmp = docFileExt.substring(0, docFileExt.indexOf(","));
            if (new File(path + tmp).exists()) {
                docFileExt = tmp;
                break;
            } else {
                docFileExt = docFileExt.substring(docFileExt.indexOf(",") + 1, docFileExt.length());
            }
        }
        docFile = new File(path + docFileExt);
    }

    private class FileExistingException extends Exception {

        public FileExistingException() {
        }

        public FileExistingException(String messaggio) {
            super(messaggio);
        }
    }

    public static void main(String[] args) throws IOException {        
//        ERPConnect eRPConnect = new ERPConnect("C:\\Users\\samir.lebaal\\Desktop\\conf");
//        eRPConnect.start();
//        ERPConnect.settings = new Properties();
//        ERPConnect.settings.load(new FileInputStream(new File("C:\\Users\\samir.lebaal\\Desktop\\conf\\config.properties")));
//        eRPConnect.outputFolder = "C:\\DOKinform\\ERPconnector\\output";
        //eRPConnect.docFile = new File("C:\\DOKinform\\ERPconnector\\work\\ERPcon-000000000361878_VERSANDPC_20191204173021.pdf");
        //eRPConnect.generateXMLFile(new File("C:\\DOKinform\\ERPconnector\\work\\ERPcon-000000000361878_VERSANDPC_20191204173021.txt"));
    }
}
