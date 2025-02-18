package es.udc.fi.dc.fd.service.user.exceptions;

public class UserIncorrectLoginException extends Exception {

	/**
	 * SERIAL UID for Serializable
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The public constructor for The User Name And Email Exists Exception
	 * 
	 * @param login    The login
	 * @param password The email
	 */
	public UserIncorrectLoginException(String login, String password) {
		super("The login : " + login + "and password: " + password + " are incorrect");
	}
}
