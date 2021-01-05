/**
 * The MIT License (MIT)
 * <p>
 * Copyright (c) 2020 the original author or authors.
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package es.udc.fi.dc.fd.model.persistence;

import java.time.LocalDateTime;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import es.udc.fi.dc.fd.model.BuyTransactionEntity;

/**
 * Persistent entity for buy transactions.
 * <p>
 * This makes use of JPA annotations for the persistence configuration.
 *
 * @author Santiago
 */
@Entity(name = "BuyTransactionEntity")
@Table(name = "buy_transactions")
public final class DefaultBuyTransactionEntity implements BuyTransactionEntity {

	/**
	 * Serialization ID.
	 */
	@Transient
	private static final long serialVersionUID = 1328776989450853491L;

	@EmbeddedId
	private BuyTransactionId id;

	@Column(name = "created_at")
	private LocalDateTime createdDate;

	/**
	 * user who buy sale advertisement
	 */
	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false, insertable = false, updatable = false)
	private DefaultUserEntity user;

	/**
	 * Sale Advertisement which user buy
	 */
	@OneToOne
	@JoinColumn(name = "sale_advertisement_id", nullable = false, insertable = false, updatable = false)
	private DefaultSaleAdvertisementEntity saleAdvertisement;

	/**
	 * Constructs an image entity.
	 */
	public DefaultBuyTransactionEntity() {
		super();
	}

	/**
	 * 
	 * Constructs an image entity.
	 *
	 * @param buyTransactionId the buy transaction identifier class
	 * @param createdDate      the created date
	 */
	public DefaultBuyTransactionEntity(BuyTransactionId buyTransactionId, LocalDateTime createdDate) {
		id = buyTransactionId;
		this.createdDate = createdDate;
	}

	/**
	 * @return the buy transaction's identifier
	 */
	@Override
	public final BuyTransactionId getId() {
		return id;
	}

	/**
	 * @return the created date
	 */
	@Override
	public final LocalDateTime getCreatedDate() {
		return createdDate;
	}

	/**
	 * @return the user who owns buy transaction
	 */
	@Override
	public final DefaultUserEntity getUser() {
		return user;
	}

	/**
	 * @return the saleAdvertisement in transaction
	 */
	@Override
	public final DefaultSaleAdvertisementEntity getSaleAdvertisement() {
		return saleAdvertisement;
	}

	/**
	 * @param identifier the buy transaction's identifier class with params
	 */
	@Override
	public final void setId(BuyTransactionId identifier) {
		id = identifier;
	}

	/**
	 * @param createdDate the created date to set
	 */
	@Override
	public final void setCreatedDate(LocalDateTime createdDate) {
		this.createdDate = createdDate;
	}

	/**
	 * @param user the user to set transaction owner
	 */
	@Override
	public final void setUser(DefaultUserEntity user) {
		this.user = user;
	}

	/**
	 * @param saleAdvertisement the sale advertisement to set in transaction
	 */
	@Override
	public final void setSaleAdvertisement(DefaultSaleAdvertisementEntity saleAdvertisement) {
		this.saleAdvertisement = saleAdvertisement;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, saleAdvertisement, user);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof DefaultBuyTransactionEntity))
			return false;
		DefaultBuyTransactionEntity other = (DefaultBuyTransactionEntity) obj;
		return Objects.equals(id, other.id) && Objects.equals(saleAdvertisement, other.saleAdvertisement)
				&& Objects.equals(user, other.user);
	}

}
