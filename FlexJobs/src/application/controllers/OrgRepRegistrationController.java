package application.controllers;
import application.OrganisationRepresentative;
import application.handlers.DBHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class OrgRepRegistrationController {

	OrganisationRepresentative OrgRep;
	
    @FXML
    private TextField nameField;
    @FXML
    private TextField phoneField;
    @FXML
    private TextField passwordField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField organisationField;
    @FXML
    private TextField positionField;

    private DBHandler dbHandler = new DBHandler();

    /**
     * Handles the registration button click event.
     */
    @FXML
    private void handleRegisterAction() {
        String name = nameField.getText().trim();
        String phone = phoneField.getText().trim();
        String password = passwordField.getText().trim();
        String email = emailField.getText().trim();
        String organisation = organisationField.getText().trim();
        String position = positionField.getText().trim();

        // Validate inputs
        if (name.isEmpty() || phone.isEmpty() || password.isEmpty() || email.isEmpty() || organisation.isEmpty() || position.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Form Error!", "Please fill all fields.");
            return;
        }

        if (!isValidEmail(email)) {
            showAlert(Alert.AlertType.ERROR, "Invalid Email", "Please enter a valid email address.");
            return;
        }

        if (!isValidPhone(phone)) {
            showAlert(Alert.AlertType.ERROR, "Invalid Phone Number", "Please enter a valid phone number.");
            return;
        }
        
        if (!dbHandler.isOrganisationRepExists(organisation, email)) {
        	showAlert(Alert.AlertType.ERROR, "Representative Exists", "An organization representative already exists for this organization with this email.");
        	return;
        }
        
        OrgRep = new OrganisationRepresentative(name,password,email,position,phone,organisation);
        
        if (!dbHandler.isOrganisationExists(organisation)) {      
            redirectToOrganisationRegistration();
            return;
        }
            
        dbHandler.addOrgRepresentative(name, phone, password, email, organisation, position);
		showAlert(Alert.AlertType.INFORMATION, "Registration Successful", "Organisation Representative registered successfully.");
		clearFields();
		redirectToLoginPage();
    }   
       
    private void redirectToOrganisationRegistration() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/UI/OrganisationRegistration.fxml"));
            Pane root = loader.load();
            
            OrganisationRegistrationController organisationController = loader.getController();
            organisationController.setOrganisationName(organisationField.getText().trim());
            organisationController.setOrganisationRepresentative(OrgRep);
            
            Stage stage = (Stage) nameField.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void redirectToLoginPage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/UI/OrgRepLogin.fxml"));
            Pane root = loader.load();
            Stage stage = (Stage) nameField.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Validates the email format.
     */
    private boolean isValidEmail(String email) {
        return email.matches("^[\\w._%+-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$");
    }

    /**
     * Validates the phone number format.
     */
    private boolean isValidPhone(String phone) {
        return phone.matches("^\\+?[0-9]{10,15}$");
    }

    /**
     * Clears all input fields after successful registration.
     */
    private void clearFields() {
        nameField.clear();
        phoneField.clear();
        passwordField.clear();
        emailField.clear();
        organisationField.clear();
        positionField.clear();
    }

    /**
     * Shows an alert dialog with the specified type and message.
     */
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
