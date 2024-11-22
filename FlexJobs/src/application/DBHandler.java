package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBHandler {
    private final String dbUrl = "jdbc:mysql://localhost:3306/FlexJobs";
    private final String dbUser = "root";
    private final String dbPassword = "adeelinmysql123!@#";
    private Connection conn;

    // Get a connection to the database
    public Connection getConnection() throws SQLException {
        if (conn != null && !conn.isClosed()) {
            return conn; 
        }

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL Driver not found: " + e.getMessage());
            throw new SQLException("Driver not found.", e);
        } catch (Exception e) {
            System.err.println("Unable to connect to the database!");
            e.printStackTrace();
            throw new SQLException("Database connection failed.", e);
        }

        return conn;
    }

    // Close the connection to the database
    public void closeConnection() {
        if (conn != null) {
            try {
                conn.close();
                conn = null; // Reset the connection to avoid reuse
            } catch (SQLException e) {
                System.err.println("Failed to close the connection: " + e.getMessage());
            }
        }
    }

    // Verify credentials of OrganisationRepresentative
    public OrganisationRepresentative verifyOrganisationRepresentativeCredentials(String email, String password) {
        String query = "SELECT * FROM OrganisationRepresentative WHERE email = ? AND password = ?";

        try (Connection connection = getConnection(); 
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, email);
            stmt.setString(2, password);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs != null && rs.next()) {
                    String name = rs.getString("name");
                    String pass = rs.getString("password");
                    String emailFromDb = rs.getString("email");
                    String position = rs.getString("position");
                    String phone = rs.getString("phone");
                    String orgID = rs.getString("orgID");

                    return new OrganisationRepresentative(name, pass, emailFromDb, position, phone, orgID);
                }
            }

        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
        }
        return null;  // Return null if no matching user found or an error occurs
    }

    // Add a new Organisation Representative to the database
    public void addOrgRepresentative(String name, String phone, String password, String email, String organisation, String position) {
        String query = "INSERT INTO OrganisationRepresentative (name, password, email, position, phone, orgID) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, name);
            statement.setString(2, password);
            statement.setString(3, email);
            statement.setString(4, position);
            statement.setString(5, phone);
            statement.setString(6, organisation);

            statement.executeUpdate();
            System.out.println("Organisation Representative added successfully.");

        } catch (SQLException e) {
            System.err.println("Error adding Organisation Representative: " + e.getMessage());
        }
    }

    // Add a new Organisation to the database
    public void addOrganisation(String name, String industry, String description, String location, String contactEmail, Boolean isVerified) {
        String orgQuery = "INSERT INTO Organisation (name, industry, description, location, contactEmail, isVerified) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = getConnection();
             PreparedStatement orgStatement = connection.prepareStatement(orgQuery)) {

            orgStatement.setString(1, name);
            orgStatement.setString(2, industry);
            orgStatement.setString(3, description);
            orgStatement.setString(4, location);
            orgStatement.setString(5, contactEmail);
            orgStatement.setBoolean(6, isVerified);

            orgStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Check if an Organisation Representative exists in the database
    public boolean isOrganisationRepExists(String organisation, String email) {
        boolean exists = false;
        String query = "SELECT * FROM OrganisationRepresentative WHERE orgID = ? AND email = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, organisation);
            statement.setString(2, email);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    exists = true;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return exists;
    }

    // Check if an Organisation exists in the database
    public boolean isOrganisationExists(String organisationName) {
        boolean exists = false;
        String query = "SELECT * FROM Organisation WHERE name = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, organisationName);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    exists = true;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return exists;
    }
}
