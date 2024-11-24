package application;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Opportunity {
	private int opportunityID;
	private String title;
	private String description;
	private String postedBy;

	public int getOpportunityID() {
		return opportunityID;
	}

	public void setOpportunityID(int opportunityID) {
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

	public String getPostedBy() {
		return postedBy;
	}

	public void setPostedBy(String postedBy) {
		this.postedBy = postedBy;
	}

	public Opportunity(int opportunityID, String title, String description, String postedBy) {
		this.opportunityID = opportunityID;
		this.title = title;
		this.description = description;
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
