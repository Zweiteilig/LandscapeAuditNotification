import java.util.logging.Logger;
import java.util.*;
import java.lang.*;
import java.net.*;
import java.sql.*;
import java.io.*;

public class Agent implements ApplicationAgency {

 private Schedule zeitplan;
 Properties agentProp = new Properties();
 static final String myDateLayout = "MM-dd-yyyy HH:mm:ss";
 private final static java.util.logging.Logger LOGGER = Logger.getLogger(Agent.class.getName());

 //-------------------------
 // The "Agent" 
 //           constructors
 // 
 //    Read the event log and compose a message.
 //    Apr 30th, 2019;
 //-------------------------
 public Agent() {
  loadConfiguredProperties();

  Schedule.setDelayAsMinutes(new Integer(getDelayAsMinutes()).intValue());

  zeitplan = new Schedule(agentProp.getProperty("EVENTSLOG"), agentProp.getProperty("MSGFILE"), this);

  return;
 }


 //-------------------------
 // methods
 //-------------------------
 public String getDelayAsMinutes() {
  if (agentProp == null || agentProp.getProperty("DelayAsMinutes") == null)
   LOGGER.info("Could not load the DelayAsMinutes.");
  return agentProp.getProperty("DelayAsMinutes");
 }


 private String getSystemClockTranslation(long cTime) {
  String lastModifiedTimeString = "";
  java.util.Calendar cal = java.util.Calendar.getInstance();
  cal.setTimeInMillis(cTime);

  lastModifiedTimeString = new java.text.
  SimpleDateFormat(Agent.myDateLayout).
  format(cal.getTime());

  return lastModifiedTimeString;
 }


 private int getLastModifiedInSecondsAgo(File file) {
  Long approxMinutesAgo = null;

  approxMinutesAgo = new Long((
   System.currentTimeMillis() - file.lastModified()
  ) / 1000);

  return approxMinutesAgo.intValue();
 }


 private int getLastModifiedAsMinutesAgo(File file) {
  Long approxMinutesAgo = null;

  approxMinutesAgo = new Long((
   System.currentTimeMillis() - file.lastModified()
  ) / 1000 / 60);

  return approxMinutesAgo.intValue();
 }

 // contract method to read agentProp.getProperty("URGENT_HOSTS")
 public String getUrgencyPortals() {
  String urgency = agentProp.getProperty("URGENT_HOSTS");
  return urgency;
 }

 // contract method to read agentProp.getProperty("PORT")
 public String getSmtpPort() {
  String port = agentProp.getProperty("PORT");
  return port;
 }


 // contract method to read agentProp.getProperty("HOST")
 public String getSmtpHost() {
  String smtpHost = agentProp.getProperty("HOST");
  return smtpHost;
 }

 // contract method to read agentProp.getProperty("MSG_FROM")
 public String getMessageSender() {
  String messageSender = agentProp.getProperty("MSG_FROM");
  return messageSender;
 }

 // contract method to read agentProp.getProperty("MSG_SUBJECT")
 public String getMessageSubject() {
  String messageSubject = agentProp.getProperty("MSG_SUBJECT");
  return messageSubject;
 }


 // contract method to get the "CC" field;
 public String getMessageCCRecipients() {
  String[] items = null;

  String ccCommaSeparated = agentProp.getProperty("MSG_CC");

  items = ccCommaSeparated.split(",");

  for (int c = 0; c < items.length; c++) {
   String cc = items[c];
  }

  return ccCommaSeparated;
 }

 // contract method to get the "All" field;
 public java.util.ArrayList getAllMessageRecipients() {
  String[] items = null;

  java.util.ArrayList messageRecipients = new
  java.util.ArrayList();

  String ccCommaSeparated = agentProp.getProperty("MSG_TO");

  items = ccCommaSeparated.split(",");

  for (int c = 0; c < items.length; c++) {
   String cc = items[c];
   messageRecipients.add(cc);
  }

  return messageRecipients;
 }


 // contract method to get the "To" field;
 public String getMessageRecipient() {
  String getTheMsg = "";
  getTheMsg = agentProp.getProperty("MSG_TO");
  return getTheMsg;
 }


 // The primary business rule.
 public boolean hasBeenProcessedOnSchedule(File processPath) {
  int theDelayAsMinutes = 0;
  boolean isProcessedOnSchedule = true;
  theDelayAsMinutes = new Integer(getDelayAsMinutes()).intValue();

  if (getLastModifiedAsMinutesAgo(processPath) > theDelayAsMinutes) {
   return false;
  }

  return isProcessedOnSchedule;
 }


 public String getLastAuditEvent() {
  String lastAuditEvent = "";
  lastAuditEvent = (String) this.agentProp.get("LASTNOTETIME");
  return lastAuditEvent;
 }


 public void setLastAuditEvent(String lastTime) {
  this.agentProp.put("LASTNOTETIME", lastTime);
  LOGGER.info(this.agentProp.toString());
  return;
 }

 public void setConfigProperty(String key, String value) {
  this.agentProp.put(key, value);
  return;
 }

 public Object getConfigProperty(String key) {
  return this.agentProp.get(key);
 }


 private int loadConfiguredProperties() {
  long nthin = -1;
  InputStream input = null;
  int thisConfiguredListSize = 0;

  try {
   input = new FileInputStream("Agent.properties");

   if (input != null && input.available() > nthin) {
    agentProp.load(input);
   } else {
    return thisConfiguredListSize;
   }
  } catch (IOException ex) {
   LOGGER.info("There was an error reading properties.");
   System.exit(0);
  } finally {
   if (input != null) {
    try {
     input.close();
    } catch (IOException e) {
     e.printStackTrace();
    }
   }
  }

  return thisConfiguredListSize;
 }

 public java.util.ArrayList getUrgenNotificationRecipients() {
  String[] items = null;
  java.util.ArrayList messageRecipients = new java.util.ArrayList();
  String ccCommaSeparated = agentProp.getProperty("MSG_URGENT");
  LOGGER.info("Who gets MSG_URGENT: " + ccCommaSeparated);
  if (ccCommaSeparated == null) return messageRecipients;

  items = ccCommaSeparated.split(",");

  if (items == null) {
   messageRecipients.add(ccCommaSeparated.trim());
   return messageRecipients;
  }

  for (int c = 0; c < items.length; c++) {
   String cc = items[c];
   messageRecipients.add(cc);
  }

  return messageRecipients;
 }

 // -- --- -----
 // Main driver:
 // -- --- -----
 public static void main(String[] cmd) throws java.lang.Exception {
  Agent agent = new Agent();
  return;
 }

};


class ConsoleCommander {
 private String[] consoleArgs = null;

 public ConsoleCommander() {
  System.out.println("[ConsoleCommander with no args]");
  return;
 }

 public ConsoleCommander(String[] cArgs) {
  System.out.println("[ConsoleCommander " + cArgs.length + " args]");

  if (cArgs.length < 1) consoleArgs = new
  String[cArgs.length];

  for (int f = 0; f < cArgs.length; f++) {
   System.out.println("[" + cArgs[f] + "]");
  }

  return;
 }

 public java.util.List getArguments() {
  java.util.List argsList = null;

  //If there are command elements, then 
  //  initialize the list and add the 
  //  elements;
  if (consoleArgs.length > 0)
   argsList = new java.util.ArrayList();
  else return argsList;

  for (int f = 0; f < argsList.size(); f++) {
   argsList.add(consoleArgs[f]);
  }

  return argsList;
 }
};