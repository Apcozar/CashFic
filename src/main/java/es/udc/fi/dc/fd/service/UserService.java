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

package es.udc.fi.dc.fd.service;

import org.springframework.data.domain.Pageable;

import es.udc.fi.dc.fd.model.UserEntity;
import es.udc.fi.dc.fd.model.persistence.DefaultUserEntity;



/**
 * Service for the user domain.
 * <p>
 * This is a domain service just to allow the endpoints querying the entities
 * they are asked for.
 *
 * @author Santiago
 */
public interface UserService {

    /**
     * Persists an user.
     * 
     * @param user
     *            user to persist
     * @return the persisted user
     */
    public UserEntity add(final DefaultUserEntity user);

    /**
     * Returns an user with the given id.
     * <p>
     * If no user exists with that id then an user with a negative id is
     * expected to be returned. Avoid returning nulls.
     *
     * @param identifier
     *            identifier of the user to find
     * @return the user for the given id
     */
    public UserEntity findById(final Integer identifier);
    
    /**
     * Returns an user with the given login.
     * <p>
     * If no user exists with that login then an user with a negative id is
     * expected to be returned. Avoid returning nulls.
     *
     * @param login
     *            login of the user to find
     * @return the user for the given login
     */
    public UserEntity findByLogin(final String login);

    /**
     * Returns all the users from the DB.
     * 
     * @return the persisted users
     */
    public Iterable<DefaultUserEntity> getAllUsers();

    /**
     * Returns a paginated collection of users.
     * 
     * @param page
     *            pagination data
     * @return a paginated collection of users
     */
    public Iterable<DefaultUserEntity> getUsers(final Pageable page);

    /**
     * Removes an user from persistence.
     * 
     * @param user
     *            user to remove
     */
    public void remove(final DefaultUserEntity user);

}
