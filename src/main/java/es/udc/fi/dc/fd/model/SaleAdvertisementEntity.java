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

package es.udc.fi.dc.fd.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

import es.udc.fi.dc.fd.model.persistence.DefaultImageEntity;
import es.udc.fi.dc.fd.model.persistence.DefaultUserEntity;

/**
 * A sale_advertisement entity.
 *
 * @author Santiago
 */
public interface SaleAdvertisementEntity extends Serializable {

	/**
	 * Returns the identifier assigned to this sale_advertisement.
	 * <p>
	 * If no identifier has been assigned yet, then the value is expected to be
	 * {@code null} or lower than zero.
	 *
	 * @return the sale_advertisement's identifier
	 */
	public Integer getId();

	/**
	 * Returns the product_title of the sale_advertisement.
	 *
	 * @return the sale_advertisement's product_title
	 */
	public String getProductTitle();

	/**
	 * Returns the product_description of the sale_advertisement.
	 *
	 * @return the sale_advertisement's product_description
	 */
	public String getProductDescription();

	/**
	 * Returns the images of the sale_advertisement.
	 *
	 * @return the sale_advertisement's images
	 */
	public Set<DefaultImageEntity> getImages();

	/**
	 * Returns the user of the sale_advertisement.
	 *
	 * @return the sale_advertisement's user
	 */
	public DefaultUserEntity getUser();

	/**
	 * Returns the date of the sale_advertisement.
	 *
	 * @return the date when the add was created.
	 */
	public LocalDateTime getDate();

	/**
	 * Gets the state.
	 *
	 * @return the state
	 */
	public State getState();

	/**
	 * Sets the id.
	 *
	 * @param identifier the new id
	 */
	public void setId(final Integer identifier);

	/**
	 * Changes the product_title of the sale_advertisement.
	 *
	 * @param product_title the product_title to set on the sale_advertisement
	 */
	public void setProductTitle(final String product_title);

	/**
	 * Changes the product_description of the sale_advertisement.
	 *
	 * @param product_description the product_description to set on the
	 *                            sale_advertisement
	 */
	public void setProductDescription(final String product_description);

	/**
	 * Changes the user of the sale_advertisement.
	 *
	 * @param value the user to set on the sale_advertisement
	 */
	public void setUser(final DefaultUserEntity value);

	/**
	 * Sets the date.
	 *
	 * @param date the new date
	 */
	public void setDate(final LocalDateTime date);

	/**
	 * Sets the state.
	 *
	 * @param state the new state
	 */
	public void setState(State state);

	/**
	 * Adds the image.
	 *
	 * @param value the value
	 */
	public void addImage(DefaultImageEntity value);

	/**
	 * Removes the image.
	 *
	 * @param image the image
	 */
	void removeImage(DefaultImageEntity image);

	/**
	 * Sets the price.
	 *
	 * @param price the new price
	 */
	public void setPrice(BigDecimal price);

	/**
	 * Gets the price.
	 *
	 * @return the price
	 */
	public BigDecimal getPrice();

}
