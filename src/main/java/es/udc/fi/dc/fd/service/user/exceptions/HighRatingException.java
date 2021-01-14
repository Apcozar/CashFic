package es.udc.fi.dc.fd.service.user.exceptions;

/**
 * The Class HighRatingException.
 */
public class HighRatingException extends Exception {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Low rating exception.
	 *
	 * @param rating    the rating
	 * @param maxRating the max rating
	 */
	public HighRatingException(Integer rating, Integer maxRating) {
		super("Cannot rate with value: " + rating + "expected max value: " + maxRating);
	}
}
