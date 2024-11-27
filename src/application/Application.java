package application;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Application {
	private int applicationID;
	private Date submitDate;
	private String status;
	private String feedback;
	private String studentID;
	private int interviewID;
	private int opportunityID;

	public int getInterviewID() {
		return this.interviewID;
	}

	public void setInterviewID(int InterviewID) {
		this.interviewID = InterviewID;
	}

	public int getApplicationID() {
		return applicationID;
	}

	public void setApplicationID(int applicationID) {
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

	public int getOpportunityID() {
		return opportunityID;
	}

	public void setOpportunityID(int opportunityID) {
		this.opportunityID = opportunityID;
	}

	public Application(int applicationID, Date submitDate, String status, String feedback, String studentID,
			int interviewID, int opportunityID) {
		this.applicationID = applicationID;
		this.submitDate = submitDate;
		this.status = status;
		this.feedback = feedback;
		this.studentID = studentID;
		this.interviewID = interviewID;
		this.opportunityID = opportunityID;
	}

	public Application(int applicationID, String status, String feedback, String rollNo, int interviewID,
			int opportunityID) {
		// TODO Auto-generated constructor stub

		this.applicationID = applicationID;
		this.status = status;
		this.feedback = feedback;
		this.interviewID = interviewID;
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
