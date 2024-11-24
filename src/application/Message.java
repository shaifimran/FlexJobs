package application;

import java.sql.Timestamp;

public class Message {
	private String senderId;
	private String receiverId;
	private String text;
	private Timestamp transmittedAt;
	private int chatId;
	public String getSenderId() {
		return senderId;
	}
	public void setSenderId(String senderId) {
		this.senderId = senderId;
	}
	public String getReceiverId() {
		return receiverId;
	}
	public void setReceiverId(String receiverId) {
		this.receiverId = receiverId;
	}
	public Timestamp getTransmittedAt() {
		return transmittedAt;
	}
	public void setTransmittedAt(Timestamp transmittedAt) {
		this.transmittedAt = transmittedAt;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public int getChatId() {
		return chatId;
	}
	public void setChatId(int chatId) {
		this.chatId = chatId;
	}
	
}
