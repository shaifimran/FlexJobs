package application;

public class ApplicationWithOpportunity {
	private Application app;
	private Opportunity opp;

	public ApplicationWithOpportunity(Application a, Opportunity o) {
		app = a;
		opp = o;
	}

	public Application getApplication() {
		return app;
	}

	public Opportunity getOpportunity() {
		return opp;
	}
}
