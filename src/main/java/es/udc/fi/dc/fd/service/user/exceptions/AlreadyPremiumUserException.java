package es.udc.fi.dc.fd.service.user.exceptions;

import es.udc.fi.dc.fd.model.UserEntity;

public class AlreadyPremiumUserException extends Exception {

	/**
	 * SERIAL UID for Serializable
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new user to already premium user exception.
	 *
	 * @param user the user
	 */
	public AlreadyPremiumUserException(UserEntity user) {
		super("The user with the id: " + user.getId() + " already is a premium user");
	}

}