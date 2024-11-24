package application;

public class OrgRepTableRow {
    private Organisation organization;
    private OrganisationRepresentative representative;

    public OrgRepTableRow(Organisation organization, OrganisationRepresentative representative) {
        this.organization = organization;
        this.representative = representative;
    }

    public Organisation getOrganization() {
        return organization;
    }

    public void setOrganization(Organisation organization) {
        this.organization = organization;
    }

    public OrganisationRepresentative getRepresentative() {
        return representative;
    }

    public void setRepresentative(OrganisationRepresentative representative) {
        this.representative = representative;
    }

    public String getOrgDetails() {
        return "Organisation Name: " + organization.getName() + "\n" + "Industry: " + organization.getIndustry() +
        "\n" + "Description: " + organization.getDescription() + "\n" + "Location: " + organization.getLocation() + 
        "\n" + "Contact Email: " + organization.getContactEmail();
    }

    public String getRepDetails() {
        return "Representative Name: " + representative.getName() + "\n" + "Email: " + representative.getEmail() + 
        		"\n" + "Position: " + representative.getPosition() + "\n" + "Phone: " + representative.getPhone();
    }
}
