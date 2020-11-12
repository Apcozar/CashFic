package es.udc.fi.dc.fd.service.user.exceptions;

import es.udc.fi.dc.fd.model.UserEntity;

public class UserToUnfollowNotFoundException extends Exception {

	/**
	 * SERIAL UID for Serializable
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new user to unfollow not found exception.
	 *
	 * @param user the user
	 */
	public UserToUnfollowNotFoundException(UserEntity user) {
		super("The user with the id: " + user.getId() + " dont exists in the followed list");
	}

}
