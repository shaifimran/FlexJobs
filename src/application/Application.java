package application;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Application {
	private String applicationID;
	private Date submitDate;
	private String status;
	private String feedback;
	private String studentID;

	public String getApplicationID() {
		return applicationID;
	}

	public void setApplicationID(String applicationID) {
		this.applicationID = applicationID;
	}

	public Date getSubmitDate() {
		return submitDate;
	}

	public void setSubmitDate(Date submitDate) {
		this.submitDate = submitDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getFeedback() {
		return feedback;
	}

	public void setFeedback(String feedback) {
		this.feedback = feedback;
	}

	public String getStudentID() {
		return studentID;
	}

	public void setStudentID(String studentID) {
		this.studentID = studentID;
	}

	public String getOpportunityID() {
		return opportunityID;
	}

	public void setOpportunityID(String opportunityID) {
		this.opportunityID = opportunityID;
	}

	private String opportunityID;

	public Application(String applicationID, Date submitDate, String status, String feedback, String studentID,
			String opportunityID) {
		this.applicationID = applicationID;
		this.submitDate = submitDate;
		this.status = status;
		this.feedback = feedback;
		this.studentID = studentID;
		this.opportunityID = opportunityID;
	}

	public List<Application> applyFilters(Map<String, Object> criteria, List<Application> applicantList) {
		// Apply filters to applications
		return new ArrayList<>();
	}

	public boolean fillApplicationForm() {
		// Fill application form logic
		return true;
	}

	public boolean sendToOrganization(String organizationId) {
		// Logic to send application to organization
		return true;
	}
}
