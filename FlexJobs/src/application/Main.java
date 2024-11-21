package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
	public String connectionString = "jdbc:mysql://localhost:3306/flexjobs";
	public String userName = "root";
	public String password = "wasay";
	Connection connection;

	@Override
	public void start(Stage primaryStage) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("./UI/StudentLogin.fxml"));
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("./UI/application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Connection getConnection() {
		try {
			if (connection == null || connection.isClosed()) {
				connection = DriverManager.getConnection(connectionString, userName, password);
			}
		} catch (SQLException e) {
			System.err.println("Error creating connection: " + e.getMessage());
			e.printStackTrace();
			System.exit(-1);
		}
		return connection;
	}

	public static void main(String[] args) {
		launch(args);
	}
}
