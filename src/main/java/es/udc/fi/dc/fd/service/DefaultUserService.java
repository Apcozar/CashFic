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

import static com.google.common.base.Preconditions.checkNotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import es.udc.fi.dc.fd.model.UserEntity;
import es.udc.fi.dc.fd.model.persistence.DefaultUserEntity;
import es.udc.fi.dc.fd.repository.UserRepository;

/**
 * Default implementation of the user service.
 * 
 * @author Santiago
 *
 */
@Service
public class DefaultUserService implements UserService {

    /**
     * Repository for the domain entities handled by the service.
     */
    private final UserRepository userRepository;

    /**
     * Constructs an user service with the specified repository.
     *
     * @param repository
     *            the repository for the user instances
     */
    @Autowired
    public DefaultUserService(
            final UserRepository repository) {
        super();

        userRepository = checkNotNull(repository,
                "Received a null pointer as repository");
    }

    @Override
    public final UserEntity add(final DefaultUserEntity user) {
        return userRepository.save(user);
    }

    /**
     * Returns an user with the given id.
     * <p>
     * If no instance exists with that id then a user with a negative id is
     * returned.
     *
     * @param identifier
     *            identifier of the user to find
     * @return the user for the given id
     */
    @Override
    public final UserEntity findById(final Integer identifier) {
        final UserEntity user;

        checkNotNull(identifier, "Received a null pointer as identifier");

        if (userRepository.existsById(identifier)) {
        	user = userRepository.getOne(identifier);
        } else {
        	user = new DefaultUserEntity();
        }

        return user;
    }

    @Override
    public final Iterable<DefaultUserEntity> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public final Iterable<DefaultUserEntity>
            getUsers(final Pageable page) {
        return userRepository.findAll(page);
    }

    @Override
    public final void remove(final DefaultUserEntity user) {
    	userRepository.delete(user);
    }

}
