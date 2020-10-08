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

import es.udc.fi.dc.fd.model.Sale_advertisementEntity;
import es.udc.fi.dc.fd.model.persistence.DefaultSale_advertisementEntity;
import es.udc.fi.dc.fd.model.persistence.DefaultUserEntity;
import es.udc.fi.dc.fd.service.Sale_advertisementService;
import es.udc.fi.dc.fd.service.UserService;

/**
 * Integration tests for the {@link UserService}.
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
public class ITSale_advertisementService {

	/**
	 * Service being tested.
	 */
	@Autowired
	private Sale_advertisementService service;

	@Autowired
	private UserService userService;

	/**
	 * Default constructor.
	 */
	public ITSale_advertisementService() {
		super();
	}

	/**
	 * Verifies that the service adds entities into persistence.
	 */
	@Test
	public void testAdd_NotExisting_Added() {
		final DefaultSale_advertisementEntity entity; // Entity to add
		final Integer entitiesCount; // Original number of entities
		final Integer finalEntitiesCount; // Final number of entities
		final DefaultUserEntity user;
		entitiesCount = ((Collection<DefaultSale_advertisementEntity>) service.getAllSale_advertisements()).size();

		entity = new DefaultSale_advertisementEntity();
		entity.setProduct_title("ExampleProductTittle");
		entity.setProduct_description("ExampleProductDescription");
		user = (DefaultUserEntity) userService.findById(1);
		entity.setUser(user);
		service.add(entity);

		finalEntitiesCount = ((Collection<DefaultSale_advertisementEntity>) service.getAllSale_advertisements()).size();

		Assert.assertEquals(finalEntitiesCount, Integer.valueOf(entitiesCount + 1));
	}

	/**
	 * Verifies that the service update entities into persistence.
	 */
	@Test
	public void testAdd_Existing_Updated() {

		Sale_advertisementEntity entity = service.findById(3);
		entity.setProduct_description("new test Sale_advertisement description");
		entity.setProduct_title("new test product title");

		service.add((DefaultSale_advertisementEntity) entity);
		Sale_advertisementEntity persistedEntity = service.findById(3);

		Assert.assertEquals(persistedEntity.getProduct_description(), "new test Sale_advertisement description");
		Assert.assertEquals(persistedEntity.getProduct_title(), "new test product title");
	}

	/**
	 * Verifies that searching an existing sale_advertisement by id returns the
	 * expected sale_advertisement.
	 */
	@Test
	public void testFindById_Existing_Valid() {
		final Sale_advertisementEntity sale_advertisement; // Found entity

		sale_advertisement = service.findById(1);

		Assert.assertEquals(sale_advertisement.getId(), Integer.valueOf(1));
	}

	/**
	 * Verifies that searching for a not existing sale_advertisement by id returns
	 * an empty sale_advertisement.
	 */
	@Test
	public void testFindById_NotExisting_Invalid() {
		final Sale_advertisementEntity sale_advertisement; // Found entity

		sale_advertisement = service.findById(100);

		Assert.assertEquals(sale_advertisement.getId(), Integer.valueOf(-1));
	}

	/**
	 * Verifies that add images to sale_advertisement and it returns a collection of
	 * added images
	 * 
	 * @throws NoValidDataException
	 */
