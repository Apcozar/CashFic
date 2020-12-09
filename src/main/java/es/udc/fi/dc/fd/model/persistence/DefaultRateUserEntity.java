package es.udc.fi.dc.fd.model.persistence;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

import es.udc.fi.dc.fd.model.RateUserEntity;

/**
 * The Class DefaultRateUserEntity.
 */
@Entity(name = "RateUserEntity")
@Table(name = "rate_user")
public class DefaultRateUserEntity implements RateUserEntity {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The id. */
	@EmbeddedId
	RateUserKey id;

	/** The user. */
	@ManyToOne
	@MapsId("userId")
	@JoinColumn(name = "user_id")
	DefaultUserEntity user;

	/** The user rated. */
	@ManyToOne
	@MapsId("userRatedId")
	@JoinColumn(name = "user_rated_id")
	DefaultUserEntity userRated;

	/** The rating. */
	@Column(name = "rating", nullable = false, unique = false)
	Integer rating;

	/**
	 * Instantiates a new default rate user entity.
	 *
	 * @param user      the user
	 * @param userRated the user rated
	 * @param rating    the rating
	 */
	public DefaultRateUserEntity(DefaultUserEntity user, DefaultUserEntity userRated, Integer rating) {
		super();
		this.id = new RateUserKey(user.getId(), userRated.getId());
		this.user = user;
		this.userRated = userRated;
		this.rating = rating;
	}

	/**
	 * Instantiates a new default rate user entity.
	 */
	public DefaultRateUserEntity() {
		super();
	}

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	@Override
	public RateUserKey getId() {
		return id;
	}

	/**
	 * Sets the id.
	 *
	 * @param id the new id
	 */
	@Override
	public void setId(RateUserKey id) {
		this.id = id;
	}

	/**
	 * Gets the user.
	 *
	 * @return the user
	 */
	@Override
	public DefaultUserEntity getUser() {
		return user;
	}

	/**
	 * Sets the user.
	 *
	 * @param user the new user
	 */
	@Override
	public void setUser(DefaultUserEntity user) {
		this.user = user;
	}

	/**
	 * Gets the user rated.
	 *
	 * @return the user rated
	 */
	@Override
	public DefaultUserEntity getUserRated() {
		return userRated;
	}

	/**
	 * Sets the user rated.
	 *
	 * @param userRated the new user rated
	 */
	@Override
	public void setUserRated(DefaultUserEntity userRated) {
		this.userRated = userRated;
	}

	/**
	 * Gets the rating.
	 *
	 * @return the rating
	 */
	@Override
	public Integer getRating() {
		return rating;
	}

	/**
	 * Sets the rating.
	 *
	 * @param rating the new rating
	 */
	@Override
	public void setRating(Integer rating) {
		this.rating = rating;
	}

	/**
	 * Hash code.
	 *
	 * @return the int
	 */
	@Override
	public int hashCode() {
		return Objects.hash(id, user, userRated, rating);
	}

	/**
	 * Equals.
	 *
	 * @param obj the obj
	 * @return true, if successful
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof DefaultRateUserEntity)) {
			return false;
		}
		DefaultRateUserEntity other = (DefaultRateUserEntity) obj;
		return Objects.equals(id, other.id) && Objects.equals(userRated, other.userRated)
				&& Objects.equals(rating, other.rating) && Objects.equals(user, other.user);
	}

	/**
	 * To string.
	 *
	 * @return the string
	 */
	@Override
	public String toString() {
		return "RatingUserEntityImplementation [id=" + id + ", user=" + user.getName() + ", ratedUser="
				+ userRated.getName() + ", rating=" + rating + "]";
	}

}
