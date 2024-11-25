package application.controllers;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import application.JobOpportunity;
import application.Opportunity;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class StudentOpportunitiesController {
	private boolean isInJobSection = true;
	DBHandler dbHandler = new DBHandler();
	Student student;
	@FXML
	private Button filterJobsButton;
	@FXML
	private Button filterEducationalButton;
	@FXML
	private Button viewJobDescriptionButton;
	@FXML
	private Button applyForJobButton;
	@FXML
	private VBox JobVBox;
	@FXML
	HBox categoryFilterHBox;
	@FXML
	TextField filterTextField;
	@FXML
	Button filterCategoryButton;
	@FXML
	ComboBox<String> categoryComboBox;

	public StudentOpportunitiesController(Student s) {
		this.student = s;
	}

	public StudentOpportunitiesController() {

	}

	public void initialize() {
		List<String> cat = dbHandler.getCategories();
		if (cat != null) {
			UIFactory.getInstance();
			UIFactory.populateCategories(cat, categoryComboBox);
		}
	}

	@FXML
	public void filterJobs(ActionEvent event) {
		isInJobSection = true;
		categoryFilterHBox.setVisible(true);
		Map<Integer, String> jobTitles;
		if (categoryComboBox.getSelectionModel().getSelectedItem() == null)
			jobTitles = dbHandler.fetchJobTitles("");
		else {
			JobVBox.getChildren().clear();
			jobTitles = dbHandler.fetchJobTitles(categoryComboBox.getSelectionModel().getSelectedItem());
			categoryComboBox.getSelectionModel().clearSelection();
		}

		if (jobTitles.isEmpty()) {
			showAlert(AlertType.ERROR, "Error", "No Job Opportunities Available!");
		} else {
			UIFactory.getInstance();
			JobVBox.getChildren().clear();
			VBox updatedVBox = UIFactory.createJobOpportunitiesUI(jobTitles, JobVBox, this);
			JobVBox = updatedVBox;
		}
	}

	// Event Listener on Button[#filterEducationalButton].onAction
	@FXML
	public void filterEducational(ActionEvent event) {
		isInJobSection = false;
		categoryFilterHBox.setVisible(false);
		Map<Integer, String> educationTitles = dbHandler.fetchEducationTitles();
		if (educationTitles.isEmpty()) {
			showAlert(AlertType.ERROR, "Error", "No Educational Opportunities Available!");
		} else {
			UIFactory.getInstance();
			JobVBox.getChildren().clear();
			VBox updatedVBox = UIFactory.createEducationalOpportunitiesUI(educationTitles, JobVBox, this);
			JobVBox = updatedVBox;
		}
	}

	// Event Listener on Button[#viewJobDescriptionButton].onAction
	@FXML
	public void viewDescription(ActionEvent event) {
		if (isInJobSection) {
			Button sourceButton = (Button) event.getSource();
			int oppId = (int) sourceButton.getUserData();
			JobOpportunity o = dbHandler.getJobById(oppId);
			if (o != null) {
				showJobDetails(o.getTitle(), o.getDescription(), o.getStatus(), o.getPostedBy());
			}
		} else {
			Button sourceButton = (Button) event.getSource();
			Integer oppId = (Integer) sourceButton.getUserData();
			Opportunity o = dbHandler.getEducationalOppDetailsById(oppId);
			if (o != null) {
				showOpportunityDetails(o.getTitle(), o.getDescription(), o.getPostedBy());
			}
		}
	}

	private void showJobDetails(String title, String description, String status, String postedBy) {

		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle("Job Opportunity Details");
		alert.setHeaderText("Details of the Job Opportunity");

		String content = "Title: " + title + "\n\n" + "Description: " + description + "\n\n" + "\n\n" + "Status: "
				+ status + "\n\n" + "Posted By: " + postedBy;

		TextArea textArea = new TextArea(content);
		textArea.setEditable(false);
		textArea.setWrapText(true);
		textArea.setMaxWidth(Double.MAX_VALUE);
		textArea.setMaxHeight(Double.MAX_VALUE);

		alert.getDialogPane().setContent(textArea);

		alert.showAndWait();
	}

	@FXML
	public void applyForJob(ActionEvent event) {
		Button sourceButton = (Button) event.getSource();
		int oppId = (int) sourceButton.getUserData();
		Boolean status = dbHandler.checkValidity(student.getRollNo(), oppId);
		if (status) {
			status = dbHandler.applyForJob(student.getRollNo(), oppId);
			System.out.println(status);
			if (status) {
				showAlert(AlertType.INFORMATION, "Successful", "Your application was submitted successfully!");
			} else {
				showAlert(AlertType.ERROR, "Unsuccessful", "Error submitting Application.");
			}
		} else {
			showAlert(AlertType.ERROR, "Unsuccessful", "You are not eligible.");
		}
	}

	private void showAlert(AlertType alertType, String title, String message) {
		Alert alert = new Alert(alertType);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}

	public void showOpportunityDetails(String title, String description, String postedBy) {

		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle("Opportunity Details");
		alert.setHeaderText("Details of the Opportunity");

		String content = "Title: " + title + "\n\n" + "Description: " + description + "\n\n" + "Posted By: " + postedBy;

		TextArea textArea = new TextArea(content);
		textArea.setEditable(false);
		textArea.setWrapText(true);
		textArea.setMaxWidth(Double.MAX_VALUE);
		textArea.setMaxHeight(Double.MAX_VALUE);

		alert.getDialogPane().setContent(textArea);

		alert.showAndWait();
	}

	@FXML
	public void filterJobsbyCategory(ActionEvent event) {
		if (categoryComboBox.getSelectionModel().getSelectedItem() == null) {
			showAlert(AlertType.ERROR, "Invalid", "Please select a category!");
			return;
		}

		filterJobs(event);
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
