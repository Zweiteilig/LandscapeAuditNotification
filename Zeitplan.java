import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

// The application schedule.
public class Zeitplan 
{
  private static int DELAY_IN_SEC;

  private static int DELAY_AS_MIN;


  public Zeitplan(final String inFile, final String outFile, final ApplicationAgency appAgency)
  {
    
    Runnable runnable = new Runnable() 
    {
      public void run() {

        Deutung deutung = new Deutung(inFile, outFile); 

        deutung.updateNotificationMessage( deutung.getMessage() );  
            try
            {
                javax.mail.Transport transport = null;               
                java.io.File f = new java.io.File(inFile);
                Nachrichtenrelay nSesNotificationRelay = null; 
                javax.mail.internet.MimeMessage message = null;    
                
                // Scheduling the primary business rule.                
                if(!appAgency.hasBeenProcessedOnSchedule(f))
                {

                    nSesNotificationRelay = new Nachrichtenrelay(
                      appAgency.getSmtpHost(), 
                      new Integer(appAgency.getSmtpPort()).intValue()
                    );
                 
                    /*message = nSesNotificationRelay.createNewEmail(
                      appAgency.getMessageRecipient(), 
                      appAgency.getMessageSender(), 
                      appAgency.getMessageCCRecipients(), 
                      appAgency.getMessageSubject(), 
                      deutung.getMessage( )
                    ); */
   
			message = nSesNotificationRelay.createNewEmail(
				appAgency.getMessageSender(),  							appAgency.getAllMessageRecipients(), 
				appAgency.getMessageSubject(),  						deutung.getMessage( )	
                    );

                    nSesNotificationRelay.issueNotification( 
                      transport, 
                      message 
                    );                

                 } 
            } 
            catch( Exception e )
            {
                e.printStackTrace();
            }        
      }
    };
    ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
    service.scheduleAtFixedRate(runnable, 0, Zeitplan.getDelayAsMinutes(), TimeUnit.MINUTES);   
    return;   
  }


  public static void setDelayInSeconds( int delayInSeconds )  
  {
     DELAY_IN_SEC = delayInSeconds;
  }

  public static void setDelayAsMinutes( int delayAsMinutes )  
  {
     DELAY_AS_MIN = delayAsMinutes;
  }

  public static int getDelayInSeconds( )  
  {
     return DELAY_IN_SEC ;
  }

  public static int getDelayAsMinutes( )  
  {
     return DELAY_AS_MIN ;
  }

};