//package de.di.license.check;
//
////import de.elo.ix.client.UserInfo;
//import de.di.erpconnect.ClientCountFile;
//import java.io.File;
//import java.net.URI;
//import java.net.URISyntaxException;
//import java.net.URL;
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.List;
//import java.util.Map;
//import org.apache.log4j.Logger;
//import de.di.erpconnect.*;
//
///**
// *
// * @author Rahman
// */
//public class Checker {
//
//    private URI dataURI = null;
//    private Logger logger;
//    private DataCollection dataCollect = null;
//    private String user = "";
//    private URI configURI;
//    private int maxClientCount;
//    public static URL dataURL;
//    private int currentClientCount;
//    private String[] wui;
//    private String NewUserList;
//    private Map<String, String> status;
//
////    private void saveData() throws FileNotFoundException {
////        int currentClientCount = 0;
////        File outputFile = null;
////        if (dataURI == null) {
////            logger.fatal("License exception occured. Software manipulation has been detected.");
//////            forceShutdown();
//////            running = false;
////            return;
////        } else if (dataURI != null) {
////            try {
////                dataCollect = DataCollection.fromFile(new File(dataURI));
////
////                if (dataCollect != null) {
////                    currentClientCount = dataCollect.getCount();
////                    if (currentClientCount > 0) {
////                        user = dataCollect.getWEBaccessUserInfos();
////                    }
//////                   logger.debug(getClass().getName() + ": ClientCount = " + currentClientCount + "..From PKG. WebAccessUsers " + WebAccessUsers + ".");
//////                    dataCollect.put("C", String.valueOf(currentClientCount));
////                    if (user != null) {
//////                        dataCollect.put("U", user);
////                    }
////
////                    outputFile = new File(dataURI);
////                    dataCollect.save(outputFile);
////                }
////            } catch (java.io.FileNotFoundException fioex) {
////                //file not writable
////                logger.fatal("License exception occured. Error getting license information.");
//////                forceShutdown();
//////                running = false;
////
////            } catch (IOException ioex) {
////                //@TODO: Remove test code
////                //@TODO: Handle exception properly            
////                logger.error("Error by writing into the file.", ioex);
////                logger.fatal("License exception occured. Software manipulation has been detected.");
////            } catch (SecurityException secex) {
////                throw new java.io.FileNotFoundException();
////            } catch (ClassNotFoundException ex) {
////                logger.error("Error Class not found.", ex);
////            }
////        }
////
////        try {
////            File configDir = new File(configURI);
////            outputFile = new File(configDir, "extra.properties");
////            ClientCountFile countFile = new ClientCountFile();
////            countFile.setClientCount(dataCollect.getCount());
////
////            countFile.save(outputFile);
////        } catch (IOException fex) {
////        } catch (SecurityException secex) {
////        }
////
////        if (dataCollect.getCount() > getMaxClientCount() * 1.2) {
////            logger.fatal("License exception occured. Maximum number of clients exceeded.");
//////            forceShutdown();
//////            running = false;
////        } else if (dataCollect.getCount() > getMaxClientCount()) {
////            logger.warn("Critical number of active clients detected! " + "Please request a new license if you want to add more clients.");
////        }
////    }
//    private void loadExtraProperties() throws StartUpException {
//        logger.trace(getClass().getName() + ": entering loadExtraProperties()");
//        URL dataURL = null;
//        boolean checkCountFile = false;
//        try {
//            if (dataURL != null) {
//                dataURI = dataURL.toURI();
//                dataCollect = DataCollection.fromFile(new File(dataURI));
//                if (dataCollect != null) {
//
////                    createNewListWithoutInhibitedUser();
//                    newListOfELOUsersForLicenseInfo();
//
//                }
//            } else {
//                checkCountFile = true;
//            }
//        } catch (java.net.URISyntaxException uriex) {
//            checkCountFile = true;
//        } catch (java.io.IOException ioex) {
//            checkCountFile = true;
//        } catch (java.lang.ClassNotFoundException classex) {
//            checkCountFile = true;
//        } catch (Exception ex) {
//            checkCountFile = true;
//        } finally {
//            if (dataCollect == null) {
//                dataCollect = new DataCollection();
//                currentClientCount = dataCollect.getSize();
////                dataCollect.put("C", String.valueOf(currentClientCount));
////                dataCollect.put("U", "null");
//                logger.trace(getClass().getName() + ": DataCollect.currentClientCount intializied with Count=" + currentClientCount);
//            }
//
//        }
//
//        if (checkCountFile) {
//            File configDir = new File(configURI);
//            File inputFile = new File(configDir, "extra.properties");
//            ClientCountFile countFile = null;
//
//            try {
//
//                // TODO Add new User to the List
//                if (dataURL == null) {
//                    if (dataURL != null) {
//                        try {
//                            File dataFile = new File(new File(dataURL.toURI()), "data.pkg");
//
//                            if (dataFile.createNewFile()) {
//                                dataURI = dataFile.toURI();
//
//                            }
//                        } catch (Exception ex) {
//                            dataURL = null;
//                        }
//                    }
//                } else {
//                    try {
//                        dataURI = dataURL.toURI();
//                    } catch (URISyntaxException ex) {
//                        throw new StartUpException("Unable to start the service. Data file not found.");
//                    }
//                }
//                dataCollect.save(new File(dataURI));
//                if (countFile == null) {
//                    countFile = new ClientCountFile();
//                    countFile.setClientCount(0);
//
//                }
//
//                // Upadate in extra Properties
//                if (countFile.getClientCount() != dataCollect.getCount()) {
//                    countFile.setClientCount(dataCollect.getCount());
//                }
//                countFile.save(inputFile);
//            } catch (java.io.IOException ioex) {
//                throw new StartUpException("Unable to start the service. Missing data file.");
//            }
//
//        }
//        logger.trace(getClass().getName() + ": leaving loadExtraProperties()");
//    }
////
////    private void createNewListWithoutInhibitedUser() {
////
////        // User Status
////        logger.trace(getClass().getName() + ": starting checkUsers() ");
////        if (eloClient.isConnected() && dataCollect != null) {
////            dataCollect.getUserlist();
////            boolean existOnMore = false;
////            NewUserList = dataCollect.getWEBaccessUserInfos();
////            try {
////                UserInfo ui = null;
////                for (Map.Entry<String, String> User : dataCollect.getUserInfo().entrySet()) {
////
////                    wui = dataCollect.getWEBaccessUserInfos().split(";");
////                    try {
////                        ui = eloClient.getUserStatus(User.getKey());
////                        if (ui != null || !(ui.getName().equals("null"))) {
////                            if ((eloClient.getServicePort().getACCESS().getFLAG_NOLOGIN() & ui.getFlags()) != 0) {
////
////                                NewUserList = RemoveInhibetedUser(wui, User.getKey() + "|" + User.getValue());
////                                logger.trace(getClass().getName() + ": User " + User.getKey() + " from " + User.getValue() + " is inactive.");
////                            }
////                        }
////                    } catch (NullPointerException e) {
////                        NewUserList = RemoveInhibetedUser(wui, User.getKey() + "|" + User.getValue());
////                        logger.trace(getClass().getName() + ":ELO User " + User.getKey() + " from " + User.getValue() + " does not exist.");
////                        continue;
////                    }
////                }
////
////            } catch (RemoteException ex) {
////                logger.debug(getClass().getName() + ": the connection failed to ix Server  ", ex);
////            } catch (IOException ex) {
////                logger.debug(getClass().getName() + ": save pkg data failed after checking out the flags.", ex);
////            } catch (ClassNotFoundException ex) {
////                logger.debug(getClass().getName() + ": save pkg data failed after checking out the flags.", ex);
////            }
////
////        }
////
////    }
//
//    private void newListOfELOUsersForLicenseInfo() {
//
//        List<String> ELOusers = null;
//        if (!NewUserList.equals("")) {
//            ELOusers = Arrays.asList(NewUserList.split(";"));
//            Collections.sort(ELOusers, StringComperator.stringAlphabeticalComparator);
//            for (int i = 0; i < ELOusers.size(); i++) {
//                if (ELOusers.get(i).contains("|")) {
//
//                    String[] UserInfoArray = ELOusers.get(i).split("\\|");
//                    status.put("ELOUser" + i, UserInfoArray[0]);
//                    status.put("ELOClientIP" + i, UserInfoArray[1]);
//                    logger.debug(getClass().getName() + ": ELO User " + UserInfoArray[0] + " from " + UserInfoArray[1] + " is active.");
//
//                }
//
//            }
//        }
//        status.put("ActiveUser", String.valueOf(ELOusers.size()));
//        currentClientCount = ELOusers.size();
//        logger.info(getClass().getName() + ": ClientCount = " + currentClientCount);
//        logger.debug(getClass().getName() + ": Active WEBaccess users: " + dataCollect.getWEBaccessUserInfos());
//        logger.trace(getClass().getName() + ": leaving checkUsers() ");
//
//    }
//
//    public int getMaxClientCount() {
//        return maxClientCount;
//    }
//
//    private class StartUpException extends Exception {
//
//        StartUpException() {
//            super();
//        }
//
//        StartUpException(String msg) {
//            super(msg);
//        }
//    }
//}
