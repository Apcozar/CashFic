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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import es.udc.fi.dc.fd.model.SaleAdvertisementEntity;
import es.udc.fi.dc.fd.model.State;
import es.udc.fi.dc.fd.model.persistence.DefaultSaleAdvertisementEntity;
import es.udc.fi.dc.fd.repository.ImageRepository;
import es.udc.fi.dc.fd.repository.SaleAdvertisementRepository;
import es.udc.fi.dc.fd.service.DefaultSaleAdvertisementService;
import es.udc.fi.dc.fd.service.DefaultUserService;
import es.udc.fi.dc.fd.service.SaleAdvertisementService;
import es.udc.fi.dc.fd.service.exceptions.SaleAdvertisementAlreadyExistsException;
import es.udc.fi.dc.fd.service.exceptions.SaleAdvertisementAlreadyOnHoldException;
import es.udc.fi.dc.fd.service.exceptions.SaleAdvertisementAlreadyOnSaleException;
import es.udc.fi.dc.fd.service.exceptions.SaleAdvertisementNotFoundException;

/**
 * Unit tests for the {@link SaleAdvertisementService}.
 * <p>
 * As this service doesn't contain any actual business logic, and it just wraps
 * the example entities repository, these tests are for verifying everything is
 * set up correctly and working.
 */
@RunWith(JUnitPlatform.class)
final class TestSaleAdvertisementService {

	@Mock
	private ImageRepository imageRepository;

	@Mock
	private SaleAdvertisementRepository saleAdvertisementRepository;

	/**
	 * Service being tested.
	 */
	@InjectMocks
	private DefaultSaleAdvertisementService saleAdvertisementService;

	static final private Integer saleAdvertisementA_ID = 1;
	static final private Integer saleAdvertisementB_ID = 2;
	static final private Integer storedSaleAdvertisement_ID = 3;
	static final private Integer saleAdvertisementNotExisting_ID = 25;

	static final private BigDecimal maximunPrice = new BigDecimal(100);

	private DefaultSaleAdvertisementEntity saleAdvertisementA;
	private DefaultSaleAdvertisementEntity saleAdvertisementB;
	private DefaultSaleAdvertisementEntity notStoredSaleAdvertisement;
	private DefaultSaleAdvertisementEntity storedSaleAdvertisement;

	private DefaultSaleAdvertisementEntity updateSaleAdvertisement;

	private ArrayList<DefaultSaleAdvertisementEntity> saleAdvertisements;

	/**
	 * Default constructor.
	 */
	public TestSaleAdvertisementService() {
		super();

	}

	@BeforeEach
	public void initialize() {
		MockitoAnnotations.initMocks(this);

		saleAdvertisementA = new DefaultSaleAdvertisementEntity();
		saleAdvertisementA.setId(saleAdvertisementA_ID);
		saleAdvertisementA.setState(State.STATE_ON_HOLD);
		saleAdvertisementB = new DefaultSaleAdvertisementEntity();
		saleAdvertisementB.setId(saleAdvertisementB_ID);
		saleAdvertisementB.setState(State.STATE_ON_SALE);
		Mockito.when(saleAdvertisementRepository.existsById(saleAdvertisementA_ID)).thenReturn(true);
		Mockito.when(saleAdvertisementRepository.getOne(saleAdvertisementA_ID)).thenReturn(saleAdvertisementA);
		Mockito.when(saleAdvertisementRepository.existsById(saleAdvertisementB_ID)).thenReturn(true);
		Mockito.when(saleAdvertisementRepository.getOne(saleAdvertisementB_ID)).thenReturn(saleAdvertisementB);

		Mockito.when(saleAdvertisementRepository.existsById(saleAdvertisementNotExisting_ID)).thenReturn(false);
		Mockito.when(saleAdvertisementRepository.existsById(storedSaleAdvertisement_ID)).thenReturn(true);
		notStoredSaleAdvertisement = new DefaultSaleAdvertisementEntity();
		storedSaleAdvertisement = new DefaultSaleAdvertisementEntity();
		storedSaleAdvertisement.setId(storedSaleAdvertisement_ID);

		Mockito.when(saleAdvertisementRepository.existsById(notStoredSaleAdvertisement.getId())).thenReturn(false);
		Mockito.when(saleAdvertisementRepository.save(notStoredSaleAdvertisement)).thenReturn(storedSaleAdvertisement);

		updateSaleAdvertisement = new DefaultSaleAdvertisementEntity();
		Mockito.when(saleAdvertisementRepository.save(storedSaleAdvertisement)).thenReturn(updateSaleAdvertisement);

		Mockito.doNothing().when(saleAdvertisementRepository).delete(saleAdvertisementA);

		saleAdvertisements = new ArrayList<>();
		saleAdvertisements.add(saleAdvertisementA);
		saleAdvertisements.add(saleAdvertisementB);

		Mockito.when(saleAdvertisementRepository.findAll()).thenReturn(saleAdvertisements);

		PageImpl<DefaultSaleAdvertisementEntity> saleAdvertisementPages = new PageImpl<>(saleAdvertisements,
				PageRequest.of(1, 3), 10);
		Mockito.when(saleAdvertisementRepository.findAll(PageRequest.of(1, 3))).thenReturn(saleAdvertisementPages);

		Mockito.when(saleAdvertisementRepository.findSaleAdvertisementsOrderByDateDesc())
				.thenReturn(saleAdvertisements);

		Mockito.when(saleAdvertisementRepository.getMaximumPrice()).thenReturn(maximunPrice);

	}

