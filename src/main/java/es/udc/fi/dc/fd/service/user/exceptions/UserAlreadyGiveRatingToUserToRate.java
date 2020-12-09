package es.udc.fi.dc.fd.service.user.exceptions;

/**
 * The Class UserAlreadyGiveRatingToUserToRate.
 */
public class UserAlreadyGiveRatingToUserToRate extends Exception {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new user already give rating to user to rate.
	 *
	 * @param userId       the user id
	 * @param userToRateId the user to rate id
	 */
	public UserAlreadyGiveRatingToUserToRate(Integer userId, Integer userToRateId) {
		super("The user with the id: " + userId + " alreay rate the user with the id:" + userToRateId);
	}

}
