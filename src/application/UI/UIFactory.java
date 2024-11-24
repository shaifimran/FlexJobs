package application.UI;

import java.util.List;
import java.util.Map;

import application.ApplicationWithOpportunity;
import application.Notification;
import application.controllers.StudentNotificationController;
import application.controllers.StudentOpportunitiesController;
import application.controllers.StudentViewStatusController;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
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

				Label notifLabel = new Label(n.getSenderId());
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

}
