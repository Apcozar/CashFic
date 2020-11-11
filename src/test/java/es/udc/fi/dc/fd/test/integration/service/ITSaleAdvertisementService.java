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

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collection;

import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import es.udc.fi.dc.fd.model.ImageEntity;
import es.udc.fi.dc.fd.model.SaleAdvertisementEntity;
import es.udc.fi.dc.fd.model.persistence.DefaultImageEntity;
import es.udc.fi.dc.fd.model.persistence.DefaultSaleAdvertisementEntity;
import es.udc.fi.dc.fd.model.persistence.DefaultUserEntity;
import es.udc.fi.dc.fd.service.ImageService;
import es.udc.fi.dc.fd.service.SaleAdvertisementService;
import es.udc.fi.dc.fd.service.UserService;
import es.udc.fi.dc.fd.service.exceptions.ImageAlreadyExistsException;
import es.udc.fi.dc.fd.service.exceptions.ImageNotFoundException;
import es.udc.fi.dc.fd.service.exceptions.ImageServiceException;
import es.udc.fi.dc.fd.service.exceptions.SaleAdvertisementAlreadyExistsException;
import es.udc.fi.dc.fd.service.exceptions.SaleAdvertisementNotFoundException;
import es.udc.fi.dc.fd.service.exceptions.SaleAdvertisementServiceException;
import es.udc.fi.dc.fd.service.user.exceptions.UserNotFoundException;

/**
 * Integration tests for the {@link UserService}.
 * <p>
 * As this service doesn't contain any actual business logic, and it just wraps
 * the example entities repository, these tests are for verifying everything is
 * set up correctly and working.
 */
@WebAppConfiguration
@RunWith(JUnitPlatform.class)
@SpringJUnitConfig
@Transactional
@Rollback
@ContextConfiguration(locations = { "classpath:context/application-context.xml" })
@TestPropertySource({ "classpath:config/persistence-access.properties" })
public class ITSaleAdvertisementService {

	/**
	 * Service being tested.
	 */
	@Autowired
	private SaleAdvertisementService service;

	@Autowired
	private UserService userService;

	@Autowired
	private ImageService imageService;

	/**
	 * Default constructor.
	 */
	public ITSaleAdvertisementService() {
		super();
	}

	/**
	 * Verifies that searching an existing sale_advertisement by id returns the
	 * expected sale_advertisement.
	 * 
	 * @throws SaleAdvertisementServiceException
	 * @throws SaleAdvertisementNotFoundException
	 */
	@Test
	public void testFindById_Existing_Valid()
			throws SaleAdvertisementServiceException, SaleAdvertisementNotFoundException {
		final SaleAdvertisementEntity sale_advertisement; // Found entity

		sale_advertisement = service.findById(1);

		Assert.assertEquals(sale_advertisement.getId(), Integer.valueOf(1));
	}

	/**
	 * Verifies that searching for a not existing sale_advertisement by id returns
	 * an empty sale_advertisement.
	 * 
	 * @throws SaleAdvertisementServiceException
	 */
	@Test
	public void testFindById_NotExisting_Invalid() throws SaleAdvertisementNotFoundException {

		Assertions.assertThrows(SaleAdvertisementNotFoundException.class, () -> service.findById(-1));
	}

