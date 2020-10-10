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

import es.udc.fi.dc.fd.model.SaleAddEntity;
import es.udc.fi.dc.fd.model.persistence.DefaultSaleAddEntity;
import es.udc.fi.dc.fd.repository.SaleAddRepository;
import es.udc.fi.dc.service.exceptions.SaleAddServiceException;

/**
 * Default implementation of the saleAdd service.
 * 
 * @author Santiago
 *
 */
@Service
public class DefaultSaleAddService implements SaleAddService {

	/**
	 * Repository for the domain entities handled by the service.
	 */
	private final SaleAddRepository saleAddRepository;

	/**
	 * Constructs an saleAdd service with the specified repository.
	 *
	 * @param repository the repository for the saleAdd instances
	 */
	@Autowired
	public DefaultSaleAddService(final SaleAddRepository repository) {
		super();

		saleAddRepository = checkNotNull(repository, "Received a null pointer as repository");
	}

	/**
	 * Returns a saleAdd with the given id.
	 * <p>
	 * Creates a saleAdd with the entity parameters received. Returns the entity
	 * persisted with id assigned
	 * 
	 * @param saleAdd the sale add
	 * @return the sale add entity
	 * @throws SaleAddServiceException The SaleAddServiceException
	 */
	@Override
	public final SaleAddEntity add(DefaultSaleAddEntity saleAdd) throws SaleAddServiceException {

		return saleAddRepository.save(saleAdd);
	}

	/**
	 * Update.
	 *
	 * @param saleAdd the sale add
	 * @return the sale add entity
	 * @throws SaleAddServiceException the sale add service exception
	 */
	@Override
	public final SaleAddEntity update(final DefaultSaleAddEntity saleAdd) throws SaleAddServiceException {
		if ((saleAdd.getId() == null || saleAdd.getId() == -1)) {
			throw new SaleAddServiceException("The sale add id cannot be null or -1");
		}
		if (!saleAddRepository.existsById(saleAdd.getId())) {
			throw new SaleAddServiceException("Sale add not exists");
		}
		return saleAddRepository.save(saleAdd);
	}

	/**
	 * Returns an saleAdd with the given id.
	 * <p>
	 * If no instance exists with that id then a saleAdd with a negative id is
	 * returned.
	 *
	 * @param identifier identifier of the saleAdd to find
	 * @return the saleAdd for the given id
	 * @throws SaleAddServiceException
	 */
	@Override
	public final SaleAddEntity findById(final Integer identifier) throws SaleAddServiceException {

		checkNotNull(identifier, "Received a null pointer as identifier");

		Optional<DefaultSaleAddEntity> saleAdd;

		saleAdd = saleAddRepository.findById(identifier);

		if (saleAdd.isPresent()) {
			return saleAdd.get();
		} else {
			throw new SaleAddServiceException("Sale add not exists");
		}

	}

	@Override
	public final Iterable<DefaultSaleAddEntity> getAllSaleAdds() {
		return saleAddRepository.findAll();
	}

	@Override
	public final Iterable<DefaultSaleAddEntity> getsaleAdds(final Pageable page) {
		return saleAddRepository.findAll(page);
	}

	@Override
	public final void remove(final DefaultSaleAddEntity saleAdd) throws SaleAddServiceException {
		checkNotNull(saleAdd, "Received a null pointer as identifier");
		if (!saleAddRepository.existsById(saleAdd.getId())) {
			throw new SaleAddServiceException("Sale add does not exists");
		}
		saleAddRepository.delete(saleAdd);
	}

}
