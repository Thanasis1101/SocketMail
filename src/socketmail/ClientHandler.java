package socketmail;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientHandler implements Runnable {

    private MailServer server;
    private final Socket socket;
    private ObjectOutputStream outObject;
    private DataOutputStream out;
    private DataInputStream in;
    private Account loggedInAccount;

    public ClientHandler(MailServer server, Socket socket) {
        this.server = server;
        this.socket = socket;

        try {
            this.outObject = new ObjectOutputStream(socket.getOutputStream());
            this.out = new DataOutputStream(socket.getOutputStream());
            this.in = new DataInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void run() {

        try {

            int emailId;
            Account acc;
            Email requestedEmail;
            String requestType, email, password;

            while (true) {

                requestType = this.in.readUTF();

                switch (requestType) {

                    case Constants.LOGIN:

                        email = in.readUTF();
                        password = in.readUTF();

                        acc = server.checkAccount(email, password);

                        if (acc != null) { // Account was found
                            loggedInAccount = acc;
                            out.writeUTF(Constants.LOGIN_CORRECT);
                        } else {
                            out.writeUTF(Constants.LOGIN_WRONG);
                        }

                        break;

                    case Constants.REGISTER:

                        email = in.readUTF();
                        password = in.readUTF();

                        acc = server.tryAddAccount(email, password);

                        if (acc != null) { // Account was created
                            loggedInAccount = acc;
                            out.writeUTF(Constants.REGISTERED_SUCCESFULLY);
                        } else {
                            out.writeUTF(Constants.EMAIL_ALREADY_EXISTS);
                        }

                        break;

                    case Constants.NEW_EMAIL:

                        String sender = loggedInAccount.getEmail();
                        String receiver = in.readUTF();
                        String subject = in.readUTF();
                        String mainBody = in.readUTF();

                        boolean emailSent = server.newEmail(sender, receiver, subject, mainBody);

                        if (emailSent) {
                            out.writeUTF(Constants.EMAIL_SENT_SUCCESFULLY);
                        } else {
                            out.writeUTF(Constants.RECEIVER_NOT_FOUND);
                        }

                        break;

                    case Constants.SHOW_EMAILS:

                        List<Email> emails = server.getEmails(loggedInAccount.getEmail());
                        outObject.reset();
                        outObject.writeObject(emails);
                        outObject.flush();

                        break;

                    case Constants.READ_EMAIL:

                        emailId = Integer.parseInt(in.readUTF());
                        requestedEmail = server.getEmail(loggedInAccount.getEmail(), emailId);
                        outObject.reset();
                        outObject.writeObject(requestedEmail);
                        outObject.flush();

                        break;

                    case Constants.DELETE_EMAIL:

                        emailId = Integer.parseInt(in.readUTF());
                        server.deleteEmail(loggedInAccount.getEmail(), emailId);

                        break;

                }
            }

        } catch (Exception e) {

            System.out.println(socket.getRemoteSocketAddress() + " disconnected");
            server.unplugClientHandler(this);

            // Close connection            
            try {
                socket.close();
                in.close();
                out.close();
                outObject.close();
            } catch (IOException ex) {
                Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

}
