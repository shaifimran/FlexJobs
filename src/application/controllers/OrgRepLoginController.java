package application.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

import java.io.IOException;

import application.OrganisationRepresentative;
import application.factory.DBFactory;
import application.handlers.DBHandler;

public class OrgRepLoginController {

	OrganisationRepresentative OrgRep;
    @FXML
    private TextField emailField; // Field for email input

    @FXML
    private PasswordField passwordField; // Field for password input

    @FXML
    private Button loginButton; // Button to trigger login

    @FXML
    private Button registerButton; // Button to redirect to registration page

	private DBHandler dbHandler = DBFactory.getInstance();

   
    public void setOrgRep(OrganisationRepresentative OrgRep) {
    	this.OrgRep = OrgRep;
    }

    /**
     * Handles the login action when the login button is clicked.
     */
    @FXML
    private void handleLoginAction() {
        String email = emailField.getText().trim();  // Get email input
        String password = passwordField.getText().trim();  // Get password input

        // Check if both fields are filled
        if (email.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Form Error!", "Please fill in both email and password.");
            return;
        }

        // Check the credentials in the database
        OrganisationRepresentative orgRep = dbHandler.verifyOrganisationRepresentativeCredentials(email, password, true);
        if (orgRep != null) {
        	
        	OrgRep = dbHandler.getOrganisationRepresentative(email,password);
            // Redirect to the main page, dashboard or home page
            redirectToDashboard();
        } else {
            showAlert(Alert.AlertType.ERROR, "Invalid Login", "Incorrect email or password or the Organisation Representative is not verified!");
        }
    }


    /**
     * Redirects to the Organization Representative registration page.
     */
    @FXML
    private void redirectToOrgRepRegistration() {
        // Implement logic to navigate to the Organization Representative Registration page
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/UI/OrgRepRegistration.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) registerButton.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load the registration page: " + e.getMessage());
        }
    }


    /**
     * Utility method to show alerts.
     */
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Redirect to the dashboard or home page after successful login.
     */
    private void redirectToDashboard() {
        try {
            // Assuming "Dashboard.fxml" is the main page for the logged-in user
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/UI/OrgRepDashboard.fxml"));
            Parent root = loader.load();
            
            OrgRepDashboardController orgrepdashboardcontroller = loader.getController();
            orgrepdashboardcontroller.setOrganisationRepresentative(OrgRep);
            
            Scene scene = new Scene(root);
            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
        	e.printStackTrace();
        }
    }
}
