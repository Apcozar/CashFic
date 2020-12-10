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

package es.udc.fi.dc.fd.service;

import java.util.List;

import es.udc.fi.dc.fd.model.BuyTransactionEntity;
import es.udc.fi.dc.fd.model.SaleAdvertisementEntity;
import es.udc.fi.dc.fd.model.UserEntity;
import es.udc.fi.dc.fd.model.persistence.BuyTransactionId;
import es.udc.fi.dc.fd.model.persistence.DefaultBuyTransactionEntity;
import es.udc.fi.dc.fd.service.exceptions.BuyTransactionAlreadyExistsException;
import es.udc.fi.dc.fd.service.exceptions.BuyTransactionNotFoundException;
import es.udc.fi.dc.fd.service.exceptions.SaleAdvertisementNotFoundException;
import es.udc.fi.dc.fd.service.user.exceptions.UserNotFoundException;

/**
 * Service for the buy transaction domain.
 * <p>
 * This is a domain service just to allow the endpoints querying the entities
 * they are asked for.
 *
 * @author Santiago
 */
public interface BuyTransactionService {

	/**
	 * Returns an buy transaction with the given identifier.
	 * <p>
	 * If no image exists with that id then throw exception
	 *
	 * @param identifier buy transaction's identifier to find @{link
	 *                   #BuyTransactionId}
	 * @return the buy transaction for the given identifier
	 * @throws BuyTransactionNotFoundException exception when buy transaction not
	 *                                         found
	 */
	public BuyTransactionEntity findById(final BuyTransactionId identifier) throws BuyTransactionNotFoundException;

	/**
	 * Store an buy transaction which no exist.
	 * <p>
	 * Create an buy transaction with the entity parameters received. Returns the
	 * entity persisted
	 * 
	 * @param user              user who do buy transaction
	 * @param saleAdvertisement sale advertisement bought by user
	 * @return the persisted buy transaction
	 * @throws BuyTransactionAlreadyExistsException when the sale advertisement has
	 *                                              been sold
	 * @throws UserNotFoundException                when user not found
	 * @throws SaleAdvertisementNotFoundException   when sale advertisement not
	 *                                              found
	 */
	public BuyTransactionEntity create(final UserEntity user, final SaleAdvertisementEntity saleAdvertisement)
			throws BuyTransactionAlreadyExistsException, UserNotFoundException, SaleAdvertisementNotFoundException;

	/**
	 * Check if user have transaction with a sale advertisement
	 *
	 * @param user              the user to check transactions
	 * @param saleAdvertisement the sale advertisement to check if is in user's
	 *                          transactions
	 * @return true if successful, false if not
	 * @throws UserNotFoundException              when user not found
	 * @throws SaleAdvertisementNotFoundException when sale advertisement not found
	 */
	public boolean userHaveBoughtSaleAdvertisement(final UserEntity user,
			final SaleAdvertisementEntity saleAdvertisement)
			throws UserNotFoundException, SaleAdvertisementNotFoundException;

	/**
	 * @return All buy transactions stored
	 */
	public List<DefaultBuyTransactionEntity> getAllBuyTransactions();

}
