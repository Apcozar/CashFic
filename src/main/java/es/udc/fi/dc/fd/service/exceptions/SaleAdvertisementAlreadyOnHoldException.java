package es.udc.fi.dc.fd.service.exceptions;

/**
 * The Class SaleAdvertisementAlreadyOnHoldException.
 */
public class SaleAdvertisementAlreadyOnHoldException extends Exception {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new sale advertisement already on hold exception.
	 *
	 * @param id the id
	 */
	public SaleAdvertisementAlreadyOnHoldException(Integer id) {
		super("The sale advertisemetn with id: " + id + "is already on hold");
	}
}
