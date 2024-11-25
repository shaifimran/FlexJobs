package application;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class Chat {
	private int chatID;
	private Timestamp createdAt;
	private String studentId;
	private String orgId;
	private List<Message> messages;

	public Chat(int chatID, Timestamp createdAt, String orgId, String studentId) {
		this.chatID = chatID;
		this.createdAt = createdAt;
		this.studentId = studentId;
		this.orgId = orgId;
	}

	public Chat() {
		// TODO Auto-generated constructor stub
	}

	public int getChatID() {
		return chatID;
	}

	public void setChatID(int chatID) {
		this.chatID = chatID;
	}

	public Timestamp getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}

	public String getStudentId() {
		return studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public List<Message> getMessages() {
		return messages;
	}

	public void setMessages(List<Message> messages) {
		this.messages = messages;
	}

	public List<Chat> loadActiveConversations() {
		// Load active conversations
		return new ArrayList<>();
	}

	public Message createMessage(String content) {
		// Create new message
		return new Message();
	}

	public boolean sendMessage(Message message) {
		// Send message logic
		return true;
	}
}
