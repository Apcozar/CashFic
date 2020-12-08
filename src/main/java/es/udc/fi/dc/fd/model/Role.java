package es.udc.fi.dc.fd.model;

public enum Role {
	/** The role user. */
	ROLE_USER("ROLE_USER"), ROLE_PREMIUM("ROLE_PREMIUM");

	/** The role. */
	private String userRole;

	/**
	 * Instantiates a new role.
	 *
	 * @param role the role
	 */
	Role(String role) {
		this.userRole = role;
	}

	/**
	 * Gets the role.
	 *
	 * @return the role
	 */
	public String getRole() {
		return this.userRole;
	}

}