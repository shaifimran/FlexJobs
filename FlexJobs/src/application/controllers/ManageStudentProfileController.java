package application.controllers;

import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.util.Duration;

public class ManageStudentProfileController {
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

	// Event Listener on Button[#studentRegisterButton].onAction
	@FXML
	public void manageStudent(ActionEvent event) {
		if (studentPassword.getText().isEmpty() || studentPassword.getText().length() < 6) {
			studentErrorLabel.setText("Must be at least 6 characters.");
			showLabel();
			return;
		}

		else if (studentDepartment.getText().isEmpty()) {
			studentErrorLabel.setText("Enter the department.");
			showLabel();
			return;
		}

		else if (studentSemester.getText().isEmpty() || !studentSemester.getText().matches("^\\d+$")) {
			studentErrorLabel.setText("Semester must be a number.");
			showLabel();
			return;
		}

		else if (Integer.parseInt(studentSemester.getText()) < 1 || Integer.parseInt(studentSemester.getText()) > 8) {
			studentErrorLabel.setText("Enter a valid Semester.");
			showLabel();
			return;
		}
		else if (studentCGPA.getText().isEmpty() || !studentCGPA.getText().matches("^\\d*\\.?\\d+$")) {
			studentErrorLabel.setText("Enter a valid CGPA.");
			showLabel();
			return;

		} else if (Double.parseDouble(studentCGPA.getText()) < 0.0 || Double.parseDouble(studentCGPA.getText()) > 4.0) {
			studentErrorLabel.setText("Enter valid CGPA.");
			showLabel();
			return;
		}
	}

	public void showLabel() {
		studentErrorHbox.setVisible(true);
		PauseTransition pause = new PauseTransition(Duration.millis(3000));
		pause.setOnFinished(event -> studentErrorHbox.setVisible(false));
		pause.playFromStart();
	}
}
