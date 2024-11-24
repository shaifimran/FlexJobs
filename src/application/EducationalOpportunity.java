package application;

public class EducationalOpportunity extends Opportunity {

	public EducationalOpportunity() {
		super(0, "", "", "");
	}

	public EducationalOpportunity(int opportunityID, String title, String description, String postedBy) {
		super(opportunityID, title, description, postedBy);
	}

//    // Example Method to Apply Filters
//    public List<EducationalOpportunity> applyFilters(Map<String, Object> criteria, List<EducationalOpportunity> opportunitiesList) {
//        // Implement filtering logic here
//        return new ArrayList<>();
//    }
//
//    // Example Method to Display Opportunities
//    public List<EducationalOpportunity> showOpportunities() {
//        // Implement display logic here
//        return new ArrayList<>();
//    }
}
