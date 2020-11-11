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

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import es.udc.fi.dc.fd.model.SaleAdvertisementEntity;
import es.udc.fi.dc.fd.model.persistence.DefaultImageEntity;
import es.udc.fi.dc.fd.model.persistence.DefaultSaleAdvertisementEntity;
import es.udc.fi.dc.fd.repository.ImageRepository;
import es.udc.fi.dc.fd.repository.SaleAdvertisementRepository;
import es.udc.fi.dc.fd.service.exceptions.SaleAdvertisementAlreadyExistsException;
import es.udc.fi.dc.fd.service.exceptions.SaleAdvertisementNotFoundException;
import es.udc.fi.dc.fd.service.exceptions.SaleAdvertisementServiceException;

/**
 * Default implementation of the saleAdd service.
 * 
 * @author Santiago
 *
 */
@Service
public class DefaultSaleAdvertisementService implements SaleAdvertisementService {

	/**
	 * Repository for the domain entities handled by the service.
	 */
	private final SaleAdvertisementRepository saleAdvertisementRepository;

	/**
	 * Repository for the domain entities handled by the service.
	 */
	private final ImageRepository imageRepository;

	/**
	 * Constructs an saleAdd service with the specified repository.
	 *
	 * @param repository   the repository for the saleAdd instances
	 * @param imRepository the repository for the images instances
	 */
	@Autowired
	public DefaultSaleAdvertisementService(final SaleAdvertisementRepository repository,
			final ImageRepository imRepository) {
		super();

		saleAdvertisementRepository = checkNotNull(repository,
				"Received a null pointer as sale advertisement repository");
		imageRepository = checkNotNull(imRepository, "Received a null pointer as image repository");
	}

	/**
	 * Returns a saleAdvertisement with the given id.
	 * <p>
	 * If no instance exists with that id throw SaleAdvertisementNotFoundException
	 *
	 * @param identifier identifier of the saleAdvertisement to find
	 * @return the saleAdvertisement for the given id
	 * @throws SaleAdvertisementNotFoundException when saleAdvertisement with given
	 *                                            identifier not found
	 */
	@Override
	public final SaleAdvertisementEntity findById(final Integer identifier) throws SaleAdvertisementNotFoundException {

		checkNotNull(identifier, "Received a null pointer as identifier");

		if (!saleAdvertisementRepository.existsById(identifier)) {
			throw new SaleAdvertisementNotFoundException(identifier);
		}
		DefaultSaleAdvertisementEntity result = saleAdvertisementRepository.getOne(identifier);
		return result;
	}

	/**
	 * Returns a saleAdvertisement created with a initialized id.
	 * <p>
	 * Creates a saleAdvertisement with the entity parameters received. Returns the
	 * entity persisted with id assigned or exception if image with id already exist
	 * 
	 * @param saleAdvertisement the sale advertisement entity that add
	 * @return the saleAdvertisement entity with initialized id
	 * @throws SaleAdvertisementAlreadyExistsException when sale advertisement
	 *                                                 already exist
	 */
	@Override
	public final SaleAdvertisementEntity add(DefaultSaleAdvertisementEntity saleAdvertisement)
			throws SaleAdvertisementAlreadyExistsException {
		checkNotNull(saleAdvertisement, "Received a null pointer as sale advertisement");
		if (saleAdvertisementRepository.existsById(saleAdvertisement.getId())) {
			throw new SaleAdvertisementAlreadyExistsException(saleAdvertisement.getId());
		}
		return saleAdvertisementRepository.save(saleAdvertisement);
	}

	/**
	 * Update.
	 *
	 * @param saleAdvertisement the sale advertisement entity with new parameters
	 * @return the sale advertisement entity updated
	 * @throws SaleAdvertisementServiceException the sale add service exception
	 */
	@Override
	public final SaleAdvertisementEntity update(final DefaultSaleAdvertisementEntity saleAdvertisement)
			throws SaleAdvertisementServiceException {
		if (saleAdvertisement.getId() == null || saleAdvertisement.getId() == -1) {
			throw new SaleAdvertisementServiceException("The sale add id cannot be null or -1");
		}
		if (!saleAdvertisementRepository.existsById(saleAdvertisement.getId())) {
			throw new SaleAdvertisementServiceException("Sale advertisement not exists");
		}
		return saleAdvertisementRepository.save(saleAdvertisement);
	}

	/**
	 * Removes the sale advertisement from persistence
	 *
	 * @param saleAdvertisement the sale advertisement to remove
	 * @throws SaleAdvertisementNotFoundException when sale advertisement not found
	 */
	@Override
	public final void remove(final DefaultSaleAdvertisementEntity saleAdvertisement)
			throws SaleAdvertisementNotFoundException {
		checkNotNull(saleAdvertisement, "Received a null pointer as identifier");
		if (!saleAdvertisementRepository.existsById(saleAdvertisement.getId())) {
			throw new SaleAdvertisementNotFoundException(saleAdvertisement.getId());
		}
		saleAdvertisement.getImages().forEach((final DefaultImageEntity image) -> imageRepository.delete(image));
		saleAdvertisementRepository.delete(saleAdvertisement);
	}

	/**
	 * Gets the all sale advertisements.
	 *
	 * @return the all sale advertisements
	 */
	@Override
	public final Iterable<DefaultSaleAdvertisementEntity> getAllSaleAdvertisements() {
		return saleAdvertisementRepository.findAll();
	}

	/**
	 * Gets the sale advertisements.
	 *
	 * @param page the page
	 * @return the sale advertisements
	 */
	@Override
	public final Iterable<DefaultSaleAdvertisementEntity> getSaleAdvertisements(final Pageable page) {
		return saleAdvertisementRepository.findAll(page);
	}

	/**
	 * Gets the sale advertisements by date desc.
	 *
	 * @return the sale advertisements by date desc
	 */
	@Override
	public final Iterable<DefaultSaleAdvertisementEntity> getSaleAdvertisementsByDateDesc() {
		return saleAdvertisementRepository.findSaleAdvertisementsOrderByDateDesc();
	}

	/**
	 * Find by id default.
	 *
	 * @param identifier the identifier
	 * @return the implemented sale advertisement entity
	 * @throws SaleAdvertisementNotFoundException the sale advertisement not found
	 *                                            exception
	 */
	@Override
	public final DefaultSaleAdvertisementEntity findByIdDefault(final Integer identifier)
			throws SaleAdvertisementNotFoundException {

		checkNotNull(identifier, "Received a null pointer as identifier");

		if (!saleAdvertisementRepository.existsById(identifier)) {
			throw new SaleAdvertisementNotFoundException(identifier);
		}

		return saleAdvertisementRepository.findById(identifier).get();
	}

	/**
	 * Gets the sale advertisements by search criteria.
	 *
	 * @param city     the city
	 * @param keywords the keywords
	 * @param date1    the date 1
	 * @param date2    the date 2
	 * @param price1   the price 1
	 * @param price2   the price 2
	 * @return the sale advertisements by search criteria
	 */
	@Override
	public Iterable<DefaultSaleAdvertisementEntity> getSaleAdvertisementsBySearchCriteria(String city, String keywords,
			LocalDateTime date1, LocalDateTime date2, BigDecimal price1, BigDecimal price2) {
		return saleAdvertisementRepository.findSaleAdvertisementsByCriteria(city, keywords, date1, date2, price1,
				price2);
	}

	/**
	 * Gets the maximum price of all sale advertisements.
	 *
	 * @return the maximum price
	 */
	@Override
	public BigDecimal getMaximumPrice() {
		return saleAdvertisementRepository.getMaximumPrice();
	}

}