	/**
	 * Verifies that the service adds sale advertisement into persistence.
	 * 
	 * @throws SaleAdvertisementAlreadyExistsException
	 */
	@Test
	public void testAdd_NotExisting_Added()
			throws SaleAdvertisementServiceException, UserNotFoundException, SaleAdvertisementAlreadyExistsException {
		final DefaultSaleAdvertisementEntity saleAdvertisement; // Sale advertisement to add
		final Integer entitiesCount; // Original number of sale advertisements
		final Integer finalEntitiesCount; // Final number of sale advertisements
		final DefaultUserEntity user; // sale advertisement's user

		entitiesCount = ((Collection<DefaultSaleAdvertisementEntity>) service.getAllSaleAdvertisements()).size();
		// Get user for sale advertisement
		user = (DefaultUserEntity) userService.findById(1);
		// Create new sale advertisement
		saleAdvertisement = new DefaultSaleAdvertisementEntity();
		saleAdvertisement.setProductTitle("ExampleProductTittle");
		saleAdvertisement.setProductDescription("ExampleProductDescription");
		saleAdvertisement.setDate(LocalDateTime.of(2020, 3, 2, 20, 50));
		saleAdvertisement.setUser(user);
		saleAdvertisement.setPrice(BigDecimal.valueOf(15));

		// Save sale advertisement entity
		SaleAdvertisementEntity savedSaleAdvertisement = service.add(saleAdvertisement);

		finalEntitiesCount = ((Collection<DefaultSaleAdvertisementEntity>) service.getAllSaleAdvertisements()).size();
		// Check if size increase
		Assert.assertEquals(finalEntitiesCount, Integer.valueOf(entitiesCount + 1));
		// Check saved data
		Assert.assertEquals(savedSaleAdvertisement.getProductTitle(), "ExampleProductTittle");
		Assert.assertEquals(savedSaleAdvertisement.getProductDescription(), "ExampleProductDescription");
		Assert.assertEquals(savedSaleAdvertisement.getDate(), LocalDateTime.of(2020, 3, 2, 20, 50));

		// Check if user have sale advertisement
		DefaultUserEntity updatedUser = userService.findById(1);
		Assert.assertTrue(updatedUser.getSaleAdvertisements().contains(savedSaleAdvertisement));
	}

	/**
	 * Verifies that cannot add existing add advertisement
	 * 
	 * @throws SaleAdvertisementNotFoundException
	 */
	@Test
	public void testAdd_Existing_Fail()
			throws SaleAdvertisementServiceException, UserNotFoundException, SaleAdvertisementNotFoundException {
		final SaleAdvertisementEntity saleAdvertisement; // Sale advertisement to add

		// Get an existing sale advertisement
		saleAdvertisement = service.findById(1);
		Assertions.assertThrows(SaleAdvertisementAlreadyExistsException.class,
				() -> service.add((DefaultSaleAdvertisementEntity) saleAdvertisement));
	}

	/**
	 * Verifies that the service update sale advertisement into persistence.
	 * 
	 * @throws UserNotFoundException
	 * @throws SaleAdvertisementAlreadyExistsException
	 */
	// NOT CHECKED WHAT HAPPEN IF UPDATE SET OF IMAGES OR USER
	@Test
	public void testUpdate_Existing_Updated()
			throws SaleAdvertisementServiceException, UserNotFoundException, SaleAdvertisementAlreadyExistsException {
		final DefaultSaleAdvertisementEntity saleAdvertisement; // Sale advertisement for update
		final DefaultUserEntity user; // User for sale Advertisement

		// Get user for sale advertisement
		user = (DefaultUserEntity) userService.findById(1);

		// Create new sale advertisement
		saleAdvertisement = new DefaultSaleAdvertisementEntity();
		saleAdvertisement.setProductTitle("test product title");
		saleAdvertisement.setProductDescription("Product description");
		saleAdvertisement.setDate(LocalDateTime.of(2020, 4, 4, 21, 50));
		saleAdvertisement.setUser(user);
		saleAdvertisement.setPrice(BigDecimal.valueOf(15));

		SaleAdvertisementEntity savedSaleAdvertisement = service.add(saleAdvertisement);

		// Update sale advertisement attributes
		savedSaleAdvertisement.setProductTitle("updated test product title");
		savedSaleAdvertisement.setProductDescription("updated test product description");
		savedSaleAdvertisement.setDate(LocalDateTime.of(2020, 6, 2, 10, 50));
		savedSaleAdvertisement.setUser(userService.findById(2));

		// Update sale advertisement
		SaleAdvertisementEntity updatedSaleAdvertisement = service
				.update((DefaultSaleAdvertisementEntity) savedSaleAdvertisement);

		// Check updated attributes
		Assert.assertEquals(updatedSaleAdvertisement.getProductTitle(), "updated test product title");
		Assert.assertEquals(updatedSaleAdvertisement.getProductDescription(), "updated test product description");
		Assert.assertEquals(updatedSaleAdvertisement.getDate(), LocalDateTime.of(2020, 6, 2, 10, 50));
		Assert.assertEquals(updatedSaleAdvertisement.getUser(), userService.findById(2));

		// Check if user old user not have sale advertisement
		Assert.assertFalse(userService.findById(1).getSaleAdvertisements().contains(updatedSaleAdvertisement));
		// Check if new sale advertisement's user owns sale advertisement
		Assert.assertTrue(userService.findById(2).getSaleAdvertisements().contains(updatedSaleAdvertisement));
	}

