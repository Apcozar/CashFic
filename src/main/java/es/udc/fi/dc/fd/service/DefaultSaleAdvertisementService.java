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

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import es.udc.fi.dc.fd.model.SaleAdvertisementEntity;
import es.udc.fi.dc.fd.model.persistence.DefaultImageEntity;
import es.udc.fi.dc.fd.model.persistence.DefaultSaleAdvertisementEntity;
import es.udc.fi.dc.fd.repository.ImageRepository;
import es.udc.fi.dc.fd.repository.SaleAddRepository;
import es.udc.fi.dc.service.exceptions.SaleAdvertisementServiceException;

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
	private final SaleAddRepository saleAddRepository;

	/**
	 * Repository for the domain entities handled by the service.
	 */
	private final ImageRepository imageRepository;

	/**
	 * Constructs an saleAdd service with the specified repository.
	 *
	 * @param repository the repository for the saleAdd instances
	 */
	@Autowired
	public DefaultSaleAdvertisementService(final SaleAddRepository repository, final ImageRepository imRepository) {
		super();

		saleAddRepository = checkNotNull(repository, "Received a null pointer as repository");
		imageRepository = checkNotNull(imRepository, "Received a null pointer as repository");
	}

	/**
	 * Returns an saleAdd with the given id.
	 * <p>
	 * If no instance exists with that id then a saleAdd with a negative id is
	 * returned.
	 *
	 * @param identifier identifier of the saleAdd to find
	 * @return the saleAdd for the given id
	 * @throws SaleAdvertisementServiceException the exception
	 */
	@Override
	public final SaleAdvertisementEntity findById(final Integer identifier) throws SaleAdvertisementServiceException {

		checkNotNull(identifier, "Received a null pointer as identifier");

		Optional<DefaultSaleAdvertisementEntity> saleAdd;

		saleAdd = saleAddRepository.findById(identifier);

		if (saleAdd.isPresent()) {
			return saleAdd.get();
		} else {
			throw new SaleAdvertisementServiceException("Sale add not exists");
		}

	}

	/**
	 * Returns a saleAdvertisement created with a initialized id.
	 * <p>
	 * Creates a saleAdvertisement with the entity parameters received. Returns the
	 * entity persisted with id assigned
	 * 
	 * @param saleAdvertisement the sale advertisement entity
	 * @return the saleAdvertisement entity with initialized id
	 * @throws SaleAdvertisementServiceException The
	 *                                           SaleAdvertisementServiceException
	 */
	@Override
	public final SaleAdvertisementEntity add(DefaultSaleAdvertisementEntity saleAdvertisement)
			throws SaleAdvertisementServiceException {
		if (!((saleAdvertisement.getId() == null || saleAdvertisement.getId() == -1))) {
			throw new SaleAdvertisementServiceException("The sale advertisement's id must be null or -1");
		}
		if (saleAddRepository.existsById(saleAdvertisement.getId())) {
			throw new SaleAdvertisementServiceException("Sale advertisement already exists");
		}

		return saleAddRepository.save(saleAdvertisement);
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
		if (!saleAddRepository.existsById(saleAdvertisement.getId())) {
			throw new SaleAdvertisementServiceException("Sale advertisement not exists");
		}
		return saleAddRepository.save(saleAdvertisement);
	}

	@Override
	public final void remove(final DefaultSaleAdvertisementEntity saleAdvertisement)
			throws SaleAdvertisementServiceException {
		checkNotNull(saleAdvertisement, "Received a null pointer as identifier");
		if (!saleAddRepository.existsById(saleAdvertisement.getId())) {
			throw new SaleAdvertisementServiceException("Sale advertisement does not exists");
		}

		saleAdvertisement.getImages().forEach((final DefaultImageEntity image) -> imageRepository.delete(image));
		saleAddRepository.delete(saleAdvertisement);
	}

	@Override
	public final Iterable<DefaultSaleAdvertisementEntity> getAllSaleAdvertisements() {
		return saleAddRepository.findAll();
	}

	@Override
	public final Iterable<DefaultSaleAdvertisementEntity> getSaleAdvertisements(final Pageable page) {
		return saleAddRepository.findAll(page);
	}

}
