
package de.di.erp.gui;

import de.elo.ix.client.ClientInfo;
import de.elo.ix.client.IXClient;
import de.elo.ix.client.IXServicePortC;
import de.elo.ix.client.ServerInfo;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.RemoteException;

/**
 *
 * @author A. Sopicki
 */
public class ELOERPClient {
    private IXClient indexClient;
    private ClientInfo clientInfo;
    private String connectionUrl;
    private String userName;
    private String password;
    private IXServicePortC ixConstants;    
    private boolean connected = false;
    
    public ELOERPClient() {
       this(null);
    }
    
    public ELOERPClient(java.util.Properties settings) {
        init(settings);
    }
    
    private void init(java.util.Properties settings) {
        
        connectionUrl = settings.getProperty("IndexServer.URL", "");
        
        if (clientInfo == null) {
            clientInfo = new ClientInfo();
        }

        clientInfo.setTimeZone(getSystemTimeZone());
        
        if (indexClient == null) {
           indexClient = new IXClient(getConnectionUrl().toString());
        }
    }
    
    
    public ServerInfo getServerInfo() throws RemoteException {
                
        return indexClient.ix.getServerInfo(clientInfo);
    }
    
    
    public void close() {
        if ( connected ) {
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
        
    
    private void logoff() {
        try {
            indexClient.logout(clientInfo);
        } catch (RemoteException ex) {

        } finally {
            connected = false;
        }
    }
    
    @Override
    public void finalize() {
        if ( connected ) {
            try {
                indexClient.logout(clientInfo);
            } catch (Exception ex) {
                
            }
        }
        
        if ( indexClient != null ) {
            indexClient.done();
        }
    }
}
