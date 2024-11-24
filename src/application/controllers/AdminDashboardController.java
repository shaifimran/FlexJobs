package application.controllers;

import java.util.List;

import application.Admin;
import application.Notification;
import application.UI.UIFactory;
import application.handlers.DBHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class AdminDashboardController {

	DBHandler dbHandler = new DBHandler();

	private Admin admin;

	@FXML
	private Label adminUsername;

	@FXML
	private Label verifiedOrgNum;

	@FXML
	private VBox notificationVBox;

	@FXML
	private HBox notificationHBox;

	@FXML
	private Button markAllAsRead;

	private List<Notification> notifications;

	public AdminDashboardController(Admin admin) {
		this.admin = admin;
	}

	// The initialize() method is called after the FXML is loaded and fields are
	// injected
	@FXML
	public void initialize() {
		// Set the admin username in the Label after FXML fields are injected
		if (admin != null && adminUsername != null) {
			adminUsername.setText("Username: " + this.admin.getName());
		}

		this.verifiedOrgNum.setText(dbHandler.getUnverifiedOrgCount());

		notifications = dbHandler.getNotificationsForAdmin();
		UIFactory.getInstance().showNotificationsForAdmin(notificationVBox, notificationHBox, notifications, this);

	}

	@FXML
	public void openVerifyOrg(ActionEvent event) {
		try {
			// Load Admin Dashboard FXML
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/UI/AdminVerifyOrganisation.fxml"));
			loader.setControllerFactory(c -> new AdminVerifyOrganisationController(admin));
			Parent root = loader.load();

			// Get current stage
			Stage stage = (Stage) verifiedOrgNum.getScene().getWindow();
			// Set new scene for Admin Dashboard
			Scene dashboardScene = new Scene(root);
			stage.setScene(dashboardScene);
			stage.setTitle("Verify Organisation");
		} catch (Exception e) {
			e.printStackTrace();
			showAlert(AlertType.ERROR, "Error", "Failed to load Verify Organisation.");
		}
	}

	@FXML
	public void openGenerateReport(ActionEvent event) {
		try {
			// Load Admin Dashboard FXML
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/UI/AdminGenerateReports.fxml"));
			Parent root = loader.load();

			// Get current stage
			Stage stage = (Stage) verifiedOrgNum.getScene().getWindow();
			// Set new scene for Admin Dashboard
			Scene dashboardScene = new Scene(root);
			stage.setScene(dashboardScene);
			stage.setTitle("Generate Reports");
		} catch (Exception e) {
			e.printStackTrace();
			showAlert(AlertType.ERROR, "Error", "Failed to load Generate Reports.");
		}
	}

	private void showAlert(AlertType alertType, String title, String message) {
		Alert alert = new Alert(alertType);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}

	public void markNotification(int notificationId) {
		// TODO Auto-generated method stub
		dbHandler.markNotificationAsRead(notificationId);

	}

	public void markAllNotificationsAsRead(ActionEvent event) {
		 // Remove all notifications from the VBox
	    notificationVBox.getChildren().clear();  // Clears all child nodes from the VBox
		for (Notification notification : notifications) {
			if (!notification.isRead()) {
				dbHandler.markNotificationAsRead(notification.getNotificationId());
			}
		}
	}
}
