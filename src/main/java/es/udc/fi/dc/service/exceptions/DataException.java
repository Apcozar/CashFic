package es.udc.fi.dc.service.exceptions;

public class DataException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1544036070769130658L;

	public DataException(String errorMessage) {
		super(errorMessage);
	}
}