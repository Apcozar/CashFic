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

package es.udc.fi.dc.fd.test.integration.service;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import es.udc.fi.dc.fd.model.BuyTransactionEntity;
import es.udc.fi.dc.fd.model.SaleAdvertisementEntity;
import es.udc.fi.dc.fd.model.persistence.BuyTransactionId;
import es.udc.fi.dc.fd.model.persistence.DefaultUserEntity;
import es.udc.fi.dc.fd.service.BuyTransactionService;
import es.udc.fi.dc.fd.service.SaleAdvertisementService;
import es.udc.fi.dc.fd.service.UserService;
import es.udc.fi.dc.fd.service.exceptions.BuyTransactionAlreadyExistsException;
import es.udc.fi.dc.fd.service.exceptions.BuyTransactionNotFoundException;
import es.udc.fi.dc.fd.service.exceptions.ImageNotFoundException;
import es.udc.fi.dc.fd.service.exceptions.SaleAdvertisementNotFoundException;
import es.udc.fi.dc.fd.service.user.exceptions.UserNotFoundException;

/**
 * Integration tests for the {@link BuyTransactionService}.
 * <p>
 * As this service doesn't contain any actual business logic, and it just wraps
 * the example entities repository, these tests are for verifying everything is
 * set up correctly and working.
 */
@WebAppConfiguration
@RunWith(JUnitPlatform.class)
@SpringJUnitConfig
@Transactional
@Rollback
@ContextConfiguration(locations = { "classpath:context/application-context.xml" })
@TestPropertySource({ "classpath:config/persistence-access.properties" })
class ITBuyTransactionService {

	/**
	 * Service being tested.
	 */
	@Autowired
	private BuyTransactionService buyTransactionService;

	@Autowired
	private SaleAdvertisementService saleAdvertisementService;

	@Autowired
	private UserService userService;

	/**
	 * Default constructor.
	 */
	public ITBuyTransactionService() {
		super();
	}

	/**
	 * Verifies that searching an existing image by id returns the expected image.
	 * 
	 * @throws ImageNotFoundException               when image not found
	 * @throws BuyTransactionNotFoundException      when transaction not found
	 * @throws SaleAdvertisementNotFoundException   when sale advertisement not
	 *                                              found
	 * @throws UserNotFoundException                when user not found
	 * @throws BuyTransactionAlreadyExistsException when transaction with sale
	 *                                              advertisement already exists
	 */
	@Test
	void testCreateAndFindById_Existing_Valid() throws ImageNotFoundException, BuyTransactionNotFoundException,
			BuyTransactionAlreadyExistsException, UserNotFoundException, SaleAdvertisementNotFoundException {
		final DefaultUserEntity user = userService.findById(1);
		final SaleAdvertisementEntity saleAdvertisement = saleAdvertisementService.findById(1);

		// Creating transaction between user and sale advertisement
		BuyTransactionEntity buyTransactionCreated = buyTransactionService.create(user, saleAdvertisement);

		BuyTransactionId identifier = new BuyTransactionId(user.getId(), saleAdvertisement.getId());

		BuyTransactionEntity findTransaction = buyTransactionService.findById(identifier);
		// Found transaction must be equals stored transaction
		Assert.assertEquals(findTransaction, buyTransactionCreated);

		Assert.assertEquals(findTransaction.getUser(), userService.findById(1));
		Assert.assertEquals(findTransaction.getSaleAdvertisement(), saleAdvertisementService.findById(1));

		Assert.assertEquals(saleAdvertisementService.findById(1).getBuyTransaction(), findTransaction);
		Assert.assertTrue(userService.findById(1).getBuyTransactions().contains(findTransaction));

		Assert.assertTrue(buyTransactionService.userHaveBoughtSaleAdvertisement(userService.findById(1),
				saleAdvertisementService.findById(1)));

		Assert.assertFalse(buyTransactionService.userHaveBoughtSaleAdvertisement(userService.findById(2),
				saleAdvertisementService.findById(1)));
	}

}
