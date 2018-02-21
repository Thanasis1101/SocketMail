package socketmail;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Thanasis
 */
public abstract class Client {

    private static Socket server = null;
    private static DataOutputStream out;
    private static DataInputStream in;
    private static ObjectInputStream inObject;

    public static boolean connectToServer(String host, int port) {
        try {
            
            server = new Socket(host, port);

            out = new DataOutputStream(server.getOutputStream());
            in = new DataInputStream(server.getInputStream());
            inObject = new ObjectInputStream(server.getInputStream());

            return true;
            
        } catch (IOException e) {
            return false;
        }
    }

    public static boolean register(String email, String password){
        if (server != null) {
            try {

                out.writeUTF(Constants.REGISTER);
                out.writeUTF(email);
                out.writeUTF(password);
                
                String registerResult = in.readUTF();
                
                switch (registerResult){
                    case Constants.REGISTERED_SUCCESFULLY:
                        return true;
                    case Constants.EMAIL_ALREADY_EXISTS:
                        return false;
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
    
    
    public static boolean login(String email, String password){
        if (server != null) {
            try {

                out.writeUTF(Constants.LOGIN);
                out.writeUTF(email);
                out.writeUTF(password);
                
                String loginResult = in.readUTF();
                
                switch (loginResult){
                    case Constants.LOGIN_CORRECT:
                        return true;
                    case Constants.LOGIN_WRONG:
                        return false;
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
    
    public static boolean sendEmail(String receiver, String subject, String mainBody){
        if (server != null) {
            try {

                out.writeUTF(Constants.NEW_EMAIL);
                out.writeUTF(receiver);
                out.writeUTF(subject);
                out.writeUTF(mainBody);
                
                String loginResult = in.readUTF();
                
                switch (loginResult){
                    case Constants.EMAIL_SENT_SUCCESFULLY:
                        return true;
                    case Constants.RECEIVER_NOT_FOUND:
                        return false;
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static ArrayList<Email> showEmails(){
        if (server != null) {
            try {

                out.writeUTF(Constants.SHOW_EMAILS);                
                return (ArrayList<Email>) inObject.readObject();
                
            } catch (IOException | ClassNotFoundException e) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        return null;
    }
    
    public static Email readEmail(int emailId){
        if (server != null) {
            try {

                out.writeUTF(Constants.READ_EMAIL); 
                out.writeUTF(String.valueOf(emailId));
                return (Email) inObject.readObject();
                
            } catch (IOException | ClassNotFoundException e) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        return null;
    }
    
    public static void deleteEmail(int emailId){
        if (server != null) {
            try {

                out.writeUTF(Constants.DELETE_EMAIL); 
                out.writeUTF(String.valueOf(emailId));
                
            } catch (IOException e) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, e);
            }
        }
    }
    
    public static void closeConnection(){
        try {
            server.close();
            in.close();
            out.close();
            inObject.close();
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