	/**
	 * Verifies that the service update sale advertisement into persistence.
	 * 
	 * @throws UserNotFoundException
	 */
	@Test
	public void testUpdate_NotExisting_Fail() throws SaleAdvertisementServiceException, UserNotFoundException {
		final DefaultSaleAdvertisementEntity saleAdvertisement; // Sale advertisement for update
		final DefaultUserEntity user; // User for sale Advertisement

		// Get user for sale advertisement
		user = (DefaultUserEntity) userService.findById(1);

		// Create new sale advertisement
		saleAdvertisement = new DefaultSaleAdvertisementEntity();
		saleAdvertisement.setProductTitle("test product title");
		saleAdvertisement.setProductDescription("Product description");
		saleAdvertisement.setDate(LocalDateTime.of(2020, 4, 4, 21, 50));
		saleAdvertisement.setUser(user);

		saleAdvertisement.setId(100);
		// Update not existing sale advertisement throw exception
		Assertions.assertThrows(SaleAdvertisementServiceException.class,
				() -> service.update((DefaultSaleAdvertisementEntity) saleAdvertisement));
	}

	/**
	 * Verifies that the service removes a sale advertisement.
	 * 
	 * @throws UserNotFoundException
	 * @throws SaleAdvertisementAlreadyExistsException
	 * @throws SaleAdvertisementNotFoundException
	 */
	@Test
	public void testRemove_Existing_Removed() throws SaleAdvertisementServiceException, UserNotFoundException,
			SaleAdvertisementAlreadyExistsException, SaleAdvertisementNotFoundException {
		final DefaultSaleAdvertisementEntity saleAdvertisement; // Sale advertisement for add and remove
		final DefaultUserEntity user; // User for sale Advertisement

		// Get user for sale advertisement
		user = (DefaultUserEntity) userService.findById(1);

		// Create new sale advertisement
		saleAdvertisement = new DefaultSaleAdvertisementEntity();
		saleAdvertisement.setProductTitle("test product title");
		saleAdvertisement.setProductDescription("Product description");
		saleAdvertisement.setDate(LocalDateTime.of(2020, 4, 4, 21, 50));
		saleAdvertisement.setUser(user);
		saleAdvertisement.setPrice(BigDecimal.valueOf(15));

		SaleAdvertisementEntity savedSaleAdvertisement = service.add(saleAdvertisement);
		// Initial count of sale advertisements
		int initialCount = ((Collection<DefaultSaleAdvertisementEntity>) service.getAllSaleAdvertisements()).size();

		service.remove((DefaultSaleAdvertisementEntity) savedSaleAdvertisement);

		int finalCount = ((Collection<DefaultSaleAdvertisementEntity>) service.getAllSaleAdvertisements()).size();
		// Check if count of initial is final+1
		Assert.assertEquals(Integer.valueOf(initialCount), Integer.valueOf(finalCount + 1));

		// Check we cannot find a sale advertisement with id
		Assertions.assertThrows(SaleAdvertisementNotFoundException.class,
				() -> service.findById(savedSaleAdvertisement.getId()));
	}

