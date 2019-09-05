public interface RecordReview {

 /* The "RecordedStatistics" are stored in a vector. 
  */
 java.util.Vector getRecordedStatistics();

 /* The "LoadrunnerRecord" is a text file that tracks portal incidents. 
     The file may not be available.
 */
 int readLoadrunnerRecord(Coordination urgency);

 /* The "ReviewNotification" is a file that is sent as
     email content.
 */
 String updateReviewNotification(String msgReviewNotification);

};