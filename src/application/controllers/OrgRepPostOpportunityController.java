package application.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import javafx.scene.control.Label;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import application.OrganisationRepresentative;
import application.handlers.DBHandler;

public class OrgRepPostOpportunityController {
	
	OrganisationRepresentative OrgRep;

	// FXML elements
	@FXML
	private Hyperlink GoBack;
	@FXML
	private TextField opportunityTitle; // TextField for entering opportunity title
	@FXML
	private ComboBox<String> selectOrganisation; // ComboBox for selecting the organization
	@FXML
	private ComboBox<String> oppCategory; // ComboBox for selecting the opportunity category
	@FXML
	private TextArea opportunityDescription; // TextArea for entering the opportunity description
	@FXML
	private Label JobLABEL;
	@FXML
	private ComboBox<String> JobCategories;

	private DBHandler dbHandler = new DBHandler();
	
	public void setOrganisationRepresentative(OrganisationRepresentative orgRep) {
        this.OrgRep = orgRep;
    }

	@FXML
	public void initialize() {
		clearFields();
		displayOrgComboBox(); // Populate the organization ComboBox
		displaycategoryComboBox(); // Populate the category ComboBox
		setupCategorySelectionLogic();
		displayJobCategoriesComboBox();
		JobLABEL.setVisible(false);
		JobCategories.setVisible(false);
	}

	// Show alert helper
	private void showAlert(Alert.AlertType alertType, String title, String message) {
		Alert alert = new Alert(alertType);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}

	// Handle the "Post" button click event
	@FXML
	void goback() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/UI/OrgRepDashboard.fxml"));
            Parent root = loader.load();
            
            OrgRepDashboardController orgrepdashboardcontroller = loader.getController();
            orgrepdashboardcontroller.setOrganisationRepresentative(OrgRep);
            
            Scene scene = new Scene(root);
            Stage stage = (Stage) JobLABEL.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}

	@FXML
	private void postOpportunity() throws SQLException {
		String title = opportunityTitle.getText();
		String description = opportunityDescription.getText();
		String type = oppCategory.getValue();
		String jobCategory = JobCategories.getValue();
		String organization = selectOrganisation.getValue(); 
		
		if (title.isEmpty() || description.isEmpty() || organization == null || type == null) {
			showAlert(AlertType.ERROR, "Error", "All fields must be filled out.");
			return;
		}

		if (type.equals("Job") && (jobCategory == null || jobCategory.isEmpty())) {
			showAlert(AlertType.ERROR, "Error", "Job category cannot be empty for Job opportunities.");
			return;
		}

		int opportunityID = dbHandler.insertIntoOpportunity(title, description, type);
		System.out.println(opportunityID);
		if (type.equals("Job")) {
			dbHandler.insertIntoJob(opportunityID, jobCategory); 
			System.out.println(opportunityID);
		} else if (type.equals("Educational")) {
			dbHandler.insertIntoEducational(opportunityID); 
			System.out.println(opportunityID);
		}
		
		dbHandler.addNotification(OrgRep.getOrgID(),"student","Organisation: " + OrgRep.getOrgID() + " has posted a New " + type + " Job Opportunity.");
		showAlert(AlertType.INFORMATION, "Success", "Opportunity posted successfully.");
		clearFields();
	}

	@FXML
	private void setupCategorySelectionLogic() {
		oppCategory.setOnAction(event -> {
			String selectedCategory = oppCategory.getValue();
			if ("Job".equals(selectedCategory)) {
				JobLABEL.setVisible(true);
				JobCategories.setVisible(true);
			} else if ("Educational".equals(selectedCategory)) {
				JobLABEL.setVisible(false);
				JobCategories.setVisible(false);
			}
		});
	}

	// Clear the input fields
	private void clearFields() {
		opportunityTitle.clear();
		selectOrganisation.getSelectionModel().clearSelection();
		oppCategory.getSelectionModel().clearSelection();
		opportunityDescription.clear();
		JobCategories.getSelectionModel().clearSelection();
	}

	// Handle the selection of an organization from the ComboBox
	@FXML
	private void displayOrgComboBox() {
	    List<String> organisations = dbHandler.getVerifiedOrganisations();
	    selectOrganisation.getItems().addAll(organisations);
	}



	@FXML
	private void displayJobCategoriesComboBox() {
		// Add job categories to the ComboBox
		JobCategories.getItems().addAll("Software Engineer", "Data Scientist", "Web Developer", "Mobile App Developer",
				"Network Administrator", "Cybersecurity Analyst", "Database Administrator", "AI/ML Engineer",
				"Cloud Architect", "Game Developer", "UI/UX Designer", "Systems Analyst", "DevOps Engineer",
				"Technical Writer", "Product Manager", "IT Support Specialist", "Blockchain Developer",
				"Full Stack Developer", "Embedded Systems Engineer", "Quality Assurance Analyst");
	}

	// Handle the selection of an opportunity category from the ComboBox
	@FXML
	private void displaycategoryComboBox() {
		oppCategory.getItems().addAll("Job", "Educational");
	}

}