//	@Test
//	public void testAddImages_To_Sale_advertisement() throws NoValidDataException {
//		final DefaultImageEntity firstImage; // First image to add
//		final DefaultImageEntity secondImage; // First image to add
//		final DefaultImageEntity thirdImage; // First image to add
//		final DefaultUserEntity user; // User who owns the sale_advertisement
//		final DefaultSale_advertisementEntity sale_advertisement; // Sale_advertisement to add and add images
//
//		DefaultSale_advertisementEntity persistedSale_advertisement; // Sale_advertisement persisted as result
//
//		final ImageEntity persistedFirstImage, persistedSecondImage, persistedThirdImage;
//
//		// create a new sale_advertisement
//		sale_advertisement = new DefaultSale_advertisementEntity();
//		sale_advertisement.setProduct_title("TestProductTitle");
//		sale_advertisement.setProduct_description("TestProductDescription");
//		user = (DefaultUserEntity) userService.findById(2);
//		sale_advertisement.setUser(user);
//
//		// Save sale_advertisement
//		persistedSale_advertisement = (DefaultSale_advertisementEntity) service.add(sale_advertisement);
//
//		// Create images
//		// First image
//		firstImage = new DefaultImageEntity();
//		firstImage.setImagePath("FirstImageTestPath");
//		firstImage.setTitle("FirstImageTitle");
//		firstImage.setSale_advertisement(persistedSale_advertisement);
//		persistedFirstImage = imageService.add(firstImage);
//
//		// Second image
//		secondImage = new DefaultImageEntity();
//		secondImage.setImagePath("SecondImageTestPath");
//		secondImage.setTitle("SecondImageTitle");
//		secondImage.setSale_advertisement(persistedSale_advertisement);
//		persistedSecondImage = imageService.add(secondImage);
//
//		// Third image
//		thirdImage = new DefaultImageEntity();
//		thirdImage.setImagePath("ThirdImageTestPath");
//		thirdImage.setTitle("ThirdImageTitle");
//		thirdImage.setSale_advertisement(persistedSale_advertisement);
//		persistedThirdImage = imageService.add(thirdImage);
//
//		// Set with the images
//		Set<DefaultImageEntity> images = new HashSet<DefaultImageEntity>();
//		images.add((DefaultImageEntity) persistedFirstImage);
//		images.add((DefaultImageEntity) persistedSecondImage);
//		images.add((DefaultImageEntity) persistedThirdImage);
//
//		// Add Set of images to Sale_advertisement
//		persistedSale_advertisement.setImages(images);
//
//		// Save sale_advertisement with the images added
//		DefaultSale_advertisementEntity Sale_advertisementWithImages = (DefaultSale_advertisementEntity) service
//				.add(persistedSale_advertisement);
//
//		// Check images
//		Assert.assertEquals(Sale_advertisementWithImages.getImages().size(), 3);
//		Assert.assertEquals(Sale_advertisementWithImages.getImages(), images);
//		// Check if the method save returns the same entity as the findById
//		Assert.assertEquals(service.findById(persistedSale_advertisement.getId()), Sale_advertisementWithImages);
//	}

//	/**
//	 * Verifies that remove images from sale_advertisement returns
//	 * sale_advertisement without the images The user, sale_advertisement and images
//	 * are initialized through initial sql script
//	 * 
//	 * @throws NoValidDataException
//	 */
//	@Test
//	public void testRemoveImages_From_Sale_advertisement() throws NoValidDataException {
//
//		// Create a sale_advertisement
//		Sale_advertisementEntity sale_advertisement = new DefaultSale_advertisementEntity();
//		sale_advertisement.setProduct_title("TestProductTitle");
//		sale_advertisement.setProduct_description("TestProductDescription");
//		sale_advertisement.setUser((DefaultUserEntity) userService.findById(1));
//
//		// Save Sale_advertisement
//
//		Sale_advertisementEntity persistedSale_advertisement = service
//				.add((DefaultSale_advertisementEntity) sale_advertisement);
//
//		// Create 3 images
//		DefaultImageEntity firstImage = new DefaultImageEntity();
//		firstImage.setTitle("First image title test");
//		firstImage.setImagePath("first image path test");
//		firstImage.setSale_advertisement((DefaultSale_advertisementEntity) persistedSale_advertisement);
//
//		DefaultImageEntity secondImage = new DefaultImageEntity();
//		secondImage.setTitle("Second image title test");
//		secondImage.setImagePath("second image path test");
//		secondImage.setSale_advertisement((DefaultSale_advertisementEntity) persistedSale_advertisement);
//
//		DefaultImageEntity thirdImage = new DefaultImageEntity();
//		thirdImage.setTitle("Third image title test");
//		thirdImage.setImagePath("third image path test");
//		thirdImage.setSale_advertisement((DefaultSale_advertisementEntity) persistedSale_advertisement);
//
//		// Save images
//		ImageEntity persistedFirstImage = imageService.add(firstImage);
//		ImageEntity persistedSecondImage = imageService.add(secondImage);
//		ImageEntity persistedThirdImage = imageService.add(thirdImage);
//
//		Set<DefaultImageEntity> images = new HashSet<DefaultImageEntity>();
//		images.add((DefaultImageEntity) persistedFirstImage);
//		images.add((DefaultImageEntity) persistedSecondImage);
//		images.add((DefaultImageEntity) persistedThirdImage);
//
//		//
//		persistedSale_advertisement.setImages(images);
//		Sale_advertisementEntity persisted_Sale_advertisement_u1 = service
//				.add((DefaultSale_advertisementEntity) persistedSale_advertisement);
//
//		// Check Sale_advertisement have 3 images
//		Assert.assertEquals(persisted_Sale_advertisement_u1.getImages().size(), 3);
//
//		// Remove first image
//		images.remove(images.iterator().next());
//		Assert.assertEquals(images.size(), 2);
//
//		imageService.remove((DefaultImageEntity) persistedFirstImage);
//
//		// Check if changes persist
//		Assert.assertEquals(service.findById(persisted_Sale_advertisement_u1.getId()).getImages(), images);
//
//	}

