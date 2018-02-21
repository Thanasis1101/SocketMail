package socketmail;

import java.io.Serializable;

/**
 *
 * @author Thanasis
 */
public class Email implements Serializable {

    private boolean isNew;
    private String sender;
    private String receiver;
    private String subject;
    private String mainBody;

    public Email(String sender, String receiver, String subject, String mainBody) {
        this.sender = sender;
        this.receiver = receiver;
        this.subject = subject;
        this.mainBody = mainBody;
        isNew = true;
    }

    public Email(String sender, String receiver, String mainBody) {
        this(sender, receiver, "", mainBody);
    }

    public boolean isNew(){
        return isNew;
    }
    
    public String getSender(){
        return sender;
    }
    
    public String getSubject(){
        return subject;
    }
    
    public String getMainBody(){
        return mainBody;
    }
    
    public void read(){
        this.isNew = false;
    }

}
