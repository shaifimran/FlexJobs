package application;

import java.util.List;

public class ChatBox {
	private int chatBoxId;
	private List<Chat> chats;
	
	private String ownerType;
	
	public List<Chat> getChats() {
		return chats;
	}

	public void setChats(List<Chat> chats) {
		this.chats = chats;
	}

	public String getOwnerType() {
		return ownerType;
	}

	public void setOwnerType(String ownerType) {
		this.ownerType = ownerType;
	}

	public int getChatBoxId() {
		return chatBoxId;
	}

	public void setChatBoxId(int chatBoxId) {
		this.chatBoxId = chatBoxId;
	}
}
