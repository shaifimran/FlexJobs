<<<<<<< HEAD
package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("./UI/AdminLogin.fxml"));
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("./UI/application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
			 // Connect to MySQL
//            String connectionString = "jdbc:mysql://localhost:3306/flexjobs";
//            String userName = "root";
//            String password = "Shaif2004.";
//            Connection connection = DriverManager.getConnection(connectionString, userName, password);
//            System.out.println("Connected to the database!");
//            Statement statement = connection.createStatement();
//            ResultSet resultSet = statement.executeQuery("SHOW TABLES");
//
//            // Print all table names
//            System.out.println("Tables in the FlexJobs database:");
//            while (resultSet.next()) {
//                String tableName = resultSet.getString(1); // Get the table name from the result set
//                System.out.println(tableName); // Print the table name
//            }
//
//            // Close resources
//            resultSet.close();
//            statement.close();
//            connection.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
=======
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
>>>>>>> e6f2383b4b6b2f9fb7897aeb94342e065ce0f6cd
