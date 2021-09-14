package de.di.elo.client;

import de.di.erpconnect.ERPConnect;
import de.elo.ix.client.ClientInfo;
import de.elo.ix.client.Document;
import de.elo.ix.client.EditInfo;
import de.elo.ix.client.EditInfoZ;
import de.elo.ix.client.IXClient;
import de.elo.ix.client.IXConnFactory;
import de.elo.ix.client.IXConnection;
import de.elo.ix.client.IXServicePortC;
import de.elo.ix.client.LockZ;
import de.elo.ix.client.LoginResult;
import de.elo.ix.client.ServerInfo;
import de.elo.ix.client.Sord;
import de.elo.ix.client.SordZ;
import java.net.InetAddress;
import java.net.UnknownHostException;
import de.elo.utils.net.RemoteException;
import org.apache.log4j.Logger;

/**
 *
 * @author A. Sopicki
 */
public class ELOERPClient {

    //private IXClient indexClient;
    private ClientInfo clientInfo;
    private String connectionUrl;
    private String userName;
    private String password;
    private IXServicePortC ixConstants;
    private boolean connected = false;
    private LoginResult loginResult;
    
    // SL +++ For ELO20 +++
    protected IXConnFactory _factory;
    protected IXConnection _connection;
    private String runAs = null;
    // +++ +++ +++ +++ +++ +++ +++ +++

    public ELOERPClient(java.util.Properties settings, Logger logger) {
        init(settings, logger);
    }

    public final void init(java.util.Properties settings, Logger logger) {
        // +++
//        while(true)
//        {
        // +++
            try {
                if (settings == null) {
                    settings = ERPConnect.getSettings();
                }
                connectionUrl = settings.getProperty("IndexServer.URL", "");
               /* if (connectionUrl.equals("")){
                    connectionUrl = settings.getProperty("Index.ServerURL", "");
                }
                */
                userName = settings.getProperty("IndexServer.User");
                password = settings.getProperty("IndexServer.Password");


                if (clientInfo == null) {
                    clientInfo = new ClientInfo();
                }

                clientInfo.setTimeZone(getSystemTimeZone());

                if (/*indexClient*/_factory == null) {                    
                    //indexClient = new IXClient(getConnectionUrl().toString());
                    _factory = new IXConnFactory(getConnectionUrl().toString(), "", "");
                }
                // +++
//                break;
                // +++
            } catch (IllegalStateException eex) {

                logger.error("Constructor Cannot connect to elo indexserver at the following addresses \'" + settings.getProperty("IndexServer.URL") + "\' ");
                logger.error("Please secure the elo indexserver is runing");
                logger.info("If the url, user or password is wrong, please correct it in config.properties file and restart ERPconnector");
                logger.info("Try to reconnect in " + Integer.valueOf(settings.getProperty("Trigger.PollTime")) / 1000 + " seconds");
                try {
                    Thread.sleep(Integer.valueOf(settings.getProperty("Trigger.PollTime")));
                } catch (InterruptedException iex) {
                    iex.getStackTrace();
                }
            }
//        }
    }

    public void alive() throws RemoteException {
        if (!connected) {
            login();

            return;
        }
        //indexClient.ix.alive(clientInfo);
        _connection.ix().alive();
    }

    public ServerInfo getServerInfo() throws RemoteException {
        /*Removed because for basic info for server not needed to make login*/
        if ( !connected ) {
            login();
        }

        //return indexClient.ix.getServerInfo(clientInfo);
        return _connection.ix().getServerInfo();
    }

    public Document checkinDocBegin(Document doc) throws RemoteException {
        if (!connected) {
            login();
        }

        //return indexClient.ix.checkinDocBegin(clientInfo, doc);
        return _connection.ix().checkinDocBegin(doc);
    }

