package es.udc.fi.dc.fd.service.user.exceptions;

public class UserLoginAndEmailExistsException extends Exception {

	/**
	 * SERIAL UID for Serializable
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The public constructor for The User Name And Email Exists Exception
	 * 
	 * @param login     The login who is already registered
	 * @param userEmail The email who is already registered
	 */
	public UserLoginAndEmailExistsException(String login, String userEmail) {
		super("The login name: " + login + "and email: " + userEmail + " already exists");
	}
}
