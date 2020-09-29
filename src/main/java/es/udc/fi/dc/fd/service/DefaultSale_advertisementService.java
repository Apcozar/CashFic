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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import es.udc.fi.dc.fd.model.Sale_advertisementEntity;
import es.udc.fi.dc.fd.model.persistence.DefaultSale_advertisementEntity;
import es.udc.fi.dc.fd.repository.Sale_advertisementRepository;

/**
 * Default implementation of the sale_advertisement service.
 * 
 * @author Santiago
 *
 */
@Service
public class DefaultSale_advertisementService implements Sale_advertisementService {

    /**
     * Repository for the domain entities handled by the service.
     */
    private final Sale_advertisementRepository sale_advertisementRepository;

    /**
     * Constructs an sale_advertisement service with the specified repository.
     *
     * @param repository
     *            the repository for the sale_advertisement instances
     */
    @Autowired
    public DefaultSale_advertisementService(
            final Sale_advertisementRepository repository) {
        super();

        sale_advertisementRepository = checkNotNull(repository,
                "Received a null pointer as repository");
    }

    @Override
    public final Sale_advertisementEntity add(final DefaultSale_advertisementEntity sale_advertisement) {
        return sale_advertisementRepository.save(sale_advertisement);
    }

    /**
     * Returns an sale_advertisement with the given id.
     * <p>
     * If no instance exists with that id then a sale_advertisement with a negative id is
     * returned.
     *
     * @param identifier
     *            identifier of the sale_advertisement to find
     * @return the sale_advertisement for the given id
     */
    @Override
    public final Sale_advertisementEntity findById(final Integer identifier) {
        final Sale_advertisementEntity sale_advertisement;

        checkNotNull(identifier, "Received a null pointer as identifier");

        if (sale_advertisementRepository.existsById(identifier)) {
        	sale_advertisement = sale_advertisementRepository.getOne(identifier);
        } else {
        	sale_advertisement = new DefaultSale_advertisementEntity();
        }

        return sale_advertisement;
    }

    @Override
    public final Iterable<DefaultSale_advertisementEntity> getAllSale_advertisements() {
        return sale_advertisementRepository.findAll();
    }

    @Override
    public final Iterable<DefaultSale_advertisementEntity>
            getSale_advertisements(final Pageable page) {
        return sale_advertisementRepository.findAll(page);
    }

    @Override
    public final void remove(final DefaultSale_advertisementEntity sale_advertisement) {
    	sale_advertisementRepository.delete(sale_advertisement);
    }

}
