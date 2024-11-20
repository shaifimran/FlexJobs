package application;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Opportunity {
	private String opportunityID;
	private String title;
	private String description;
	private String category;
	
	public void setCategory(String Category) {
		this.category = Category;
	}
	
	public String getCategory() {
		return this.category;
	}

	public String getOpportunityID() {
		return opportunityID;
	}

	public void setOpportunityID(String opportunityID) {
		this.opportunityID = opportunityID;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public String getPostedBy() {
		return postedBy;
	}

	public void setPostedBy(String postedBy) {
		this.postedBy = postedBy;
	}

	private String requirements;
	private String status;
	private String postedBy;

	public Opportunity(String opportunityID, String title, String description, String requirements, String status,
			String postedBy) {
		this.opportunityID = opportunityID;
		this.title = title;
		this.description = description;
		this.requirements = requirements;
		this.status = status;
		this.postedBy = postedBy;
	}

	public List<Opportunity> applyFilters(Map<String, Object> criteria, List<Opportunity> opportunitiesList) {
		// Apply filters to opportunities
		return new ArrayList<>();
	}

	public List<Opportunity> showOpportunities() {
		// Display opportunities
		return new ArrayList<>();
	}
}
