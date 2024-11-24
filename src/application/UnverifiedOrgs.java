package application;

import java.util.ArrayList;
import java.util.List;

public class UnverifiedOrgs {
	private List<Organisation> unverifiedOrgs;

	private List<OrganisationRepresentative> unverifiedInitialOrgReps;

	public UnverifiedOrgs() {
		this.unverifiedOrgs = new ArrayList<>();
		this.unverifiedInitialOrgReps = new ArrayList<>();
	}

	public void addOrganization(Organisation organisation) {
		if (this.unverifiedOrgs == null) {
			this.unverifiedOrgs = new ArrayList<>();
		}
		this.unverifiedOrgs.add(organisation);
	}

	public UnverifiedOrgs(List<Organisation> unverifiedOrgs,
			List<OrganisationRepresentative> unverifiedRepresentatives) {
		this.unverifiedOrgs = unverifiedOrgs;
		this.unverifiedInitialOrgReps = unverifiedRepresentatives;
	}

	public List<Organisation> getUnverifiedOrgs() {
		return unverifiedOrgs;
	}

	public List<OrganisationRepresentative> getUnverifiedRepresentatives() {
		return unverifiedInitialOrgReps;
	}

	public void addRepresentative(OrganisationRepresentative representative) {
		if (this.unverifiedInitialOrgReps == null) {
			this.unverifiedInitialOrgReps = new ArrayList<>();
		}
		this.unverifiedInitialOrgReps.add(representative);
	}

}
