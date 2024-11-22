package application;

import java.sql.Date;

public class Notification {
	private String notificationId;
	private String message;
	private Date timestamp;
	private boolean isRead;

	public Notification(String notificationId, String message, Date timestamp, boolean isRead) {
		this.notificationId = notificationId;
		this.message = message;
		this.timestamp = timestamp;
		this.isRead = isRead;
	}

	public String getNotificationId() {
		return notificationId;
	}

	public void setNotificationId(String notificationId) {
		this.notificationId = notificationId;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public boolean isRead() {
		return isRead;
	}

	public void setRead(boolean isRead) {
		this.isRead = isRead;
	}

	public boolean sendVerificationNotification(String orgId) {
		// Logic to send verification notification
		return true;
	}

	public boolean sendInterviewNotification(String studentId) {
		// Logic to send interview notification
		return true;
	}
}
