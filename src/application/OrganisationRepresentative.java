package application;

public class OrganisationRepresentative {
	private String name;
	private String password;
	private String email;
	private String position;
	private String phone;
	private String orgID;
	private Boolean isVerified;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getOrgID() {
		return orgID;
	}

	public void setOrgID(String orgID) {
		this.orgID = orgID;
	}

	public OrganisationRepresentative(String name, String password, String email, String position, String phone, String orgID) {
		this.name = name;
		this.password = password;
		this.email = email;
		this.position = position;
		this.phone = phone;
		this.orgID = orgID;
	}

	public Chat openChatBox() {
		// Open chat box logic
		return new Chat(email, null, email, email, null);
	}

	public Boolean getVerified() {
		return isVerified;
	}

	public void setVerified(Boolean isVerified) {
		this.isVerified = isVerified;
	}
}
