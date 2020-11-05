package es.udc.fi.dc.fd.service.user.exceptions;

import es.udc.fi.dc.fd.model.UserEntity;

public class UserToFollowExistsException extends Exception {

	/**
	 * SERIAL UID for Serializable
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new user to follow exists exception.
	 *
	 * @param user the user
	 */
	public UserToFollowExistsException(UserEntity user) {
		super("The user with the id: " + user.getId() + " already exists in followed list");
	}

}
