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

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.udc.fi.dc.fd.model.Role;
import es.udc.fi.dc.fd.model.persistence.DefaultUserEntity;
import es.udc.fi.dc.fd.repository.UserRepository;
import es.udc.fi.dc.fd.service.user.exceptions.UserEmailNotFoundException;
import es.udc.fi.dc.fd.service.user.exceptions.UserIncorrectLoginException;
import es.udc.fi.dc.fd.service.user.exceptions.UserEmailExistsException;
import es.udc.fi.dc.fd.service.user.exceptions.UserLoginAndEmailExistsException;
import es.udc.fi.dc.fd.service.user.exceptions.UserLoginExistsException;
import es.udc.fi.dc.fd.service.user.exceptions.UserNotFoundException;

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
	private final UserRepository userDao;

	/**
	 * Constructs an user service with the specified repository.
	 *
	 * @param repository the repository for the user instances
	 */
	@Autowired
	public DefaultUserService(final UserRepository repository) {
		super();

		userDao = checkNotNull(repository, "Received a null pointer as repository");
	}

	@Override
	public void signUp(DefaultUserEntity user)
			throws UserLoginExistsException, UserEmailExistsException, UserLoginAndEmailExistsException {

		if ((userDao.existsByLogin(user.getLogin())) && (userDao.existsByEmail(user.getEmail()))) {
			throw new UserLoginAndEmailExistsException(user.getLogin(), user.getEmail());
		}
		if (userDao.existsByLogin(user.getLogin())) {
			throw new UserLoginExistsException(user.getLogin());
		}
		if (userDao.findByEmail(user.getEmail()) != null) {
			throw new UserEmailExistsException(user.getEmail());
		}

		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		user.setRole(Role.ROLE_USER);

		userDao.save(user);
	}

	@Override
	@Transactional(readOnly = true)
	public DefaultUserEntity login(String login, String password)
			throws UserNotFoundException, UserIncorrectLoginException {

		if (!userDao.existsByLogin(login)) {
			throw new UserNotFoundException(login);
		}

		DefaultUserEntity user = userDao.findByLogin(login);

		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

		if (!passwordEncoder.matches(password, user.getPassword())) {
			throw new UserIncorrectLoginException(login, password);
		}

		return user;
	}

	@Override
	public DefaultUserEntity findById(Integer identifier) throws UserNotFoundException {
		Optional<DefaultUserEntity> user = userDao.findById(identifier);

		if (!user.isPresent()) {
			throw new UserNotFoundException(identifier.toString());
		}
		return user.get();
	}

	@Override
	public DefaultUserEntity findByLogin(String login) throws UserNotFoundException {
		if (!userDao.existsByLogin(login)) {
			throw new UserNotFoundException(login);
		}

		return userDao.findByLogin(login);
	}

	@Override
	public DefaultUserEntity findByEmail(String email) throws UserEmailNotFoundException {
		if (!userDao.existsByEmail(email)) {
			throw new UserEmailNotFoundException(email);
		}

		return userDao.findByEmail(email);
	}
}
