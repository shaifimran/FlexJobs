package application;

import java.util.List;

public class Organisation {
	private String orgID;
	private String name;
	private String industry;
	private String description;
	private String location;
	private String contactEmail;
	private boolean isVerified;
	private List<OrganisationRepresentative> representatives; // List of representatives

	public Organisation(String orgID, String name, String industry, String description, String location,
			String contactEmail, boolean isVerified) {
		this.setOrgID(orgID);
		this.name = name;
		this.industry = industry;
		this.description = description;
		this.location = location;
		this.contactEmail = contactEmail;
		this.isVerified = isVerified;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIndustry() {
		return industry;
	}

	public void setIndustry(String industry) {
		this.industry = industry;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getContactEmail() {
		return contactEmail;
	}

	public void setContactEmail(String contactEmail) {
		this.contactEmail = contactEmail;
	}

	public boolean isVerified() {
		return isVerified;
	}

	public void setVerified(boolean isVerified) {
		this.isVerified = isVerified;
	}

	public List<OrganisationRepresentative> getRepresentatives() {
		return representatives;
	}

	public String getOrgID() {
		return orgID;
	}

	public void setOrgID(String orgID) {
		this.orgID = orgID;
	}

	public void setRepresentatives(List<OrganisationRepresentative> representatives) {
		this.representatives = representatives;
	}

	public boolean approveOrReject(boolean status) {
		// Approval or rejection logic
		return true;
	}

	public Opportunity createOpportunity(String type, String title, String description, String requirements,
			List<String> eligibleDepartments) {
		// Create opportunity logic
		return new Opportunity();
	}

}
