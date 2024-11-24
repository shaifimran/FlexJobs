package application.controllers;

import java.io.IOException;
import java.util.List;

import application.Notification;
import application.Student;
import application.UI.UIFactory;
import application.handlers.DBHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class StudentNotificationController {
	private Student student;
	DBHandler dbHandler = new DBHandler();

	public StudentNotificationController(Student student) {
		this.student = student;
	}

	@FXML
	private VBox NotifVBox;
	@FXML
	private Button markAsReadButton;
	@FXML
	private Button expandNotificationButton;

	@FXML
	private void initialize() {
		List<Notification> notifications = dbHandler.fetchStudentNotifications(student.getRollNo());
		if (notifications != null) {
			UIFactory.getInstance();
			UIFactory.generateNotificationUI(notifications, this, NotifVBox);
		}
	}

	@FXML
	public void markAsRead(ActionEvent event) {
		Button b = (Button) event.getSource();
		int notifID = (int) b.getUserData();
		Boolean success = dbHandler.markRead(student.getRollNo(), notifID);
		if (success) {
			showAlert(AlertType.INFORMATION, "Successful", "Marked as Read.");
			this.initialize();
		} else {
			showAlert(AlertType.ERROR, "Unsuccessful", "An error occurred when marking as read.");
		}
	}

	@FXML
	public void expandNotification(ActionEvent event) {
		Button b = (Button) event.getSource();
		Notification n = (Notification) b.getUserData();
		if (n != null) {
			showExpandedNotification(n);
		} else {
			showAlert(AlertType.ERROR, "Unsuccessful", "An error occurred when expanding the notification.");
		}

	}

	private void showAlert(AlertType alertType, String title, String message) {
		Alert alert = new Alert(alertType);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}

	public void showExpandedNotification(Notification n) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle("Notification Details");
		alert.setHeaderText("Details of the Notifcation");

		String content = "From: " + n.getSenderID() + "\n\n" + "Message: " + n.getMessage() + "\n\n" + "Received at: "
				+ n.getTimestamp();

		TextArea textArea = new TextArea(content);
		textArea.setEditable(false);
		textArea.setWrapText(true);
		textArea.setMaxWidth(Double.MAX_VALUE);
		textArea.setMaxHeight(Double.MAX_VALUE);

		alert.getDialogPane().setContent(textArea);

		alert.showAndWait();
	}

	@FXML
	public void goBackToDashBoard(ActionEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/UI/StudentDashboard.fxml"));
			loader.setControllerFactory(c -> new StudentDashboardController(student));
			Parent root = loader.load();
			Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();

			Scene scene = new Scene(root);

			stage.setScene(scene);
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
