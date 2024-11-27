package application.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import application.Application;
import application.OrganisationRepresentative;
import application.factory.DBFactory;
import application.handlers.DBHandler;

public class OrgRepVFUController {

	OrganisationRepresentative OrgRep;
	private DBHandler dbHandler = DBFactory.getInstance();

	@FXML
	private TableView<Application> applicationTable;
	@FXML
	private ComboBox<String> filterBy;
	@FXML
	private ComboBox<Integer> updateApplicationID;
	@FXML
	private ComboBox<String> updateApplicationStatus;
	@FXML
	private TextField viewApplication;
	@FXML
	private Button updateButton;
	@FXML
	private Button viewButton;

	private final ObservableList<Application> applications = FXCollections.observableArrayList();

	public void setOrganisationRepresentative(OrganisationRepresentative orgRep) {
		this.OrgRep = orgRep;
	}

	public void goback() {
		try {
			// Assuming "Dashboard.fxml" is the main page for the logged-in user
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/UI/OrgRepDashboard.fxml"));
			Parent root = loader.load();

			OrgRepDashboardController orgrepdashboardcontroller = loader.getController();
			orgrepdashboardcontroller.setOrganisationRepresentative(OrgRep);

			Scene scene = new Scene(root);
			Stage stage = (Stage) filterBy.getScene().getWindow();
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

	@FXML
	public void initialize() {
		// Initialize TableView columns
		initializeTable();

		// Add filter options
		filterBy.setItems(FXCollections.observableArrayList("All Applications", "In Progress", "Approved", "Rejected",
				"Call For Interview"));

		// Set status options
		updateApplicationStatus
				.setItems(FXCollections.observableArrayList("Approved", "Rejected", "Call For Interview"));

		// Populate Application IDs
		updateApplicationID.setItems(FXCollections.observableArrayList(getFilteredApplicationIDs()));
	}

	private List<Integer> getFilteredApplicationIDs() {
		// Get the value of filterBy
		String filterValue = filterBy.getValue();

		// Fetch filtered applications from the database
		List<Application> filteredApplications;
		if (filterValue == null || filterValue.equals("All Applications")) {
			// Fetch all applications if filter is null or "All Applications"
			filteredApplications = dbHandler.getApplicationList();
		} else {
			// Fetch applications with the specific status
			filteredApplications = dbHandler.getApplicationsByStatus(filterValue);
		}

		// Extract and return Application IDs from the filtered applications
		return filteredApplications.stream().map(Application::getApplicationID).collect(Collectors.toList());
	}

	private void initializeTable() {
		// Initialize TableView columns
		TableColumn<Application, Integer> idColumn = new TableColumn<>("Application ID");
		idColumn.setCellValueFactory(new PropertyValueFactory<>("applicationID"));

		TableColumn<Application, Date> dateColumn = new TableColumn<>("Submit Date");
		dateColumn.setCellValueFactory(new PropertyValueFactory<>("submitDate"));

		TableColumn<Application, String> studentColumn = new TableColumn<>("Student ID");
		studentColumn.setCellValueFactory(new PropertyValueFactory<>("studentID"));

		TableColumn<Application, String> feedbackColumn = new TableColumn<>("Feedback");
		feedbackColumn.setCellValueFactory(new PropertyValueFactory<>("feedback"));

		TableColumn<Application, String> statusColumn = new TableColumn<>("Status");
		statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

		TableColumn<Application, Integer> interviewColumn = new TableColumn<>("Interview ID");
		interviewColumn.setCellValueFactory(new PropertyValueFactory<>("interviewID"));

		TableColumn<Application, Integer> opportunityColumn = new TableColumn<>("Opportunity ID");
		opportunityColumn.setCellValueFactory(new PropertyValueFactory<>("opportunityID"));

		// Add all columns to the TableView
		applicationTable.getColumns().addAll(idColumn, dateColumn, studentColumn, feedbackColumn, statusColumn,
				interviewColumn, opportunityColumn);

		// Fetch all applications from DBHandler and populate the TableView
		List<Application> fetchedApplications = dbHandler.getApplicationList();
		applications.setAll(fetchedApplications); // Update the ObservableList
		applicationTable.setItems(applications); // Set the items of TableView to the ObservableList
	}

	@FXML
	private void updateStatus(ActionEvent event) {
		Integer selectedAppID = updateApplicationID.getValue();
		String newStatus = updateApplicationStatus.getValue();

		if (selectedAppID != null && newStatus != null) {
			Application appToUpdate = null;

			// Find the application to update
			for (Application app : applications) {
				if (app.getApplicationID() == selectedAppID) {
					appToUpdate = app;
					break;
				}
			}

			// If the application is found, update the status and refresh the table
			if (appToUpdate != null) {
				if (appToUpdate.getStatus().equals(newStatus)) {
					showAlert(Alert.AlertType.ERROR, "Status not updated", "It is already the same Status.");
					return;
				}
				// Call the updateApplicationStatus method from DBHandler to update the status
				// in the database
				boolean isUpdated = dbHandler.updateApplicationStatus(selectedAppID, newStatus);

				if (isUpdated) {
					showAlert(Alert.AlertType.INFORMATION, "Status Updated", "Status has been updated successfully!");
					dbHandler.addNotification(OrgRep.getOrgID(), dbHandler.getStudentEmail(0, appToUpdate.getStudentID()),
							"Your Application with Application ID: " + appToUpdate.getApplicationID()
									+ " has been updated.");
					applicationTable.refresh();
					if (filterBy.getValue() == null || filterBy.getValue().equals("All Applications")) {
						List<Application> fetchedApplications = dbHandler.getApplicationList();
						applications.setAll(fetchedApplications);
						applicationTable.setItems(applications);
					} else {
						List<Application> filteredApplications = dbHandler
								.getApplicationsByStatus(appToUpdate.getStatus());
						applicationTable.setItems(FXCollections.observableArrayList(filteredApplications));
						appToUpdate.setStatus(newStatus);
					}
				} else {
					// Handle error in status update (you can show an alert or log the error)
					System.out.println("Error: Status update failed.");
				}
			}
		} else {
			showAlert(Alert.AlertType.ERROR, "Fields not filled :(",
					"Select Application ID and Status and Click on Update.");
		}
	}

	@FXML
	private void viewApplication(ActionEvent event) {
		String appIDText = viewApplication.getText();
		if (appIDText != null && !appIDText.isEmpty()) {
			try {
				int appID = Integer.parseInt(appIDText); // Convert text to Application ID
				Application appToView = applications.stream().filter(app -> app.getApplicationID() == appID).findFirst()
						.orElse(null); // Find the application with the given ID

				if (appToView != null) {
					// Show application details using an Alert dialog
					showApplicationDetails(String.valueOf(appToView.getApplicationID()), appToView.getFeedback(),
							appToView.getStudentID(), appToView.getStatus(), appToView.getSubmitDate().toString());
				} else {
					showAlert(Alert.AlertType.ERROR, "NOT FOUND!", "Application Not Found!");
				}
			} catch (NumberFormatException e) {
				// Handle invalid Application ID input
				showAlert(Alert.AlertType.ERROR, "Invalid", "Invalid Application ID!");
			}
		} else {
			showAlert(Alert.AlertType.ERROR, "Fields not filled :(", "Select Application ID and then Click on View.");
		}
	}

	private void showApplicationDetails(String applicationID, String feedback, String studentID, String status,
			String submitDate) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle("Application Details");
		alert.setHeaderText("Details of the Application");

		// Layout for the dialog
		VBox dialogLayout = new VBox(10);

		// Existing application details
		String content = "Application ID: " + applicationID + "\n\n" + "Student ID: " + studentID + "\n\n" + "Status: "
				+ status + "\n\n" + "Submit Date: " + submitDate;

		TextArea detailsArea = new TextArea(content);
		detailsArea.setEditable(false);
		detailsArea.setWrapText(true);
		detailsArea.setMaxWidth(Double.MAX_VALUE);
		detailsArea.setMaxHeight(Double.MAX_VALUE);

		// Feedback label and TextField
		Label feedbackLabel = new Label("Feedback:");
		TextField feedbackField = new TextField(feedback);
		feedbackField.setPromptText("Enter feedback here");

		// Add details and feedback input to the layout
		dialogLayout.getChildren().addAll(detailsArea, feedbackLabel, feedbackField);
		alert.getDialogPane().setContent(dialogLayout);

		// Add "Update Feedback" button
		ButtonType updateButton = new ButtonType("Update Feedback", ButtonData.OK_DONE);
		alert.getButtonTypes().addAll(updateButton, ButtonType.CANCEL);

		// Use a single-element array to hold the application reference
		final Application[] appToUpdate = { null };

		// Find the application to update
		for (Application app : applications) {
			if (String.valueOf(app.getApplicationID()).equals(applicationID)) {
				appToUpdate[0] = app;
				break;
			}
		}

		String oldStatus = appToUpdate[0].getStatus();
		// Show the dialog and handle update
		alert.showAndWait().ifPresent(response -> {
			if (response == updateButton) {
				String newFeedback = feedbackField.getText();
				if (newFeedback != null && !newFeedback.trim().isEmpty()) {
					boolean isUpdated = dbHandler.setFeedback(Integer.parseInt(applicationID), newFeedback);
					dbHandler.addNotification(OrgRep.getEmail(), dbHandler.getStudentEmail(0, appToUpdate[0].getStudentID()),
							"Organisation Representative: " + OrgRep.getEmail() + " has given you Feedback.");

					if (isUpdated) {
						showAlert(Alert.AlertType.INFORMATION, "Success", "Feedback updated successfully.");
					} else {
						showAlert(Alert.AlertType.ERROR, "Error", "Failed to update feedback.");
					}
				} else {
					showAlert(Alert.AlertType.WARNING, "Warning", "No feedback entered.");
				}
			}
		});

		// Retrieve applications by status from DBHandler
		List<Application> filteredApplications = dbHandler.getApplicationsByStatus(oldStatus);
		applicationTable.setItems(FXCollections.observableArrayList(filteredApplications));
	}

	@FXML
    private void filterApplications(ActionEvent event) {
        String selectedStatus = filterBy.getValue();
                
     // Populate Application IDs
        updateApplicationID.setItems(FXCollections.observableArrayList(
        	    getFilteredApplicationIDs()
        	));
        
        if (selectedStatus != null) {
            if (selectedStatus.equals("All Applications")) {
            	List<Application> fetchedApplications = dbHandler.getApplicationList();
                applications.setAll(fetchedApplications);  
                applicationTable.setItems(applications);  
            	return;
            }
            // Retrieve applications by status from DBHandler
            List<Application> filteredApplications = dbHandler.getApplicationsByStatus(selectedStatus);
            applicationTable.setItems(FXCollections.observableArrayList(filteredApplications));
        }
        else {
        	showAlert(Alert.AlertType.ERROR, "Error", "Select a Filter First!");
        }
        
	
    
    DatePicker datePicker = new DatePicker();
	datePicker.setDayCellFactory(picker -> new DateCell() {
        @Override
        public void updateItem(LocalDate item, boolean empty) {
            super.updateItem(item, empty);
            // Disable past dates
            setDisable(item.isBefore(LocalDate.now()));
        }
    });
	}

@FXML
private void scheduleInterview(ActionEvent event) {
    // Create a new Stage for scheduling interviews
    Stage scheduleStage = new Stage();
    scheduleStage.setTitle("Schedule Interview");

    // Layout for the new scene
    VBox layout = new VBox(10);
    layout.setPadding(new Insets(10));

    // ComboBoxes for Application ID and Candidate ID
    Label applicationIDLabel = new Label("Select Application ID:");
    ComboBox<Integer> applicationIDComboBox = new ComboBox<>();
    applicationIDComboBox.setItems(FXCollections.observableArrayList(dbHandler.getAllApplicationIDs()));

    Label candidateIDLabel = new Label("Select Candidate ID:");
    ComboBox<String> candidateIDComboBox = new ComboBox<>();
    candidateIDComboBox.setItems(FXCollections.observableArrayList(dbHandler.getAllStudentIDs()));

    // Other input fields
    Label timeSlotLabel = new Label("Select Time Slot:");
    DatePicker datePicker = new DatePicker();
    datePicker.setDayCellFactory(picker -> new DateCell() {
        @Override
        public void updateItem(LocalDate item, boolean empty) {
            super.updateItem(item, empty);
            // Disable past dates
            setDisable(item.isBefore(LocalDate.now()));
        }
    });

    TextField timeField = new TextField();
    timeField.setPromptText("HH:mm");

    Label typeLabel = new Label("Enter Interview Type:");
    TextField typeField = new TextField();
    typeField.setPromptText("Type");

    Label locationLabel = new Label("Enter Location:");
    TextField locationField = new TextField();
    locationField.setPromptText("Location");

    // Create the Schedule Button with the specified style
    Button scheduleButton = new Button("Schedule Interview");
    scheduleButton.setPrefWidth(200);  // Set appropriate width
    scheduleButton.setPrefHeight(42.0);
    scheduleButton.setStyle("-fx-background-color: #4A44F2; -fx-border-radius: 15; -fx-background-radius: 40;");
    Font font = Font.font("Lorin Bold", 17);
    scheduleButton.setFont(font);
    scheduleButton.setTextFill(Color.WHITE);
    scheduleButton.setTextAlignment(TextAlignment.CENTER);
    scheduleButton.setAlignment(Pos.CENTER);  // Center the button
    scheduleButton.setCursor(Cursor.HAND);
    scheduleButton.getStylesheets()
            .add(getClass().getResource("/application/UI/cupertino-dark.css").toExternalForm());

    // Add fields to layout
    layout.getChildren().addAll(applicationIDLabel, applicationIDComboBox, candidateIDLabel, candidateIDComboBox,
            timeSlotLabel, datePicker, timeField, typeLabel, typeField, locationLabel, locationField,
            scheduleButton);

    // Apply the dark theme CSS to the new stage
    Scene scheduleScene = new Scene(layout, 350, 450);
    String cssPath = getClass().getResource("/application/UI/cupertino-dark.css").toExternalForm();
    scheduleScene.getStylesheets().add(cssPath); // Apply cupertino-dark theme

    scheduleStage.setScene(scheduleScene);

    // Make the window modal (prevent interaction with the previous scene)
    scheduleStage.initModality(Modality.APPLICATION_MODAL);

    // Show the schedule interview stage
    scheduleStage.show();

    // Handle scheduling when the button is clicked
    scheduleButton.setOnAction(e -> {
        Integer applicationID = applicationIDComboBox.getValue();
        String candidateID = candidateIDComboBox.getValue();
        LocalDate date = datePicker.getValue();
        String time = timeField.getText();
        String type = typeField.getText();
        String location = locationField.getText();

        // Validate input fields
        if (applicationID == null || candidateID == null || date == null || time.isEmpty() || type.isEmpty()
                || location.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "All fields must be filled!");
            return;
        }

        // Check if the selected date is in the past
        if (date.isBefore(LocalDate.now())) {
            showAlert(Alert.AlertType.ERROR, "Error", "The selected date cannot be in the past!");
            return;
        }

        // Validate time format (HH:mm)
        if (!time.matches("\\d{2}:\\d{2}")) {
            showAlert(Alert.AlertType.ERROR, "Error", "Time must be in HH:mm format.");
            return;
        }

        // Combine date and time into a timestamp string
        String dateTime = date.toString() + " " + time;

        // Call DBHandler to schedule the interview
        boolean isScheduled = dbHandler.scheduleInterview(applicationID, candidateID, dateTime, type, location);

        if (isScheduled) {
        	dbHandler.addNotification(OrgRep.getOrgID(), dbHandler.getStudentEmail(0, candidateID),
					"Your Application with Application ID: " + applicationID
							+ " has got a new update regarding Interview.");
            showAlert(Alert.AlertType.INFORMATION, "Success", "Interview scheduled successfully!");
            scheduleStage.close();
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to schedule the interview. Check the details.");
        }
    });
}


}
