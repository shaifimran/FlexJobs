package application.UI;

import java.util.List;
import java.util.Map;

import application.ApplicationWithOpportunity;
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
import application.controllers.StudentNotificationController;
import application.controllers.StudentOpportunitiesController;
import application.controllers.StudentViewStatusController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

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

	public static VBox createJobOpportunitiesUI(Map<Integer, String> jobData, VBox vbox,
			StudentOpportunitiesController controller) {

		vbox.getChildren().clear();

		for (Map.Entry<Integer, String> entry : jobData.entrySet()) {
			HBox hBox = new HBox();
			hBox.setAlignment(Pos.CENTER);
			hBox.setMaxHeight(100);
			hBox.setMinHeight(100);
			hBox.setPrefHeight(101);
			hBox.setPrefWidth(1114);
			hBox.setStyle("-fx-background-color: none;");

			Label jobLabel = new Label(entry.getValue());
			jobLabel.setPrefHeight(28);
			jobLabel.setPrefWidth(509);
			jobLabel.setStyle("-fx-text-fill: white; -fx-font-size: 26px; -fx-font-family: 'Lorin Bold';");

			Region region1 = new Region();
			region1.setPrefWidth(271);
			region1.setPrefHeight(80);

			Region region2 = new Region();
			region2.setPrefWidth(34);
			region2.setPrefHeight(80);

			Button viewDescriptionButton = new Button("Description");
			viewDescriptionButton.setStyle("-fx-background-color: transparent; "
					+ "-fx-border-color: #4A44F2; -fx-border-radius: 40; -fx-text-fill: white; "
					+ "-fx-font-family: 'Lorin Bold'; -fx-font-size: 17px;");
			viewDescriptionButton.setPrefHeight(50);
			viewDescriptionButton.setPrefWidth(130);
			viewDescriptionButton.setMaxHeight(50);
			viewDescriptionButton.setUserData(entry.getKey());
			viewDescriptionButton.setOnAction(event -> {
				controller.viewDescription(event);
			});

			// Create the 'Apply' Button
			Button applyButton = new Button("Apply");
			applyButton.setPrefHeight(58);
			applyButton.setPrefWidth(130);
			applyButton.setStyle("-fx-background-color: #4A44F2; -fx-background-radius: 40; "
					+ "-fx-text-fill: white; -fx-font-family: 'Lorin Bold'; -fx-font-size: 17px;");
			applyButton.setUserData(entry.getKey());
			applyButton.setOnAction(event -> {
				controller.applyForJob(event);
			});

			// Add the Label and Buttons to HBox
			hBox.getChildren().addAll(jobLabel, region1, viewDescriptionButton, region2, applyButton);

			// Add the HBox to the VBox
			vbox.getChildren().add(hBox);
		}

		return vbox; // Return the updated VBox
	}

	public static VBox createEducationalOpportunitiesUI(Map<Integer, String> educationalData, VBox vbox,
			StudentOpportunitiesController controller) {

		vbox.getChildren().clear();

		for (Map.Entry<Integer, String> entry : educationalData.entrySet()) {
			HBox hBox = new HBox();
			hBox.setAlignment(Pos.CENTER);
			hBox.setMaxHeight(100);
			hBox.setMinHeight(100);
			hBox.setPrefHeight(101);
			hBox.setPrefWidth(1114);
			hBox.setStyle("-fx-background-color: none;");

			Label jobLabel = new Label(entry.getValue());
			jobLabel.setPrefHeight(28);
			jobLabel.setPrefWidth(509);
			jobLabel.setStyle("-fx-text-fill: white; -fx-font-size: 26px; -fx-font-family: 'Lorin Bold';");
			// Create the spacer (Region)
			Region region1 = new Region();
			region1.setPrefWidth(400);
			region1.setPrefHeight(80);

			Button viewDescription = new Button("Description");
			viewDescription.setPrefHeight(58);
			viewDescription.setPrefWidth(132);
			viewDescription.setStyle("-fx-background-color: #4A44F2; -fx-background-radius: 40; "
					+ "-fx-text-fill: white; -fx-font-family: 'Lorin Bold'; -fx-font-size: 17px;");
			viewDescription.setUserData(entry.getKey());
			viewDescription.setOnAction(event -> {
				controller.viewDescription(event);
			});

			hBox.getChildren().addAll(jobLabel, region1, viewDescription);

			vbox.getChildren().add(hBox);
		}
		return vbox;
	}

	public static VBox generateApplicationUI(List<ApplicationWithOpportunity> appOpp,
			StudentViewStatusController controller, VBox vbox) {
		vbox.getChildren().clear();

		for (ApplicationWithOpportunity appO : appOpp) {
			HBox hBox = new HBox();
			hBox.setAlignment(Pos.CENTER);
			hBox.setMaxHeight(100);
			hBox.setMinHeight(100);
			hBox.setPrefHeight(101);
			hBox.setPrefWidth(1114);
			hBox.setStyle("-fx-background-color: none;");

			Label jobLabel = new Label(appO.getOpportunity().getTitle() + " | " + appO.getOpportunity().getPostedBy());
			jobLabel.setPrefHeight(28);
			jobLabel.setPrefWidth(509);
			jobLabel.setStyle("-fx-text-fill: white; -fx-font-size: 20px; -fx-font-family: 'Lorin Bold';");

			Region region1 = new Region();
			region1.setPrefWidth(271);
			region1.setPrefHeight(80);

			Region region2 = new Region();
			region2.setPrefWidth(34);
			region2.setPrefHeight(80);

			Button viewStatus = new Button(appO.getApplication().getStatus());
			viewStatus.setStyle("-fx-background-color: transparent; "
					+ "-fx-border-color: #4A44F2; -fx-border-radius: 40; -fx-text-fill: white; "
					+ "-fx-font-family: 'Lorin Bold'; -fx-font-size: 17px;");
			viewStatus.setPrefHeight(50);
			viewStatus.setPrefWidth(130);
			viewStatus.setMaxHeight(50);
//			viewStatus.setUserData(entry.getKey());

			Button viewDetails = new Button("View Status");
			viewDetails.setPrefHeight(58);
			viewDetails.setPrefWidth(130);
			viewDetails.setStyle("-fx-background-color: #4A44F2; -fx-background-radius: 40; "
					+ "-fx-text-fill: white; -fx-font-family: 'Lorin Bold'; -fx-font-size: 17px;");

			viewDetails.setUserData(appO.getApplication().getStatus());
			viewDetails.setOnAction(event -> {
				controller.viewJobStatusDetail(event);
			});

			// Add the Label and Buttons to HBox
			hBox.getChildren().addAll(jobLabel, region1, viewStatus, region2, viewDetails);

			// Add the HBox to the VBox
			vbox.getChildren().add(hBox);
		}
		return vbox;

	}

	public static VBox generateNotificationUI(List<Notification> notifications,
			StudentNotificationController controller, VBox vbox) {
		vbox.getChildren().clear();

		for (Notification n : notifications) {
			if (!n.isRead()) {
				HBox hBox = new HBox();
				hBox.setAlignment(Pos.CENTER);
				hBox.setMaxHeight(100);
				hBox.setMinHeight(100);
				hBox.setPrefHeight(101);
				hBox.setPrefWidth(1114);
				hBox.setStyle("-fx-background-color: none;");

				Label notifLabel = new Label(n.getSenderID());
				notifLabel.setPrefHeight(28);
				notifLabel.setPrefWidth(509);
				notifLabel.setStyle("-fx-text-fill: white; -fx-font-size: 20px; -fx-font-family: 'Lorin Bold';");

				Region region1 = new Region();
				region1.setPrefWidth(271);
				region1.setPrefHeight(80);

				Region region2 = new Region();
				region2.setPrefWidth(34);
				region2.setPrefHeight(80);

				Button markRead = new Button("Mark as Read");
				markRead.setStyle("-fx-background-color: transparent; "
						+ "-fx-border-color: #4A44F2; -fx-border-radius: 40; -fx-text-fill: white; "
						+ "-fx-font-family: 'Lorin Bold'; -fx-font-size: 17px;");
				markRead.setPrefHeight(50);
				markRead.setPrefWidth(150);
				markRead.setMaxHeight(50);
				markRead.setUserData(n.getNotificationId());
				markRead.setOnAction(event -> {
					controller.markAsRead(event);
				});
				Button expand = new Button("Expand");
				expand.setPrefHeight(58);
				expand.setPrefWidth(130);
				expand.setStyle("-fx-background-color: #4A44F2; -fx-background-radius: 40; "
						+ "-fx-text-fill: white; -fx-font-family: 'Lorin Bold'; -fx-font-size: 17px;");

				expand.setUserData(n);
				expand.setOnAction(event -> {
					controller.expandNotification(event);
				});

				// Add the Label and Buttons to HBox
				hBox.getChildren().addAll(notifLabel, region1, markRead, region2, expand);

				// Add the HBox to the VBox
				vbox.getChildren().add(hBox);
			}
		}
		return vbox;

	}

	public static void fillStdDashBoard(Double cgpa, Map<String, Integer> externalInfo, VBox profileSettingsVBox,
			VBox opportunitiesVBox, VBox applicationsVBox, VBox messagesVBox) {
		Label cgpaLabel = (Label) profileSettingsVBox.getChildren().get(1);
		cgpaLabel.setText(String.format("%.2f", cgpa));
		Label opportunities = (Label) opportunitiesVBox.getChildren().get(1);
		opportunities.setText(String.valueOf(externalInfo.get("opportunities")));
		Label applications = (Label) applicationsVBox.getChildren().get(1);
		applications.setText(String.valueOf(externalInfo.get("applications")));
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
			addChatToVBox(chat, chatsHBox, chatsVBox, student.getMyChatBox(), controller, true);
		}
	}

	public void displayOrganisationChats(VBox chatsVBox, HBox chatsHBox, Organisation organisation,
			ChatBoxController controller) {

		// Disable template HBox initially
		chatsHBox.setVisible(false);
		chatsHBox.setManaged(false);

		// Add each chat to the VBox
		for (Chat chat : organisation.getMyChatBox().getChats()) {
			addChatToVBox(chat, chatsHBox, chatsVBox, organisation.getMyChatBox(), controller, false);
		}
	}

	private void addChatToVBox(Chat chat, HBox chatsHBox, VBox chatsVBox, ChatBox chatBox, ChatBoxController controller,
			Boolean isStudent) {
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
			Label chatLabel;
			if (isStudent) {
				chatLabel = new Label(chat.getOrgId());
			} else {
				chatLabel = new Label(chat.getStudentId());
			}
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
			if (message.getSenderId().equalsIgnoreCase(organisation.getName())) {
				// Organisation is the sender, use sent message template
				addMsgToVBox(message, sentMsgHBox, msgVBox);
			} else if (message.getReceiverId().equalsIgnoreCase(organisation.getName())) {
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

	public static void populateDeps(List<String> deps, ComboBox<String> depComboBox) {
		ObservableList<String> observableDeps = FXCollections.observableList(deps);
		depComboBox.setItems(observableDeps);
	}

	public static void populateCategories(List<String> cat, ComboBox<String> categoryComboBox) {
		ObservableList<String> observableDeps = FXCollections.observableList(cat);
		categoryComboBox.setItems(observableDeps);

	}

}
