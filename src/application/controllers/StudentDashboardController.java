package application.controllers;

import application.Student;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.SwipeEvent;

public class StudentDashboardController {
	private Student student;

	public StudentDashboardController(Student student) {
		this.student = student;
	}

	@FXML
	private Label studentName;

	@FXML
	public void initialize() {

		if (student != null && studentName != null) {
			studentName.setText(student.getName());
		}
	}

	// Event Listener on VBox.onSwipeLeft
	@FXML
	public void buttonControl(SwipeEvent event) {
		// TODO Autogenerated
	}

	// Event Listener on VBox.onSwipeLeft
	@FXML
	public void buttonControl(SwipeEvent event) {
		// TODO Autogenerated
	}

	// Event Listener on VBox.onSwipeLeft
	@FXML
	public void buttonControl(SwipeEvent event) {
		// TODO Autogenerated
	}

	// Event Listener on VBox.onSwipeLeft
	@FXML
	public void buttonControl(SwipeEvent event) {
		// TODO Autogenerated
	}
}