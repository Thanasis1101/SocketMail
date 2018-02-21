package socketmail;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 *
 * @author Thanasis
 */
public class SocketMail {

    private static Scanner scanner;
    private static String email;
    private static String password;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        if (args.length != 2) {
            System.out.println("Wrong parameters. Expected 2 parameters.");
            System.exit(0);
        }

        String host = args[0]; // First parameter -> Host IP
        int port = 0;
        try {
            port = Integer.valueOf(args[1]); // Second parameter -> Server's port
        } catch (NumberFormatException e) {
            System.out.println("The second parameter must be an integer.");
            System.exit(0);
        }

        int option;
        scanner = new Scanner(System.in);

        System.out.println("***********************************");
        System.out.println("*                                 *");
        System.out.println("*      Welcome to SocketMail      *");
        System.out.println("*                                 *");
        System.out.println("***********************************");

        while (true) {

            System.out.println("");
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.println("3. Exit");
            System.out.println("");
            System.out.println("Type the number of your option");

            option = readOption(3);

            if (option == 3) { // ============ EXIT ============ //
                System.exit(0);
            }

            // Try to connect to server
            boolean connected = Client.connectToServer(host, port);
            if (!connected) {
                System.out.println("There is a problem with the server. Try again Later");
                System.exit(0);
            }

            // Perform login or register
            switch (option) {
                case 1: // ============ LOGIN ============ //
                    performLogin();
                    break;
                case 2: // ============ REGISTER ============ //
                    performRegistration();
                    break;

            }

            boolean loop = true; // it will become false on logout

            while (loop) {

                System.out.println("");
                System.out.println("1. New Email");
                System.out.println("2. Show Emails");
                System.out.println("3. Read Email");
                System.out.println("4. Delete Email");
                System.out.println("5. Logout");
                System.out.println("6. Exit");
                System.out.println("");
                System.out.println("Type the number of your option");

                option = readOption(6);

                if (option == 6) { // ============ EXIT ============ //
                    Client.closeConnection();
                    System.exit(0);
                }

                switch (option) {
                    case 1: // ============ NEW EMAIL ============ //
                        sendEmail();
                        break;
                    case 2: // ============ SHOW EMAILS ============ //
                        showEmails();
                        break;
                    case 3: // ============ READ EMAIL ============ //
                        readEmail();
                        break;
                    case 4: // ============ DELETE EMAIL ============ //
                        deleteEmail();
                        break;
                    case 5: // ============ LOGOUT ============ //
                        loop = false;
                        break;
                }

            }
        }

    }

    // Read option while checking it is a valid option
    private static int readOption(int numOfOptions) {
        int option;
        while (true) {
            try {
                option = scanner.nextInt();
                if (option < 1 || option > numOfOptions) {
                    System.out.println("Your option must be an integer between 1 and " + numOfOptions);
                    System.out.println("Type the number of your option");
                    scanner.nextLine(); // to clear the scenner
                } else {
                    break;
                }
            } catch (InputMismatchException e) {
                System.out.println("Your option must be an integer");
                System.out.println("Type the number of your option");
                scanner.nextLine(); // to clear the scenner
            }
        }
        scanner.nextLine(); // to clear the scenner
        return option;
    }

    private static void performRegistration() {

        System.out.println("");
        System.out.println("***********************************");
        System.out.println("****     Registration Form     ****");
        System.out.println("***********************************");
        System.out.println("");

        while (true) {

            System.out.println("Choose your email");
            email = scanner.nextLine();
            System.out.println("Choose your password");
            password = scanner.nextLine();

            boolean registered = Client.register(email, password);

            if (registered) {
                System.out.println("Registered succesefully !!!");
                break;
            } else {
                System.out.println("This email is already in use. Try again.");
            }
        }

    }

    private static void performLogin() {
        System.out.println("");
        System.out.println("***********************************");
        System.out.println("*****       Login Form        *****");
        System.out.println("***********************************");
        System.out.println("");

        while (true) {

            System.out.println("Type your email");
            email = scanner.nextLine();
            System.out.println("Type your password");
            password = scanner.nextLine();

            boolean logged = Client.login(email, password);

            if (logged) {
                System.out.println("Welcome back " + email);
                break;
            } else {
                System.out.println("Email or password is incorrect. Try again.");
            }
        }

    }

    private static void sendEmail() {

        System.out.println("");
        System.out.println("***********************************");
        System.out.println("*****       New Email         *****");
        System.out.println("***********************************");
        System.out.println("");

        System.out.println("Type the receiver's email");
        String receiver = scanner.nextLine();
        System.out.println("Type the subject of the email");
        String subject = scanner.nextLine();
        System.out.println("Type the main body of the email");
        String mainBody = scanner.nextLine();

        boolean emailSent = Client.sendEmail(receiver, subject, mainBody);

        if (emailSent) {
            System.out.println("Email was sent Succesfully");
        } else {
            System.out.println("The receiver does not exist. Try again.");
        }

    }

    // Shows the emails and returns the number of the emails (this is used by readEmail())
    private static int showEmails() {

        System.out.println("");
        System.out.println("***********************************");
        System.out.println("*****       Your Emails       *****");
        System.out.println("***********************************");
        System.out.println("");

        ArrayList<Email> emails = Client.showEmails();

        if (emails != null && !emails.isEmpty()) {
            System.out.format("%-5s%-8s%-30s  %-50s%n", "Id", "", "From", "Subject");

            int i = 0;

            for (Email email : emails) {

                String emailIsNew = "";
                if (email.isNew()) {
                    emailIsNew = "[NEW]";
                }

                System.out.format("%-5s%-8s%-30s  %-50s%n", (++i) + ".", emailIsNew, email.getSender(), email.getSubject());
            }

            return i;

        } else {
            System.out.println("You have no emails.");
            return 0;
        }

    }

    private static void readEmail() {

        int numOfEmails = showEmails();

        if (numOfEmails == 0) {
            return;
        }

        System.out.println("");
        System.out.println("***********************************");
        System.out.println("*****       Read Email        *****");
        System.out.println("***********************************");
        System.out.println("");

        System.out.println("Type the id of the email you want to read");

        int emailId = readOption(numOfEmails);

        Email requestedEmail = Client.readEmail(emailId);

        System.out.println("");
        System.out.println("From: " + requestedEmail.getSender());
        System.out.println("Subject: " + requestedEmail.getSubject());
        System.out.println("");
        System.out.println(requestedEmail.getMainBody());

    }

    private static void deleteEmail() {

        int numOfEmails = showEmails();

        if (numOfEmails == 0) {
            return;
        }

        System.out.println("");
        System.out.println("***********************************");
        System.out.println("*****      Delete Email       *****");
        System.out.println("***********************************");
        System.out.println("");

        System.out.println("Type the id of the email you want to delete");

        int emailId = readOption(numOfEmails);

        Client.deleteEmail(emailId);

        System.out.println("Email deleted succesfully");

    }

}
