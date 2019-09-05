import java.io.File;
public interface ApplicationAgency
{
public String getSmtpPort( );
public String getSmtpHost( );    
public String getMessageSender();
public String getMessageSubject();
public String getMessageRecipient( );
public String getMessageCCRecipients( );
public String getLastAuditEvent( );
public String getUrgencyPortals( );
public void setLastAuditEvent(String lastTime);
public Object getConfigProperty( String key );
public java.util.ArrayList getAllMessageRecipients( );
public java.util.ArrayList getUrgenNotificationRecipients();
public boolean hasBeenProcessedOnSchedule(File processPath);
};