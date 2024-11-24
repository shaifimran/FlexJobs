package application.controllers;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import application.Student;
import application.handlers.DBHandler;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Duration;

public class ManageStudentProfileController {
	DBHandler dbHandler = new DBHandler();
	@FXML
	private Button studentRegisterButton;
	@FXML
	private PasswordField studentPassword;
	@FXML
	private TextField studentDepartment;
	@FXML
	private TextField studentSemester;
	@FXML
	private TextField studentCGPA;
	@FXML
	private HBox studentErrorHbox;
	@FXML
	private Label studentErrorLabel;

	private Student student;

	public ManageStudentProfileController(Student student) {
		this.student = student;
	}

	@FXML
	public void manageStudent(ActionEvent event) {
		Map<String, Object> updates = new HashMap<>();
		if (!studentPassword.getText().isEmpty()) {
			if (studentPassword.getText().length() < 6) {
				studentErrorLabel.setText("Password must be at least 6 characters.");
				showLabel();
				return;
			}
			updates.put("password", studentPassword.getText());
		}

		if (!studentDepartment.getText().isEmpty()) {
			updates.put("department", studentPassword.getText());
		}

		if (!studentSemester.getText().isEmpty()) {
			if (!studentSemester.getText().matches("^\\d+$")) {

				studentErrorLabel.setText("Semester must be a number.");
				showLabel();
				return;
			} else if (Integer.parseInt(studentSemester.getText()) < 1
					|| Integer.parseInt(studentSemester.getText()) > 8) {
				studentErrorLabel.setText("Enter a valid Semester.");
				showLabel();
				return;
			} else {
				updates.put("semester", Integer.parseInt(studentSemester.getText()));
			}
		}

		if (!studentCGPA.getText().isEmpty()) {
			if (!studentCGPA.getText().matches("^\\d*\\.?\\d+$")) {
				studentErrorLabel.setText("Enter a valid CGPA.");
				showLabel();
				return;
			} else if (Double.parseDouble(studentCGPA.getText()) < 0.0
					|| Double.parseDouble(studentCGPA.getText()) > 4.0) {
				studentErrorLabel.setText("Enter valid CGPA.");
				showLabel();
				return;
			}
			updates.put("cgpa", Double.parseDouble(studentCGPA.getText()));
		}
		if (!updates.isEmpty()) {
			Boolean success = dbHandler.updateStudentData(student.getEmail(), updates);
			if (success) {
				showAlert(AlertType.INFORMATION, "Update", "Your information was successfully updated!");
			} else {
				showAlert(AlertType.ERROR, "Error", "Update failed. Please try again.");
			}
		}
	}

	public void showLabel() {
		studentErrorHbox.setVisible(true);
		PauseTransition pause = new PauseTransition(Duration.millis(3000));
		pause.setOnFinished(event -> studentErrorHbox.setVisible(false));
		pause.playFromStart();
	}

	private void showAlert(AlertType alertType, String title, String message) {
		Alert alert = new Alert(alertType);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(message);
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
