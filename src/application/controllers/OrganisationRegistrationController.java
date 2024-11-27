package application.controllers;

import application.OrganisationRepresentative;
import application.factory.DBFactory;
import application.handlers.DBHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class OrganisationRegistrationController {

	String orgName;
	OrganisationRepresentative OrgRep;
	@FXML
	private TextField orgNameField;
	@FXML
	private TextField orgIndustryField;
	@FXML
	private TextField orgDescriptionField;
	@FXML
	private TextField orgLocationField;
	@FXML
	private TextField orgContactEmailField;

	private DBHandler dbHandler = DBFactory.getInstance();

	public void setOrganisationName(String organisationName) {
		orgName = organisationName;
	}

	public void setOrganisationRepresentative(OrganisationRepresentative orgRep2) {
		// TODO Auto-generated method stub
		this.OrgRep = orgRep2;
	}

	// Function to handle the organization registration
	public void registerOrganization(ActionEvent event) {
		String orgIndustry = orgIndustryField.getText().trim();
		String orgDescription = orgDescriptionField.getText().trim();
		String orgLocation = orgLocationField.getText().trim();
		String orgContactEmail = orgContactEmailField.getText().trim();

		// Validate inputs
		if (orgName == null || orgName.isEmpty() || orgIndustry.isEmpty() || orgDescription.isEmpty()
				|| orgLocation.isEmpty() || orgContactEmail.isEmpty()) {
			showAlert("Error", "Please fill in all fields.");
			return;
		}

		// Register the organization
		dbHandler.addOrganisation(orgName.toLowerCase(), orgIndustry, orgDescription, orgLocation, orgContactEmail, false);

		// Register Organisation Representative
		dbHandler.addOrgRepresentative(OrgRep.getName(), OrgRep.getPhone(), OrgRep.getPassword(), OrgRep.getEmail(),
				OrgRep.getOrgID(), OrgRep.getPosition(), false);
		
		showAlert(Alert.AlertType.INFORMATION, "Registration Successful", "Organisation Representative and Organisation have been added successfully..");
		
		redirectToLoginPage();
	}

	private void showAlert(String title, String message) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}

	private void redirectToLoginPage() {
		// Redirect to login page after successful registration
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/UI/OrgRepLogin.fxml"));
			Stage stage = (Stage) orgIndustryField.getScene().getWindow();
			Scene scene = new Scene(loader.load());
			stage.setScene(scene);
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
