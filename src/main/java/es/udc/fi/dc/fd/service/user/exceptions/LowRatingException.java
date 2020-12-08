package es.udc.fi.dc.fd.service.user.exceptions;

/**
 * The Class LowRatingException.
 */
public class LowRatingException extends Exception {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new low rating exception.
	 *
	 * @param rating    the rating
	 * @param minRating the min rating
	 */
	public LowRatingException(Integer rating, Integer minRating) {
		super("Cannot rate with value: " + rating + "expected almost: " + minRating);
	}

}
