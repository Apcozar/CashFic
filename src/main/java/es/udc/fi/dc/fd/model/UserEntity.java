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

import es.udc.fi.dc.fd.model.persistence.DefaultSale_advertisementEntity;



/**
 * A user entity.
 *
 * @author Santiago
 */
public interface UserEntity extends Serializable {

    /**
     * Returns the identifier assigned to this user.
     * <p>
     * If no identifier has been assigned yet, then the value is expected to be
     * {@code null} or lower than zero.
     *
     * @return the user's identifier
     */
    public Integer getId();

    /**
     * Returns the login of the user.
     *
     * @return the user's login
     */
    public String getLogin();
    
    /**
     * Returns the name of the user.
     *
     * @return the user's name
     */
    public String getName();
    
    /**
     * Returns the city of the user.
     *
     * @return the user's city
     */
    public String getCity();
     
    /**
     * Returns the sale_advertisements of the user.
     *
     * @return the user's sale_advertisements
     */
    public Set<DefaultSale_advertisementEntity> getSale_advertisements();
    
    /**
     * Sets the identifier assigned to this user.
     *
     * @param identifier
     *            the identifier for the user
     */
    public void setId(final Integer identifier);

    /**
     * Changes the login of the user.
     *
     * @param login
     *            the login to set on the user
     */
    public void setLogin(final String login);
    
    /**
     * Changes the name of the user.
     *
     * @param name
     *            the name to set on the user
     */
    public void setName(final String name);

    /**
     * Changes the city of the user.
     *
     * @param city
     *            the city to set on the user
     */
    public void setCity(final String city);
    
    /**
     * Changes the sale_advertisements of the user.
     *
     * @param sale_advertisements
     *            the sale_advertisements to set on the user
     */
    public void setSale_advertisements(final Set<DefaultSale_advertisementEntity> sale_advertisements);
    
}
