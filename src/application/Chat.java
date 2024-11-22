package application;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class Chat {
	private String chatID;
	private Date createdAt;
	private String sender;
	private String receiver;
	private List<Message> messages;

	public Chat(String chatID, Date createdAt, String sender, String receiver, List<Message> messages) {
		this.chatID = chatID;
		this.createdAt = createdAt;
		this.sender = sender;
		this.receiver = receiver;
		this.messages = messages;
	}

	public String getChatID() {
		return chatID;
	}

	public void setChatID(String chatID) {
		this.chatID = chatID;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
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
