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
import java.util.Set;

import es.udc.fi.dc.fd.model.persistence.DefaultImageEntity;
import es.udc.fi.dc.fd.model.persistence.DefaultUserEntity;

/**
 * A sale_advertisement entity.
 *
 * @author Santiago
 */
public interface Sale_advertisementEntity extends Serializable {

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
    public String getProduct_title();
    
    /**
     * Returns the product_description of the sale_advertisement.
     *
     * @return the sale_advertisement's product_description
     */
    public String getProduct_description();
    
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
     * Sets the identifier assigned to this sale_advertisement.
     *
     * @param identifier
     *            the identifier for the sale_advertisement
     */
    public void setId(final Integer identifier);

    /**
     * Changes the product_title of the sale_advertisement.
     *
     * @param product_title
     *            the product_title to set on the sale_advertisement
     */
    public void setProduct_title(final String product_title);
    
    /**
     * Changes the product_description of the sale_advertisement.
     *
     * @param product_description
     *            the product_description to set on the sale_advertisement
     */
    public void setProduct_description(final String product_description);

    /**
     * Changes the images of the sale_advertisement.
     *
     * @param value
     *            the images to set on the sale_advertisement
     */
    public void setImages(final Set<DefaultImageEntity> value);
    
    /**
     * Changes the user of the sale_advertisement.
     *
     * @param value
     *            the user to set on the sale_advertisement
     */
    public void setUser(final DefaultUserEntity value);
}
