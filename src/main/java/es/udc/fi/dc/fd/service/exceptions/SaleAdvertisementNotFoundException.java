package es.udc.fi.dc.fd.service.exceptions;

/**
 * The ImageNotFoundException used when an image not by the service
 * 
 * @author Santiago
 */
public class SaleAdvertisementNotFoundException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1544036070769130658L;

	private final Integer id;

	public SaleAdvertisementNotFoundException(Integer saleAdvertisementId) {
		super("The image with id: " + saleAdvertisementId + " not found");
		id = saleAdvertisementId;
	}

	public Integer getImageId() {
		return id;
	}

}