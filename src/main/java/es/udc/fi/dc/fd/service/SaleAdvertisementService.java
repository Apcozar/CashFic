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

import es.udc.fi.dc.fd.model.SaleAdvertisementEntity;
import es.udc.fi.dc.fd.model.persistence.DefaultSaleAdvertisementEntity;
import es.udc.fi.dc.service.exceptions.SaleAdvertisementServiceException;

/**
 * Service for the saleAdd domain.
 * <p>
 * This is a domain service just to allow the endpoints querying the entities
 * they are asked for.
 *
 * @author Santiago
 */
public interface SaleAdvertisementService {

	/**
	 * Returns a saleAdd with the given id.
	 * <p>
	 * If no saleAdd exists with that id then an saleAdd with a negative id is
	 * expected to be returned. Avoid returning nulls.
	 *
	 * @param identifier identifier of the saleAdd to find
	 * @return the saleAdd for the given id
	 * @throws SaleAdvertisementServiceException the sale exception
	 */
	public SaleAdvertisementEntity findById(final Integer identifier) throws SaleAdvertisementServiceException;

	/**
	 * Adds the.
	 *
	 * @param saleAdd the sale add
	 * @return the sale add entity
	 * @throws SaleAdvertisementServiceException the sale add service exception
	 */
	public SaleAdvertisementEntity add(final DefaultSaleAdvertisementEntity saleAdd)
			throws SaleAdvertisementServiceException;

	/**
	 * Returns all the saleAdds from the DB.
	 * 
	 * @return the persisted saleAdds
	 */
	public Iterable<DefaultSaleAdvertisementEntity> getAllSaleAdvertisements();

	/**
	 * Returns a paginated collection of saleAdd.
	 * 
	 * @param page pagination data
	 * @return a paginated collection of saleAdds
	 */
	public Iterable<DefaultSaleAdvertisementEntity> getSaleAdvertisements(final Pageable page);

	/**
	 * Removes the.
	 *
	 * @param saleAdd the sale add
	 * @throws SaleAdvertisementServiceException the sale add service exception
	 */
	public void remove(final DefaultSaleAdvertisementEntity saleAdd) throws SaleAdvertisementServiceException;

	/**
	 * Update.
	 *
	 * @param saleAdd the sale add
	 * @return the sale add entity
	 * @throws SaleAdvertisementServiceException the sale add service exception
	 */
	SaleAdvertisementEntity update(DefaultSaleAdvertisementEntity saleAdd) throws SaleAdvertisementServiceException;

}
