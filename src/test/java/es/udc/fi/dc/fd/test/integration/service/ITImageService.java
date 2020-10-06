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

package es.udc.fi.dc.fd.test.integration.service;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Collection;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.annotation.Transactional;

import es.udc.fi.dc.fd.model.ImageEntity;
import es.udc.fi.dc.fd.model.persistence.DefaultImageEntity;
import es.udc.fi.dc.fd.model.persistence.DefaultSale_advertisementEntity;
import es.udc.fi.dc.fd.service.ImageService;
import es.udc.fi.dc.fd.service.Sale_advertisementService;

/**
 * Integration tests for the {@link ImageService}.
 * <p>
 * As this service doesn't contain any actual business logic, and it just wraps
 * the example entities repository, these tests are for verifying everything is
 * set up correctly and working.
 */
@RunWith(JUnitPlatform.class)
@SpringJUnitConfig
@Transactional
@Rollback
@ContextConfiguration(locations = { "classpath:context/application-context.xml" })
@TestPropertySource({ "classpath:config/persistence-access.properties" })
public class ITImageService {

	/**
	 * Service being tested.
	 */
	@Autowired
	private ImageService service;

	@Autowired
	private Sale_advertisementService saleService;

	/**
	 * Default constructor.
	 */
	public ITImageService() {
		super();
	}

	/**
	 * Verifies that the service adds entities into persistence.
	 */
	@Test
	public void testAdd_NotExisting_Added() {
		final DefaultImageEntity entity; // Entity to add
		final Integer entitiesCount; // Original number of entities
		final Integer finalEntitiesCount; // Final number of entities

		entitiesCount = ((Collection<DefaultImageEntity>) service.getAllImages()).size();

		entity = new DefaultImageEntity();
		entity.setImagePath("newTestImagePath");
		entity.setTitle("newTestImageTitle");
		DefaultSale_advertisementEntity var = (DefaultSale_advertisementEntity) saleService.findById(1);
		entity.setSale_advertisement(var);

		service.add(entity);

		finalEntitiesCount = ((Collection<DefaultImageEntity>) service.getAllImages()).size();

		Assert.assertEquals(finalEntitiesCount, Integer.valueOf(entitiesCount + 1));
	}

	/**
	 * Verifies that the service update entities into persistence.
	 */
	@Test
	public void testAdd_Existing_Updated() {

		ImageEntity entity = service.findById(3);
		entity.setTitle("new test title");
		entity.setImagePath("new Image path");

		service.add((DefaultImageEntity) entity);
		ImageEntity persistedEntity = service.findById(3);

		Assert.assertEquals(persistedEntity.getTitle(), "new test title");
		Assert.assertEquals(persistedEntity.getImagePath(), "new Image path");
	}

	/**
	 * Verifies that searching an existing image by id returns the expected image.
	 */
	@Test
	public void testFindById_Existing_Valid() {
		final ImageEntity image; // Found entity

		image = service.findById(1);

		Assert.assertEquals(image.getId(), Integer.valueOf(1));
	}

	/**
	 * Verifies that searching for a not existing image by id returns an empty
	 * image.
	 */
	@Test
	public void testFindById_NotExisting_Invalid() {
		final ImageEntity image; // Found entity

		image = service.findById(100);
		Assert.assertEquals(image.getId(), Integer.valueOf(-1));
	}

	/**
	 * Verifies that remove existing image by id returns an empty image.
	 */
	@Test
	public void testRemove_Existing_Removed() {
		int finalEntitiesCount = ((Collection<DefaultImageEntity>) service.getAllImages()).size();

		ImageEntity image = service.findById(1);
		service.remove((DefaultImageEntity) image);

		Assert.assertEquals(((Collection<DefaultImageEntity>) service.getAllImages()).size(), (finalEntitiesCount - 1));

		Assert.assertEquals((service.findById(image.getId()).getId()), Integer.valueOf(-1));
	}

	/**
	 * Verifies that remove not existing image returns an exception.
	 */
	@Test
	public void testRemove_Not_Existing_Invalid() {
		DefaultImageEntity image = new DefaultImageEntity();
		assertThrows(org.springframework.dao.DataIntegrityViolationException.class, () -> {
			service.remove(image);
		});
	}
	/**
	 * Verifies that remove existing image two times return an exception.
	 */
	@Test
	public void testRemove_Same_Tow_Times_Invalid() {
		 ImageEntity image = service.findById(1);
		 service.remove((DefaultImageEntity) image);
		assertThrows(org.springframework.dao.InvalidDataAccessApiUsageException.class, () -> {
			service.remove((DefaultImageEntity) image);
		});
	}

}
