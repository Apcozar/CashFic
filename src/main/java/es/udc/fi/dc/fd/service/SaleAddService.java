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

import es.udc.fi.dc.fd.model.SaleAddEntity;
import es.udc.fi.dc.fd.model.persistence.DefaultSaleAddEntity;
import es.udc.fi.dc.service.exceptions.SaleAddServiceException;

/**
 * Service for the saleAdd domain.
 * <p>
 * This is a domain service just to allow the endpoints querying the entities
 * they are asked for.
 *
 * @author Santiago
 */
public interface SaleAddService {

	/**
	 * Adds the.
	 *
	 * @param saleAdd the sale add
	 * @return the sale add entity
	 * @throws SaleAddServiceException the sale add service exception
	 */
	public SaleAddEntity add(final DefaultSaleAddEntity saleAdd) throws SaleAddServiceException;

	/**
	 * Returns a saleAdd with the given id.
	 * <p>
	 * If no saleAdd exists with that id then an saleAdd with a negative id is
	 * expected to be returned. Avoid returning nulls.
	 *
	 * @param identifier identifier of the saleAdd to find
	 * @return the saleAdd for the given id
	 * @throws SaleAddServiceException
	 */
	public SaleAddEntity findById(final Integer identifier) throws SaleAddServiceException;

	/**
	 * Returns all the saleAdds from the DB.
	 * 
	 * @return the persisted saleAdds
	 */
	public Iterable<DefaultSaleAddEntity> getAllSaleAdds();

	/**
	 * Returns a paginated collection of saleAdd.
	 * 
	 * @param page pagination data
	 * @return a paginated collection of saleAdds
	 */
	public Iterable<DefaultSaleAddEntity> getsaleAdds(final Pageable page);

	/**
	 * Removes the.
	 *
	 * @param saleAdd the sale add
	 * @throws SaleAddServiceException the sale add service exception
	 */
	public void remove(final DefaultSaleAddEntity saleAdd) throws SaleAddServiceException;

	/**
	 * Update.
	 *
	 * @param saleAdd the sale add
	 * @return the sale add entity
	 * @throws SaleAddServiceException the sale add service exception
	 */
	SaleAddEntity update(DefaultSaleAddEntity saleAdd) throws SaleAddServiceException;

}
