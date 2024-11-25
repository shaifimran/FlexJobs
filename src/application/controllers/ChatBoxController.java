package application.controllers;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import application.Chat;
import application.Organisation;
import application.Student;
import application.UI.UIFactory;
import application.handlers.ChatHandler;
import application.handlers.DBHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

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

	@FXML
	private TextField searchField;

	@FXML
	private Hyperlink goBackButton;

	@FXML
	private Hyperlink addNewChatButton;

	private ArrayList originalChats;

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
			addNewChatButton.setVisible(false);
			addNewChatButton.setManaged(false);

			organisation.setMyChatBox(dbHandler.getOrganisationChatBox(organisation.getName().toLowerCase()));
			organisation.getMyChatBox().setChats(dbHandler.getOrganisationChats(organisation.getName().toLowerCase()));
			UIFactory.getInstance().displayOrganisationChats(chatsVBox, chatsHBox, organisation, this);
		}

		originalChats = new ArrayList<>(chatsVBox.getChildren());

		// Add a listener to the searchField
		searchField.textProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue.isEmpty()) {
				// Reset to the original layout if the search field is empty
				chatsVBox.getChildren().setAll(originalChats);
			} else {
				// Filter and reorder chats based on the query
				filterAndReorderChats(newValue);
			}
		});
	}

	private void filterAndReorderChats(String query) {
	    // Get the HBox that contains the searchField, it should be part of chatsVBox
	    HBox searchHBox = (HBox) searchField.getParent();  // Get the parent HBox of searchField

	    // Filter out only chat items and exclude the search HBox
	    List<Node> filteredChats = (List<Node>) originalChats.stream()
	        .filter(node -> node != searchHBox)  // Exclude the HBox containing searchField
	        .filter(node -> {
	            if (node instanceof HBox) {
	                HBox chatBox = (HBox) node;
	                for (Node child : chatBox.getChildren()) {
	                    if (child instanceof Label) {
	                        Label chatLabel = (Label) child;
	                        return chatLabel.getText().toLowerCase().contains(query.toLowerCase());
	                    }
	                }
	            }
	            return false;
	        })
	        .collect(Collectors.toList());

	    // Clear VBox and set it back with the search HBox and filtered chats
	    chatsVBox.getChildren().setAll(searchHBox);  // Add the HBox with the searchField back
	    chatsVBox.getChildren().addAll(filteredChats);

	    // Optional: Handle no matches (e.g., display a placeholder)
	    if (filteredChats.isEmpty()) {
	    	  Label noResultsLabel = new Label("No matching chats found.");
	          noResultsLabel.setStyle("-fx-text-fill: black; -fx-font-style: italic; -fx-font-size: 14px;");

	          // Create a StackPane to center the label within the VBox
	          StackPane centeredMessage = new StackPane(noResultsLabel);
	          centeredMessage.setAlignment(Pos.CENTER);  // Align label to the center

	          // Set the preferred height to take up the whole VBox space
	          centeredMessage.setPrefHeight(chatsVBox.getHeight());
	          centeredMessage.setPrefWidth(chatsVBox.getWidth());

	          // Add the centered StackPane to the VBox
	          chatsVBox.getChildren().add(centeredMessage);
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

	public void goBackToDashboard(ActionEvent event) {
		try {
			if (student != null && organisation == null) {
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/UI/StudentDashboard.fxml"));
				loader.setControllerFactory(c -> new StudentDashboardController(student));
				Parent root = loader.load();
				Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();

				Scene scene = new Scene(root);

				stage.setScene(scene);
				stage.show();
			} else if (student == null && organisation != null) {
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/UI/OrgRepDashboard.fxml"));
				loader.setControllerFactory(c -> new OrgRepDashboardController(organisation.getRepresentatives().getLast()));
				Parent root = loader.load();
				Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();

				Scene scene = new Scene(root);

				stage.setScene(scene);
				stage.show();

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void addNewChat(ActionEvent event) {
		while (true) {
			// Create a dialog box to ask for the organization's name
			TextInputDialog dialog = new TextInputDialog();
			dialog.setTitle("New Chat");
			dialog.setHeaderText("Start a New Chat");
			dialog.setContentText("Enter the organization name:");

			// Show the dialog and capture the input
			Optional<String> result = dialog.showAndWait();
			if (result.isPresent()) {
				String orgName = result.get().toLowerCase();

				// Check if the organization exists in the database
				if (dbHandler.organisationExists(orgName)) {
					try {
						// Create a new chat in the database
						Chat newChat = new Chat();
						newChat.setOrgId(orgName);
						newChat.setStudentId(student.getRollNo());
						newChat.setCreatedAt(new Timestamp(System.currentTimeMillis())); // Use current timestamp

						int chatId = dbHandler.createChat(newChat);
						newChat.setChatID(chatId);
						student.getMyChatBox().getChats().add(newChat);

						// Reload the scene to refresh chats
						FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/UI/ChatBox.fxml"));
						loader.setControllerFactory(c -> new ChatBoxController(student));
						Parent root = loader.load();

						Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
						stage.setScene(new Scene(root));
						stage.show();
						break; // Exit the loop
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else {
					// Show an alert if the organization doesn't exist
					Alert alert = new Alert(Alert.AlertType.ERROR);
					alert.setTitle("Error");
					alert.setHeaderText("Organization Not Found");
					alert.setContentText("The organization you entered does not exist. Please try again.");
					alert.showAndWait();
				}
			} else {
				// If the user cancels or closes the dialog, exit the loop
				break;
			}
		}
	}

}