	/**
	 * Verifies that the service removes a sale advertisement and images associated.
	 * 
	 * @throws UserNotFoundException
	 * @throws ImageServiceException
	 * @throws ImageNotFoundException
	 * @throws ImageAlreadyExistsException
	 * @throws SaleAdvertisementNotFoundException
	 */
	@Test
	public void testRemove_ExistingWithImages_Removed()
			throws SaleAdvertisementServiceException, UserNotFoundException, ImageServiceException,
			ImageNotFoundException, ImageAlreadyExistsException, SaleAdvertisementNotFoundException {
		final SaleAdvertisementEntity saleAdvertisement; // Sale advertisement for add and remove

		saleAdvertisement = service.findById(1);
		// Create sale advertisement's images
		// First image
		ImageEntity firstImage = new DefaultImageEntity();
		firstImage.setTitle("test image title");
		firstImage.setImagePath("test image path");
		firstImage.setSale_advertisement((DefaultSaleAdvertisementEntity) saleAdvertisement);

		ImageEntity savedFirstImage = imageService.add((DefaultImageEntity) firstImage);
		// Second image
		ImageEntity secondImage = new DefaultImageEntity();
		secondImage.setTitle("test image title");
		secondImage.setImagePath("test second image path");
		secondImage.setSale_advertisement((DefaultSaleAdvertisementEntity) saleAdvertisement);

		ImageEntity savedSecondImage = imageService.add((DefaultImageEntity) secondImage);

		// Get updated sale advertisement
		SaleAdvertisementEntity updatedSaleAdvertisement = service.findById(1);

		// Initial count of sale advertisements
		int initialCount = ((Collection<DefaultSaleAdvertisementEntity>) service.getAllSaleAdvertisements()).size();
		// Remove saleAdvertisement and associated images
		service.remove((DefaultSaleAdvertisementEntity) updatedSaleAdvertisement);
		// Count after remove
		int finalCount = ((Collection<DefaultSaleAdvertisementEntity>) service.getAllSaleAdvertisements()).size();
		Assert.assertEquals(Integer.valueOf(initialCount), Integer.valueOf(finalCount + 1));
		// Check that images do not exist
		Assertions.assertThrows(ImageNotFoundException.class, () -> imageService.findById(savedFirstImage.getId()));
		Assertions.assertThrows(ImageNotFoundException.class, () -> imageService.findById(savedSecondImage.getId()));

		// Check cannot find a sale advertisement with id
		Assertions.assertThrows(SaleAdvertisementNotFoundException.class,
				() -> service.findById(updatedSaleAdvertisement.getId()));
	}

	@Test
	public void testRemove_NotExisting_Fails() throws UserNotFoundException, SaleAdvertisementServiceException {
		final DefaultSaleAdvertisementEntity saleAdvertisement; // Sale advertisement to remove
		final DefaultUserEntity user; // User for sale Advertisement

		// Get user for sale advertisement
		user = (DefaultUserEntity) userService.findById(1);

		// Create new sale advertisement
		saleAdvertisement = new DefaultSaleAdvertisementEntity();
		saleAdvertisement.setProductTitle("test product title");
		saleAdvertisement.setProductDescription("Product description");
		saleAdvertisement.setDate(LocalDateTime.of(2020, 4, 4, 21, 50));
		saleAdvertisement.setUser(user);
		saleAdvertisement.setId(100);
		// Remove sale no exist
		Assertions.assertThrows(SaleAdvertisementNotFoundException.class, () -> service.remove(saleAdvertisement));
	}

