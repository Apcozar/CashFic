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

package es.udc.fi.dc.fd.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import es.udc.fi.dc.fd.model.persistence.BuyTransactionId;
import es.udc.fi.dc.fd.model.persistence.DefaultSaleAdvertisementEntity;
import es.udc.fi.dc.fd.model.persistence.DefaultUserEntity;

/**
 * A image entity.
 *
 * @author Santiago
 */
public interface BuyTransactionEntity extends Serializable {

	/**
	 * Returns the identifier assigned to this buy transaction.
	 * <p>
	 * If no identifier has been assigned yet, then the value is expected to be
	 * {@code null} or lower than zero.
	 *
	 * @return the buy transaction's identifier
	 */
	public BuyTransactionId getId();

	/**
	 * Returns the creation date of the buy transaction.
	 *
	 * @return the buy transaction's path
	 */
	public LocalDateTime getCreatedDate();

	/**
	 * Sets the identifier assigned to buy transaction.
	 *
	 * @param identifier the identifier for the buy transaction
	 */
	public void setId(final BuyTransactionId identifier);

	/**
	 * Changes the createdDate of the buy transaction.
	 *
	 * @param createdDate the created date to set on buy transaction
	 */
	public void setCreatedDate(final LocalDateTime createdDate);

	/**
	 * @return the user who owns buy transaction
	 */
	DefaultUserEntity getUser();

	/**
	 * @return the saleAdvertisement in transaction
	 */
	DefaultSaleAdvertisementEntity getSaleAdvertisement();

	/**
	 * @param user the user to set transaction owner
	 */
	void setUser(DefaultUserEntity user);

	/**
	 * @param saleAdvertisement the sale advertisement to set in transaction
	 */
	void setSaleAdvertisement(DefaultSaleAdvertisementEntity saleAdvertisement);

}
