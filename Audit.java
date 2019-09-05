import java.io.*;
import java.util.*;
import java.util.logging.Logger;
/** 
Read the rows in the log file.
Match the urgent events in the log file.
*/
public class Audit implements RecordReview {
 int events = 0;
 java.lang.String fHistoryNm;
 java.util.Vector auditVector;
 Coordination serviceCoordination;
 private final int HOST_CSV_POSITION = 1;
 private final int CLOSE_CSV_POSITION = 2;
 private String auditorApplictionErr = "";
 private String collectForMatch = "ERROR";
 String msgNotificationFileName = "notifyForNow.msg";
 private final static java.util.logging.Logger LOGGER = Logger.getLogger(Audit.class.getName());


 public Audit(String inEventLogFile, String outEventsMsg, Coordination urgency) {
  this.serviceCoordination = urgency;
  this.msgNotificationFileName = outEventsMsg;

  if (!supportedExtension(inEventLogFile))
   return;

  this.fHistoryNm = inEventLogFile;
  this.auditVector = new java.util.Vector();

  /* Read the error record & prioritize. */
  readLoadrunnerRecord(serviceCoordination);

  return;
 }

 private int getEvents() {
  return this.events;
 }
 private boolean setEvents(int e) {
  boolean isSet = true;
  if (e > 0) this.events = e;
  LOGGER.info("\tEvents set, " + this.events + ". " +
   new java.sql.Timestamp(System.currentTimeMillis())
  );
  return isSet;
 }

 private boolean supportedExtension(String fByName) {
  String support = null;
  boolean isSupported = true;
  String[] successSupport = {
   ".csv",
   ".txt",
   ".test"
  };

  if (fByName == null || fByName.equals(""))
   return false;

  support = fByName.substring(fByName.indexOf(".")).toLowerCase().trim();

  for (int i = 0; i < successSupport.length; i++) {
   String EXT = successSupport[i].trim();

   if (support.equals(EXT)) {
    return isSupported;
   }
  }

  return false;
 }


 public synchronized int readLoadrunnerRecord(Coordination urgency) {
  int row = 0;
  int Nothing = 0;

  try {

   RandomAccessFile randomaccessfile = new
    RandomAccessFile(this.fHistoryNm, "r");

   long fileLength = randomaccessfile.length();

   if (fileLength == Nothing) 
   {
     LOGGER.info("\tThe " + this.fHistoryNm + " is empty.");
   } 

   do 
   {
    String sReadLine = randomaccessfile.readLine();

    evaluateRecordForError(sReadLine, this.collectForMatch, urgency);

    row++;

   } while(randomaccessfile.getFilePointer() < fileLength);

   randomaccessfile.close();

  } catch (IOException ioexception) {
   auditorApplictionErr = ioexception.toString();
  }

  setEvents(row);

  return getEvents();
 }


 public void setScanForEvent(String scanForEvent) {
  this.collectForMatch = "";

  if (scanForEvent != null) this.collectForMatch = scanForEvent.trim();

  return;
 }


 private synchronized Vector evaluateRecordForError(String logRecord, String match, Coordination coordinate) {

  Vector evaluate = null;

  String evaluateLogRecord = "";
  
  if (logRecord == null) return this.auditVector;

  if (logRecord != null) evaluateLogRecord = logRecord.trim();

  if(evaluateLogRecord.indexOf(match) > 0) 
  {
   coordinate.categorizeHostByUrgency(evaluateLogRecord,
    parsePortalID(evaluateLogRecord)
   );
  
   coordinate.eventForTextNotification(evaluateLogRecord,
    parsePortalID(evaluateLogRecord)
   );

   this.auditVector.add(evaluateLogRecord);
  }

  return this.auditVector;
 }


 private String parsePortalID(String stringIncludesPortal) {
  String pVal = "";

  String[] values = null;

  String SCSV = stringIncludesPortal;

  if (SCSV != null && !SCSV.trim().equals(""))
   values = SCSV.split(";");

  if(SCSV.indexOf(";")<0) 
    LOGGER.info("\n\tFormat error on, \"" + 
    SCSV + "\". The record is not delimitted by, \';\'.");

  return values[HOST_CSV_POSITION];
 }

