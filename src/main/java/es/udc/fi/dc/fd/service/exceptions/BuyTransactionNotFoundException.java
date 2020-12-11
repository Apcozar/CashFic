package es.udc.fi.dc.fd.service.exceptions;

import es.udc.fi.dc.fd.model.persistence.BuyTransactionId;

/**
 * The BuyTransactionNotFoundException used when a buy transaction not find by
 * the service
 * 
 * @author Santiago
 */
public class BuyTransactionNotFoundException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1544036070769130658L;

	private final BuyTransactionId identifier;

	public BuyTransactionNotFoundException(BuyTransactionId identifier) {
		super("The buy transaction with identifier  - saleAdvertisementId:" + identifier.getSaleAdvertisementId()
				+ " - userId: " + identifier.getUserId() + " not found");
		this.identifier = identifier;
	}

	public BuyTransactionId getBuyTransactionId() {
		return identifier;
	}

}