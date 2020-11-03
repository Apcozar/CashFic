package es.udc.fi.dc.fd.service.user.exceptions;

public class UserNotFoundException extends Exception {

	/**
	 * SERIAL UID for Serializable
	 */
	private static final long serialVersionUID = -200841644731353422L;

	/**
	 * The public constructor for The User Not Found Exception
	 * 
	 * @param login The login of the user who is not found
	 */
	public UserNotFoundException(String login) {
		super("The user with login: " + login + " not found");
	}

	public UserNotFoundException(Integer id) {
		super("The user with id: " + id + " not exist");
	}

}
