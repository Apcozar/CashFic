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

package es.udc.fi.dc.fd.repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import es.udc.fi.dc.fd.model.persistence.DefaultSaleAdvertisementEntity;

/**
 * Spring-JPA repository for {@link DefaultSaleAdvertisementEntity}.
 * <p>
 * This is a simple repository just to allow the endpoints querying the
 * Sale_advertisements they are asked for.
 *
 * @author Santiago
 */
public interface SaleAdvertisementRepository extends JpaRepository<DefaultSaleAdvertisementEntity, Integer> {

	/**
	 * Find sale advertisements order by date desc.
	 *
	 * @return the sale advertisements order by date with the most recents first
	 */
	@Query("SELECT s from SaleAdvertisementEntity s ORDER BY s.date desc")
	Iterable<DefaultSaleAdvertisementEntity> findSaleAdvertisementsOrderByDateDesc();

	/**
	 * Find sale advertisements by search criteria.
	 *
	 * @param city     the city of the sale advertisement
	 * @param keywords the keywords that should be in the title or description
	 * @param date1    the oldest date in the date range
	 * @param date2    the most recent date in the date range
	 * @param price1   the minimum price in the price range
	 * @param price2   the maximum price in the price range
	 * @return the sale advertisements that meet the search criteria order by date
	 *         with the most recents first
	 */
	@Query("SELECT s FROM SaleAdvertisementEntity s JOIN UserEntity u ON s.user = u WHERE u.city LIKE ?1 "
			+ "AND (s.product_title LIKE %?2% OR s.product_description LIKE %?2%) AND s.date BETWEEN ?3 AND ?4 "
			+ "AND (s.price BETWEEN ?5 AND ?6 OR s.price is null) ORDER BY u.role DESC, s.date DESC")
	Iterable<DefaultSaleAdvertisementEntity> findSaleAdvertisementsByCriteria(String city, String keywords,
			LocalDateTime date1, LocalDateTime date2, BigDecimal price1, BigDecimal price2);

	/**
	 * Gets the maximum price of all sale advertisements.
	 *
	 * @return the maximum price
	 */
	@Query("SELECT max(s.price) FROM SaleAdvertisementEntity s")
	BigDecimal getMaximumPrice();

}
