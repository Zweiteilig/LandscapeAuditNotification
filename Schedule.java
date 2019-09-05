import java.util.logging.Logger;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class Schedule {

 private static long SSTATUS = 1;

 private static int DELAY_IN_SEC;

 private static int DELAY_AS_MIN;

 private final static java.util.logging.Logger LOGGER = Logger.getLogger(Schedule.class.getName());

 public Schedule(final String evaluateRunnerRecord, final String auditMsgFile, final ApplicationAgency appAgency) 
 {

  Runnable runnable = new Runnable() 
  {
   public void run() {

    long noteSize = -1L;

    String bString = "";

    String hostsNotification = "";

    java.util.Hashtable urgentNotification = null;

    ServiceCoordination triage = new ServiceCoordination(appAgency);

    Audit audit = new Audit(evaluateRunnerRecord, auditMsgFile, triage);

    urgentNotification = triage.getUrgentHostsTable();

    if (urgentNotification.size() > 0) {
        hostsNotification = "\nUrgent: " + urgentNotification.size() +
      " Unavailable portal(s)! " + urgentNotification.toString() + "\n\n" + audit.getMessage();
    } else {
     hostsNotification = audit.getMessage();
    }

    bString = audit.updateReviewNotification(hostsNotification);

    if (bString.equals("") == true) {
     LOGGER.info("Results have no bytes. No info to email.");
     return;
    } else {
      LOGGER.info("Processing info to email...");     
    }

    try {
     javax.mail.Transport transport = null;
     java.io.File f = new java.io.File(evaluateRunnerRecord);
     MailTransfer nSesNotificationRelay = null;
     javax.mail.internet.MimeMessage message = null;

     nSesNotificationRelay = new MailTransfer(
      appAgency.getSmtpHost(),
      new Integer(appAgency.getSmtpPort()).intValue()
     );

     appAgency.getAllMessageRecipients().addAll(
      appAgency.getUrgenNotificationRecipients()
     );

     message = nSesNotificationRelay.createNewEmail(
      appAgency.getMessageSender(), appAgency.getAllMessageRecipients(),
      appAgency.getMessageSubject(),
      audit.getMessage()
     );

     LOGGER.info("Issuing Notification... to " + appAgency.getAllMessageRecipients() );

     nSesNotificationRelay.issueNotification(
      transport,
      message
     );
    
    } 
    catch (Exception e) {
     LOGGER.info("Notification Failed");
    }
   }

  };
  ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
  service.scheduleAtFixedRate(runnable, 0, Schedule.getDelayAsMinutes(), TimeUnit.MINUTES);
  return;
 }


 public static void setDelayInSeconds(int delayInSeconds) {
  DELAY_IN_SEC = delayInSeconds;
 }

 public static void setDelayAsMinutes(int delayAsMinutes) {
  DELAY_AS_MIN = delayAsMinutes;
 }

 public static int getDelayAsMinutes() {
  return DELAY_AS_MIN;
 }

};