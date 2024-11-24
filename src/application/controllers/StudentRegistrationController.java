package application.controllers;

import java.io.IOException;

import application.handlers.DBHandler;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Duration;

public class StudentRegistrationController {
	DBHandler dbHandler = new DBHandler();
	@FXML
	private Button studentRegisterButton;
	@FXML
	private TextField studentRollNo;
	@FXML
	private TextField studentName;
	@FXML
	private TextField studentEmail;
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

	@FXML
	public void registerStudent(ActionEvent event) {

		if (studentRollNo.getText().isEmpty() || !studentRollNo.getText().matches("^\\d{2}i-\\d{4}$")) {
			studentErrorLabel.setText("Enter a valid roll number.");
			showLabel();
			return;
		} else if (studentName.getText().isEmpty() || !studentName.getText().matches("^[a-zA-Z\\s]+$")) {
			studentErrorLabel.setText("Enter a valid name.");
			showLabel();
			return;
		} else if (studentEmail.getText().isEmpty()
				|| !studentEmail.getText().matches("^[a-zA-Z0-9._%+-]+@nu\\.edu\\.pk$")) {
			studentErrorLabel.setText("Enter a valid NU email.");
			showLabel();
			return;
		}

		else if (studentPassword.getText().isEmpty() || studentPassword.getText().length() < 6) {
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

		if (dbHandler.checkStudentExistence(studentEmail.getText()) == null) {
			Boolean success = dbHandler.addStudent(studentRollNo.getText(), studentEmail.getText(),
					studentName.getText(), studentPassword.getText(), studentDepartment.getText(),
					Integer.parseInt(studentSemester.getText()), Double.parseDouble(studentCGPA.getText()));
			if (success) {
				try {

					FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/UI/StudentLogin.fxml"));
					Parent root = loader.load();
					Stage stage = (Stage) studentRegisterButton.getScene().getWindow();
					Scene scene = new Scene(root);

					stage.setScene(scene);
					stage.show();
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				studentErrorLabel.setText("Please try again.");
				showLabel();
				return;
			}
		} else {
			studentErrorLabel.setText("Student already exists.");
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