package application.controllers;

import java.io.IOException;

import application.Organisation;
import application.OrganisationRepresentative;
import application.factory.DBFactory;
import application.handlers.DBHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class OrgRepDashboardController {

    private OrganisationRepresentative orgRep;

    @FXML
    private Label orgRepNameLabel;

    @FXML
    private Label orgRepPositionLabel;
    
    @FXML
    private Label OpportunityPostedLabel;
    
    @FXML
    private Label ApplicantsLabel;

    @FXML
    private VBox opportunitiesBox;

    @FXML
    private VBox applicantsBox;

    @FXML
    private VBox chatBox;

    @FXML
    private Hyperlink GoBack;
    
    @FXML
    private Hyperlink postOpportunityLink;

    @FXML
    private Hyperlink verifyApplicantsLink;

    @FXML
    private Hyperlink chatWithApplicantsLink;
    
    private DBHandler dbHandler = DBFactory.getInstance();
    
    public OrgRepDashboardController(OrganisationRepresentative orgRep) {
    	this.orgRep = orgRep;
    }
    
    public OrgRepDashboardController() {
    	
    }
    
    @FXML
    public void initialize() {
        this.setOrganisationRepresentative(orgRep); 
        updateLabels();
    }

    
    public void updateLabels() {

        int opportunitiesCount = dbHandler.getOpportunitiesCount();
        int applicantsCount = dbHandler.getApplicantsCount();

        OpportunityPostedLabel.setText(String.valueOf(opportunitiesCount));
        ApplicantsLabel.setText(String.valueOf(applicantsCount));
    }



    public void setOrganisationRepresentative(OrganisationRepresentative orgRep) {
        this.orgRep = orgRep;
        if (orgRep != null) {
            orgRepNameLabel.setText(orgRep.getName());
            orgRepPositionLabel.setText(orgRep.getPosition());
        }
    }
    
    public void goback() {
    	try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/UI/OrgRepLogin.fxml"));
            Parent root = loader.load();          
            Scene scene = new Scene(root);
            Stage stage = (Stage) orgRepNameLabel.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handlePostOpportunity() {
        redirectToPostOpportunity();
    }
    
    private void redirectToPostOpportunity() {
    	try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/UI/OrgRepPostOpportunity.fxml"));
            Pane root = loader.load();
            
            OrgRepPostOpportunityController orgreppostcontroller = loader.getController();
            orgreppostcontroller.setOrganisationRepresentative(orgRep);
            
            Stage stage = (Stage) orgRepNameLabel.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleVerifyApplicants() {
    	redirectToVFUApplicants();
    }
    
    private void redirectToVFUApplicants() {
    	try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/UI/OrgRepVFU.fxml"));
            Pane root = loader.load();  
            
            OrgRepVFUController orgrepVFUcontroller = loader.getController();
            orgrepVFUcontroller.setOrganisationRepresentative(orgRep);
            
            Stage stage = (Stage) orgRepNameLabel.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleChatWithApplicants(ActionEvent event) {
    	try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/UI/ChatBox.fxml"));
			Organisation o = new Organisation();
			o.setName(orgRep.getOrgID());
			o.getRepresentatives().addLast(orgRep);
	

			loader.setControllerFactory(c -> new ChatBoxController(o));
			Parent root = loader.load();
			Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();

			Scene scene = new Scene(root);

			stage.setScene(scene);
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    public void goBackToMainApplication(ActionEvent event) {
		try {
			// Load Admin Dashboard FXML
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/UI/MainApplication.fxml"));
			Parent root = loader.load();

			// Get current stage
			Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
			// Set new scene for Admin Dashboard
			Scene dashboardScene = new Scene(root);
			stage.setScene(dashboardScene);
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
