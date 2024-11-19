package application;

import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;

public class Student {
	private int semester;
	String rollNo;
	private String email;
	private String name;
	private String password;
	private String department;
	private double cgpa;

	// Constructor
	public Student(String rollNo, String email, String name, String password, String department, double cgpa) {
		this.rollNo = rollNo;
		this.email = email;
		this.name = name;
		this.password = password;
		this.department = department;
		this.cgpa = cgpa;
	}

	public String getRollNo() {
		return rollNo;
	}

	public void setRollNo(String rollNo) {
		this.rollNo = rollNo;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public double getCgpa() {
		return cgpa;
	}

	public void setCgpa(double cgpa) {
		this.cgpa = cgpa;
	}

	public double getSemester() {
		return semester;
	}

	public void setSemester(int sem) {
		semester = sem;
	}

	public boolean applyForOpportunity(String opportunityId) {
		return true;
	}

	public Chat openChatBox() {
		return new Chat();
	}

	public List<Application> getApplications() {
		return new ArrayList<>();
	}
}
