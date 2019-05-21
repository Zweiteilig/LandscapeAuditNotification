import java.util.*;
import java.lang.*;
import java.net.*;
import java.sql.*;
import java.io.*;

public class Agent implements ApplicationAgency
{

private Zeitplan Zeitplan ;
static String VERBOSITY = "TRUE";
Properties agentProp = new Properties();
java.util.ArrayList ocspResponderList = null;
static final String myDateLayout = "MM-dd-yyyy HH:mm:ss";

//-------------------------
// The "Agent" 
//           constructors
// 
//    Read the event log and compose a message.
//    Apr 30th, 2019;
//-------------------------
public Agent()
{    
   
    loadConfiguredProperties();
    
    Zeitplan.setDelayAsMinutes(
        new Integer(getDelayAsMinutes()).intValue()
        );
    
    Zeitplan = new Zeitplan(
        agentProp.getProperty("EVENTSLOG"),
        agentProp.getProperty("MSGFILE"),
        this);

    return;
}


private static void cout( String log )
{
  System.out.println("\t["+log+"]");  
  return;
}

//-------------------------
// methods
//-------------------------
public String getDelayInSeconds( ){
    if( agentProp == null || agentProp.getProperty("DelayInSeconds") == null) 
    cout("\tCould not load the DelayInSeconds property.");
    return agentProp.getProperty("DelayInSeconds") ;
}


public String getDelayAsMinutes( ){
    if( agentProp == null || agentProp.getProperty("DelayAsMinutes") == null) 
    cout("\tCould not load the DelayAsMinutes.");
    return agentProp.getProperty("DelayAsMinutes") ;
}


private String getSystemClockTranslation(long cTime)
{
       String lastModifiedTimeString = "";
       java.util.Calendar cal = java.util.Calendar.getInstance();
       cal.setTimeInMillis(cTime);

       lastModifiedTimeString = new java.text.
        SimpleDateFormat(Agent.myDateLayout).
            format(cal.getTime());

       return lastModifiedTimeString;
} 


private int getLastModifiedInSecondsAgo(File file)
{
    Long approxMinutesAgo = null;

    approxMinutesAgo = new Long(( 
        System.currentTimeMillis() - file.lastModified() 
        ) /1000 );
    
    System.out.println("\tLastModifiedInSecondsAgo: " + 
        approxMinutesAgo.toString()
    );

    return  approxMinutesAgo.intValue();
}


private int getLastModifiedAsMinutesAgo(File file)
{
    Long approxMinutesAgo = null;

    approxMinutesAgo = new Long(( 
        System.currentTimeMillis() - file.lastModified() 
        ) /1000 /60);
    
    System.out.println("\tLastModifiedAsMinutesAgo: " + 
        approxMinutesAgo.toString()
    );

    return  approxMinutesAgo.intValue();
}


// contract method to read agentProp.getProperty("PORT")
public String getSmtpPort( )
{
     String port = agentProp.getProperty("PORT");
     System.out.println("\tI want to getSmtpPort:" + port );
     return port;    
}


// contract method to read agentProp.getProperty("HOST")
public String getSmtpHost( )
{
     String smtpHost = agentProp.getProperty("HOST");
     System.out.println("\tI want to getSmtpHost:" + smtpHost );
     return smtpHost;    
}

// contract method to read agentProp.getProperty("MSG_FROM")
public String getMessageSender()
{
     String messageSender = agentProp.getProperty("MSG_FROM");
     System.out.println("\tI want to getMessageSender:" + messageSender);
     return messageSender;
}

// contract method to read agentProp.getProperty("MSG_SUBJECT")
public String getMessageSubject()
{
     String messageSubject = agentProp.getProperty("MSG_SUBJECT");
     System.out.println("\tI want to getMessageSubject:" + messageSubject );
     return messageSubject;
}


// contract method to get the "CC" field;
public String getMessageCCRecipients( )
{
    String [] items = null;

    String ccCommaSeparated = agentProp.getProperty("MSG_CC");
    
    System.out.println("\tI want to getMessageCCRecipients:" + ccCommaSeparated );

    items = ccCommaSeparated.split(",");

    for( int c = 0; c < items.length; c++) 
    {
        String cc = items[c];
         System.out.println("\tCCRecipient ["+cc+"] ");
    }

    return ccCommaSeparated;
}

// contract method to get the "All" field;
public java.util.ArrayList getAllMessageRecipients( )
{
    String [] items = null;

    java.util.ArrayList messageRecipients = new 
	java.util.ArrayList();
 	
    String ccCommaSeparated = agentProp.getProperty("MSG_TO");
    
    System.out.println("\tI want to get All Message Recipients:" 	+ ccCommaSeparated );

    items = ccCommaSeparated.split(",");

    for( int c = 0; c < items.length; c++) 
    {
        String cc = items[c];
         System.out.println("\tRecipient ["+cc+"] ");
	    messageRecipients.add(	cc );
    }

    return messageRecipients;
}


// contract method to get the "To" field;
public String getMessageRecipient( )
{
    String getTheMsg = "";
    getTheMsg = agentProp.getProperty("MSG_TO");
    System.out.println("\tI want to getMessageRecipient:" + getTheMsg );
    return getTheMsg;
}


// The primary business rule.
public boolean hasBeenProcessedOnSchedule(File processPath)
{
    int theDelayAsMinutes = 0;
    boolean isProcessedOnSchedule = true;
    theDelayAsMinutes = new Integer(getDelayAsMinutes()).intValue();
    System.out.println("\tThe DelayAsMinutes: "+ theDelayAsMinutes );

    if( getLastModifiedAsMinutesAgo(processPath) > theDelayAsMinutes )
    {
        System.out.println("\tThe "+processPath.getName() +
            " has NOT Been Processed on Schedule." );
        return false;
    }

    System.out.println("\tThe "+processPath.getName() +
    " hasBeenProcessedOnSchedule." );

    return isProcessedOnSchedule;
}


private int loadConfiguredProperties( ){
    long nthin = -1;
    InputStream input = null;    
    int thisConfiguredListSize = 0;

              try 
        {
                             input = new FileInputStream("Agent.properties");

          if(input !=null && input.available()>nthin)
          {  
            // load a properties file
                                 agentProp.load(input);
            VERBOSITY = agentProp.getProperty("VERBOSE").
                toString().trim();
          }
          else
          {
            return thisConfiguredListSize;
          }
        } 
        catch (IOException ex) {
                                cout("There was an error reading LandscapeEvent.properties. Check the config file.");
            System.exit(0);
                  } 
        finally 
        {
                                 if (input != null) {
                                           try {
                                                          input.close();
                                               } catch (IOException e) {e.printStackTrace();}
                                 }
                  }
        
return thisConfiguredListSize;        
}


// -- --- -----
// Main driver:
// -- --- -----
public static void main (String[] cmd) throws java.lang.Exception
{
        long sysClock = System.currentTimeMillis();
                             Agent Agent = new Agent( );
        return;
}

};


class ConsoleCommander
{
  private String[] consoleArgs = null;

  public ConsoleCommander(){
      System.out.println("[ConsoleCommander with no args]"); 
      return;
  }

  public ConsoleCommander(String[] cArgs){
      System.out.println("[ConsoleCommander "+cArgs.length+" args]"); 

      if(cArgs.length < 1) consoleArgs = new 
        String[cArgs.length];

        for ( int f = 0; f < cArgs.length; f++ )
        {
            System.out.println("["+cArgs[f]+"]");            
        }

      return;      
  }

   public java.util.List getArguments() {
       java.util.List argsList = null;

        //If there are command elements, then 
        //  initialize the list and add the 
        //  elements;
        if(consoleArgs.length>0) 
        argsList = new java.util.ArrayList();
        else return argsList;

        for ( int f = 0; f < argsList.size(); f++ )
        {
            argsList.add(consoleArgs[f]);
        }

       return argsList;
   }
};