 /** The "ReviewNotification" file is sent as email content.
  */
 public synchronized String updateReviewNotification(String msgReviewNotification) {
  long noteSize = 0;

  FileOutputStream truncChanel = null;

  String notificationInformation = "";

  RandomAccessFile msgReviewNotificationFile = null;

  if (msgReviewNotification == null || msgReviewNotification.trim().length() == 0)
   return notificationInformation;

  try {
   truncChanel = new FileOutputStream(this.msgNotificationFileName);
   truncChanel.getChannel().truncate(0).close();

   msgReviewNotificationFile = new RandomAccessFile(this.msgNotificationFileName, "rw");
   msgReviewNotificationFile.seek(0);
   msgReviewNotificationFile.writeBytes(msgReviewNotification);
   noteSize = msgReviewNotificationFile.length();
   msgReviewNotificationFile.close();
  } catch (IOException ioexception) {
   ioexception.printStackTrace();
  }

  if (noteSize > 1) return msgReviewNotification;
  return notificationInformation;
 }


S public synchronized java.lang.String getMessage() {
  int errCount = 0;
  String nwln = "\n";
  java.lang.String empty = "";
  java.util.Enumeration ease = null;
  java.lang.String currentAuditDate = "";
  java.lang.StringBuffer msgBuffer = null;

  LandscapePoliteCommunication landscapePoliteCommunication =
   new LandscapePoliteCommunication();

  if (this.auditVector == null || this.auditVector.size() == 0) {
   return empty;
  } else if (this.auditVector.size() > 0) {
   ease = this.auditVector.elements();
   msgBuffer = new java.lang.StringBuffer();
  }

  while (ease.hasMoreElements()) {
   String easyNext = (String)
   ease.nextElement();

   //The string stamp date;
   if (AuditStats.hasCurrentAuditEligibility(easyNext.substring(0, easyNext.indexOf(";")))) {
    landscapePoliteCommunication.readRawMessage(easyNext);

    currentAuditDate = easyNext.substring(0, easyNext.indexOf(";"));

    errCount++;
   }

  }

  landscapePoliteCommunication.setErrorCount(errCount);
  landscapePoliteCommunication.setLogSource(this.fHistoryNm);
  msgBuffer.append(landscapePoliteCommunication.getPoliteDateStamp());
  msgBuffer.append(landscapePoliteCommunication.writePoliteMessage());
  msgBuffer.append(nwln);
  AuditStats.setTheCurrentAuditStamp(currentAuditDate);
  AuditStats.truncateLog(this.fHistoryNm);
  return msgBuffer.toString();
 }


 private synchronized java.util.Vector setMessageBuffer(java.lang.String msgLine) {
  java.util.Vector buf = new java.util.Vector();

  java.lang.StringBuffer xEvidence = null;

  if (msgLine == null || msgLine.length() == 0) {
   return buf;
  }

  if (msgLine.indexOf("ERROR") > 0)
   buf.add(msgLine);

  return buf;
 }


 public java.util.Vector getRecordedStatistics() {
  return this.auditVector;
 }


 public int getEventCount() {
  return this.events;
 }

 public boolean setEventCount(int count) {
  boolean updated = true;
  int compare = this.events;

  if (compare == count) return false;

  this.events = count;
  return updated;
 }

 public synchronized String getNotificationMsgFileName() {
  return this.msgNotificationFileName;
 }

 public synchronized java.util.Vector getMessageBuffer() {
  return this.auditVector;
 }

 private String getSystemClock() {
  String lastModifiedTimeString = "";
  long cTime = System.currentTimeMillis();
  java.util.Calendar cal = java.util.Calendar.getInstance();
  cal.setTimeInMillis(cTime);

  lastModifiedTimeString = new java.text.
  SimpleDateFormat(Agent.myDateLayout).
  format(cal.getTime());

  return lastModifiedTimeString;
 }

