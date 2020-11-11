package es.udc.fi.dc.fd.service.exceptions;

/**
 * The Class SaleAdvertisementAlreadyOnSaleException.
 */
public class SaleAdvertisementAlreadyOnSaleException extends Exception {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new sale advertisement already on sale exception.
	 *
	 * @param id the id
	 */
	public SaleAdvertisementAlreadyOnSaleException(Integer id) {
		super("The sale advertisemetn with id: " + id + "is already on sale");
	}
}
