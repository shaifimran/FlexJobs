package application.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class MainApplicationController {
	@FXML
	private Button adminNavButton;
	@FXML
	private Button orgNavButton;
	@FXML
	private Button studentNavButton;

	// Event Listener on Button[#adminNavButton].onAction
	@FXML
	public void navigateToAdmin(ActionEvent event) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/application/UI/AdminLogin.fxml"));
			Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Event Listener on Button[#orgNavButton].onAction
	@FXML
	public void navigateToOrganization(ActionEvent event) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/application/UI/OrgRepLogin.fxml"));
			Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void navigateToStudent(ActionEvent event) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/application/UI/StudentLogin.fxml"));
			Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
