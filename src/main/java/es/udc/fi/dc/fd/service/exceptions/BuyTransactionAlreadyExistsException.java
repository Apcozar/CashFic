package es.udc.fi.dc.fd.service.exceptions;

import es.udc.fi.dc.fd.model.persistence.BuyTransactionId;

/**
 * The BuyTransactionAlreadyExistsException used when add a buy transaction with
 * sale advertisement which is sold
 * 
 * 
 * @author Santiago
 */
public class BuyTransactionAlreadyExistsException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1544036070769130658L;

	private final BuyTransactionId identifier;

	public BuyTransactionAlreadyExistsException(BuyTransactionId identifier) {
		super("The buy transaction with sale advertisement: " + identifier.getSaleAdvertisementId()
				+ " already exists");
		this.identifier = identifier;
	}

	public BuyTransactionId getBuyTransactionId() {
		return identifier;
	}

}