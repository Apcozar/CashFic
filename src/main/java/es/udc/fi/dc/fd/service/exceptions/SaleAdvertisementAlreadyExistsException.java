package es.udc.fi.dc.fd.service.exceptions;

/**
 * The ImageAlreadyExistsException used when add an image that already exists
 * 
 * @author Santiago
 */
public class SaleAdvertisementAlreadyExistsException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1544036070769130658L;

	private final Integer id;

	public SaleAdvertisementAlreadyExistsException(Integer saleAdvertisementId) {
		super("The sale advertisement with id: " + saleAdvertisementId + " already exists");
		id = saleAdvertisementId;
	}

	public Integer getImageId() {
		return id;
	}

}