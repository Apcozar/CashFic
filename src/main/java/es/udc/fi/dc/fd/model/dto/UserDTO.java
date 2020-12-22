/*
 * 
 */
package es.udc.fi.dc.fd.model.dto;

import java.io.Serializable;
import java.util.Objects;

import es.udc.fi.dc.fd.model.Role;
import es.udc.fi.dc.fd.model.persistence.DefaultUserEntity;

/**
 * The Class UserDTO. Used to send user information from the controller to the
 * view.
 */
public class UserDTO implements Serializable {

	/**
	 * Serialization ID.
	 */
	private static final long serialVersionUID = 1L;

	/** The user id. */
	private final Integer id;

	/** The user login. */
	private final String login;

	/** The user name. */
	private final String name;

	/** The user last name. */
	private final String lastName;

	/** The user email. */
	private final String email;

	/** The user city. */
	private final String city;

	/** The number of ads published by the user. */
	private final Integer countAdvertisements;

	/** The user's like number. */
	private final Integer countLikes;

	/** The user's followers number. */
	private final Integer countFollowers;

	/** The user's followed number. */
	private final Integer countFollowed;

	/** True if the user is the user logged. */
	private final boolean isUserLogged;

	/** True if the user is premium user. */
	private final boolean isPremium;

	/**
	 * True if the user is not the user logged and it is followed by the user
	 * logged.
	 */
	private final boolean isFollowedByUserLogged;

	/**
	 * True if the user is not the user logged and it is rated by the user logged.
	 */
	private final boolean isRatedByUserLogged;

	/** The rated given from the user logged. */
	private final Integer rateByUserLogged;

	/** True if the user has average rating. */
	private final boolean existsAverageRating;

	/** The average rating. */
	private final Double averageRating;

	/**
	 * Instantiates a new user DTO.
	 *
	 * @param user                   the user
	 * @param isUserLogged           the is user logged
	 * @param isFollowedByUserLogged the is followed by user logged
	 * @param isRatedByUserLogged    the is rated by user logged
	 * @param rateByUserLogged       the rate by user logged
	 * @param existsAverageRating    the exists average rating
	 * @param averageRating          the average rating
	 */
	public UserDTO(DefaultUserEntity user, boolean isUserLogged, boolean isFollowedByUserLogged,
			boolean isRatedByUserLogged, Integer rateByUserLogged, boolean existsAverageRating, Double averageRating) {
		super();
		this.id = user.getId();
		this.login = user.getLogin();
		this.name = user.getName();
		this.lastName = user.getLastName();
		this.email = user.getEmail();
		this.city = user.getCity();
		this.countAdvertisements = user.getSaleAdvertisements().size();
		this.countLikes = user.getLikes().size();
		this.countFollowers = user.getFollowers().size();
		this.countFollowed = user.getFollowed().size();
		this.isPremium = user.getRole().equals(Role.ROLE_PREMIUM);
		this.isUserLogged = isUserLogged;
		this.isFollowedByUserLogged = isFollowedByUserLogged;
		this.isRatedByUserLogged = isRatedByUserLogged;
		this.rateByUserLogged = rateByUserLogged;
		this.existsAverageRating = existsAverageRating;
		this.averageRating = averageRating;
	}

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * Gets the login.
	 *
	 * @return the login
	 */
	public String getLogin() {
		return login;
	}

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gets the last name.
	 *
	 * @return the last name
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * Gets the email.
	 *
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Gets the city.
	 *
	 * @return the city
	 */
	public String getCity() {
		return city;
	}

	/**
	 * Gets the count advertisements.
	 *
	 * @return the count advertisements
	 */
	public Integer getCountAdvertisements() {
		return countAdvertisements;
	}

	/**
	 * Gets the count likes.
	 *
	 * @return the count likes
	 */
	public Integer getCountLikes() {
		return countLikes;
	}

	/**
	 * Gets the count followers.
	 *
	 * @return the count followers
	 */
	public Integer getCountFollowers() {
		return countFollowers;
	}

	/**
	 * Gets the count followed.
	 *
	 * @return the count followed
	 */
	public Integer getCountFollowed() {
		return countFollowed;
	}

	/**
	 * Checks if is premium.
	 *
	 * @return true, if is premium
	 */
	public boolean isPremium() {
		return isPremium;
	}

	/**
	 * Checks if is followed by user logged.
	 *
	 * @return true, if is followed by user logged
	 */
	public boolean isFollowedByUserLogged() {
		return isFollowedByUserLogged;
	}

	/**
	 * Checks if is rated by user logged.
	 *
	 * @return true, if is rated by user logged
	 */
	public boolean isRatedByUserLogged() {
		return isRatedByUserLogged;
	}

	/**
	 * Gets the rate by user logged.
	 *
	 * @return the rate by user logged
	 */
	public Integer getRateByUserLogged() {
		return rateByUserLogged;
	}

	/**
	 * Checks if is exists average rating.
	 *
	 * @return true, if is exists average rating
	 */
	public boolean isExistsAverageRating() {
		return existsAverageRating;
	}

	/**
	 * Gets the average rating.
	 *
	 * @return the average rating
	 */
	public Double getAverageRating() {
		return averageRating;
	}

	/**
	 * Checks if is user logged.
	 *
	 * @return true, if is user logged
	 */
	public boolean isUserLogged() {
		return isUserLogged;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, login, name, lastName, email, city, countAdvertisements, countLikes, countFollowers,
				countFollowed, isUserLogged, isPremium, isFollowedByUserLogged, isRatedByUserLogged, rateByUserLogged,
				existsAverageRating, averageRating);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserDTO other = (UserDTO) obj;
		return Objects.equals(id, other.id) && Objects.equals(login, other.login) && Objects.equals(name, other.name)
				&& Objects.equals(lastName, other.lastName) && Objects.equals(email, other.email)
				&& Objects.equals(city, other.city) && Objects.equals(countAdvertisements, other.countAdvertisements)
				&& Objects.equals(countLikes, other.countLikes) && Objects.equals(countFollowers, other.countFollowers)
				&& Objects.equals(countFollowed, other.countFollowed)
				&& Objects.equals(isUserLogged, other.isUserLogged) && Objects.equals(isPremium, other.isPremium)
				&& Objects.equals(isFollowedByUserLogged, other.isFollowedByUserLogged)
				&& Objects.equals(isRatedByUserLogged, other.isRatedByUserLogged)
				&& Objects.equals(rateByUserLogged, other.rateByUserLogged)
				&& Objects.equals(existsAverageRating, other.existsAverageRating)
				&& Objects.equals(averageRating, other.averageRating);
	}

	@Override
	public String toString() {
		return "UserDTO [id=" + id + ", login=" + login + ", name=" + name + ", lastName=" + lastName + ", email="
				+ email + ", city=" + city + ", countAdvertisements=" + countAdvertisements + ", countLikes="
				+ countLikes + ", countFollowers=" + countFollowers + ", countFollowed=" + countFollowed
				+ ", isUserLogged=" + isUserLogged + ", isPremium=" + isPremium + ", isFollowedByUserLogged="
				+ isFollowedByUserLogged + ", isRatedByUserLogged=" + isRatedByUserLogged + ", rateByUserLogged="
				+ rateByUserLogged + ", existsAverageRating=" + existsAverageRating + ", averageRating=" + averageRating
				+ "]";
	}

}
