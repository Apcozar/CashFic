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

// TODO: Auto-generated Javadoc
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
	 * Find sale advertisements by city.
	 *
	 * @param city the city
	 * @return the sale advertisements in the city
	 */
	@Query("SELECT s from SaleAdvertisementEntity s JOIN UserEntity u ON s.user = u.id WHERE u.city = ?1")
	Iterable<DefaultSaleAdvertisementEntity> findSaleAdvertisementsByCity(String city);

	/**
	 * Find sale advertisements by keywords.
	 *
	 * @param keywords the keywords
	 * @return the sale advertisements that contains the keywords in its description
	 */
	@Query("SELECT s from SaleAdvertisementEntity s WHERE s.product_description LIKE '%?1%'")
	Iterable<DefaultSaleAdvertisementEntity> findSaleAdvertisementsByKeywords(String keywords);

	/**
	 * Find sale advertisements by price range.
	 *
	 * @param minPrice the min price in the range
	 * @param maxPrice the max price in the range
	 * @return the sale advertisements in the price range searched
	 */
	@Query("SELECT s from SaleAdvertisementEntity s WHERE s.price BETWEEN ?1 AND ?2")
	Iterable<DefaultSaleAdvertisementEntity> findSaleAdvertisementsByPrice(BigDecimal minPrice, BigDecimal maxPrice);

	/**
	 * Find sale advertisements by date.
	 *
	 * @param firstDate  the first date
	 * @param secondDate the second date
	 * @return the sale advertisements in the price range searched
	 */
	@Query("SELECT s from SaleAdvertisementEntity s WHERE s.date BETWEEN ?1 AND ?2")
	Iterable<DefaultSaleAdvertisementEntity> findSaleAdvertisementsByDate(LocalDateTime firstDate,
			LocalDateTime secondDate);

}
