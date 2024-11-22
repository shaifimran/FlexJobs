package application;

import java.util.Date;
import java.util.List;

public class Admin {
	private String name;
	private String email;
	private String password;

	// Constructor
	public Admin(String name, String email) {
		this.name = name;
		this.email = email;
	}

	// Getter and Setter for name
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	// Getter and Setter for email
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	// Getter and Setter for password
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	// Method to prepare a report
	public Report prepareReport(Date startDate, Date endDate, String department, List<Integer> batches) {
		// Report preparation logic
		return new Report();
	}

	public void verifyOrganization(int regID) {
		// Report preparation logic
	}
}
