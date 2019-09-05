public interface Coordination {
 /* The set of known error categories. */
 public static final int ServiceUnavailable = 1;
 public static final int OpenPortal = 2;
 public static final int UserConsent = 3;
 public static final int UserAuthentic = 4;
 public static final int DLAUser = 5;
 public static final int PortalWelcome = 6;
 public static final int PortalLogoff = 7;

 public static final String ServiceUnavailableErrType = "503";
 public static final String OpenPortalErrType = "Open Portal URL";
 public static final String UserConsentErrType = "Check user consent";
 public static final String UserAuthenticErrType = "Check User authentication";
 public static final String DLAUserErrType = "Check DLA UserID";
 public static final String PortalWelcomeErrType = "Check Portal Welcome";
 public static final String PortalLogoffErrType = "Portal Logoff URL";

 public java.util.Hashtable getUrgentHostsTable();
 public java.util.Properties getTextNotificationList();
 public java.util.Properties eventForTextNotification(String record, String host);
 public java.util.Hashtable categorizeHostByUrgency(String errorRecord, String host);
};

