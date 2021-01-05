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

package es.udc.fi.dc.fd.test.unit.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import es.udc.fi.dc.fd.model.persistence.BuyTransactionId;
import es.udc.fi.dc.fd.model.persistence.DefaultBuyTransactionEntity;
import es.udc.fi.dc.fd.model.persistence.DefaultSaleAdvertisementEntity;
import es.udc.fi.dc.fd.model.persistence.DefaultUserEntity;
import es.udc.fi.dc.fd.repository.BuyTransactionRepository;
import es.udc.fi.dc.fd.repository.SaleAdvertisementRepository;
import es.udc.fi.dc.fd.repository.UserRepository;
import es.udc.fi.dc.fd.service.BuyTransactionService;
import es.udc.fi.dc.fd.service.DefaultBuyTransactionService;
import es.udc.fi.dc.fd.service.exceptions.BuyTransactionAlreadyExistsException;
import es.udc.fi.dc.fd.service.exceptions.BuyTransactionNotFoundException;
import es.udc.fi.dc.fd.service.exceptions.SaleAdvertisementNotFoundException;
import es.udc.fi.dc.fd.service.user.exceptions.UserNotFoundException;

/**
 * Unit tests for the {@link BuyTransactionService}.
 * <p>
 * As this service doesn't contain any actual business logic, and it just wraps
 * the example entities repository, these tests are for verifying everything is
 * set up correctly and working.
 */
@RunWith(JUnitPlatform.class)
final class TestBuyTransactionService {

	@Mock
	private SaleAdvertisementRepository saleAdvertisementRepository;

	@Mock
	private UserRepository userRepository;

	@Mock
	private BuyTransactionRepository buyTransactionRepository;
	/**
	 * Service being tested.
	 */
	@InjectMocks
	private DefaultBuyTransactionService buyTransactionService;

	/**
	 * Default constructor.
	 */
	public TestBuyTransactionService() {
		super();
	}

	@BeforeEach
	public void initialize() {
		MockitoAnnotations.initMocks(this);

	}

	@Test
	void findByIdNullThrowException() {
		assertThrows(NullPointerException.class, () -> {
			buyTransactionService.findById(null);
		});
	}

	@Test
	void findByIdNotFoundThrowException() {
		BuyTransactionId notExistingIdentifier = new BuyTransactionId();
		Mockito.when(buyTransactionRepository.existsById(notExistingIdentifier)).thenReturn(false);

		assertThrows(BuyTransactionNotFoundException.class, () -> {
			buyTransactionService.findById(notExistingIdentifier);
		});
	}