    public Document checkinDocEnd(Sord docSord, SordZ sordInfo, Document doc, LockZ lockInfo) throws RemoteException {

        if (!connected) {
            login();
        }

        //return indexClient.ix.checkinDocEnd(clientInfo, docSord, sordInfo, doc, lockInfo);
        return _connection.ix().checkinDocEnd(docSord, sordInfo, doc, lockInfo);
    }

    public int checkinSord(Sord parent, SordZ sordInfo, LockZ lockInfo) throws RemoteException {
        if (!connected) {
            login();
        }

        //return indexClient.ix.checkinSord(clientInfo, parent, sordInfo, lockInfo);
        return _connection.ix().checkinSord(parent, sordInfo, lockInfo);
    }

    public EditInfo checkoutSord(String path, EditInfoZ editInfo, LockZ lockInfo) throws RemoteException {
        if (!connected) {
            login();
        }

        //return indexClient.ix.checkoutSord(clientInfo, path, editInfo, lockInfo);
        return _connection.ix().checkoutSord(path, editInfo, lockInfo);
    }

    public EditInfo createDoc(String parent, String mask, String template, EditInfoZ editInfo) throws RemoteException {
        if (!connected) {
            login();
        }

        //return indexClient.ix.createSord(clientInfo, parent, mask, editInfo);
        return _connection.ix().createSord(parent, mask, editInfo);
    }

    public EditInfo createSord(String parent, String mask, EditInfoZ editInfo) throws RemoteException {
        if (!connected) {
            login();
        }

        //return indexClient.ix.createSord(clientInfo, parent, mask, editInfo);
        return _connection.ix().createSord(parent, mask, editInfo);
    }

    public void close() {
        if (connected) {
            logoff();
        }
    }

    public boolean isConnected() {
        return connected;
    }

    public String getUserName() {
        return userName;
    }

    public IXServicePortC getServicePort() {
        return ixConstants;
    }

    public int getTicketLifeTime() {
        if (!connected) {
            return 0;
        }

        return loginResult.getTicketLifetime();
    }

    public void refSord(String oldParentId, String newParentId, String objId, int manSortIdx) throws RemoteException {
        //indexClient.ix.refSord(clientInfo, oldParentId, newParentId, objId, manSortIdx);
        _connection.ix().refSord(oldParentId, newParentId, objId, manSortIdx);
    }

    public String upload(String url, java.io.File file) throws RemoteException {
        //return indexClient.upload(url, file);
        return _connection.upload(url, file);
    }

    private String getPassword() {
        return password;
    }

    private String getConnectionUrl() {
        return connectionUrl;
    }

    private String getSystemTimeZone() {
        return java.util.TimeZone.getDefault().getID();
    }

    private String getLocalHost() throws UnknownHostException {
        return InetAddress.getLocalHost().getHostName();
    }

    private void login() throws RemoteException {        
        try {
            // SL +++
            _connection = _factory.create(getUserName(), getPassword(), "", null);
            // +++ +++ +++
            //loginResult = indexClient.login(clientInfo, getUserName(), getPassword(), getLocalHost(), null);
            loginResult = _connection.getLoginResult();
            //ixConstants = indexClient.getCONST(clientInfo);
            ixConstants = _connection.getCONST();
            clientInfo = loginResult.getClientInfo();
        } catch (/*UnknownHostException*/byps.RemoteException uhex) {
            throw new RemoteException("Unable to login due to a network problem", uhex);
        }

        connected = true;
    }

    private void logoff() {
        //try {
            //indexClient.logout(clientInfo);
            if(_connection != null) {
                _connection = null;
            }
        /*} catch (RemoteException ex) {
        } finally {
            connected = false;
        }*/
    }

    @Override
    public void finalize() throws Throwable {
        // SL +++
        super.finalize();
        logoff();
        // +++ +++ +++
        /*if (connected) {
            try {
                indexClient.logout(clientInfo);
            } catch (Exception ex) {
            }
        }

        if (indexClient != null) {
            indexClient.done();
        }*/
    }
}
