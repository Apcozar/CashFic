package es.udc.fi.dc.fd.model.persistence;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * Composite primary key class for {@link DefaultBuyTransactionEntity}
 * 
 * @author Santiago
 */
@Embeddable
public final class BuyTransactionId implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6359321665490887294L;

	@Column(name = "user_id")
	private Integer userId;

	@Column(name = "sale_advertisement_id")
	private Integer saleAdvertisementId;

	/**
	 * Serialization ID.
	 */
	public BuyTransactionId() {
	}

	/**
	 * @param userId              user's identifier
	 * @param saleAdvertisementId sale advertisement's identifier
	 */
	public BuyTransactionId(Integer userId, Integer saleAdvertisementId) {
		this.userId = userId;
		this.saleAdvertisementId = saleAdvertisementId;
	}

	/**
	 * @return the user's identifier
	 */
	public Integer getUserId() {
		return userId;
	}

	/**
	 * @param userId the user's identifier to set
	 */
	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	/**
	 * @return the sale advertisement's identifier
	 */
	public Integer getSaleAdvertisementId() {
		return saleAdvertisementId;
	}

	/**
	 * @param saleAdvertisementId the sale advertisement's identifier to set
	 */
	public void setSaleAdvertisementId(Integer saleAdvertisementId) {
		this.saleAdvertisementId = saleAdvertisementId;
	}

	@Override
	public int hashCode() {
		return Objects.hash(saleAdvertisementId, userId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof BuyTransactionId))
			return false;
		BuyTransactionId other = (BuyTransactionId) obj;
		return Objects.equals(saleAdvertisementId, other.saleAdvertisementId) && Objects.equals(userId, other.userId);
	}

}
