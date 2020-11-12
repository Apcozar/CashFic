package es.udc.fi.dc.fd.service.user.exceptions;

public class UserEmailNotFoundException extends Exception {

	/**
	 * SERIAL UID for Serializable
	 */
	private static final long serialVersionUID = -8925347363352788545L;

	/**
	 * The public constructor for Email Not Found Exception
	 * 
	 * @param userEmail the user email
	 */
	public UserEmailNotFoundException(String userEmail) {
		super("User with email: " + userEmail + " not found");
	}

}