import java.util.List;
import java.util.ArrayList;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/* May 17th, TESTED on AWS. */
class Nachricht
{
    private long sStartTime;
    private String noteTo;
    private String noteFrom;
    private String fromName;
    private String noteSubject;
    private String recipient;         
    private String noteContent;
    private String noteAttachment; 
    private String deliveryErrors;
    private Message noteMessage = null;
    private MimeMessage mimeMessage = null; 
    private List<String> ccRecipient;
    private List<String> theEntireRecipientList;
    private String ccRecipientCSV;

    public Nachricht(String to, String from, String subject)
    {
        this.noteTo = to;
        this.noteFrom = from;
        this.noteSubject = subject;          
        return;
    }


    public Nachricht( String to, String from, String csvRecipients, String subject, String message )
    {
        if(to==null) this.noteTo = "hwestmoreland@protonmail.com";
        else this.noteTo = to;

        if(subject==null) this.noteSubject = " System Notification ";
        else this.noteSubject = subject;          

        if(from==null) this.noteFrom = "hwestmoreland@protonmail.com";
        else this.noteFrom = from;
        
        if(message==null) this.noteContent = " System Notification alert ... ";
        else this.noteContent = message;  
        
        if(csvRecipients==null) this.ccRecipientCSV = "";        
        else this.ccRecipientCSV = csvRecipients;

        System.out.println("\tSystem Administration Notification Stats");
        System.out.println("\tnoteTo: " + this.noteTo);
        System.out.println("\tnoteSubject: " + this.noteSubject);
        System.out.println("\tnoteFrom: " + this.noteFrom);
        System.out.println("\tnoteContent: " + this.noteContent);
        System.out.println("\tccRecipientCSV: " + this.ccRecipientCSV);
        return;
    }


    public Nachricht( java.util.ArrayList toList, String from, String subject, String message )
    {
        if(subject==null) this.noteSubject = " System Notification ";
        else this.noteSubject = subject;          

        if(from==null) this.noteFrom = "hwestmoreland@protonmail.com";
        else this.noteFrom = from;
        
        if(message==null) this.noteContent = " System Notification alert ... ";
        else this.noteContent = message;  
        
        if(toList==null) this.theEntireRecipientList = new java.util.ArrayList();        
        else setRecipientList(toList);

        System.out.println("\tSystem Administration Notification Stats");
        System.out.println("\tnoteSubject: " + this.noteSubject);
        System.out.println("\tnoteFrom: " + this.noteFrom);
        System.out.println("\tnoteContent: " + this.noteContent);
        System.out.println("\ttheEntireRecipientList: " + this.theEntireRecipientList);
        return;
    }


    public void setNotificationContent(String content)
    {
        if(content==null) this.noteContent = " System Notification without content ";
        else this.noteContent = content;          
    }
    
    public String getNotificationContent()
    {
        return this.noteContent;
    }

    public int setSubject( String subject )
    {
        if(subject != null)
        this.noteSubject = subject.trim();
        else this.noteSubject = "System Email";
        return this.noteSubject.length();
    }

    public String getSubject( )
    {
        if(noteSubject == null)
            return "System Email";

        return this.noteSubject ;
    }

    public void setNotificationMessage( Message msg )
    {
        this.noteMessage = msg;
    }

    public Message getNotificationMessage( )
    {
        return this.noteMessage ;
    }


/**
* Return the list of recipients on the list.
*
* @return the recipients of the list
*/
public InternetAddress[] getRecipientList( )
{
    int lsize = 0;
    int counter = 0;
    if(this.theEntireRecipientList != null)
        lsize = this.theEntireRecipientList.size();

    InternetAddress[] recipientAddress = new InternetAddress[lsize];

    try{
        for (String recipient : theEntireRecipientList) {
        recipientAddress[counter] = new InternetAddress(recipient.trim());
        counter++;
        }
    } 
    catch( Exception e ) 
    {
        e.printStackTrace();
    }   
   
    return recipientAddress;
}


/**
* Create a "new" list of recipients.
* Return the number of recipients on the list.
* 
 * @return the size of the list
*/
public int setRecipientList( List<String> all )
{
   int rListSize = 0;

    this.theEntireRecipientList = new 
        java.util.ArrayList<String>(); 

   if(all==null || all.size()==0) 
   {
       return rListSize ;
   }    

    for (int i=0; i < all.size() ; i++)
              {
        String to = "";
        to = all.get(i).toString();
        System.out.println("\tTO: " + to);

        if(to.indexOf("@") > 0) 
        {            
            System.out.println("\tAdding: " + to);
            this.theEntireRecipientList.add(to);
        }
    }

  return this.theEntireRecipientList.size();
}

    /**
    * @return the from object in the Notification.
    */
    public java.lang.String getEmailTo( ){
        return  this.noteTo;  
    }

    /**
    * @return the from object in the Notification.
    */
    public  void setEmailTo(String to) {
        this.noteTo = to;
        return;
    }

    /**
    * @return the user's name in the Notification.
    */
    public  String getFromName( )
    {
        return this.fromName;
    }

    public void setFromName( String fromName )
    {
        this.fromName = fromName;
    }

    /**
    * @return the from object in the Notification.
    */
    public java.lang.String getEmailFrom( ){
        return  this.noteFrom;  
    }

    /**
    * @return the from object in the Notification.
    */
    public  void setEmailFrom(String from) {
        this.noteFrom = from;
        return;
    }


    /**
    * @return the from object in the NotificationUser.
    */
    public  void setStartTime(long startSystemClock) {
        this.sStartTime = startSystemClock;
        return;
    }


    /**
    * @return the from object in the NotificationUser.
    */
    public long getEndTime() {
        return  System.currentTimeMillis();
    }

};


