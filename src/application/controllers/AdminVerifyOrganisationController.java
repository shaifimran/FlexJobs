package application.controllers;

import application.Admin;
import application.OrgRepTableRow;
import application.Organisation;
import application.OrganisationRepresentative;
import application.UnverifiedOrgs;
import application.UI.UIFactory;
import application.handlers.DBHandler;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;

public class AdminVerifyOrganisationController {

	private DBHandler dbHandler = new DBHandler();

	private UnverifiedOrgs unverifiedOrgs;
	
	private Admin admin;
	@FXML
	private TableView<OrgRepTableRow> verifyOrgTable;
	@FXML
	private TableColumn<OrgRepTableRow, String> orgCol;
	@FXML
	private TableColumn<OrgRepTableRow, String> repCol;
	@FXML
	private TableColumn<OrgRepTableRow, HBox> verCol;

	@FXML
	private HBox verifyOrgErrorHbox;

	@FXML
	private Label verifyOrgErrorLabel;

	public AdminVerifyOrganisationController(Admin admin) {
		this.admin = admin;
		unverifiedOrgs = dbHandler.getUnverifiedOrgsandInitialReps();
	}

	@FXML
	public void initialize() {
		UIFactory.getInstance().createVerifyOrganisationTable(verifyOrgTable, orgCol, repCol, verCol, unverifiedOrgs,
				this);
	}

	public void handleVerify(OrgRepTableRow row) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Verify Confirmation");
		alert.setHeaderText("Are you sure you want to verify this organization and representative?");
		alert.setContentText("This action will mark them as verified.");

		// Show the confirmation dialog and wait for the user to respond
		alert.showAndWait().ifPresent(response -> {
			if (response == ButtonType.OK) {
				// If confirmed, proceed with the verification
				Organisation org = row.getOrganization();
				OrganisationRepresentative rep = row.getRepresentative();

				// Set isVerified to true for both the Organization and Representative
				org.setVerified(true);
				rep.setVerified(true);

				// Remove the row from the TableView
				verifyOrgTable.getItems().remove(row);

				// Optionally, update the database with the new "isVerified" status
				dbHandler.updateOrganisationVerificationStatus(org);
				dbHandler.updateRepresentativeVerificationStatus(rep);
				verifyOrgErrorHbox.setStyle("-fx-background-color: green;");
				verifyOrgErrorLabel.setText("Verification Successful!");
				showLabel();

			}
		});
	}

	public void goBackToAdminDashboard(ActionEvent event) {
		try {
			// Load Admin Dashboard FXML
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/UI/AdminDashboard.fxml"));
			loader.setControllerFactory(c -> new AdminDashboardController(admin));
			Parent root = loader.load();

			// Get current stage
			Stage stage = (Stage) verifyOrgErrorLabel.getScene().getWindow();
			// Set new scene for Admin Dashboard
			Scene dashboardScene = new Scene(root);
			stage.setScene(dashboardScene);
			stage.setTitle("Admin Dashboard");
		} catch (Exception e) {
			e.printStackTrace();
			showAlert(AlertType.ERROR, "Error", "Failed to load Admin Dashboard.");
		}
	}

	public void handleUnverify(OrgRepTableRow row) {
		// Show confirmation dialog before removing the row
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Unverify Confirmation");
		alert.setHeaderText("Are you sure you want to unverify this organization and representative?");
		alert.setContentText("This action will remove them from the list.");

		// Show the confirmation dialog and wait for the user to respond
		alert.showAndWait().ifPresent(response -> {
			if (response == ButtonType.OK) {
				// Get the organization and representative from the row
				Organisation org = row.getOrganization();
				OrganisationRepresentative rep = row.getRepresentative();

				// Remove the row from the TableView
				verifyOrgTable.getItems().remove(row);

				// Remove the organization and its representative from the database
				dbHandler.deleteOrganisationRepresentative(rep);
				dbHandler.deleteOrganisation(org);
				verifyOrgErrorHbox.setStyle("-fx-background-color:  #c94b42;");
				verifyOrgErrorLabel.setText("Unverified Successfully!");
				showLabel();

			}
		});
	}

	private void showLabel() {

		verifyOrgErrorHbox.setVisible(true);

		PauseTransition pause = new PauseTransition(Duration.millis(3000));
		pause.setOnFinished(event -> verifyOrgErrorHbox.setVisible(false));
		pause.playFromStart();
	}

	private void showAlert(AlertType alertType, String title, String message) {
		Alert alert = new Alert(alertType);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}

}