//	/**
//	 * Add same image to 2 Sale_advertisements returns
//	 * 
//	 * @throws NoValidDataException
//	 * 
//	 */
//	@Test
//	public void testAdd_Image_To_Diferent_Sales_Invalid() throws NoValidDataException {
//		DefaultSale_advertisementEntity sale_advertisement;
//		DefaultSale_advertisementEntity secondSale_advertisement;
//		DefaultImageEntity image;
//
//		// sale_advertisement.setImages(value);
//		// Create two Sale_advertisements
//		sale_advertisement = new DefaultSale_advertisementEntity();
//		sale_advertisement.setProduct_description("test product description");
//		sale_advertisement.setProduct_title("test product title");
//		sale_advertisement.setUser((DefaultUserEntity) userService.findById(1));
//
//		// Save Sale_advertisements
//		Sale_advertisementEntity persistedSale_advertisement = service
//				.add((DefaultSale_advertisementEntity) sale_advertisement);
//
//		// Create image
//		image = new DefaultImageEntity();
//		image.setImagePath("test image path");
//		image.setTitle("test image title");
//		image.setSale_advertisement((DefaultSale_advertisementEntity) persistedSale_advertisement);
//		// Save image
//		ImageEntity persistedImage = imageService.add((DefaultImageEntity) image);
//
//		// Modify first sale_advertisement
//		Set<DefaultImageEntity> images = new HashSet<DefaultImageEntity>();
//		images.add((DefaultImageEntity) persistedImage);
//
//		persistedSale_advertisement.setImages(images);
//		persistedSale_advertisement = service.add((DefaultSale_advertisementEntity) persistedSale_advertisement);
//
//		// secondSale_advertisement.setImages(value);
//		secondSale_advertisement = new DefaultSale_advertisementEntity();
//		secondSale_advertisement.setProduct_description("test product description");
//		secondSale_advertisement.setProduct_title("second test product title");
//		secondSale_advertisement.setUser((DefaultUserEntity) userService.findById(1));
//		secondSale_advertisement.setImages(images);
//		Sale_advertisementEntity persistedSecondSale_advertisement = service.add(secondSale_advertisement);
//
//		// TO-DO WHY I CAN HAVE 2 sale_advertisement with the same set of images
//		System.out.println();
////		// Save image
////		images.add((DefaultImageEntity) persistedImage);
////		
////		
////		persistedSale_advertisement.setImages(images);
////		service.add((DefaultSale_advertisementEntity) persistedSale_advertisement);
//
//	}

}
