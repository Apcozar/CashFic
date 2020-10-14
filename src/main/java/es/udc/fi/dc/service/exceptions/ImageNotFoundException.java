package es.udc.fi.dc.service.exceptions;

/**
 * The ImageNotFoundException used when an image not by the service
 * 
 * @author Santiago
 */
public class ImageNotFoundException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1544036070769130658L;

	private final Integer id;

	public ImageNotFoundException(Integer imageId) {
		super("The image with id: " + imageId + " not found");
		id = imageId;
	}

	public Integer getImageId() {
		return id;
	}

}