	@Test
	void findSaleAdvertisementByIdReturnsSaleAdvertisement() {
		try {
			SaleAdvertisementEntity saleAdvertisementFound = saleAdvertisementService.findById(saleAdvertisementA_ID);
			assertEquals(saleAdvertisementFound, saleAdvertisementA);
		} catch (SaleAdvertisementNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Test
	void findSaleAdvertisementByIdNotExistingIdThrowsException() {
		assertThrows(SaleAdvertisementNotFoundException.class, () -> {
			saleAdvertisementService.findById(saleAdvertisementNotExisting_ID);
		});
	}

	@Test
	void findSaleAdvertisementByIdNullThrowsException() {
		assertThrows(NullPointerException.class, () -> {
			saleAdvertisementService.findById(null);
		});
	}

	@Test
	void addSaleAdvertisementNullThrowsException() {
		assertThrows(NullPointerException.class, () -> {
			saleAdvertisementService.add(null);
		});
	}

	@Test
	void addExistingSaleAdvertisementThrowsException() {
		assertThrows(SaleAdvertisementAlreadyExistsException.class, () -> {
			saleAdvertisementService.add(saleAdvertisementA);
		});
	}

	@Test
	void addSaleAdvertisementReturnStoredSaleAdvertisement() {
		try {
			SaleAdvertisementEntity savedSaleAdvertisement = saleAdvertisementService.add(notStoredSaleAdvertisement);
			assertEquals(storedSaleAdvertisement, savedSaleAdvertisement);
		} catch (SaleAdvertisementAlreadyExistsException e) {
			e.printStackTrace();
		}
	}

	@Test
	void updateNullSaleAdvertisementThrowException() {
		assertThrows(NullPointerException.class, () -> {
			saleAdvertisementService.update(null);
		});
	}

	@Test
	void updateNotExistingSaleAdvertisementThrowException() {
		assertThrows(SaleAdvertisementNotFoundException.class, () -> {
			saleAdvertisementService.update(notStoredSaleAdvertisement);
		});
	}

	@Test
	void updateExistingSaleAdvertisementReturnsUpdated() {
		SaleAdvertisementEntity storedUpdate;
		try {
			storedUpdate = saleAdvertisementService.update(storedSaleAdvertisement);
			assertEquals(storedUpdate, updateSaleAdvertisement);
		} catch (SaleAdvertisementNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Test
	void removeNullSaleAdvertisementThrowException() {
		assertThrows(NullPointerException.class, () -> {
			saleAdvertisementService.remove(null);
		});
	}

	@Test
	void removeNotExistingSaleAdvertisementThrowException() {
		assertThrows(SaleAdvertisementNotFoundException.class, () -> {
			saleAdvertisementService.remove(notStoredSaleAdvertisement);
		});
	}

	@Test
	void removeSaleAdvertisementRemoves() {
		try {
			saleAdvertisementService.remove(saleAdvertisementA);
			assertTrue(true);
		} catch (SaleAdvertisementNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Test
	void getAllSaleAdvertisements() {
		Iterable<DefaultSaleAdvertisementEntity> allSaleAdvertisements = saleAdvertisementService
				.getAllSaleAdvertisements();
		assertEquals(saleAdvertisements, allSaleAdvertisements);
	}

	@Test
	void getSaleAdvertisements() {
		Iterable<DefaultSaleAdvertisementEntity> allSaleAdvertisements = saleAdvertisementService
				.getSaleAdvertisements(PageRequest.of(1, 3));
		allSaleAdvertisements.forEach(saleAdvertisement -> {
			assertTrue(saleAdvertisements.contains(saleAdvertisement));
		});
	}

	@Test
	void getOrderedSaleAdvertisements() {
		Iterable<DefaultSaleAdvertisementEntity> orderedSaleAdvertisements = saleAdvertisementService
				.getSaleAdvertisementsByDateDesc();
		orderedSaleAdvertisements.forEach(saleAdvertisement -> {
			assertTrue(saleAdvertisements.contains(saleAdvertisement));
		});
	}

	@Test
	void getMaximunPrice() {
		BigDecimal maxPrice = saleAdvertisementService.getMaximumPrice();
		assertEquals(maximunPrice, maxPrice);
	}

	@Test
	void areOnHoldAdvertisementNullThrowException() {
		assertThrows(NullPointerException.class, () -> {
			saleAdvertisementService.areOnHoldAdvertisement(null);
		});
	}

	@Test
	void areOnHoldAdvertisementNotExistThrowException() {
		assertThrows(SaleAdvertisementNotFoundException.class, () -> {
			saleAdvertisementService.areOnHoldAdvertisement(saleAdvertisementNotExisting_ID);
		});
	}

	@Test
	void areOnHoldAdvertisementTrue() {
		try {
			assertTrue(saleAdvertisementService.areOnHoldAdvertisement(saleAdvertisementA_ID));
		} catch (SaleAdvertisementNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Test
	void areOnHoldAdvertisementFalse() {
		try {
			assertFalse(saleAdvertisementService.areOnHoldAdvertisement(saleAdvertisementB_ID));
		} catch (SaleAdvertisementNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Test
	void setOnHoldAdvertisementNullThrowException() {
		assertThrows(NullPointerException.class, () -> {
			saleAdvertisementService.setOnHoldAdvertisement(null);
		});
	}

	@Test
	void setOnHoldAdvertisementNotExistsThrowException() {
		assertThrows(SaleAdvertisementNotFoundException.class, () -> {
			saleAdvertisementService.setOnHoldAdvertisement(saleAdvertisementNotExisting_ID);
		});
	}

	@Test
	void setOnHoldAdvertisementAlreadyOnHoldThrowException() {
		assertThrows(SaleAdvertisementAlreadyOnHoldException.class, () -> {
			saleAdvertisementService.setOnHoldAdvertisement(saleAdvertisementA_ID);
		});
	}

	@Test
	void setOnSaleAdvertisementNullThrowException() {
		assertThrows(NullPointerException.class, () -> {
			saleAdvertisementService.setOnSaleAdvertisement(null);
		});
	}

	@Test
	void setOnSaleAdvertisementNotExistsThrowException() {
		assertThrows(SaleAdvertisementNotFoundException.class, () -> {
			saleAdvertisementService.setOnSaleAdvertisement(saleAdvertisementNotExisting_ID);
		});
	}

	@Test
	void setOnSaleAdvertisementAlreadyOnHoldThrowException() {
		assertThrows(SaleAdvertisementAlreadyOnSaleException.class, () -> {
			saleAdvertisementService.setOnSaleAdvertisement(saleAdvertisementB_ID);
		});
	}

	@Test
	void getSaleAdvertisementsBySearchCriteriaRatingNull() {

		Mockito.when(saleAdvertisementRepository.findSaleAdvertisementsByCriteria(null, null, null, null, null, null))
				.thenReturn(saleAdvertisements);
		Iterable<DefaultSaleAdvertisementEntity> saleAdvertisementIterable = saleAdvertisementService
				.getSaleAdvertisementsBySearchCriteria(null, null, null, null, null, null, null);
		assertEquals(saleAdvertisements, saleAdvertisementIterable);
	}

	@Test
	void getSaleAdvertisementsBySearchCriteriaRatingLowerThanMin() {

		Mockito.when(saleAdvertisementRepository.findSaleAdvertisementsByCriteriaAndRating(null, null, null, null, null,
				null, Double.valueOf(DefaultUserService.MIN_RATING))).thenReturn(saleAdvertisements);
		Iterable<DefaultSaleAdvertisementEntity> saleAdvertisementIterable = saleAdvertisementService
				.getSaleAdvertisementsBySearchCriteria(null, null, null, null, null, null, Double.valueOf(-99));
		assertEquals(saleAdvertisements, saleAdvertisementIterable);
	}

	@Test
	void getSaleAdvertisementsBySearchCriteriaRatingLowerUpperThanMax() {

		Mockito.when(saleAdvertisementRepository.findSaleAdvertisementsByCriteriaAndRating(null, null, null, null, null,
				null, Double.valueOf(DefaultUserService.MAX_RATING))).thenReturn(saleAdvertisements);
		Iterable<DefaultSaleAdvertisementEntity> saleAdvertisementIterable = saleAdvertisementService
				.getSaleAdvertisementsBySearchCriteria(null, null, null, null, null, null, Double.valueOf(99));
		assertEquals(saleAdvertisements, saleAdvertisementIterable);
	}

}
