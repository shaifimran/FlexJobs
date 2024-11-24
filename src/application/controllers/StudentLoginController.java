package application.controllers;

import java.io.IOException;

import application.Student;
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

public class StudentLoginController {
	DBHandler dbHandler = new DBHandler();
	@FXML
	private TextField studentEmail;
	@FXML
	private PasswordField studentPassword;
	@FXML
	private Button studentLoginButton;
	@FXML
	private HBox studentErrorHbox;
	@FXML
	private Label studentErrorLabel;
	@FXML
	Button studentRegisterButton;

	@FXML
	public void handleLogin(ActionEvent event) {
		if (studentEmail.getText().isEmpty() || !studentEmail.getText().matches("^[a-zA-Z0-9]+@nu\\.edu\\.pk$")) {
			studentErrorLabel.setText("Enter a valid email");
			showLabel();
		} else if (studentPassword.getText().isEmpty()) {
			studentErrorLabel.setText("Enter your password");
			showLabel();
		} else {
			Student s = dbHandler.authenticateStudent(studentEmail.getText(), studentPassword.getText());
			if (s == null) {
				studentErrorLabel.setText("Login Unsuccessful.");
				showLabel();
				return;
			} else {
				try {
					FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/UI/StudentDashboard.fxml"));
					loader.setControllerFactory(c -> new StudentDashboardController(s));
					Parent root = loader.load();
					Stage stage = (Stage) studentLoginButton.getScene().getWindow();
					Scene scene = new Scene(root);

					stage.setScene(scene);
					stage.show();
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		}
	}

	public void showLabel() {

		studentErrorHbox.setVisible(true);

		PauseTransition pause = new PauseTransition(Duration.millis(3000));

		pause.setOnFinished(event -> studentErrorHbox.setVisible(false));

		pause.playFromStart();
	}

	@FXML
	public void handleRegistration(ActionEvent event) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/application/UI/StudentRegistration.fxml"));
			Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
