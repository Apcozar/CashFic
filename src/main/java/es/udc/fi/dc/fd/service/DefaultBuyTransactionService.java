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

import static com.google.common.base.Preconditions.checkNotNull;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.udc.fi.dc.fd.model.BuyTransactionEntity;
import es.udc.fi.dc.fd.model.SaleAdvertisementEntity;
import es.udc.fi.dc.fd.model.UserEntity;
import es.udc.fi.dc.fd.model.persistence.BuyTransactionId;
import es.udc.fi.dc.fd.model.persistence.DefaultBuyTransactionEntity;
import es.udc.fi.dc.fd.model.persistence.DefaultSaleAdvertisementEntity;
import es.udc.fi.dc.fd.model.persistence.DefaultUserEntity;
import es.udc.fi.dc.fd.repository.BuyTransactionRepository;
import es.udc.fi.dc.fd.repository.SaleAdvertisementRepository;
import es.udc.fi.dc.fd.repository.UserRepository;
import es.udc.fi.dc.fd.service.exceptions.BuyTransactionAlreadyExistsException;
import es.udc.fi.dc.fd.service.exceptions.BuyTransactionNotFoundException;
import es.udc.fi.dc.fd.service.exceptions.SaleAdvertisementNotFoundException;
import es.udc.fi.dc.fd.service.user.exceptions.UserNotFoundException;

/**
 * Default implementation of the @{link #BuyTransactionService}.
 * 
 * @author Santiago
 *
 */
@Service
public class DefaultBuyTransactionService implements BuyTransactionService {

	/**
	 * Repositories for the domain entities handled by the service.
	 */
	private final SaleAdvertisementRepository saleAdvertisementRepository;
	private final UserRepository userRepository;
	private final BuyTransactionRepository buyTransactionRepository;

	/**
	 * Constructs an image service with the specified repositories.
	 *
	 * @param saleAdvertisementRepository the repository for the sale advertisement
	 *                                    instances
	 * @param buyTransactionRepository    the repository for the buy transaction
	 *                                    instances
	 * @param userRepository              the repository for the user instances
	 */
	@Autowired
	public DefaultBuyTransactionService(final SaleAdvertisementRepository saleAdvertisementRepository,
			final BuyTransactionRepository buyTransactionRepository, final UserRepository userRepository) {
		super();

		this.saleAdvertisementRepository = checkNotNull(saleAdvertisementRepository,
				"Received a null pointer as saleAdvertisementRepository");
		this.buyTransactionRepository = checkNotNull(buyTransactionRepository,
				"Received a null pointer as buyTransactionRepository");
		this.userRepository = checkNotNull(userRepository, "Received a null pointer as userRepository");
	}

	@Override
	public BuyTransactionEntity findById(BuyTransactionId identifier) throws BuyTransactionNotFoundException {
		BuyTransactionEntity result = null;
		if (!buyTransactionRepository.existsById(identifier)) {
			throw new BuyTransactionNotFoundException(identifier);
		} else {
			result = buyTransactionRepository.getOne(identifier);
		}
		return result;
	}

	@Override
	public BuyTransactionEntity create(UserEntity user, SaleAdvertisementEntity saleAdvertisement)
			throws UserNotFoundException, SaleAdvertisementNotFoundException, BuyTransactionAlreadyExistsException {
		if (!userRepository.existsById(user.getId())) {
			throw new UserNotFoundException(user.getId());
		}
		if (!saleAdvertisementRepository.existsById(saleAdvertisement.getId())) {
			throw new SaleAdvertisementNotFoundException(saleAdvertisement.getId());
		}
		BuyTransactionId transactionIdentifier = new BuyTransactionId(user.getId(), saleAdvertisement.getId());
		// DEBERIA COMPROBARSE SI EXISTE ALGUN SALE ADVERT CON ESE ID, no la COMBINACION
		// USUARIO-ID y SALE-ID
		if (buyTransactionRepository.existsById(transactionIdentifier)) {
			throw new BuyTransactionAlreadyExistsException(transactionIdentifier);
		}
		// SOLUCION TEMPORAL
		if (saleAdvertisementRepository.getOne(saleAdvertisement.getId()).getBuyTransaction() != null) {
			throw new BuyTransactionAlreadyExistsException(transactionIdentifier);
		}

		// Create new BuyTransactionEntity
		DefaultBuyTransactionEntity newBuyTransaction = new DefaultBuyTransactionEntity(transactionIdentifier,
				LocalDateTime.now());
		newBuyTransaction.setUser((DefaultUserEntity) user);
		newBuyTransaction.setSaleAdvertisement((DefaultSaleAdvertisementEntity) saleAdvertisement);
		// Store buy transaction
		DefaultBuyTransactionEntity result = buyTransactionRepository.save(newBuyTransaction);

		// Modify user and saleAdvertisement

		saleAdvertisement.setBuyTransaction(result);
		user.addBuyTransaction(result);

		return result;
	}

	@Override
	public boolean userHaveBoughtSaleAdvertisement(UserEntity user, SaleAdvertisementEntity saleAdvertisement)
			throws UserNotFoundException, SaleAdvertisementNotFoundException {
		if (!userRepository.existsById(user.getId())) {
			throw new UserNotFoundException(user.getId());
		}
		if (!saleAdvertisementRepository.existsById(saleAdvertisement.getId())) {
			throw new SaleAdvertisementNotFoundException(saleAdvertisement.getId());
		}
		BuyTransactionId transactionIdentifier = new BuyTransactionId(user.getId(), saleAdvertisement.getId());

		return buyTransactionRepository.existsById(transactionIdentifier);
	}

	@Override
	public List<DefaultBuyTransactionEntity> getAllBuyTransactions() {
		return buyTransactionRepository.findAll();
	}

}
