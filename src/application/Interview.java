package application;

import java.sql.Date;

public class Interview {
	private String interviewID;
	private String candidateID;
	private Date timeSlot;
	private String location;

	public String getInterviewID() {
		return interviewID;
	}

	public void setInterviewID(String interviewID) {
		this.interviewID = interviewID;
	}

	public String getCandidateID() {
		return candidateID;
	}

	public void setCandidateID(String candidateID) {
		this.candidateID = candidateID;
	}

	public Date getTimeSlot() {
		return timeSlot;
	}

	public void setTimeSlot(Date timeSlot) {
		this.timeSlot = timeSlot;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	private String type;
	private String status;

	public Interview(String interviewID, String candidateID, Date timeSlot, String location, String type,
			String status) {
		this.interviewID = interviewID;
		this.candidateID = candidateID;
		this.timeSlot = timeSlot;
		this.location = location;
		this.type = type;
		this.status = status;
	}

	public boolean setInterviewDetails(String studentID, Date time) {
		// Logic to set interview details
		return true;
	}
}