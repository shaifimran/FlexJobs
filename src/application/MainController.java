package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class MainController {

	@FXML
	public void buttonControl(ActionEvent event) {
		// Cast the source to a JavaFX Button
		Button clickedButton = (Button) event.getSource();

		// Get the fx:id of the clicked button
		String buttonId = clickedButton.getId();
		System.out.println("Button ID: " + buttonId);
	}
}