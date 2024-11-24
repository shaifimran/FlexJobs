package application;

import java.sql.Timestamp;

public class Notification {
	private int notificationId;
	private String message;
	private String senderID;
	private String receiverID;
	private Timestamp timestamp;
	private boolean isRead;

	public Notification(int notificationId, String senderId, String receiverId, String message, Timestamp timestamp,
			boolean isRead) {
		this.notificationId = notificationId;
		this.message = message;
		this.setSenderId(senderId);
		this.setReceiverId(receiverId);
		this.timestamp = timestamp;
		this.isRead = isRead;
	}

	public Notification(int notificationId, String message, boolean isRead) {
		this.notificationId = notificationId;
		this.message = message;
		this.isRead = isRead;
	}

	public int getNotificationId() {
		return notificationId;
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

	public String getSenderId() {
		return senderID;
	}

	public void setSenderId(String senderId) {
		this.senderID = senderId;
	}

	public String getReceiverId() {
		return receiverID;
	}

	public void setReceiverId(String receiverId) {
		this.receiverID = receiverId;
	}
}
