import java.util.ArrayList;
import java.text.SimpleDateFormat;

public class LandscapePoliteCommunication
{   
    private  Integer nErrors;
    private  String ActivityLog;
    private  ArrayList Portals;    
    private static int DATE = 0;
    private static int PORTAL = 1;
    private static int MESSAGE = 2;      
    private  String CommunicationDate;  
    private  StringBuffer politeCommunication;
    private java.util.HashSet portalSet = null;
    private static final String MessageDateFormat = "yyyy-MM-dd HH:mm:ss";

    public LandscapePoliteCommunication( )
    {
        portalSet = new java.util.HashSet();
        this.politeCommunication = new StringBuffer();
        SimpleDateFormat format = new SimpleDateFormat(
            LandscapePoliteCommunication.MessageDateFormat);
        CommunicationDate = format.format(new java.util.Date());
    }


    public LandscapePoliteCommunication(String eventCsv)
    {        
        if( eventCsv == null )
        this.politeCommunication = new StringBuffer();

        portalSet = new java.util.HashSet();
        SimpleDateFormat format = new SimpleDateFormat(
            LandscapePoliteCommunication.MessageDateFormat);
        CommunicationDate = format.format(new java.util.Date());
    }


    public String readRawMessage(String rawMsg)
    {
            String messageEvent = "";            
            getPortalFromContent( rawMsg );
            return messageEvent;
    }    


    public String readRawMessage(java.util.Vector msgElements)
    {   
        int emptyCount = 0;

        int mSize = msgElements.size();

        setErrorCount(msgElements.size());

        java.util.Enumeration ease = null;

        java.lang.StringBuffer msgBuffer = null;

        if(msgElements.size() > emptyCount) 
        {
            ease = msgElements.elements();  

            msgBuffer = new java.lang.StringBuffer ();          
        }
        
        if(mSize==1)
        {
            String messageEvent = 
                (String)msgElements.get(0);            
            getPortalFromContent( messageEvent );
            msgBuffer.append(" " +messageEvent+ " ");
            return msgBuffer.toString();
        }

        while(ease.hasMoreElements())
        {
            String messageEvent = 
                (String)ease.nextElement();
            getPortalFromContent( messageEvent );
            msgBuffer.append(" " +messageEvent+ " ");
        }

        return msgBuffer.toString();
    } 


    public String writePoliteMessage( )
    {
        String politeMsg = "";
        Integer errCountInt = null;
        String prepped = "Errors on ";
        String politeEnding = "\n\nThanks.\n";
        String logSource = "\nlog: " + 
            getLogSource( ) + "\n";

        //Date;
        politeCommunication.append(CommunicationDate);

        //Error count;
        errCountInt = new 
            Integer(getErrorCount());
            politeCommunication.append("\nPortal Team;\nThere are "+ 
            errCountInt.toString()+" errors (reported).\n");
        
        //Log source;
        politeCommunication.append(logSource);

        //The portals;
        politeCommunication.append(prepped + portalSet.toString());
      
        //Coda;
        politeCommunication.append(politeEnding);

        if(this.politeCommunication==null) 
            return "";
            
        System.out.println( politeCommunication.toString() );

        return this.politeCommunication.toString();
    }


    // Include the log source location, Requirement.
    public String getLogSource( )
    {
        return this.ActivityLog;
    }


    // Include the log source location, Requirement.
    public boolean setLogSource( String sourceFile )
    {
        boolean bExists = true;

        java.io.File bFile = null;
        
        try 
        {
         // create new files
         bFile = new java.io.File(sourceFile);
         
         /* Create a new file.
         bFile.createNewFile(); */

         bExists = bFile.exists();
  
         if(bExists == true) {
             this.ActivityLog = sourceFile;
            return bExists;
         }
         
      } catch(Exception e) 
      {
         // if any error occurs
         e.printStackTrace();
      } 

      return false;
    }

    // Include the number of errors, Requirement.
    public void setErrorCount(int errCount)
    {
        this.nErrors = errCount;
    }
    

    // Include the number of errors, Requirement.
    public int getErrorCount()
    {
        return this.nErrors;
    }


    // Include the timestamp, Requirement.
    public String getPoliteDateStamp ()
    {
        String sds = "<br>Message time, " + 
            this.CommunicationDate + "<br>";
        return sds;
    }


    public int setEventContent( String eventCsv )
    {
        int bytesInMessage = -1;
        return bytesInMessage;
    }


    // Include the portal, Requirement.
    public  java.lang.String getPortalFromContent( String eventCsv )
    {
        String portalID = ""; 

        java.util.List<String> result = java.util.Arrays.asList(eventCsv.split("\\s*;\\s*"));
 
        portalID = (String)result.get(LandscapePoliteCommunication.PORTAL);

        portalSet.add(portalID);

        return portalID;
    }

    public static void main( String[] args )
    {
        java.util.Vector v = new java.util.Vector();

        String mainString = "3/20/2017 14:00; dp03; " +
            "Test Status:ERROR:Check User authentication failed";
        String additionalString = "5/30/2017 14:00; sz01; " +
            "Test Status:ERROR:Check User authentication failed";            

        v.add(mainString);

        v.add(additionalString);

        //Create the Date information;
        LandscapePoliteCommunication landscapePoliteCommunication = new
            LandscapePoliteCommunication();
        
        //Identify the log source;
        landscapePoliteCommunication.setLogSource(
            "C:\\Mesh\\BlueSky\\Agent\\PortalAgent\\PEZ\\recent.csv");

        //Inventory the log events;
        landscapePoliteCommunication.readRawMessage( v ) ;

        //Create the message;
        landscapePoliteCommunication.writePoliteMessage();

        return;
    }
};
