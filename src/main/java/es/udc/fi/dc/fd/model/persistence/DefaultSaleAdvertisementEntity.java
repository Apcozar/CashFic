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

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.google.common.base.MoreObjects;

import es.udc.fi.dc.fd.model.SaleAdvertisementEntity;
import es.udc.fi.dc.fd.model.State;

/**
 * Persistent entity for the sale_advertisements.
 * <p>
 * This makes use of JPA annotations for the persistence configuration.
 *
 * @author Santiago
 */
/**
 * @author Martin
 *
 */
@Entity(name = "SaleAdvertisementEntity")
@Table(name = "sale_advertisements")
public class DefaultSaleAdvertisementEntity implements SaleAdvertisementEntity {

	/**
	 * Serialization ID.
	 */
	@Transient
	private static final long serialVersionUID = 1328776989450853491L;

	/**
	 * Sale_advertisement's ID.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false, unique = true)
	private Integer id = -1;

	/**
	 * Product_title of the sale_advertisement.
	 * <p>
	 * This is to have additional data apart from the id, to be used on the tests.
	 */
	@Column(name = "product_title", nullable = false, unique = true)
	private String product_title = "";

	/**
	 * Product_description of the sale_advertisement.
	 * <p>
	 * This is to have additional data apart from the id, to be used on the tests.
	 */
	@Column(name = "product_description", nullable = true, unique = false)
	private String product_description = "";

	/**
	 * Images of the sale_advertisement.
	 * <p>
	 * This is to have additional data apart from the id, to be used on the tests.
	 */
	@OneToMany(mappedBy = "sale_advertisement")
	private Set<DefaultImageEntity> images = new HashSet<>();

	/**
	 * user of the sale_advertisement.
	 * <p>
	 * This is to have additional data apart from the id, to be used on the tests.
	 */
	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private DefaultUserEntity user;

	/**
	 * The date of the sale_advertisement.
	 * <p>
	 * To know when the ad was created.
	 */
	@Column(name = "add_date", nullable = false, unique = false)
	private LocalDateTime date;

	@ManyToMany(mappedBy = "likedSaleAdvertisements")
	private Set<DefaultUserEntity> usersLikes = new HashSet<>();

	@OneToOne(mappedBy = "saleAdvertisement")
	private DefaultBuyTransactionEntity buyTransaction;

	/**
	 * The price of the sale_advertisement.
	 * <p>
	 * To know the price of the sale_advertisement.
	 */
	@Column(name = "price", nullable = true, unique = false)
	private BigDecimal price;

	/** The state. */
	@Column(name = "state", nullable = true, unique = false)
	private State state;

	/**
	 * Constructs an sale_advertisement entity.
	 */
	public DefaultSaleAdvertisementEntity() {
		super();
	}

	public DefaultSaleAdvertisementEntity(Integer id, String productTitle, String productDescription,
			DefaultUserEntity user, LocalDateTime date) {
		super();
		this.id = id;
		this.product_title = productTitle;
		this.product_description = productDescription;
		this.user = user;
		this.date = date;
		this.images = new HashSet<>();
		this.state = State.STATE_ON_SALE;
	}

	public DefaultSaleAdvertisementEntity(String product_title, String product_description,
			Set<DefaultImageEntity> images, DefaultUserEntity user, LocalDateTime date) {
		super();
		this.product_title = product_title;
		this.product_description = product_description;
		this.images = images;
		this.user = user;
		this.date = date;
		this.state = State.STATE_ON_SALE;
	}

	public DefaultSaleAdvertisementEntity(String productTitle, String productDescription, DefaultUserEntity user,
			LocalDateTime date) {
		super();
		this.product_title = productTitle;
		this.product_description = productDescription;
		this.user = user;
		this.date = date;
		this.state = State.STATE_ON_SALE;
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

		final DefaultSaleAdvertisementEntity other = (DefaultSaleAdvertisementEntity) obj;
		return Objects.equals(id, other.id) && Objects.equals(date, other.date)
				&& Objects.equals(product_description, other.product_description)
				&& Objects.equals(product_title, other.product_title) && Objects.equals(state, other.state);
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
	public String getProductTitle() {
		return product_title;
	}

	@Override
	public String getProductDescription() {
		return product_description;
	}

	@Override
	public Set<DefaultImageEntity> getImages() {
		return images;
	}

	@Override
	public DefaultUserEntity getUser() {
		return user;
	}

	@Override
	public LocalDateTime getDate() {
		return date;
	}

	@Override
	public State getState() {
		return state;
	}

	/**
	 * @return the buyTransaction
	 */
	@Override
	public final DefaultBuyTransactionEntity getBuyTransaction() {
		return buyTransaction;
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
	public void setProductTitle(final String value) {
		product_title = checkNotNull(value, "Received a null pointer as product_title");
	}

	@Override
	public void setProductDescription(final String value) {
		product_description = checkNotNull(value, "Received a null pointer as product_description");
	}

	@Override
	public void setUser(DefaultUserEntity value) {
		user = checkNotNull(value, "Received a null pointer as value");
	}

	@Override
	public void addImage(final DefaultImageEntity image) {
		images.add(checkNotNull(image, "Received a null pointer as images"));
	}

	@Override
	public void removeImage(final DefaultImageEntity image) {
		images.remove(image);
	}

	@Override
	public void setDate(final LocalDateTime value) {
		date = checkNotNull(value, "Received a null pointer as date");
	}

	/**
	 * Set an associated buy transaction
	 * 
	 * @param buyTransaction the buyTransaction to set
	 */
	@Override
	public final void setBuyTransaction(DefaultBuyTransactionEntity buyTransaction) {
		this.buyTransaction = buyTransaction;
	}

	@Override

	public void addUsersLike(DefaultUserEntity user) {
		checkNotNull(user, "Received a null pointer as user");
		usersLikes.add(user);
	}

	@Override
	public void removeUsersLike(DefaultUserEntity user) {
		checkNotNull(user, "Received a null pointer as user");
		usersLikes.remove(user);
	}

	@Override
	public Set<DefaultUserEntity> getLikes() {
		return usersLikes;
	}

	public void setState(final State value) {
		state = checkNotNull(value, "Received a null pointer as state");

	}

	@Override
	public final String toString() {
		return MoreObjects.toStringHelper(this).add("imageId", id).toString();
	}

	@Override
	public BigDecimal getPrice() {
		return price;
	}

	@Override
	public void setPrice(BigDecimal price) {
		this.price = price;
	}

}
