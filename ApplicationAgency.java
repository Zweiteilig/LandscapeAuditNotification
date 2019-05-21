import java.io.File;
public interface ApplicationAgency
{
public String getSmtpPort( );
public String getSmtpHost( );    
public String getMessageSender();
public String getMessageSubject();
public String getMessageRecipient( );
public String getMessageCCRecipients( );
public java.util.ArrayList getAllMessageRecipients( );
public boolean hasBeenProcessedOnSchedule(File processPath);
};