package es.udc.fi.dc.fd.service.user.exceptions;

public class UserLoginExistsException extends Exception {

	/**
	 * SERIAL UID for Serializable
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The public constructor for The User Name Exists Exception
	 * 
	 * @param login The login who is already registered
	 */
	public UserLoginExistsException(String login) {
		super("The login: " + login + " already exists");
	}
}