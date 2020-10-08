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

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import es.udc.fi.dc.fd.model.persistence.DefaultUserEntity;

/**
 * Spring-JPA repository for {@link DefaultUserEntity}.
 * <p>
 * This is a simple repository just to allow the endpoints querying the User
 * they are asked for.
 *
 * @author Santiago
 */
public interface UserRepository extends JpaRepository<DefaultUserEntity, Integer> {

	/**
	 * Exists by login.
	 *
	 * @param login the login
	 * @return true, if successful
	 */
	@Query("select count(u)>0 from UserEntity u where u.login = ?1")
	boolean existsByLogin(String login);

	/**
	 * Find by login.
	 *
	 * @param login the login
	 * @return the user entity
	 */
	@Query("select u from UserEntity u where u.login = ?1")
	DefaultUserEntity findByLogin(String login);

	/**
	 * Exists by email.
	 *
	 * @param email the email
	 * @return true, if successful
	 */
	@Query("select count(u)>0 from UserEntity u where u.email = ?1")
	boolean existsByEmail(String email);

	/**
	 * Find by email
	 * 
	 * @param email the user email
	 * @return user entity or null if not found
	 */
	@Query("SELECT u FROM UserEntity u WHERE u.email = ?1")
	DefaultUserEntity findByEmail(String email);
}