	@Test
	void findByIdReturnBuyTransaction() {
		BuyTransactionId identifier = new BuyTransactionId();
		Mockito.when(buyTransactionRepository.existsById(identifier)).thenReturn(true);
		DefaultBuyTransactionEntity buyTransaction = new DefaultBuyTransactionEntity();
		buyTransaction.setId(identifier);
		Mockito.when(buyTransactionRepository.getOne(identifier)).thenReturn(buyTransaction);
		try {
			assertEquals(buyTransaction, buyTransactionService.findById(identifier));
			assertEquals(identifier, buyTransactionService.findById(identifier).getId());
		} catch (BuyTransactionNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Test
	void createUserNullThrowException() {
		DefaultSaleAdvertisementEntity saleAdvertisement = new DefaultSaleAdvertisementEntity();
		assertThrows(NullPointerException.class, () -> {
			buyTransactionService.create(null, saleAdvertisement);
		});
	}

	@Test
	void createSaleAdvertisementNullThrowException() {
		DefaultUserEntity user = new DefaultUserEntity();
		assertThrows(NullPointerException.class, () -> {
			buyTransactionService.create(user, null);
		});
	}

	@Test
	void createUserNotFoundThrowException() {
		int userId = 1;
		int saleAdvertisementId = 1;
		DefaultUserEntity user = new DefaultUserEntity();
		user.setId(userId);
		DefaultSaleAdvertisementEntity saleAdvertisement = new DefaultSaleAdvertisementEntity();
		saleAdvertisement.setId(saleAdvertisementId);
		Mockito.when(userRepository.existsById(userId)).thenReturn(false);
		Mockito.when(saleAdvertisementRepository.existsById(saleAdvertisementId)).thenReturn(true);
		assertThrows(UserNotFoundException.class, () -> {
			buyTransactionService.create(user, saleAdvertisement);
		});
	}

	@Test
	void createSaleAdvertisementNotFoundThrowException() {
		int userId = 1;
		int saleAdvertisementId = 1;
		DefaultUserEntity user = new DefaultUserEntity();
		user.setId(userId);
		DefaultSaleAdvertisementEntity saleAdvertisement = new DefaultSaleAdvertisementEntity();
		saleAdvertisement.setId(saleAdvertisementId);
		Mockito.when(userRepository.existsById(userId)).thenReturn(true);
		Mockito.when(saleAdvertisementRepository.existsById(saleAdvertisementId)).thenReturn(false);
		assertThrows(SaleAdvertisementNotFoundException.class, () -> {
			buyTransactionService.create(user, saleAdvertisement);
		});
	}

	@Test
	void createBuyTransactionExistsThrowException() {
		int userId = 1;
		int saleAdvertisementId = 1;
		DefaultUserEntity user = new DefaultUserEntity();
		user.setId(userId);
		DefaultSaleAdvertisementEntity saleAdvertisement = new DefaultSaleAdvertisementEntity();
		saleAdvertisement.setId(saleAdvertisementId);
		Mockito.when(userRepository.existsById(userId)).thenReturn(true);
		Mockito.when(saleAdvertisementRepository.existsById(saleAdvertisementId)).thenReturn(true);

		BuyTransactionId buyTransactionId = new BuyTransactionId(userId, saleAdvertisementId);
		Mockito.when(buyTransactionRepository.existsById(buyTransactionId)).thenReturn(true);

		assertThrows(BuyTransactionAlreadyExistsException.class, () -> {
			buyTransactionService.create(user, saleAdvertisement);
		});
	}

	@Test
	void createSaleAdvertisementHaveBuyTransactionThrowException() {
		int userId = 1;
		int saleAdvertisementId = 1;
		DefaultUserEntity user = new DefaultUserEntity();
		user.setId(userId);
		DefaultSaleAdvertisementEntity saleAdvertisement = new DefaultSaleAdvertisementEntity();
		saleAdvertisement.setId(saleAdvertisementId);
		Mockito.when(userRepository.existsById(userId)).thenReturn(true);
		Mockito.when(saleAdvertisementRepository.existsById(saleAdvertisementId)).thenReturn(true);

		BuyTransactionId buyTransactionId = new BuyTransactionId(userId, saleAdvertisementId);
		Mockito.when(buyTransactionRepository.existsById(buyTransactionId)).thenReturn(false);

		DefaultBuyTransactionEntity buyTransaction = new DefaultBuyTransactionEntity();
		buyTransaction.setId(buyTransactionId);
		saleAdvertisement.setBuyTransaction(buyTransaction);

		Mockito.when(saleAdvertisementRepository.getOne(saleAdvertisementId)).thenReturn(saleAdvertisement);

		assertThrows(BuyTransactionAlreadyExistsException.class, () -> {
			buyTransactionService.create(user, saleAdvertisement);
		});
	}

	@Test
	void createBuyTransaction() {
		int userId = 1;
		int saleAdvertisementId = 1;
		DefaultUserEntity user = new DefaultUserEntity();
		user.setId(userId);
		DefaultSaleAdvertisementEntity saleAdvertisement = new DefaultSaleAdvertisementEntity();
		saleAdvertisement.setId(saleAdvertisementId);
		Mockito.when(userRepository.existsById(userId)).thenReturn(true);
		Mockito.when(saleAdvertisementRepository.existsById(saleAdvertisementId)).thenReturn(true);

		BuyTransactionId buyTransactionId = new BuyTransactionId(userId, saleAdvertisementId);
		Mockito.when(buyTransactionRepository.existsById(buyTransactionId)).thenReturn(false);

		DefaultBuyTransactionEntity buyTransaction = new DefaultBuyTransactionEntity();
		buyTransaction.setId(buyTransactionId);
		buyTransaction.setUser(user);
		buyTransaction.setSaleAdvertisement(saleAdvertisement);
		Mockito.when(saleAdvertisementRepository.getOne(saleAdvertisementId)).thenReturn(saleAdvertisement);
		Mockito.when(buyTransactionRepository.save(buyTransaction)).thenReturn(buyTransaction);

		try {
			assertEquals(buyTransactionService.create(user, saleAdvertisement), buyTransaction);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	void getAllBuyTransactions() {
		List<DefaultBuyTransactionEntity> buyTransactions = new ArrayList<>();
		Mockito.when(buyTransactionRepository.findAll()).thenReturn(buyTransactions);
		assertEquals(buyTransactionService.getAllBuyTransactions(), buyTransactions);
	}

	@Test
	void userHaveBoughtSaleAdvertisementUserNullThrowException() {
		int saleAdvertisementId = 1;
		DefaultSaleAdvertisementEntity saleAdvertisement = new DefaultSaleAdvertisementEntity();
		saleAdvertisement.setId(saleAdvertisementId);
		assertThrows(NullPointerException.class, () -> {
			buyTransactionService.userHaveBoughtSaleAdvertisement(null, saleAdvertisement);
		});
	}

	@Test
	void userHaveBoughtSaleAdvertisementSaleAdvertisementNullThrowException() {
		int userId = 1;
		DefaultUserEntity user = new DefaultUserEntity();
		user.setId(userId);
		assertThrows(NullPointerException.class, () -> {
			buyTransactionService.userHaveBoughtSaleAdvertisement(user, null);
		});
	}

	@Test
	void userHaveBoughtSaleAdvertisementSaleAdvertisementNotFoundThrowException() {
		int userId = 1;
		DefaultUserEntity user = new DefaultUserEntity();
		user.setId(userId);
		int saleAdvertisementId = 1;
		DefaultSaleAdvertisementEntity saleAdvertisement = new DefaultSaleAdvertisementEntity();
		saleAdvertisement.setId(saleAdvertisementId);

		Mockito.when(userRepository.existsById(userId)).thenReturn(true);
		Mockito.when(saleAdvertisementRepository.existsById(saleAdvertisementId)).thenReturn(false);
		assertThrows(SaleAdvertisementNotFoundException.class, () -> {
			buyTransactionService.userHaveBoughtSaleAdvertisement(user, saleAdvertisement);
		});
	}

	@Test
	void userHaveBoughtSaleAdvertisementUserNotFoundThrowException() {
		int userId = 1;
		DefaultUserEntity user = new DefaultUserEntity();
		user.setId(userId);
		int saleAdvertisementId = 1;
		DefaultSaleAdvertisementEntity saleAdvertisement = new DefaultSaleAdvertisementEntity();
		saleAdvertisement.setId(saleAdvertisementId);

		Mockito.when(userRepository.existsById(userId)).thenReturn(false);
		Mockito.when(saleAdvertisementRepository.existsById(saleAdvertisementId)).thenReturn(true);
		assertThrows(UserNotFoundException.class, () -> {
			buyTransactionService.userHaveBoughtSaleAdvertisement(user, saleAdvertisement);
		});
	}

	@Test
	void userHaveBoughtSaleAdvertisementReturnBoolean() {
		int userId = 1;
		DefaultUserEntity user = new DefaultUserEntity();
		user.setId(userId);
		int saleAdvertisementId = 1;
		DefaultSaleAdvertisementEntity saleAdvertisement = new DefaultSaleAdvertisementEntity();
		saleAdvertisement.setId(saleAdvertisementId);

		Mockito.when(userRepository.existsById(userId)).thenReturn(true);
		Mockito.when(saleAdvertisementRepository.existsById(saleAdvertisementId)).thenReturn(true);
		BuyTransactionId buyTransactionid = new BuyTransactionId(userId, saleAdvertisementId);
		Mockito.when(buyTransactionRepository.existsById(buyTransactionid)).thenReturn(true);

		try {
			assertTrue(buyTransactionService.userHaveBoughtSaleAdvertisement(user, saleAdvertisement));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
