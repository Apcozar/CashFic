package es.udc.fi.dc.fd.service.user.exceptions;

/**
 * The Class UserNoRatingException.
 */
public class UserNoRatingException extends Exception {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * The public constructor for The User No Rating Exception.
	 *
	 * @param userId the user id
	 */
	public UserNoRatingException(Integer userId) {
		super("The user with the id: " + userId + " no have rating yet");
	}
}
