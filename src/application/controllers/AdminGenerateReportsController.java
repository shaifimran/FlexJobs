package application.controllers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import application.Admin;
import application.factory.DBFactory;
import application.handlers.DBHandler;

import jxl.Workbook;
import jxl.write.*;
import jxl.write.Boolean;
import jxl.write.Number;

public class AdminGenerateReportsController {

	private DBHandler dbHandler = DBFactory.getInstance();

	private Admin admin;
	private String folderPath = "D:/git_repo/FlexJobs/Reports";
	private String subFolderPath = folderPath;
	@FXML
	private Button orgReportButton;
	@FXML
	private Button studentReportButton;
	@FXML
	private Button orgRepReportButton;

	public AdminGenerateReportsController(Admin admin) {
		
		this.admin = admin;
		File folder = new File(folderPath);

		// Ensure the folder exists
		if (!folder.exists()) {
			if (folder.mkdir()) {
				System.out.println("Folder created: " + folderPath);
			} else {
				System.out.println("Failed to create folder: " + folderPath);
				return;
			}
		}

		subFolderPath += "/Report-on-" + java.time.LocalDate.now();
		folder = new File(subFolderPath);

		if (!folder.exists()) {
			if (folder.mkdir()) {
				System.out.println("Sub-folder created: " + subFolderPath);
			} else {
				System.out.println("Failed to create sub-folder: " + subFolderPath);
				return;
			}
		}
	}

	// Event Listener on Button[#orgReportButton].onAction

	@FXML
	public void genOrgReport(ActionEvent event) {
		String orgDetailsQuery = "SELECT * FROM Organisation";
		String orgDetailsFilePath = subFolderPath + "/organisation-report.xls";

		String orgOpportunityQuery = "SELECT o.name AS organisation_name, COUNT(op.opportunityID) AS opportunity_count "
				+ "FROM Organisation o LEFT JOIN Opportunity op ON o.name = op.postedBy " + "GROUP BY o.name";

		try {
			// Create the workbook for organisation details report
			WritableWorkbook workbook = Workbook.createWorkbook(new File(orgDetailsFilePath));

			// Organisation Details Sheet
			WritableSheet detailsSheet = workbook.createSheet("Organisation Details", 0);
			detailsSheet.addCell(new Label(0, 0, "Name"));
			detailsSheet.addCell(new Label(1, 0, "Industry"));
			detailsSheet.addCell(new Label(2, 0, "Description"));
			detailsSheet.addCell(new Label(3, 0, "Location"));
			detailsSheet.addCell(new Label(4, 0, "Contact Email"));
			detailsSheet.addCell(new Label(5, 0, "Verified"));

			ResultSet rs = dbHandler.executeQuery(orgDetailsQuery);
			int rowIndex = 1;
			while (rs.next()) {
				detailsSheet.addCell(new Label(0, rowIndex, rs.getString("name")));
				detailsSheet.addCell(new Label(1, rowIndex, rs.getString("industry")));
				detailsSheet.addCell(new Label(2, rowIndex, rs.getString("description")));
				detailsSheet.addCell(new Label(3, rowIndex, rs.getString("location")));
				detailsSheet.addCell(new Label(4, rowIndex, rs.getString("contactEmail")));
				detailsSheet.addCell(new Boolean(5, rowIndex, rs.getBoolean("isVerified")));
				rowIndex++;
			}

			// Organisation Opportunity Sheet
			WritableSheet opportunitySheet = workbook.createSheet("Organisation Opportunity", 1);
			opportunitySheet.addCell(new Label(0, 0, "Organisation Name"));
			opportunitySheet.addCell(new Label(1, 0, "Number of Opportunities"));

			rs = dbHandler.executeQuery(orgOpportunityQuery);
			rowIndex = 1;
			while (rs.next()) {
				opportunitySheet.addCell(new Label(0, rowIndex, rs.getString("organisation_name")));
				opportunitySheet.addCell(new Number(1, rowIndex, rs.getInt("opportunity_count")));
				rowIndex++;
			}

			// Write the data to the file
			workbook.write();
			workbook.close();
			// Show an alert when the report is generated
			Alert alert = new Alert(AlertType.INFORMATION, "Organisation reports generated: \n" + orgDetailsFilePath,
					ButtonType.OK);
			alert.setTitle("Report Generated");
			alert.setHeaderText("Reports successfully generated");
			alert.showAndWait();
		} catch (SQLException | IOException | WriteException e) {
			e.printStackTrace();
			// Show an error alert if something goes wrong
			Alert alert = new Alert(AlertType.ERROR,
					"An error occurred while generating the reports. Please try again later.", ButtonType.OK);
			alert.setTitle("Error");
			alert.setHeaderText("Error generating reports");
			alert.showAndWait();
		}
	}

