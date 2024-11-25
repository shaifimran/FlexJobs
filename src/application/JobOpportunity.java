package application;

public class JobOpportunity extends Opportunity {
	private String category;
	private String requirements;
	private String status;

	// Default Constructor
	public JobOpportunity() {
		super(0, "", "", "");
		this.category = "";
		this.requirements = "";
		this.status = "";
	}

	public JobOpportunity(int opportunityID, String title, String description, String category, String requirements,
			String status, String postedBy) {
		super(opportunityID, title, description, postedBy);
		this.category = category;
		this.requirements = requirements;
		this.status = status;
	}

	public JobOpportunity(int opportunityID, String title, String description, String category, String status,
			String postedBy) {
		super(opportunityID, title, description, postedBy);
		this.category = category;
		this.status = status;
	}

	public JobOpportunity(int opportunityID, String title, String description) {
		super(opportunityID, title, description, "");
		this.category = "";
		this.requirements = "";
		this.status = "";
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getRequirements() {
		return requirements;
	}

	public void setRequirements(String requirements) {
		this.requirements = requirements;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
