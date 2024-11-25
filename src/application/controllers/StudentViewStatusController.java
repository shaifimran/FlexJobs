package application.controllers;

import java.io.IOException;
import java.util.List;

import application.ApplicationWithOpportunity;
import application.Student;
import application.UI.UIFactory;
import application.handlers.DBHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class StudentViewStatusController {
	DBHandler dbHandler = new DBHandler();

	@FXML
	private Button viewJobStatusDetailsButton;
	Student student;
	@FXML
	VBox statusVBox;

	public StudentViewStatusController(Student s) {
		student = s;

	}

	@FXML
	private void initialize() {
		List<ApplicationWithOpportunity> appOpp = dbHandler.retrieveApplicationsWithOpportunities(student.getRollNo());
		if (appOpp != null) {
			UIFactory.getInstance();
			UIFactory.generateApplicationUI(appOpp, this, statusVBox);
		}
	}

	@FXML
	public void viewJobStatusDetail(ActionEvent event) {
		Button viewJobStatusDetailsButton = (Button) event.getSource();
		String st = (String) viewJobStatusDetailsButton.getUserData();
		showApplicationStatusDetails(st);
	}

	public void showApplicationStatusDetails(String status) {

		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle("Application Status Details");
		alert.setHeaderText("Status of Your Application");

		String content = "";

		switch (status.toLowerCase()) {
		case "In Progress":
			content = "Your application has been successfully submitted.\n\n"
					+ "You will be notified once the recruiter reviews your application.";
			break;

		case "Call For Interview":
			content = "Congratulations! Your interview has been scheduled.\n\n"
					+ "Please make sure to prepare for your interview on the scheduled date.";
			break;

		case "Approved":
			content = "You have been accepted for the position!\n\n"
					+ "Next, we will provide you with more details about the offer.";
			break;

		case "Rejected":
			content = "Unfortunately, your application has been rejected.\n\n"
					+ "Thank you for your time and effort. Keep applying to other opportunities!";
			break;

		default:
			content = "The application status is unknown.\n\n" + "Please check again later.";
			break;
		}

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
