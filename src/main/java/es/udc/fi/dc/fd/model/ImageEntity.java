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

import es.udc.fi.dc.fd.model.persistence.DefaultSale_advertisementEntity;

/**
 * A image entity.
 *
 * @author Santiago
 */
public interface ImageEntity extends Serializable {

    /**
     * Returns the identifier assigned to this image.
     * <p>
     * If no identifier has been assigned yet, then the value is expected to be
     * {@code null} or lower than zero.
     *
     * @return the image's identifier
     */
    public Integer getId();

    /**
     * Returns the title of the image.
     *
     * @return the image's title
     */
    public String getTitle();
    
    /**
     * Returns the path of the image.
     *
     * @return the image's path
     */
    public String getImagePath();
    
    /**
     * Returns the sale_advertisement of the image.
     *
     * @return the image's sale_advertisement
     */
    public Sale_advertisementEntity getSale_advertisement();

    /**
     * Sets the identifier assigned to this image.
     *
     * @param identifier
     *            the identifier for the image
     */
    public void setId(final Integer identifier);

    /**
     * Changes the title of the image.
     *
     * @param title
     *            the title to set on the image
     */
    public void setTitle(final String title);
    
    /**
     * Changes the path of the image.
     *
     * @param image_path
     *            the path to set on the image
     */
    public void setImagePath(final String image_path);

    /**
     * Changes the sale_advertisement of the image.
     *
     * @param sale_advertisement
     *            the sale_advertisement to set on the image
     */
    public void setSale_advertisement(final DefaultSale_advertisementEntity sale_advertisement);
    
}
