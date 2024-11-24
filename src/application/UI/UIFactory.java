package application.UI;

import java.util.List;
import java.util.Map;

import application.Chat;
import application.ChatBox;
import application.Message;
import application.Notification;
import application.OrgRepTableRow;
import application.Organisation;
import application.OrganisationRepresentative;
import application.Student;
import application.UnverifiedOrgs;
import application.controllers.AdminDashboardController;
import application.controllers.AdminVerifyOrganisationController;
import application.controllers.ChatBoxController;
import application.controllers.StudentOpportunitiesController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class UIFactory {

	private static UIFactory instance;

	private UIFactory() {
	}

	public static UIFactory getInstance() {
		if (instance == null) {
			instance = new UIFactory();
		}
		return instance;
	}

	public static void createVerifyOrganisationTable(TableView<OrgRepTableRow> verifyOrgTable,
			TableColumn<OrgRepTableRow, String> orgCol, TableColumn<OrgRepTableRow, String> repCol,
			TableColumn<OrgRepTableRow, HBox> verCol, UnverifiedOrgs unverifiedOrgs,
			AdminVerifyOrganisationController controller) {
		// Bind data to the table columns
		orgCol.setCellValueFactory(new PropertyValueFactory<>("orgDetails"));
		repCol.setCellValueFactory(new PropertyValueFactory<>("repDetails"));

		// Add custom cell factory for action buttons
		verCol.setCellFactory(column -> new TableCell<>() {
			@Override
			protected void updateItem(HBox actions, boolean empty) {
				super.updateItem(actions, empty);
				if (empty || getTableRow() == null || getTableRow().getItem() == null) {
					setGraphic(null);
					return;
				}

				// Create "Verify" and "Unverify" buttons
				Button verifyButton = new Button("✔");
				Button unverifyButton = new Button("✖");
				verifyButton.setStyle("-fx-background-color: green; -fx-text-fill: white;");
				unverifyButton.setStyle("-fx-background-color: red; -fx-text-fill: white;");

				// Handle button actions
				verifyButton.setOnAction(event -> controller.handleVerify(getTableRow().getItem()));
				unverifyButton.setOnAction(event -> controller.handleUnverify(getTableRow().getItem()));

				// Add buttons to an HBox
				HBox actionBox = new HBox(10, verifyButton, unverifyButton);
				actionBox.setAlignment(Pos.CENTER);
				setGraphic(actionBox);
			}
		});

		// Populate the table
		loadTableData(unverifiedOrgs, verifyOrgTable);
	}

	private static void loadTableData(UnverifiedOrgs unverifiedOrgs, TableView<OrgRepTableRow> verifyOrgTable) {
		ObservableList<OrgRepTableRow> rows = FXCollections.observableArrayList();

		for (Organisation org : unverifiedOrgs.getUnverifiedOrgs()) {
			OrganisationRepresentative rep = unverifiedOrgs.getUnverifiedRepresentatives().stream()
					.filter(r -> r.getOrgID().equals(org.getName())).findFirst().orElse(null);

			rows.add(new OrgRepTableRow(org, rep));
		}

		verifyOrgTable.setItems(rows);
	}

	public static VBox createJobOpportunitiesUI(Map<Integer, String> jobTitles, VBox vBox,
			StudentOpportunitiesController controller) {
		// Clear the VBox before adding new content
		vBox.getChildren().clear();

		// Iterate over the job titles map to create UI elements
		for (Map.Entry<Integer, String> entry : jobTitles.entrySet()) {
			String jobTitle = entry.getValue();

			HBox hBox = new HBox(20);
			hBox.setStyle("-fx-padding: 20;");

			// Label for the job title
			Label jobLabel = new Label(jobTitle);
			jobLabel.setStyle("-fx-text-fill: white; -fx-font-size: 18px;");
			hBox.getChildren().add(jobLabel);

			Button viewJobDescriptionButton = new Button("Description");
			viewJobDescriptionButton.setStyle(
					"-fx-background-color: transparent; -fx-border-color: #4A44F2; -fx-border-radius: 40; -fx-text-fill: white; -fx-font-size: 17px;");
			viewJobDescriptionButton.setOnAction(e -> controller.viewJobDescription(e));
			hBox.getChildren().add(viewJobDescriptionButton);

			// "Apply" button
			Button applyButton = new Button("Apply");
			applyButton.setStyle(
					"-fx-background-color: #4A44F2; -fx-border-radius: 40; -fx-text-fill: white; -fx-font-size: 17px;");
			applyButton.setOnAction(e -> controller.applyForJob(e));
			hBox.getChildren().add(applyButton);

			// Add the HBox to the VBox
			vBox.getChildren().add(hBox);
		}

		// Return the updated VBox
		return vBox;
	}

	public static void showNotificationsForAdmin(VBox notificationVBox, HBox notificationHBox,
			List<Notification> notifications, AdminDashboardController adminDashboardController) {
		// TODO Auto-generated method stub

		// Ensure notificationHBox is disabled initially, as it's the template
		notificationHBox.setVisible(false); // Hide the original template
		notificationHBox.setManaged(false); // Prevent it from affecting layout

		// Load notifications dynamically

		// Add each notification to the VBox
		for (Notification notification : notifications) {
			addNotificationToVBox(notification, notificationHBox, notificationVBox, adminDashboardController);
		}

	}

	private static void addNotificationToVBox(Notification notification, HBox notificationHBox, VBox notificationVBox,
			AdminDashboardController controller) {
		try {
			// Clone the HBox
			HBox newNotificationHBox = new HBox();

			// Copy layout properties from the template HBox
			newNotificationHBox.setAlignment(notificationHBox.getAlignment());
			newNotificationHBox.setPrefWidth(notificationHBox.getPrefWidth());
			newNotificationHBox.setPrefHeight(notificationHBox.getPrefHeight());
			newNotificationHBox.setMinWidth(notificationHBox.getMinWidth());
			newNotificationHBox.setMinHeight(notificationHBox.getMinHeight());
			newNotificationHBox.setMaxWidth(notificationHBox.getMaxWidth());
			newNotificationHBox.setMaxHeight(notificationHBox.getMaxHeight());
			newNotificationHBox.setStyle(notificationHBox.getStyle()); // Copy CSS style
			newNotificationHBox.setSpacing(notificationHBox.getSpacing()); // Copy spacing
			newNotificationHBox.setPadding(notificationHBox.getPadding()); // Copy padding

			// Add a margin at the top
			VBox.setMargin(newNotificationHBox, new Insets(10, 0, 0, 0));

			// Clone the Label
			Label templateLabel = (Label) notificationHBox.getChildren().get(0);
			Label notificationLabel = new Label(notification.getMessage());
			notificationLabel.setPrefWidth(templateLabel.getPrefWidth());
			notificationLabel.setPrefHeight(templateLabel.getPrefHeight());
			notificationLabel.setMinWidth(templateLabel.getMinWidth());
			notificationLabel.setMinHeight(templateLabel.getMinHeight());
			notificationLabel.setMaxWidth(templateLabel.getMaxWidth());
			notificationLabel.setMaxHeight(templateLabel.getMaxHeight());
			notificationLabel.setStyle(templateLabel.getStyle()); // Copy CSS style
			notificationLabel.setFont(templateLabel.getFont()); // Copy font
			notificationLabel.setTextAlignment(templateLabel.getTextAlignment());
			notificationLabel.setAlignment(templateLabel.getAlignment());
			notificationLabel.setPadding(templateLabel.getPadding());

			// Enable wrapping for the Label
			notificationLabel.setWrapText(true); // Allow text to wrap

			// Ensure the Label expands within the HBox
			HBox.setHgrow(notificationLabel, Priority.ALWAYS);

			// Clone the Button
			Button templateButton = (Button) notificationHBox.getChildren().get(1);
			Button closeButton = new Button(templateButton.getText());
			closeButton.setPrefWidth(templateButton.getPrefWidth());
			closeButton.setPrefHeight(templateButton.getPrefHeight());
			closeButton.setMinWidth(templateButton.getMinWidth());
			closeButton.setMinHeight(templateButton.getMinHeight());
			closeButton.setMaxWidth(templateButton.getMaxWidth());
			closeButton.setMaxHeight(templateButton.getMaxHeight());
			closeButton.setStyle(templateButton.getStyle()); // Copy CSS style
			closeButton.setFont(templateButton.getFont()); // Copy font
			closeButton.setAlignment(templateButton.getAlignment());
			closeButton.setPadding(templateButton.getPadding());
			closeButton.setOnAction(event -> notificationVBox.getChildren().remove(newNotificationHBox));

			// Remove the newNotificationHBox when the close button is clicked
			closeButton.setOnAction(event -> {
				notificationVBox.getChildren().remove(newNotificationHBox);
				notification.setRead(true);
				controller.markNotification(notification.getNotificationId());
			});

			// Add the Label and Button to the cloned HBox
			newNotificationHBox.getChildren().addAll(notificationLabel, closeButton);

			// Add the cloned HBox to the VBox
			notificationVBox.getChildren().add(newNotificationHBox);
		} catch (Exception e) {
			e.printStackTrace(); // Log any errors
		}
	}

	public void displayStudentChats(VBox chatsVBox, HBox chatsHBox, Student student, ChatBoxController controller) {

		// Disable template HBox initially
		chatsHBox.setVisible(false);
		chatsHBox.setManaged(false);

		// Add each chat to the VBox
		for (Chat chat : student.getMyChatBox().getChats()) {
			addChatToVBox(chat, chatsHBox, chatsVBox, student.getMyChatBox(), controller);
		}
	}

	public void displayOrganisationChats(VBox chatsVBox, HBox chatsHBox, Organisation organisation,
			ChatBoxController controller) {

		// Disable template HBox initially
		chatsHBox.setVisible(false);
		chatsHBox.setManaged(false);

		// Add each chat to the VBox
		for (Chat chat : organisation.getMyChatBox().getChats()) {
			addChatToVBox(chat, chatsHBox, chatsVBox, organisation.getMyChatBox(), controller);
		}
	}

	private void addChatToVBox(Chat chat, HBox chatsHBox, VBox chatsVBox, ChatBox chatBox,
			ChatBoxController controller) {
		try {
			// Clone the HBox
			HBox newChatHBox = new HBox();

			// Copy layout properties from the template HBox
			newChatHBox.setAlignment(chatsHBox.getAlignment());
			newChatHBox.setPrefWidth(chatsHBox.getPrefWidth());
			newChatHBox.setPrefHeight(chatsHBox.getPrefHeight());
			newChatHBox.setMinWidth(chatsHBox.getMinWidth());
			newChatHBox.setMinHeight(chatsHBox.getMinHeight());
			newChatHBox.setMaxWidth(chatsHBox.getMaxWidth());
			newChatHBox.setMaxHeight(chatsHBox.getMaxHeight());
			newChatHBox.setStyle(chatsHBox.getStyle()); // Copy CSS style
			newChatHBox.setSpacing(chatsHBox.getSpacing()); // Copy spacing
			newChatHBox.setPadding(chatsHBox.getPadding()); // Copy padding

			// Add a margin at the top
			VBox.setMargin(newChatHBox, new Insets(10, 0, 0, 0));

			// Clone the Label (template label from the original HBox)
			Label templateLabel = (Label) chatsHBox.getChildren().get(0);
			Label chatLabel = new Label(chat.getOrgId()); // Set label text to Org name or Student name here
			chatLabel.setPrefWidth(templateLabel.getPrefWidth());
			chatLabel.setPrefHeight(templateLabel.getPrefHeight());
			chatLabel.setMinWidth(templateLabel.getMinWidth());
			chatLabel.setMinHeight(templateLabel.getMinHeight());
			chatLabel.setMaxWidth(templateLabel.getMaxWidth());
			chatLabel.setMaxHeight(templateLabel.getMaxHeight());
			chatLabel.setStyle(templateLabel.getStyle()); // Copy CSS style
			chatLabel.setFont(templateLabel.getFont()); // Copy font
			chatLabel.setTextAlignment(templateLabel.getTextAlignment());
			chatLabel.setAlignment(templateLabel.getAlignment());
			chatLabel.setPadding(templateLabel.getPadding());

			// Ensure the Label expands within the HBox
			HBox.setHgrow(chatLabel, Priority.ALWAYS);

			// Add the cloned Label to the cloned HBox
			newChatHBox.getChildren().add(chatLabel);

			newChatHBox.setOnMouseClicked(event -> {
				// Call the action in the controller
				controller.handleChatClick(chat);
			});
			// Add the cloned HBox to the VBox
			chatsVBox.getChildren().add(newChatHBox);
		} catch (Exception e) {
			e.printStackTrace(); // Log any errors
		}
	}

	public static void showStudentMsgs(Student student, Chat chat, VBox msgVBox, HBox receivedMsgHBox, HBox sentMsgHBox,
			ChatBoxController controller) {
		
		// Clear previous messages in the VBox
		msgVBox.getChildren().clear();

		// Iterate through messages in the chat
		for (Message message : chat.getMessages()) {
			// Determine if the student is the sender or receiver
			if (message.getSenderId().equals(student.getRollNo())) {
				// Student is the sender, use sent message template
				addMsgToVBox(message, sentMsgHBox, msgVBox);
			} else if (message.getReceiverId().equals(student.getRollNo())) {
				// Student is the receiver, use received message template
				addMsgToVBox(message, receivedMsgHBox, msgVBox);
			}
		}
	}

	public static void showOrgMsgs(Organisation organisation, Chat chat, VBox msgVBox, HBox receivedMsgHBox,
			HBox sentMsgHBox, ChatBoxController controller) {

		
		// Clear previous messages in the VBox
		msgVBox.getChildren().clear();

		// Iterate through messages in the chat
		for (Message message : chat.getMessages()) {
			// Determine if the organisation is the sender or receiver
			if (message.getSenderId().equals(organisation.getName())) {
				// Organisation is the sender, use sent message template
				addMsgToVBox(message, sentMsgHBox, msgVBox);
			} else if (message.getReceiverId().equals(organisation.getName())) {
				// Organisation is the receiver, use received message template
				addMsgToVBox(message, receivedMsgHBox, msgVBox);
			}
		}
	}

	// Helper method to add a message to the VBox
	private static void addMsgToVBox(Message message, HBox templateHBox, VBox msgVBox) {
		try {
			// Clone the HBox for a new message
			HBox newMsgHBox = new HBox();

			// Copy layout properties from the template
			newMsgHBox.setAlignment(templateHBox.getAlignment());
			newMsgHBox.setPrefWidth(templateHBox.getPrefWidth());
			newMsgHBox.setPrefHeight(templateHBox.getPrefHeight());
			newMsgHBox.setMinWidth(templateHBox.getMinWidth());
			newMsgHBox.setMinHeight(templateHBox.getMinHeight());
			newMsgHBox.setMaxWidth(templateHBox.getMaxWidth());
			newMsgHBox.setMaxHeight(templateHBox.getMaxHeight());
			newMsgHBox.setStyle(templateHBox.getStyle()); // Copy CSS style
			newMsgHBox.setSpacing(templateHBox.getSpacing()); // Copy spacing
			newMsgHBox.setPadding(templateHBox.getPadding()); // Copy padding

			// Clone the Label for the message text
			Label templateLabel = (Label) templateHBox.getChildren().get(0);
			Label messageLabel = new Label(message.getText());
			messageLabel.setPrefWidth(templateLabel.getPrefWidth());
			messageLabel.setPrefHeight(templateLabel.getPrefHeight());
			messageLabel.setMinWidth(templateLabel.getMinWidth());
			messageLabel.setMinHeight(templateLabel.getMinHeight());
			messageLabel.setMaxWidth(templateLabel.getMaxWidth());
			messageLabel.setMaxHeight(templateLabel.getMaxHeight());
			messageLabel.setStyle(templateLabel.getStyle()); // Copy CSS style
			messageLabel.setFont(templateLabel.getFont()); // Copy font
			messageLabel.setTextAlignment(templateLabel.getTextAlignment());
			messageLabel.setAlignment(templateLabel.getAlignment());
			messageLabel.setPadding(templateLabel.getPadding());

			// Add the cloned Label to the new HBox
			newMsgHBox.getChildren().add(messageLabel);

			// Add a margin to separate messages
			VBox.setMargin(newMsgHBox, new Insets(10, 0, 0, 0));

			// Add the new HBox to the VBox
			msgVBox.getChildren().add(newMsgHBox);
		} catch (Exception e) {
			e.printStackTrace(); // Log any errors
		}
	}

}
