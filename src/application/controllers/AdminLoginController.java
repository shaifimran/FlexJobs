package application.controllers;

import application.Admin;
import application.factory.DBFactory;
import application.handlers.DBHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.HBox;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;

public class AdminLoginController {

	private DBHandler dbHandler = DBFactory.getInstance();
	@FXML
	private TextField adminEmail;
	@FXML
	private PasswordField adminPassword;

	@FXML
	private HBox adminErrorHbox;

	@FXML
	private Label adminErrorLabel;

	@FXML
	private Button loginButton;

	// Event Listener on Button.onAction
	@FXML
	public void loginAdmin(ActionEvent event) {
		String email = adminEmail.getText();
		String password = adminPassword.getText();

		Admin admin = authenticateAdmin(email, password);
		if (admin != null) {
			// Proceed to the admin dashboard
			showAlert(AlertType.INFORMATION, "Login Successful", "Welcome, Admin!");

			// Add code to navigate to the admin dashboard here
			this.navigateToDashboard(admin);
		} else {
			// Display an error message
			adminErrorLabel.setText("Enter a valid email or password");
			showLabel();
		}
	}

	private Admin authenticateAdmin(String email, String password) {
		Admin admin = dbHandler.getAdminInfo(email, password);

		if (admin != null) {
			return admin;
		}

		return null;
	}

	private void navigateToDashboard(Admin admin) {
		try {
			// Load Admin Dashboard FXML
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/UI/AdminDashboard.fxml"));
			loader.setControllerFactory(c -> new AdminDashboardController(admin));
			Parent root = loader.load();

			// Get current stage
			Stage stage = (Stage) adminEmail.getScene().getWindow();
			// Set new scene for Admin Dashboard
			Scene dashboardScene = new Scene(root);
			stage.setScene(dashboardScene);
			stage.setTitle("Admin Dashboard");
		} catch (Exception e) {
			e.printStackTrace();
			showAlert(AlertType.ERROR, "Error", "Failed to load Admin Dashboard.");
		}
	}

	private void showAlert(AlertType alertType, String title, String message) {
		Alert alert = new Alert(alertType);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}

	private void showLabel() {

		adminErrorHbox.setVisible(true);

		PauseTransition pause = new PauseTransition(Duration.millis(3000));
		pause.setOnFinished(event -> adminErrorHbox.setVisible(false));
		pause.playFromStart();
	}

}
