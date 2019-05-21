import java.io.*;
import java.util.*;

/* parse the event log */
public class Deutung 
{    
    String fHistoryNm;
    java.util.Vector veez;    
    boolean verboseLog = true;
    private String nwln = "\n";
    final String collectForMatch = "ERROR";    
    private final int CLOSE_CSV_POSITION = 2;        
    String msgNotificationFileName = "notifyForNow.msg"; 
              
              /**
              * Construct one;
              * @param s is the path to the CSV, may NOT be null
              */
    public Deutung(String inEventLogFile, String outEventsMsg)
    {
        if(!supportedExtension( inEventLogFile )) return;
        System.out.println("\t[Deutung with - "+inEventLogFile+" and "+outEventsMsg+"]");
        msgNotificationFileName = outEventsMsg;
        fHistoryNm = inEventLogFile;
        this.veez = new java.util.Vector();
        readHistory(fHistoryNm );        
        return;
    }
    

    private boolean supportedExtension( String fByName )
    {
        String support = null;
        boolean isSupported = true;
        String[] successSupport = {".csv", ".txt" };

        if( fByName == null || fByName.equals("")) 
        {    
            System.out.println("\t[Unsupported Extension - Error]");
            return false;
        }

        support = fByName.substring(fByName.indexOf("."))
            .toLowerCase().trim();
        
        System.out.println("\tWhat is this extension, " +support + "?\n");

         // Success: "csv" OR "txt"
        for(int i = 0; i< successSupport.length; i++)
        {
            String EXT = successSupport[i].trim();

            System.out.println("\tEXT is: " + EXT + "\n");

             if( support.equals(EXT) )
             {  
                
                System.out.println("\tsuccess is: " + successSupport[i] );
                
                return isSupported;
             }
            
        }

        return false;
    }


    public void readHistory( String fHistoryNm ) 
    {
        int row = 0 ;
              
        try
        {
            RandomAccessFile randomaccessfile = new RandomAccessFile(fHistoryNm, "r");
            long l = randomaccessfile.length();
            if(l==0) System.exit(0);
            
                                           do
            {
                String s = randomaccessfile.readLine();
                if( row > 0 ) collectDelimitedString(s, this.collectForMatch);
                row ++;
            } 
                                           while(randomaccessfile.getFilePointer() < l);
        }
                             catch(IOException ioexception) {  }

        return;
    }

              
private synchronized Vector collectDelimitedString(String s, String match) 
{
        int i = 0;
        int j = 0;        
        String slim = "";
        Object obj = null;
        boolean flag = false;        
        String limiter = ";";
        if( s == null ) return this.veez;
        if(s != null ) slim = s.trim();

        if(s.indexOf(match)>=0)
        {
            this.veez.add( slim ) ;
        }

        return this.veez;
    }


    /** There is a message, Q with notification information.
        If Q, at time of the message has no information, 
        then return message size.
    */
    public long updateNotificationMessage( String msgNotification )
    {
        File fio = null;
        
        long noteSize = 0;

        RandomAccessFile msgNotificationFile = null;

        if(msgNotification==null||msgNotification.trim().length()==0)
            return -1;
        
        try
        {
            fio = new File(this.msgNotificationFileName);

            if(verboseLog==true && fio.exists()) 
                System.out.println("\t"+this.msgNotificationFileName+
                " has " + fio.length() + " bytes, was modified about " +
                 ((System.currentTimeMillis() - fio.lastModified())/1000)/60 + 
                 " minutes ago."
                 );
            
            noteSize = fio.length();

            msgNotificationFile = new RandomAccessFile(this.msgNotificationFileName, "rw");   
                msgNotificationFile.seek(0);
                msgNotificationFile.writeBytes(msgNotification);
                noteSize = msgNotificationFile.length();
                msgNotificationFile.close(); 
        }
                  catch(IOException ioexception) 
                  {  
            ioexception.printStackTrace();
                  }

        return noteSize;
    }


    public synchronized java.lang.String  getMessage(  )
    {
        java.lang.String empty = "";

        java.util.Enumeration ease = null;

        java.lang.StringBuffer msgBuffer = null;

        if(this.veez == null || this.veez.size()==0)
        {
            if(verboseLog==true) System.out.println("\tExit. There were no messages.\n");

            return empty;
        }
        else if(this.veez.size()>0) 
        {
            ease = this.veez.elements();  

            msgBuffer = new java.lang.StringBuffer ();          
        }

        msgBuffer.append(this.nwln);
        
        while(ease.hasMoreElements())
        {
            msgBuffer.append(" " +(String)ease.nextElement()+ "<BR>");

            msgBuffer.append(this.nwln);
        }

        return msgBuffer.toString();
    }


    
    private synchronized java.util.Vector  setMessageBuffer( java.lang.String msgLine )
    {
        java.util.Vector buf = new java.util.Vector() ;

        java.lang.StringBuffer xEvidence = null;

        if(msgLine==null||msgLine.length()==0)
        {
            return buf;
        }

        if(msgLine.indexOf("ERROR")>0) buf.add( msgLine );

      return buf;
    }


    public static void main( String [] args )
    {
        Deutung data = new Deutung("recent.csv", "Msg.out");

        return;
    }

};


class StatistikInterpretation
{
    private int asMsgType = 1;
    public static final int TXT = 0;
    public static final int HTML = 1;
    public static final int EMAIL = 2;
    public String triggerStatus = "ERROR";
    private Integer TotalErrorCount = null;
    private Integer TotalRecordCount = null;
    private String IncidentInstanceName = "";
    private String IncidentPointOfContact = "";
    private String[] ColorSeverity = { "RED", "ORANGE", "GREEN"};
    private String lSep = System.getProperty("line.separator") ;
    //2019-03-01 08:46:04;sep4;Test Status:ERROR:Check User authentication failed    

    public StatistikInterpretation()
    {
        /* 
         * Put a row into a message. 
         */
         
    }

    public int getFormat( )
    {
       return this.asMsgType; 
    }


    public int setFormat( final int format )
    {
        this.asMsgType = format;
        return this.asMsgType ;
    }


    public synchronized String messageMap( String dateTime, String cid, String mesTxt )
    {
        java.lang.StringBuffer mBuffer = null;

        mBuffer = new java.lang.StringBuffer();

        mBuffer.append(formatDateTime(dateTime));

        if(getFormat( ) == TXT)
        {
             mBuffer.append(lSep);
        }
        else 
        {
            mBuffer.append("<BR>");
        }

        mBuffer.append(formatIdentity(cid));

        mBuffer.append(formatPayloadMessage(mesTxt));

        String mapped = mBuffer.toString();

        return mapped;
    }


    private String formatDateTime( String dt )
    {
        String formedDateTime = dt;
        return formedDateTime;
    }

    private String formatIdentity( String id )
    {
        String formedIdentity = "<B>"+id+"</B>";
        return formedIdentity;
    }

    private String formatPayloadMessage( String plm )
    {
        String formedPayload = plm;
        return formedPayload;
    }
};