public class Nachrichtenrelay
{   
    String host = null;
    Session session = null;
    MimeMessage msg = null;
    Properties props = null;
    Transport transport = null;
    Nachricht Nachricht = null;

    // Replace smtp_username with your Amazon SES SMTP user name.
    public static final String SMTP_USERNAME = "AKIAUSTAYHWB3J4DVSEH";
    
    // Replace smtp_password with your Amazon SES SMTP password.
    public static final String SMTP_PASSWORD = "BF18l4BADCB0ldrML/BscCtwlDf/KrB5zsFNGkWhfTb8";
    
    // Amazon SES SMTP host name. This example uses the US West (Oregon) region.
    public static final String HOST = "email-smtp.us-east-1.amazonaws.com";
    
    // The port you will connect to on the Amazon SES SMTP endpoint. 
    public static final int PORT = 587;

  
  public Nachrichtenrelay(final String host, final int P)
  {
      this.host = host;
      props = System.getProperties();
              props.put("mail.smtp.port", P); 
        props.put("mail.smtp.auth", "true"); 
        props.put("mail.transport.protocol", "smtp"); 
              props.put("mail.smtp.starttls.enable", "true");
        this.session = Session.getDefaultInstance(props); 
       return;
  }

  public String getHostName(  )
  {
      return this.host;
  }

  public String setHostName( final String hostname )
  {
      this.host = hostname ;
      return this.host;
  }  
  

/*Creat the message with 4 arguments.*/  
public MimeMessage createNewEmail(String from, java.util.ArrayList recipients, String subject, String message) 
  {    
    Nachricht nachricht = null;

    System.out.println("\n\tcreateNewEmail");
    
    try
    {
        this.msg = new MimeMessage(this.session);   

        nachricht = new Nachricht( recipients, from, 
            subject, message 
            );

        nachricht.setRecipientList( recipients );

        this.msg.setRecipients(Message.RecipientType.TO,
            nachricht.getRecipientList()
            );

        this.msg.addRecipients( Message.RecipientType.TO, 
            nachricht.getRecipientList() ) ;

        this.msg.setFrom(new InternetAddress(nachricht.getEmailFrom(),
            nachricht.getFromName())
            );

        this.msg.setContent(nachricht.getNotificationContent(),"text/html");

        this.msg.setSubject(nachricht.getSubject());
        
        System.out.println("\t-- Message Detail -- "+
            "\n\t"+"from: " + from +
            "\n\t"+"recipients: " + nachricht.getRecipientList().toString() +
            "\n\t"+"subject: " + subject +
            "\n\t"+"message: " + message +
            "\n\t--  -- "
        );

        this.transport = session.getTransport();
    }
    catch( Exception e )
    {
        e.printStackTrace();
    }
    
    return this.msg;
  }


/* Create the message with 5 arguments.*/
public MimeMessage createNewEmail(String to, String from, String recipients, String subject, String message) 
  {    
    Nachricht nachricht = null;

    try
    {
        this.msg = new MimeMessage(this.session);   

        nachricht = new Nachricht( to, from, recipients, 
            subject, message 
            );
        
        this.msg.setRecipient(Message.RecipientType.TO,
            new InternetAddress(nachricht.getEmailTo())
            );

        this.msg.setFrom(new InternetAddress(nachricht.getEmailFrom(),
            nachricht.getFromName())
            );

        this.msg.setContent(nachricht.getNotificationContent(),"text/html");

        this.msg.setSubject(nachricht.getSubject());
        
        System.out.println("\tMessage Detail, "+"to: " + to +
        "\n\t"+"from: " + from +
        "\n\t"+"recipients: " + recipients +
        "\n\t"+"subject: " + subject +
        "\n\t"+"message: " + message
        );

        this.transport = session.getTransport();
    }
    catch( Exception e )
    {
        e.printStackTrace();
    }
    
    return this.msg;
  }


  public String sendNotificationTo(String to)
  {
      String notificationTo = "";
      return notificationTo;
  }


  public String sendNotificationFrom(String from)
  {
      String notificationFrom = "";
      return notificationFrom;
  }  
    
  public Session getSmtpSession( )
  {
    Session session = Session.getDefaultInstance(props);
    return session;
  } 

  
  public boolean issueNotification( Transport transport, MimeMessage message )
  {
      boolean issued = true;

        try
        {
            System.out.println("\n\tIssue Notification to the following: " + message.getAllRecipients());
            transport = getSmtpSession().getTransport();
            transport.connect(getHostName(), 
            Nachrichtenrelay.SMTP_USERNAME, 
            Nachrichtenrelay.SMTP_PASSWORD);
              
            // Send the email.
            transport.sendMessage(message, message.getAllRecipients());
            System.out.println("\tEmail sent;");
        }
        catch (Exception ex) 
        {
            System.out.println("The email was not sent.");
            System.out.println("Error message: " + ex.getMessage());
        }
        finally
        {
            try{ transport.close(); }
            catch( Exception t )
            {t.printStackTrace();}
        }

    return issued;
  }


  public static void main(String[] args) throws Exception 
  {
        Transport transport = null;
        MimeMessage message = null;
        java.util.ArrayList toEveryOne = new ArrayList();
        toEveryOne.add("hwestmoreland@protonmail.com");
        toEveryOne.add("2028703515@tmomail.net");
        toEveryOne.add("h.westmoreland.cloud@mail.com");

        Nachrichtenrelay mainNachrichtRelay = new Nachrichtenrelay(
            "email-smtp.us-east-1.amazonaws.com", 587
                );

        message = mainNachrichtRelay.createNewEmail("hwestmoreland@protonmail.com", 
                toEveryOne, "It's a time", 
                "flipped"); 
         
        mainNachrichtRelay.issueNotification( transport, message );
        
      return;
    }

};
