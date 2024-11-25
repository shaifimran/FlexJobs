package application;

import application.controllers.ChatBoxController;
import application.controllers.StudentDashboardController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
//			Organisation o = new Organisation();
//			o.setName("Org1");
//			FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/UI/ChatBox.fxml"));
//			loader.setControllerFactory(c -> new ChatBoxController(o));
//			Parent root = loader.load();
			Parent root = FXMLLoader.load(getClass().getResource("./UI/MainApplication.fxml"));
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("./UI/application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
