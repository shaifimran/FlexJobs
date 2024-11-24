package application.controllers;

import application.Chat;
import application.Organisation;
import application.Student;
import application.UI.UIFactory;
import application.handlers.ChatHandler;
import application.handlers.DBHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ChatBoxController {
	DBHandler dbHandler = new DBHandler();
	ChatHandler chatHandler = new ChatHandler();

	private Chat currentChat;
	private Student student;
	private Organisation organisation;

	@FXML
	private VBox chatsVBox;

	@FXML
	private HBox chatsHBox;

	@FXML
	private VBox msgVBox;

	@FXML
	private HBox receivedMsgHBox;

	@FXML
	private HBox sentMsgHBox;

	@FXML
	private HBox typeMsgHBox;

	@FXML
	private Button msgSendButton;

	@FXML
	private TextField msgContainer;

	public ChatBoxController(Student s) {
		this.student = s;
		this.organisation = null;
	}

	public ChatBoxController(Organisation org) {
		this.student = null;
		this.organisation = org;
	}

	@FXML
	public void initialize() {
		// Set templates to invisible
		receivedMsgHBox.setVisible(false);
		receivedMsgHBox.setManaged(false);
		sentMsgHBox.setVisible(false);
		sentMsgHBox.setManaged(false);

		typeMsgHBox.setVisible(false);
		typeMsgHBox.setManaged(false);
		if (student != null && organisation == null) {
			student.setMyChatBox(dbHandler.getStudentChatBox(student.getRollNo()));
			student.getMyChatBox().setChats(dbHandler.getStudentChats(student.getRollNo()));
			UIFactory.getInstance().displayStudentChats(chatsVBox, chatsHBox, student, this);
		} else if (student == null && organisation != null) {
			organisation.setMyChatBox(dbHandler.getOrganisationChatBox(organisation.getName()));
			organisation.getMyChatBox().setChats(dbHandler.getOrganisationChats(organisation.getName()));
			UIFactory.getInstance().displayOrganisationChats(chatsVBox, chatsHBox, organisation, this);
		}
	}

	public void handleChatClick(Chat chat) {
		currentChat = chat;
		

		// TODO Auto-generated method stub
		if (student != null && organisation == null) {
			typeMsgHBox.setVisible(true);
			typeMsgHBox.setManaged(true);
			dbHandler.getMsgs(chat);
			UIFactory.getInstance().showStudentMsgs(student, chat, msgVBox, receivedMsgHBox, sentMsgHBox, this);
		} else if (student == null && organisation != null) {
			typeMsgHBox.setVisible(true);
			typeMsgHBox.setManaged(true);
			dbHandler.getMsgs(chat);
			UIFactory.getInstance().showOrgMsgs(organisation, chat, msgVBox, receivedMsgHBox, sentMsgHBox, this);
		}
	}

	public void sendMsg(ActionEvent event) {
		if (!msgContainer.getText().equalsIgnoreCase(null) && !msgContainer.getText().equalsIgnoreCase("")) {
			if (student != null && organisation == null) {
				dbHandler.storeMessage(chatHandler.sendMsg(msgContainer.getText(), currentChat, student));
				handleChatClick(currentChat);
			} else if (student == null && organisation != null) {
		
				dbHandler.storeMessage(chatHandler.sendMsg(msgContainer.getText(), currentChat, organisation));
				handleChatClick(currentChat);

			}
		}
	}

}
