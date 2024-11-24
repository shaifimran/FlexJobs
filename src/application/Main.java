package application;

import application.controllers.AdminDashboardController;
import application.controllers.AdminVerifyOrganisationController;
import application.controllers.ChatBoxController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			Student s = new Student();
			s.setRollNo("S12345");
			Organisation o = new Organisation();
			o.setName("Org1");
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/UI/AdminLogin.fxml"));
//            loader.setControllerFactory(c -> new ChatBoxController(o));
            Parent root = loader.load();
			
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