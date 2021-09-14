package de.di.license.check;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataCollection {

    private Map<String, String> data = new HashMap<String, String>();
    private List<String> client = new ArrayList<String>();
    private String WEBaccessUserInfos = null;    
    List<String> UserList=new ArrayList<String>();    
    private Map<String, String> UserInfo = new HashMap<String, String>();
    
    public void putClient(String client, File outFile) throws IOException{
        this.client.add(XOR_Encryption(client));
        saveClient(outFile);
    }
    
    public boolean isAssociate(String client){
        return this.client.contains(XOR_Encryption(client));
    }
    
    public int getClientCount(){
        return this.client.size();
    }
    
    public void saveClient(File outFile) throws IOException {
        DataWriter writer = new DataWriter(outFile);
        try {
            writer.writeObject(data);
        } finally {
            try {
                writer.close();
            } catch (IOException ex) {
            }
        }
    }

    public DataCollection() {
    }

    /**
     *
     * @param d
     * @param t
     */
    public void put(String d, String t, File outputFile) throws IOException {
        data.put(d, XOR_Encryption(t));
        save(outputFile);
    }

    
    /**
     *
     * @return Number of pc
     */
    public int getCount() {
//        System.out.println(" C equals " + Integer.parseInt(XOR_Decryption(data.get("C"))));
//        System.out.println(" U equals " + data.get("U"));

        return Integer.parseInt(XOR_Decryption(data.get("C")));
    }

    /**
     *
     * @param infos
     * @return
     */
    public boolean verifyUserInfoExists(String[] infos) {
        boolean exist = false;
        for (Map.Entry e : UserInfo.entrySet()) {

            if (e.getKey().equals(infos[0]) && e.getValue().equals(infos[1])) {
                exist = true;
                break;
            } else if (e.getKey().equals(infos[0]) && !e.getValue().equals(infos[1])) {
                 System.out.println(getClass().getName() +" : the already existing user has other IP adress");
                exist = false;
                break;
            } else {
                exist = false;
            }

        }
        return exist;
    }

    /**
     *
     * @param Info
     */
    public void getUserInfo(String Info) {
        if (Info.contains("|")) {

            String[] UserInfoArray = Info.split("\\|");

            UserInfo.put(UserInfoArray[0], UserInfoArray[1]);

        } else {
            UserInfo.put(Info, "null");
        }

    }

    /**
     *
     * @return
     */
    public int getSize() {
        return data.size();
    }

    /**
     *
     * @param outFile
     * @throws IOException
     */
    public void save(File outFile) throws IOException {
        DataWriter writer = new DataWriter(outFile);
        try {
            writer.writeObject(data);
        } finally {
            try {
                writer.close();
            } catch (IOException ex) {
            }
        }
    }

    /**
     *
     * @param inputFile
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static DataCollection fromFile(File inputFile) throws IOException, ClassNotFoundException {
        DataReader reader = new DataReader(inputFile);
        DataCollection c = new DataCollection();

        if (inputFile.length() == 0) {
            return c;
        }

        try {
            c.data = (HashMap<String, String>) reader.readObject();
        } finally {
            try {
                reader.close();
            } catch (IOException ex) {
            }
        }

        return c;
    }

    /**e
     *
     * @param object
     * @return
     */
    String XOR_Encryption(String object) {
        char temp;
        char key = 'e';
        String CText = "";
        int xor;
        for (int i = 0; i < object.length(); i++) {

            xor = object.charAt(i) ^ key;
            temp = (char) xor;
            CText = CText + temp;
        }
        return CText;

    }

    /**
     *
     * @param CText
     * @return
     */
    static String XOR_Decryption(String CText) {
        char temp;
        char key = 'e';
        String object = "";
        int xor;
        for (int i = 0; i < CText.length(); i++) {

            xor = CText.charAt(i) ^ key;
            temp = (char) xor;
            object = object + temp;
        }
        return object;

    }

    /**
     * @return the Userlist
     */
    public void getUserlist() {
        String ulist = XOR_Decryption(data.get("U"));
        
        if (ulist.contains(";")) {
            UserList =  Arrays.asList(ulist.split(";")) ;
            for (String ul : UserList) {
                getUserInfo(ul);
            }

        } else {
                
                UserList.add(ulist);
                getUserInfo(UserList.get(0));
        }
        
    }

    /**
     * @return the WEBaccessUserInfos
     */
    public String getWEBaccessUserInfos() {
        WEBaccessUserInfos = XOR_Decryption(data.get("U"));
        return WEBaccessUserInfos;
    }

    /**
     * @param WEBaccessUserInfos the WEBaccessUserInfos to set
     */
    public void setWEBaccessUserInfos(String WEBaccessUserInfos) {
        this.WEBaccessUserInfos = WEBaccessUserInfos;
    }

    /**
     * @return the UserInfo
     */
    public Map<String, String> getUserInfo() {
        return UserInfo;
    }

    /**
     * @param UserInfo the UserInfo to set
     */
    public void setUserInfo(Map<String, String> UserInfo) {
        this.UserInfo = UserInfo;
    }
}
