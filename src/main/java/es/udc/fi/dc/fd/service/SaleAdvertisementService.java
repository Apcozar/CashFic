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

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.data.domain.Pageable;

import es.udc.fi.dc.fd.model.SaleAdvertisementEntity;
import es.udc.fi.dc.fd.model.persistence.DefaultSaleAdvertisementEntity;
import es.udc.fi.dc.fd.service.exceptions.SaleAdvertisementAlreadyExistsException;
import es.udc.fi.dc.fd.service.exceptions.SaleAdvertisementAlreadyOnHoldException;
import es.udc.fi.dc.fd.service.exceptions.SaleAdvertisementAlreadyOnSaleException;
import es.udc.fi.dc.fd.service.exceptions.SaleAdvertisementNotFoundException;

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
	 * Returns a saleAdvertisement with the given id.
	 * <p>
	 * If no saleAdvertisement exists with that id then throws
	 * SaleAdvertisementNotFoundException
	 *
	 * @param identifier identifier of the saleAdd to find
	 * @return the saleAdvertisement for the given id
	 * @throws SaleAdvertisementNotFoundException when a saleAdvertisement with
	 *                                            given identifier not found
	 */
	public SaleAdvertisementEntity findById(final Integer identifier) throws SaleAdvertisementNotFoundException;

	/**
	 * Adds sale advertisement into persistence
	 * <p>
	 * If sale advertisement already exists throw an exception
	 *
	 * @param saleAvertisement the sale advertisement to add, id should be -1
	 * @return the sale advertisement added with initialized id
	 * @throws SaleAdvertisementAlreadyExistsException when sale advertisement
	 *                                                 already exists
	 */
	public SaleAdvertisementEntity add(final DefaultSaleAdvertisementEntity saleAvertisement)
			throws SaleAdvertisementAlreadyExistsException;

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
	 * Removes the sale advertisement from persistence
	 *
	 * @param saleAdvertisement the sale advertisement to add
	 * @throws SaleAdvertisementNotFoundException when sale advertisement not found
	 */
	public void remove(final DefaultSaleAdvertisementEntity saleAdvertisement)
			throws SaleAdvertisementNotFoundException;

	/**
	 * Update.
	 *
	 * @param saleAdd the sale add
	 * @return the sale add entity
	 * @throws SaleAdvertisementNotFoundException when no sale advertisement found
	 */
	SaleAdvertisementEntity update(DefaultSaleAdvertisementEntity saleAdd) throws SaleAdvertisementNotFoundException;

	/**
	 * Gets the sale advertisements by date.
	 *
	 * @return the sale advertisements by date
	 */
	public Iterable<DefaultSaleAdvertisementEntity> getSaleAdvertisementsByDateDesc();

	/**
	 * Find sale advertisements by search criteria.
	 *
	 * @param city     the city of the sale advertisement
	 * @param keywords the keywords that should be in the title or description
	 * @param date1    the oldest date in the date range
	 * @param date2    the most recent date in the date range
	 * @param price1   the minimum price in the price range
	 * @param price2   the maximum price in the price range
	 * @param rating   the rating
	 * @return the sale advertisements that meet the search criteria order by date
	 *         with the most recents first
	 */
	public Iterable<DefaultSaleAdvertisementEntity> getSaleAdvertisementsBySearchCriteria(String city, String keywords,
			LocalDateTime date1, LocalDateTime date2, BigDecimal price1, BigDecimal price2, Double rating);

	/**
	 * Gets the maximum price of all sale advertisements.
	 *
	 * @return the maximum price
	 */
	public BigDecimal getMaximumPrice();

	/**
	 * Are hold advertisement.
	 *
	 * @param identifier the identifier
	 * @return true, if successful
	 * @throws SaleAdvertisementNotFoundException the sale advertisement not found
	 *                                            exception
	 */
	public boolean areOnHoldAdvertisement(Integer identifier) throws SaleAdvertisementNotFoundException;

	/**
	 * Sets the advertisement on hold state.
	 *
	 * @param identifier the new on hold advertisement
	 * @throws SaleAdvertisementNotFoundException      the sale advertisement not
	 *                                                 found exception
	 * @throws SaleAdvertisementAlreadyOnHoldException the sale advertisement
	 *                                                 already on hold exception
	 */
	public void setOnHoldAdvertisement(Integer identifier)
			throws SaleAdvertisementNotFoundException, SaleAdvertisementAlreadyOnHoldException;

	/**
	 * Sets the advertisement on sale state.
	 *
	 * @param identifier the new on sale advertisement
	 * @throws SaleAdvertisementNotFoundException      the sale advertisement not
	 *                                                 found exception
	 * @throws SaleAdvertisementAlreadyOnSaleException the sale advertisement
	 *                                                 already on sale exception
	 */
	public void setOnSaleAdvertisement(Integer identifier)
			throws SaleAdvertisementNotFoundException, SaleAdvertisementAlreadyOnSaleException;

}
