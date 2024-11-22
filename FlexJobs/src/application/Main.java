package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.SQLException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // Load the Organisation Representative Registration FXML
            Parent root = FXMLLoader.load(getClass().getResource("./UI/OrgRepLogin.fxml"));
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("./UI/application.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.setTitle("Organisation Representative Registration");
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // Create an instance of DBHandler
        DBHandler dbHandler = new DBHandler();
        
        // Test database connection using DBHandler
        try (Connection connection = dbHandler.getConnection()) {
            if (connection != null) {
                System.out.println("Database connected successfully!");
            }
        } catch (SQLException e) {
            System.out.println("Database connection failed: " + e.getMessage());
        }

        // Launch the JavaFX application
        launch(args);
    }
}
