package es.udc.fi.dc.fd.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import es.udc.fi.dc.fd.model.persistence.DefaultRateUserEntity;
import es.udc.fi.dc.fd.model.persistence.RateUserKey;

/**
 * The Interface RateUserRepository.
 */
public interface RateUserRepository extends JpaRepository<DefaultRateUserEntity, RateUserKey> {

	/**
	 * Find average rating.
	 *
	 * @param userId the user id
	 * @return the double
	 */
	@Query("SELECT AVG(u.rating) FROM RateUserEntity u WHERE u.userRated.id = :userId")
	Double findAverageRating(@Param("userId") Integer userId);

	/**
	 * Exists rated user.
	 *
	 * @param userId the user id
	 * @return true, if successful
	 */
	@Query("select count(u)>0 from RateUserEntity u where u.userRated.id = :userId")
	boolean existsRatedUser(@Param("userId") Integer userId);

	/**
	 * Exists rating from user to rated user.
	 *
	 * @param userId      the user id
	 * @param ratedUserId the rated user id
	 * @return true, if successful
	 */
	@Query("select count(u)>0 from RateUserEntity u where u.user.id = :userId AND u.userRated.id = :ratedUserId")
	boolean existsRatingFromUserToRatedUser(@Param("userId") Integer userId, @Param("ratedUserId") Integer ratedUserId);

	/**
	 * Given rating from user to rated user.
	 *
	 * @param userId      the user id
	 * @param ratedUserId the rated user id
	 * @return the integer
	 */
	@Query("select u.rating from RateUserEntity u where u.user.id = :userId AND u.userRated.id = :ratedUserId")
	Integer givenRatingFromUserToRatedUser(@Param("userId") Integer userId, @Param("ratedUserId") Integer ratedUserId);

}