	@FXML
	public void genStudentReport(ActionEvent event) {
		String studentDetailsQuery = "SELECT * FROM Student";
		String studentDetailsFilePath = subFolderPath + "/student-report.xls";

		try (FileOutputStream fileOut = new FileOutputStream(studentDetailsFilePath)) {
			WritableWorkbook workbook = Workbook.createWorkbook(fileOut);

			// Student Details Report
			WritableSheet sheet = workbook.createSheet("Student Details", 0);
			sheet.addCell(new Label(0, 0, "Roll No"));
			sheet.addCell(new Label(1, 0, "Name"));
			sheet.addCell(new Label(2, 0, "Email"));
			sheet.addCell(new Label(3, 0, "Department"));
			sheet.addCell(new Label(4, 0, "Semester"));
			sheet.addCell(new Label(5, 0, "CGPA"));

			ResultSet rs = dbHandler.executeQuery(studentDetailsQuery);
			int rowIndex = 1;

			while (rs.next()) {
				sheet.addCell(new Label(0, rowIndex, rs.getString("rollNo")));
				sheet.addCell(new Label(1, rowIndex, rs.getString("name")));
				sheet.addCell(new Label(2, rowIndex, rs.getString("email")));
				sheet.addCell(new Label(3, rowIndex, rs.getString("department")));
				sheet.addCell(new jxl.write.Number(4, rowIndex, rs.getInt("semester")));
				sheet.addCell(new jxl.write.Number(5, rowIndex, rs.getDouble("cgpa")));
				rowIndex++;
			}

			workbook.write();
			workbook.close();
			// Show an alert when the report is generated
			Alert alert = new Alert(AlertType.INFORMATION,
					"Student details report generated: " + studentDetailsFilePath, ButtonType.OK);
			alert.setTitle("Report Generated");
			alert.setHeaderText("Reports successfully generated");
			alert.showAndWait();

		} catch (SQLException | IOException | WriteException e) {
			e.printStackTrace();
			// Show an error alert if something goes wrong
			Alert alert = new Alert(AlertType.ERROR,
					"An error occurred while generating the reports. Please try again later.", ButtonType.OK);
			alert.setTitle("Error");
			alert.setHeaderText("Error generating reports");
			alert.showAndWait();
		}
	}

	@FXML
	public void genOrgRepReport(ActionEvent event) {
		// Query to fetch data from the OrganisationRepresentative table
		String orgRepDetailsQuery = "SELECT email, name, orgID, phone, position, isVerified FROM OrganisationRepresentative";
		String orgRepDetailsFilePath = subFolderPath + "/org-rep-report.xls";

		try (FileOutputStream fileOut = new FileOutputStream(orgRepDetailsFilePath)) {
			// Create a writable workbook
			WritableWorkbook workbook = Workbook.createWorkbook(fileOut);

			// Create a sheet for organization representative details
			WritableSheet sheet = workbook.createSheet("Org Rep Details", 0);

			// Create the header row
			sheet.addCell(new Label(0, 0, "Email"));
			sheet.addCell(new Label(1, 0, "Name"));
			sheet.addCell(new Label(2, 0, "Organisation ID"));
			sheet.addCell(new Label(3, 0, "Phone"));
			sheet.addCell(new Label(4, 0, "Position"));
			sheet.addCell(new Label(5, 0, "Is Verified"));

			// Execute the query to fetch organization representative details
			ResultSet rs = dbHandler.executeQuery(orgRepDetailsQuery);
			int rowIndex = 1;

			// Populate the sheet with data from the result set
			while (rs.next()) {
				sheet.addCell(new Label(0, rowIndex, rs.getString("email")));
				sheet.addCell(new Label(1, rowIndex, rs.getString("name")));
				sheet.addCell(new Label(2, rowIndex, rs.getString("orgID")));
				sheet.addCell(new Label(3, rowIndex, rs.getString("phone")));
				sheet.addCell(new Label(4, rowIndex, rs.getString("position")));
				sheet.addCell(new Boolean(5, rowIndex, rs.getBoolean("isVerified")));
				rowIndex++;
			}

			// Write the workbook to the file system
			workbook.write();
			workbook.close();
			Alert alert = new Alert(AlertType.INFORMATION,
					"Organisation representative report generated: " + orgRepDetailsFilePath, ButtonType.OK);
			alert.setTitle("Report Generated");
			alert.setHeaderText("Reports successfully generated");
			alert.showAndWait();

		} catch (SQLException | IOException | WriteException e) {
			e.printStackTrace();
			// Show an error alert if something goes wrong
			Alert alert = new Alert(AlertType.ERROR,
					"An error occurred while generating the reports. Please try again later.", ButtonType.OK);
			alert.setTitle("Error");
			alert.setHeaderText("Error generating reports");
			alert.showAndWait();
		}
	}
	
	public void goBackToAdminDashboard(ActionEvent event) {
		try {
			// Load Admin Dashboard FXML
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/UI/AdminDashboard.fxml"));
			loader.setControllerFactory(c -> new AdminDashboardController(admin));
			Parent root = loader.load();

			// Get current stage
			Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
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
