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

package es.udc.fi.dc.fd.model.persistence;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Objects;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.google.common.base.MoreObjects;

import es.udc.fi.dc.fd.model.UserEntity;

/**
 * Persistent entity for the users.
 * <p>
 * This makes use of JPA annotations for the persistence configuration.
 *
 * @author Santiago
 */
@Entity(name = "UserEntity")
@Table(name = "users")
public class DefaultUserEntity implements UserEntity {

    /**
     * Serialization ID.
     */
    @Transient
    private static final long serialVersionUID = 1328776989450853491L;

    /**
     * User's ID.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Integer           id               = -1;

    /**
     * login of the user.
     * <p>
     * This is to have additional data apart from the id, to be used on the
     * tests.
     */
    @Column(name = "login", nullable = false, unique = true)
    private String            login             = "";
    
    /**
     * name of the user.
     * <p>
     * This is to have additional data apart from the id, to be used on the
     * tests.
     */
    @Column(name = "name", nullable = false, unique = false)
    private String            name             = "";

    /**
     * city of the user.
     * <p>
     * This is to have additional data apart from the id, to be used on the
     * tests.
     */
    @Column(name = "city", nullable = false, unique = false)
    private String            city             = "";
    
    /**
     * Sale_advertisements of the user.
     * <p>
     * This is to have additional data apart from the id, to be used on the
     * tests.
     */
    @OneToMany(
    		mappedBy="user",
    		cascade = CascadeType.ALL,
    		orphanRemoval = true
    		)
    private Set<DefaultSale_advertisementEntity> sale_advertisements;
    
    
    
    /**
     * Constructs an sale_advertisement entity.
     */
    public DefaultUserEntity() {
        super();
    }

    @Override
    public final boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null) {
            return false;
        }

        if (getClass() != obj.getClass()) {
            return false;
        }

        final DefaultUserEntity other = (DefaultUserEntity) obj;
        return Objects.equals(id, other.id);
    }

    /**
     * Returns the identifier assigned to this sale_advertisement.
     * <p>
     * If no identifier has been assigned yet, then the value will be lower than
     * zero.
     *
     * @return the sale_advertisement's identifier
     */
    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public String getLogin() {
        return login;
    }
    
    @Override 
    public String getName() {
    	return name;
    }

    @Override
    public String getCity() {
    	return city;
    }
    
    @Override
    public Set<DefaultSale_advertisementEntity> getSale_advertisements(){
    	return sale_advertisements;
    }
    
    
    @Override
    public final int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public void setId(final Integer value) {
        id = checkNotNull(value, "Received a null pointer as identifier");
    }

    @Override
    public void setLogin(final String value) {
    	login = checkNotNull(value, "Received a null pointer as product_title");
    }
    
    @Override
    public void setName(final String value) {
    	name = checkNotNull(value, "Received a null pointer as product_description");
    }

    @Override
    public void setCity(final String value) {
    	city = checkNotNull(value, "Received a null pointer as product_description");
    }
    
    @Override
    public void setSale_advertisements(final Set<DefaultSale_advertisementEntity> value) {
    	sale_advertisements = checkNotNull(value,"Received a null pointer as images");
    }
    
    @Override
    public final String toString() {
        return MoreObjects.toStringHelper(this).add("imageId", id).toString();
    }

}
