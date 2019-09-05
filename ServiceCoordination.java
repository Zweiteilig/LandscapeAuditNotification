import java.util.*;
import java.util.logging.Logger;
public class ServiceCoordination implements Coordination {
 ApplicationAgency appAgency = null;
 java.util.Hashtable urgentHosts = null;
 java.util.Properties coordinatedHosts = null;
 private final static java.util.logging.Logger LOGGER = Logger.getLogger(ServiceCoordination.class.getName());

 public ServiceCoordination() {
  if (urgentHosts != null) urgentHosts = null;
  urgentHosts = new java.util.Hashtable();

  if (coordinatedHosts != null) coordinatedHosts = null;
  coordinatedHosts = new java.util.Properties();

  if (coordinatedHosts.size() == 0 && urgentHosts.size() == 0)
   LOGGER.info("The (initial) ServiceCoordination has 0 events.");
  return;
 }

 public ServiceCoordination(ApplicationAgency appAgent) {
  this.appAgency = appAgent;

  if (urgentHosts != null) urgentHosts = null;
  urgentHosts = new java.util.Hashtable();

  if (coordinatedHosts != null) coordinatedHosts = null;
  coordinatedHosts = new java.util.Properties();

  return;
 }

 public synchronized java.util.Hashtable categorizeHostByUrgency(String errorRecord, String host) {
  java.util.Hashtable urgent = new java.util.Hashtable(0);

  if (eventContainsType(errorRecord, Coordination.ServiceUnavailableErrType)) 
  {
   ErrorMatch eErrorMatch = new ErrorMatch(Coordination.ServiceUnavailableErrType);
   eErrorMatch.filterTextForTarget(errorRecord);

    if (eErrorMatch.foundErrorMatch()) 
    {
      if (this.urgentHosts == null) this.urgentHosts = new Hashtable();
      this.urgentHosts.put(host, Coordination.ServiceUnavailableErrType);
      LOGGER.info("\t"+host+" has an urgent "+
        Coordination.ServiceUnavailableErrType+" event.");
    }

   eErrorMatch = null;

   if (this.urgentHosts.size() > 0 )
    LOGGER.info("\tServiceCoordination has "+
    this.urgentHosts.size()+" events.");
   
   return this.urgentHosts;
  }

  return urgent;
 }


 public synchronized java.util.Properties eventForTextNotification(String record, String host) {
  Set < String > reportedHosts = null;
  java.util.Properties text = new java.util.Properties();
  String urgencyPortalList = appAgency.getUrgencyPortals();
  ErrorMatch eErrorMatch = new ErrorMatch(host.trim().toLowerCase());
  eErrorMatch.filterTextForTarget(urgencyPortalList.trim().toLowerCase());

  if (eErrorMatch.foundErrorMatch()) {
   if (this.coordinatedHosts == null) this.coordinatedHosts = new Properties();
   this.coordinatedHosts.setProperty(host, record);
   return this.coordinatedHosts;
  }

  eErrorMatch = null;
  return text;
 }


 private boolean eventContainsType(String event, String type) {
  boolean hasType = true;
  if (event.indexOf(type) >= 0) return hasType;
  return false;
 }

 public synchronized java.util.Properties getTextNotificationList() {
   return this.coordinatedHosts;
 }

 public synchronized java.util.Hashtable getUrgentHostsTable() {
  if (this.urgentHosts == null) this.urgentHosts = new java.util.Hashtable();
  return this.urgentHosts;
 }

 public static void main(String[] args) {
  Agent manFromUncle = new Agent();
  ServiceCoordination serviceCoordination = new ServiceCoordination(manFromUncle);
  return;
 }

};