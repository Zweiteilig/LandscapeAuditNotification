import java.io.*;
import java.nio.channels.*;

public class FileTransition implements CompleteExpection
{
 
  public FileTransition()
  {
    File trunk = null;  
    FileChannel src = null;
    FileChannel dest = null;
    FileOutputStream truncChanel = null;
    String namedSrcFileID =   "README.TXT";
    String namedDstFileID =   "BEEN_READ." + auditTruncateSourceTime();

    System.out.println( namedDstFileID );
    //The actual file, named, "README.TXT" WOULD
    //  HAVE TO EXIST for the logical_condition
    //  to be expressed.
    try{
        //Try to manage a logical_condition Expectation
        //  that "README.TXT" and "BEEN_READ.TXT" exist (& are available);
         src = new FileInputStream(namedSrcFileID).getChannel();
         dest = new FileOutputStream(namedDstFileID).getChannel();

        dest.transferFrom(src, 0, src.size());
        truncChanel = new FileOutputStream(namedSrcFileID);
        truncChanel.getChannel().truncate(0).close(); 
    }
    catch( IOException mngExc )
    {
        manageCompletionExpection(namedSrcFileID);
    }
    return;
  }


  public FileTransition(String source, String destination)
  {
    File trunk = null;  
    FileChannel src = null;
    FileChannel dest = null;
    FileOutputStream truncChanel = null;
    String namedSrcFileID =   source ;
    String namedDstFileID =   destination + "." + auditTruncateSourceTime();

    System.out.println( namedDstFileID );
    //The actual file, named, "README.TXT" WOULD
    //  HAVE TO EXIST for the logical_condition
    //  to be expressed.
    try{
        //Try to manage a logical_condition Expectation
        //  that "README.TXT" and "BEEN_READ.TXT" exist (& are available);
         src = new FileInputStream(namedSrcFileID).getChannel();
         dest = new FileOutputStream(namedDstFileID).getChannel();

        dest.transferFrom(src, 0, src.size());
        truncChanel = new FileOutputStream(namedSrcFileID);
        truncChanel.getChannel().truncate(0).close(); 
    }
    catch( IOException mngExc )
    {
        manageCompletionExpection(namedSrcFileID);
    }
    return;
  }
  

  public static String auditTruncateSourceTime( )
  {
    String truncTime = "";
      java.sql.Timestamp auditTruncateTime = null;
     auditTruncateTime = new java.sql.Timestamp(System.currentTimeMillis());
    truncTime = auditTruncateTime.toString().replaceAll("\\s+","");
    truncTime = truncTime.replaceAll(":", "");
    System.out.println(truncTime);
    return truncTime;
  }


  public String manageCompletionExpection(String completionTarget)
  {
      System.out.println("Manage Completion Expection for, " + completionTarget );
      return completionTarget;
  }


  public static void main(String[] arg)
  {
      FileTransition mainFileTransition = new FileTransition("notifyForNow.msg", "notifyForNow.msg");
      return;
  }
};