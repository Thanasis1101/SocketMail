package socketmail;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Thanasis
 */
public class AccountsList {
    
    private final List<Account> accounts;
    
    public AccountsList(){
        accounts = new ArrayList<>();
    }
    
    // Sends the new email if the receiver exists
    public boolean newEmail(String sender, String receiver, String subject, String mainBody){
        Account receiverAccount = getAccountByEmail(receiver);
        if (receiverAccount != null){
            receiverAccount.addEmail(new Email(sender, receiver, subject, mainBody));
            return true;
        }
        return false;
    }
    
    // Returns all emails for the account with the given email
    public List<Email> getEmails(String email){
        Account account = getAccountByEmail(email);
        if (account != null){
            return account.getMailBox();
        }
        return null;
    }
    
    
    // Returns the email with the given emailId for the account with the given email
    public Email readEmailById(String email, int emailId){
        Account account = getAccountByEmail(email);
        if (account != null){
            account.getMailBox().get(emailId-1).read(); // Mark as read (-1 because id starts from 0)
            return account.getMailBox().get(emailId-1);
        }
        return null;
    }
    
    // Deletes the email with the given emailId for the account with the given email
    public void deleteEmailById(String email, int emailId){
        Account account = getAccountByEmail(email);
        if (account != null){
            account.getMailBox().remove(emailId-1); // -1 because id starts from 0
        }
    }
    
    
    // Adds account if email not already exists
    public Account tryAddAccount(String email, String password){
        if (!accountExists(email)){
            Account newAccount = new Account(email, password);
            accounts.add(newAccount);
            return newAccount;
        }
        return null;
    }
    
    public Account checkAccount(String email, String password){
        for (Account account : accounts){
            if (account.getEmail().equals(email) && account.checkPassword(password)){
                return account;
            }
        }
        return null;
    }
    
    // Checks if email is used by any account
    public boolean accountExists(String emailToCheck){
        for (Account account : accounts){
            if (emailToCheck.equals(account.getEmail())){
                return true;
            }
        }
        return false;
    }
    
    // Returns the account object with the email given, or null if not found
    public Account getAccountByEmail(String email){
        for (Account account : accounts){
            if (email.equals(account.getEmail())){
                return account;
            }
        }
        return null;
    }
    
    
}
