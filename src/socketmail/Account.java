package socketmail;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Thanasis
 */
public class Account {

    private final String email;
    private String password;
    private final List<Email> mailBox;

    public Account(String email, String password) {
        this.email = email;
        this.password = password;
        mailBox = new ArrayList<>();
    }

    public void addEmail(Email email) {
        mailBox.add(email);
    }

    public String getEmail() {
        return email;
    }

    public List<Email> getMailBox() {
        return mailBox;
    }

    public boolean changePassword(String oldPassword, String newPassword) {
        if (password.equals(oldPassword)) {
            password = newPassword;
            return true;
        }
        return false;
    }

    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }

}