	@Test
	public void testCreate_Remove_Images() throws UserNotFoundException, SaleAdvertisementServiceException,
			ImageServiceException, ImageNotFoundException, ImageAlreadyExistsException,
			SaleAdvertisementNotFoundException, SaleAdvertisementAlreadyExistsException {
		final DefaultSaleAdvertisementEntity saleAdvertisement; // add sale advertisement
		final DefaultUserEntity user; // User for sale Advertisement

		// Get user for sale advertisement
		user = (DefaultUserEntity) userService.findById(1);

		// Create new sale advertisement
		saleAdvertisement = new DefaultSaleAdvertisementEntity();
		saleAdvertisement.setProductTitle("test product title");
		saleAdvertisement.setProductDescription("Product description");
		saleAdvertisement.setDate(LocalDateTime.of(2020, 4, 4, 21, 50));
		saleAdvertisement.setUser(user);
		saleAdvertisement.setPrice(BigDecimal.valueOf(15));

		// Sale advertisement 0 images
		SaleAdvertisementEntity savedSaleAdvertisement = service.add(saleAdvertisement);
		int imageSize = savedSaleAdvertisement.getImages().size();
		Assert.assertEquals(Integer.valueOf(imageSize), Integer.valueOf(0));

		// Create first image
		DefaultImageEntity firstImage = new DefaultImageEntity();
		firstImage.setImagePath("test first image path");
		firstImage.setTitle("test first image title");
		firstImage.setSale_advertisement((DefaultSaleAdvertisementEntity) savedSaleAdvertisement);

		ImageEntity firstSavedImage = imageService.add(firstImage);
		SaleAdvertisementEntity updatedSaleAdvertisement = service.findById(savedSaleAdvertisement.getId());
		// Check image
		Assert.assertEquals(firstSavedImage.getSale_advertisement(), updatedSaleAdvertisement);
		int oneImage = updatedSaleAdvertisement.getImages().size();
		Assert.assertEquals(Integer.valueOf(oneImage), Integer.valueOf(1));

		// Create two images
		DefaultImageEntity secondImage = new DefaultImageEntity();
		secondImage.setImagePath("test second image path");
		secondImage.setTitle("test second image title");
		secondImage.setSale_advertisement((DefaultSaleAdvertisementEntity) savedSaleAdvertisement);

		DefaultImageEntity thirdImage = new DefaultImageEntity();
		thirdImage.setImagePath("test third image path");
		thirdImage.setTitle("test third image title");
		thirdImage.setSale_advertisement((DefaultSaleAdvertisementEntity) savedSaleAdvertisement);

		// Add two more images
		ImageEntity secondSavedImage = imageService.add(secondImage);
		ImageEntity thirdSavedImage = imageService.add(thirdImage);
		// Get Updated sale advertisement
		SaleAdvertisementEntity afterAddSaleAdvertisement = service.findById(savedSaleAdvertisement.getId());
		int imagesSize = afterAddSaleAdvertisement.getImages().size();
		// Image size = 3
		Assert.assertEquals(Integer.valueOf(imagesSize), Integer.valueOf(3));
		// Sale advertisement owns images
		Assert.assertTrue(afterAddSaleAdvertisement.getImages().contains(firstSavedImage));
		Assert.assertTrue(afterAddSaleAdvertisement.getImages().contains(secondSavedImage));
		Assert.assertTrue(afterAddSaleAdvertisement.getImages().contains(thirdSavedImage));

		// Remove second image
		imageService.remove((DefaultImageEntity) secondSavedImage);
		// Now cannot find second image, does not exist
		Assertions.assertThrows(ImageNotFoundException.class, () -> imageService.findById(secondSavedImage.getId()));

		// Sale advertisement not have second image
		SaleAdvertisementEntity afterRemoveSaleAdvertisement = service.findById(savedSaleAdvertisement.getId());
		Assert.assertFalse(afterRemoveSaleAdvertisement.getImages().contains(secondSavedImage));

		// Sale advertisement have first and third images
		Assert.assertTrue(afterRemoveSaleAdvertisement.getImages().contains(firstSavedImage));
		Assert.assertTrue(afterRemoveSaleAdvertisement.getImages().contains(thirdSavedImage));
	}

}