 public static void main(String[] args) {
  Agent agent = new Agent();
  ServiceCoordination urgentCoordination = new ServiceCoordination(agent);
  Audit data = new Audit("C:\\Mesh\\BlueSky\\Agent\\PortalAgent\\PEZ\\unit_test_script_503_results.txt", "Msg.out", urgentCoordination);
  return;
 }

};


class AuditStats {
 private int asMsgType = 1;
 public static final int TXT = 0;
 public static final int HTML = 1;
 public static final int EMAIL = 2;
 public String triggerStatus = "ERROR";
 private Integer TotalErrorCount = null;
 private Integer TotalRecordCount = null;
 private String IncidentInstanceName = "";
 private String IncidentPointOfContact = "";
 private String[] ColorSeverity = {
  "RED",
  "ORANGE",
  "GREEN"
 };
 private String lSep = System.getProperty("line.separator");
 private static String TheCurrentAuditStamp = null;
 //2019-03-01 08:46:04;sep4;Test Status:ERROR:Check User authentication failed    

 public AuditStats() {
  /* 
   * Put a row into a message. 
   */
 }



 public static String truncateLog(String log) {
  FileTransition mainFileTransition = new FileTransition(log, log);
  return log;
 }


 public synchronized static String getTheCurrentAuditStamp() {
  if (TheCurrentAuditStamp == null) TheCurrentAuditStamp = "";
  return TheCurrentAuditStamp;
 }

 public static void setTheCurrentAuditStamp(String currentAuditStamp) {
  TheCurrentAuditStamp = "";
  TheCurrentAuditStamp = currentAuditStamp.trim();
  return;
 }

 public static boolean hasCurrentAuditEligibility(String time_stamped_string) {
  boolean eligible = true;
  Lantime lantime = new Lantime();
  // The log is over a week old, then ignore it.
  if (lantime.withinThisNetworkWeek(time_stamped_string))
   return eligible;

  return false;
 }


 public int getFormat() {
  return this.asMsgType;
 }


 public int setFormat(final int format) {
  this.asMsgType = format;
  return this.asMsgType;
 }


 public synchronized String messageMap(String dateTime, String cid, String mesTxt) {
  java.lang.StringBuffer mBuffer = null;

  mBuffer = new java.lang.StringBuffer();

  mBuffer.append(formatDateTime(dateTime));

  if (getFormat() == TXT) {
   mBuffer.append(lSep);
  } else {
   mBuffer.append("");
  }

  mBuffer.append(formatIdentity(cid));

  mBuffer.append(formatPayloadMessage(mesTxt));

  String mapped = mBuffer.toString();

  return mapped;
 }


 private String formatDateTime(String dt) {
  String formedDateTime = dt;
  return formedDateTime;
 }

 private String formatIdentity(String id) {
  String formedIdentity = "<B>" + id + "</B>";
  return formedIdentity;
 }

 private String formatPayloadMessage(String plm) {
  String formedPayload = plm;
  return formedPayload;
 }
};

class ErrorMatch {
 private boolean theErrMatched = false;
 private String matchThisTarget = "";

 public ErrorMatch(String target) {
  setMatchTarget(target);
 }

 public boolean foundErrorMatch() {
  return this.theErrMatched;
 }
 public void errorMatchFound(boolean m) {
  this.theErrMatched = m;
 }
 public void setMatchTarget(final String mTarget) {
  this.matchThisTarget = mTarget;
  return;
 }
 public String getMatchTarget() {
  if (this.matchThisTarget == null)
   this.matchThisTarget = "";
  return this.matchThisTarget;
 }

 public int filterTextForTarget(final String filterThisText) {
  int fState = -1;

  fState = filterThisText.indexOf(
   getMatchTarget());

  if (fState > -1) errorMatchFound(true);
  else errorMatchFound(false);

  return fState;
 }

};