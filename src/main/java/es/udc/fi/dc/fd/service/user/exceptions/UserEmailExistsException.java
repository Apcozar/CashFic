package es.udc.fi.dc.fd.service.user.exceptions;

public class UserEmailExistsException extends Exception {

	/**
	 * SERIAL UID for Serializable
	 */
	private static final long serialVersionUID = -5869182150630035885L;

	/**
	 * The public constructor for The User Email Exists Exception
	 * 
	 * @param email The email who is already registered
	 */
	public UserEmailExistsException(String email) {
		super("The user email: " + email + " already exists");
	}
}
