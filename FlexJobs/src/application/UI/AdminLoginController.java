package application.UI;

import application.Admin;
import application.DBHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.event.ActionEvent;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminLoginController {
	
	DBHandler dbHandler = new DBHandler();
    @FXML
    private TextField adminEmail;
    @FXML
    private PasswordField adminPassword;
    
    
    @FXML
    private Button loginButton;
    // Event Listener on Button.onAction
    @FXML
    public void loginAdmin(ActionEvent event) {
        String email = adminEmail.getText();
        String password = adminPassword.getText();

        Admin admin = authenticateAdmin(email, password);
        if (admin != null) {
            // Proceed to the admin dashboard
            showAlert(AlertType.INFORMATION, "Login Successful", "Welcome, Admin!");
          
            // Add code to navigate to the admin dashboard here
            this.navigateToDashboard(admin);
        } else {
            // Display an error message
            showAlert(AlertType.ERROR, "Login Failed", "Invalid email or password.");
        }
    }

    private Admin authenticateAdmin(String email, String password) {
    	Admin admin = dbHandler.getAdminInfo(email);
    	
    	if (admin != null) {
    		if (password.equals(admin.getPassword())) {
    			return admin;
			}
    	}
   
        return null;
    }
    
    private void navigateToDashboard(Admin admin) {
        try {
            // Load Admin Dashboard FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/UI/AdminDashboard.fxml"));
            loader.setControllerFactory(c -> new AdminDashboardController(admin));
            Parent root = loader.load();

            // Get current stage
            Stage stage = (Stage) adminEmail.getScene().getWindow();
            // Set new scene for Admin Dashboard
            Scene dashboardScene = new Scene(root);
            stage.setScene(dashboardScene);
            stage.setTitle("Admin Dashboard");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Error", "Failed to load Admin Dashboard.");
        }
    }

    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    

}
=======
package application.UI;

import application.DBHandler;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.PasswordField;
import javafx.event.ActionEvent;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AdminLoginController {
	
	DBHandler dbHandler = new DBHandler();
    @FXML
    private TextField adminEmail;
    @FXML
    private PasswordField adminPassword;

    // Event Listener on Button.onAction
    @FXML
    public void loginAdmin(ActionEvent event) {
        String email = adminEmail.getText();
        String password = adminPassword.getText();

        if (authenticateAdmin(email, password)) {
            // Proceed to the admin dashboard
            showAlert(AlertType.INFORMATION, "Login Successful", "Welcome, Admin!");
            // Add code to navigate to the admin dashboard here
        } else {
            // Display an error message
            showAlert(AlertType.ERROR, "Login Failed", "Invalid email or password.");
        }
    }

    private boolean authenticateAdmin(String email, String password) {

        String query = "SELECT * FROM Admin WHERE email = ? AND password = ?";

        try (Connection conn = dbHandler.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            // Set parameters for the query
            stmt.setString(1, email);
            stmt.setString(2, password);

            // Execute the query
            ResultSet rs = stmt.executeQuery();

            // If a result exists, the credentials are valid
            return rs.next();

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Database Error", "Unable to connect to the database.");
        }
        

        return false;
    }

