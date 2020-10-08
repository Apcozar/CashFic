package es.udc.fi.dc.service.exceptions;

public class ImageServiceException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1544036070769130658L;

	public ImageServiceException(String errorMessage) {
		super(errorMessage);
	}
}