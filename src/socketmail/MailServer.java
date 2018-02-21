package socketmail;

import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class MailServer extends Thread {

    private static final int DEFAULT_PORT = 5005;
    private final ServerSocket serverSocket;
    private final ArrayList<ClientHandler> clientHandlers;
    private final AccountsList accounts;
    

    public MailServer(int port) {

        clientHandlers = new ArrayList<>();
        accounts = new AccountsList();
        serverSocket = startServer(port);

    }

    private ServerSocket startServer(int port) {
        
        ServerSocket serverSocket = null;

        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Started server on port " + port);
            
            // Adding three test accounts
            tryAddAccount("test1@socketmail.gr", "pass1");
            tryAddAccount("test2@socketmail.gr", "pass2");
            tryAddAccount("test3@socketmail.gr", "pass3");
            
            // Test emails
            newEmail("test1@socketmail.gr", "test1@socketmail.gr", "Test subject (1)", "This is a test email (1)");
            newEmail("test2@socketmail.gr", "test1@socketmail.gr", "Test subject (2)", "This is a test email (2)");
            newEmail("test3@socketmail.gr", "test1@socketmail.gr", "Test subject (3)", "This is a test email (3)");
            newEmail("test1@socketmail.gr", "test2@socketmail.gr", "Test subject (1)", "This is a test email (1)");
            newEmail("test2@socketmail.gr", "test2@socketmail.gr", "Test subject (2)", "This is a test email (2)");
            newEmail("test3@socketmail.gr", "test2@socketmail.gr", "Test subject (3)", "This is a test email (3)");
            newEmail("test1@socketmail.gr", "test3@socketmail.gr", "Test subject (1)", "This is a test email (1)");
            newEmail("test2@socketmail.gr", "test3@socketmail.gr", "Test subject (2)", "This is a test email (2)");
            newEmail("test3@socketmail.gr", "test3@socketmail.gr", "Test subject (3)", "This is a test email (3)");
            
            
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        return serverSocket;
    }

    
    public Account tryAddAccount(String email, String password){
        return accounts.tryAddAccount(email, password);
    }

    public Account checkAccount(String email, String password){
        return accounts.checkAccount(email, password);
    }
    
    public boolean newEmail(String sender, String receiver, String subject, String mainBody){
        return accounts.newEmail(sender, receiver, subject, mainBody);
    }

    public List<Email> getEmails(String email){
        return accounts.getEmails(email);
    }

    public Email getEmail(String account, int emailId){
        return accounts.readEmailById(account, emailId);
    }
    
    public void deleteEmail(String account, int emailId){
        accounts.deleteEmailById(account, emailId);
    }
    
    public void unplugClientHandler(ClientHandler clientHandler) {

        clientHandlers.remove(clientHandler);

    }
    
    @Override
    public void run() {

        System.out.println("Server is on");
        System.out.println("Waiting for clients...");
        System.out.println("");

        while (true) {

            try {
                Socket socket = serverSocket.accept();
                System.out.println(socket.getRemoteSocketAddress() + " connected");

                ClientHandler ch = new ClientHandler(this, socket);
                new Thread(ch).start();
                clientHandlers.add(ch);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException, Exception {
        Thread server = new MailServer(DEFAULT_PORT);
        server.start();
    }

}
