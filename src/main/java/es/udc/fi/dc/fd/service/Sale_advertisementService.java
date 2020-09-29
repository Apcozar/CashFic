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

import org.springframework.data.domain.Pageable;

import es.udc.fi.dc.fd.model.Sale_advertisementEntity;
import es.udc.fi.dc.fd.model.persistence.DefaultSale_advertisementEntity;



/**
 * Service for the sale_advertisement domain.
 * <p>
 * This is a domain service just to allow the endpoints querying the entities
 * they are asked for.
 *
 * @author Santiago
 */
public interface Sale_advertisementService {

    /**
     * Persists an sale_advertisement.
     * 
     * @param sale_advertisement
     *            sale_advertisement to persist
     * @return the persisted sale_advertisement
     */
    public Sale_advertisementEntity add(final DefaultSale_advertisementEntity sale_advertisement);

    /**
     * Returns an sale_advertisement with the given id.
     * <p>
     * If no sale_advertisement exists with that id then an sale_advertisement with a negative id is
     * expected to be returned. Avoid returning nulls.
     *
     * @param identifier
     *            identifier of the sale_advertisement to find
     * @return the sale_advertisement for the given id
     */
    public Sale_advertisementEntity findById(final Integer identifier);

    /**
     * Returns all the sale_advertisements from the DB.
     * 
     * @return the persisted sale_advertisements
     */
    public Iterable<DefaultSale_advertisementEntity> getAllSale_advertisements();

    /**
     * Returns a paginated collection of sale_advertisement.
     * 
     * @param page
     *            pagination data
     * @return a paginated collection of sale_advertisements
     */
    public Iterable<DefaultSale_advertisementEntity> getSale_advertisements(final Pageable page);

    /**
     * Removes an sale_advertisement from persistence.
     * 
     * @param sale_advertisement
     *            sale_advertisement to remove
     */
    public void remove(final DefaultSale_advertisementEntity sale_advertisement);

}
