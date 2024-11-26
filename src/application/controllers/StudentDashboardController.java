package application.controllers;

import java.io.IOException;
import java.util.Map;

import application.Student;
import application.factory.DBFactory;
import application.factory.UIFactory;
import application.handlers.DBHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.input.SwipeEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class StudentDashboardController {
	private Student student;
	private DBHandler dbHandler = DBFactory.getInstance();

	public StudentDashboardController(Student student) {
		this.student = student;
	}

	@FXML
	private Label studentName;
	@FXML
	Hyperlink checkStatusButton;
	@FXML
	Hyperlink studentNotificationButton;
	@FXML
	Label studentCGPA;
	@FXML
	Label opportunitiesAvailable;
	@FXML
	Label submittedApplications;
	@FXML
	Label studentMessages;
	@FXML
	VBox ProfileSettingsVBox;
	@FXML
	VBox opportunitiesVBox;
	@FXML
	VBox applicationsVBox;
	@FXML
	VBox messagesVBox;
	
	@FXML
	private Hyperlink chatBoxButton;

	@FXML
	public void initialize() {
		if (student != null && studentName != null) {
			studentName.setText(student.getName());
			Map<String, Integer> info = dbHandler.getStdDashBoardInfo(student.getRollNo());
			if (info != null) {
				UIFactory.getInstance();
				UIFactory.fillStdDashBoard(student.getCgpa(), info, ProfileSettingsVBox, opportunitiesVBox,
						applicationsVBox, messagesVBox);
			}
		}
	}

	@FXML
	public void manageProfile(ActionEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/UI/ManageStudentProfile.fxml"));
			loader.setControllerFactory(c -> new ManageStudentProfileController(student));
			Parent root = loader.load();
			Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();

			Scene scene = new Scene(root);

			stage.setScene(scene);
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	@FXML
	public void viewOpportunitiesButton(ActionEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/UI/StudentOpportunities.fxml"));
			loader.setControllerFactory(c -> new StudentOpportunitiesController(student));
			Parent root = loader.load();
			Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();

			Scene scene = new Scene(root);

			stage.setScene(scene);
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void checkStatus(ActionEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/UI/StudentViewStatus.fxml"));
			loader.setControllerFactory(c -> new StudentViewStatusController(student));
			Parent root = loader.load();
			Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();

			Scene scene = new Scene(root);

			stage.setScene(scene);
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void viewStudentNotification(ActionEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/UI/StudentNotification.fxml"));
			loader.setControllerFactory(c -> new StudentNotificationController(student));
			Parent root = loader.load();
			Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();

			Scene scene = new Scene(root);

			stage.setScene(scene);
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void openChatBox(ActionEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/UI/ChatBox.fxml"));
			loader.setControllerFactory(c -> new ChatBoxController(student));
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
