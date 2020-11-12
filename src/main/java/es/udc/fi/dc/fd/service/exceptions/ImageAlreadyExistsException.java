package es.udc.fi.dc.fd.service.exceptions;

/**
 * The ImageAlreadyExistsException used when add an image that already exists
 * 
 * @author Santiago
 */
public class ImageAlreadyExistsException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1544036070769130658L;

	private final Integer id;

	public ImageAlreadyExistsException(Integer imageId) {
		super("The image with id: " + imageId + " already exists");
		id = imageId;
	}

	public Integer getImageId() {
		return id;
	}

}