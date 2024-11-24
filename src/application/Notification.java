package application;

import java.sql.Timestamp;

public class Notification {

	private int notificationId;
	private String senderID;
	private String receiverID;
	private String message;
	private Timestamp timestamp;
	private boolean isRead;

	
	public Notification(int notificationId, String senderID, String receiverID, String message, Timestamp timestamp,
			boolean isRead) {
		this.notificationId = notificationId;
		this.senderID = senderID;
		this.receiverID = receiverID;
		this.message = message;
		this.timestamp = timestamp;
		this.isRead = isRead;
	}
	
	public Notification() {
		
	}

	public int getNotificationId() {
		return notificationId;
	}
	
	public Notification(int notificationId, String message, boolean isRead) {
		this.notificationId = notificationId;
		this.message = message;
		this.isRead = isRead;
	}


	public void setNotificationId(int notificationId) {
		this.notificationId = notificationId;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Timestamp getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}

	public boolean isRead() {
		return isRead;
	}

	public void setRead(boolean isRead) {
		this.isRead = isRead;
	}

	public boolean sendVerificationNotification(String orgId) {
		return true;
	}

	public boolean sendInterviewNotification(String studentId) {
		return true;
	}

	public String getSenderID() {
		return senderID;
	}

	public void setSenderID(String senderID) {
		this.senderID = senderID;
	}

	public String getReceiverID() {
		return receiverID;
	}

	public void setReceiverID(String receiverID) {
		this.receiverID = receiverID;
	}
}
