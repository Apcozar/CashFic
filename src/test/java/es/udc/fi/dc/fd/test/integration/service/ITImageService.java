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
import java.util.Iterator;

import org.junit.Assert;
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
import es.udc.fi.dc.fd.service.ImageService;
import es.udc.fi.dc.fd.service.SaleAdvertisementService;
import es.udc.fi.dc.fd.service.exceptions.ImageAlreadyExistsException;
import es.udc.fi.dc.fd.service.exceptions.ImageNotFoundException;
import es.udc.fi.dc.fd.service.exceptions.ImageServiceException;
import es.udc.fi.dc.fd.service.exceptions.SaleAdvertisementNotFoundException;
import es.udc.fi.dc.fd.service.exceptions.SaleAdvertisementServiceException;

/**
 * Integration tests for the {@link ImageService}.
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
public class ITImageService {

	/**
	 * Service being tested.
	 */
	@Autowired
	private ImageService service;

	@Autowired
	private SaleAdvertisementService saleService;

	/**
	 * Default constructor.
	 */
	public ITImageService() {
		super();
	}

	/**
	 * Verifies that searching an existing image by id returns the expected image.
	 * 
	 * @throws ImageNotFoundException
	 */
	@Test
	public void testFindById_Existing_Valid() throws ImageNotFoundException {
		final ImageEntity image; // Found entity

		image = service.findById(1);

		Assert.assertEquals(image.getId(), Integer.valueOf(1));
	}

	/**
	 * Verifies that searching for a not existing image by id returns an empty
	 * image.
	 * 
	 * @throws ImageNotFoundException
	 */
	@Test
	public void testFindById_NotExisting_Invalid() throws ImageNotFoundException {

		assertThrows(ImageNotFoundException.class, () -> {
			service.findById(100); // Image with id 100 no exist
		});
	}

	/**
	 * Verifies that the service adds entities into persistence.
	 * 
	 * @throws ImageServiceException
	 * @throws SaleAdvertisementServiceException
	 * @throws ImageAlreadyExistsException
	 * @throws SaleAdvertisementNotFoundException
	 * 
	 */
	@Test
	public void testAdd_NotExisting_Added() throws ImageServiceException, SaleAdvertisementServiceException,
			ImageAlreadyExistsException, SaleAdvertisementNotFoundException {
		final ImageEntity image; // image to add
		final Integer imagesCount; // Original number of images
		final Integer finalImagesCount; // Final number of images
		final ImageEntity savedImage; // Image stored as result of use service
		DefaultSaleAdvertisementEntity saleAdvertisement; // Image's sale advertisement

		// Get sale advertisement for the image
		saleAdvertisement = (DefaultSaleAdvertisementEntity) saleService.findById(1);

		// Get number of stored images
		imagesCount = ((Collection<DefaultImageEntity>) service.getAllImages()).size();

		// Create a image
		image = new DefaultImageEntity();
		image.setImagePath("newTestImagePath");
		image.setTitle("newTestImageTitle");
		image.setSale_advertisement(saleAdvertisement);

		// Save the image
		savedImage = service.add((DefaultImageEntity) image);

		// Get stored images count
		finalImagesCount = ((Collection<DefaultImageEntity>) service.getAllImages()).size();

		// Check if image has been added by count of all images
		Assert.assertEquals(finalImagesCount, Integer.valueOf(imagesCount + 1));

		// Check savedImage data
		Assert.assertEquals(savedImage.getTitle(), "newTestImageTitle");
		Assert.assertEquals(savedImage.getImagePath(), "newTestImagePath");
		// Check image's sale advertisement is the same as stored
		Assert.assertEquals(savedImage.getSale_advertisement(), saleService.findById(1));
		// Sale advertisement has been updated and now owns the image
		Assert.assertTrue(saleService.findById(1).getImages().contains(savedImage));
	}

	@Test
	public void testAdd_Image_Already_Exist_Fail() throws ImageServiceException, SaleAdvertisementServiceException,
			ImageAlreadyExistsException, SaleAdvertisementNotFoundException {
		ImageEntity image; // image to add
		DefaultSaleAdvertisementEntity saleAdvertisement;

		// Get sale advertisement for the image
		saleAdvertisement = (DefaultSaleAdvertisementEntity) saleService.findById(1);

		// Create a image
		image = new DefaultImageEntity();
		image.setImagePath("firstTestImagePath");
		image.setTitle("firstTestImageTitle");
		image.setSale_advertisement(saleAdvertisement);

		// Save the image
		ImageEntity savedImage = service.add((DefaultImageEntity) image);

		// Change path it must be unique
		savedImage.setImagePath("Changed image path");

		// Expected exception, cannot add an existing image (image with assigned id)
		assertThrows(ImageAlreadyExistsException.class, () -> {
			service.add((DefaultImageEntity) savedImage);
		});
	}

	@Test
	public void testRemove_Image_Exist_Removed() throws ImageServiceException, SaleAdvertisementServiceException,
			ImageNotFoundException, ImageAlreadyExistsException, SaleAdvertisementNotFoundException {
		final Integer imagesCount; // Original number of images
		final Integer finalImagesCount; // Final number of images
		ImageEntity image; // image to add
		DefaultSaleAdvertisementEntity saleAdvertisement;

		// Get sale advertisement for the image
		saleAdvertisement = (DefaultSaleAdvertisementEntity) saleService.findById(1);

		// Create a image
		image = new DefaultImageEntity();
		image.setImagePath("first Test ImagePath");
		image.setTitle("first Test ImageTitle");
		image.setSale_advertisement(saleAdvertisement);
		// Save the image
		ImageEntity savedImage = service.add((DefaultImageEntity) image);
		imagesCount = ((Collection<DefaultImageEntity>) service.getAllImages()).size();
		// Remove image and get images count
		service.remove((DefaultImageEntity) savedImage);
		finalImagesCount = ((Collection<DefaultImageEntity>) service.getAllImages()).size();
		// Size decrease 1
		Assert.assertEquals(Integer.valueOf(finalImagesCount + 1), imagesCount);
		// Not found image by id
		assertThrows(ImageNotFoundException.class, () -> {
			service.findById(savedImage.getId());
		});
	}

	@Test
	public void testRemove_Image_Not_Exist_Fails()
			throws ImageServiceException, SaleAdvertisementServiceException, SaleAdvertisementNotFoundException {
		DefaultSaleAdvertisementEntity saleAdvertisement;
		ImageEntity image; // image to remove

		// Get sale advertisement for the image
		saleAdvertisement = (DefaultSaleAdvertisementEntity) saleService.findById(1);

		// Set image
		image = new DefaultImageEntity();
		image.setImagePath("firstTestImagePath");
		image.setTitle("firstTestImageTitle");
		image.setSale_advertisement(saleAdvertisement);
		image.setId(100);

		// Remove image which not have been saved
		assertThrows(ImageNotFoundException.class, () -> {
			service.remove((DefaultImageEntity) image);
		});
	}

	/**
	 * Verifies that remove existing image two times return an exception.
	 * 
	 * @throws ImageNotFoundException
	 */
	@Test
	public void testRemove_Same_Tow_Times_Invalid() throws ImageNotFoundException {
		ImageEntity image = service.findById(1);
		service.remove((DefaultImageEntity) image);
		assertThrows(ImageNotFoundException.class, () -> {
			service.remove((DefaultImageEntity) image);
		});
	}

	/**
	 * Verifies that updating an image returns the image with changed attributes
	 * 
	 * @throws ImageServiceException
	 * @throws SaleAdvertisementServiceException
	 * @throws ImageAlreadyExistsException
	 * @throws SaleAdvertisementNotFoundException
	 * @throws ImageNotFoundException
	 * 
	 */
	@Test
	public void testUpdate_Existing_Image_Updated() throws ImageServiceException, SaleAdvertisementServiceException,
			ImageAlreadyExistsException, SaleAdvertisementNotFoundException, ImageNotFoundException {
		DefaultSaleAdvertisementEntity saleAdvertisement; // Initial image's sale advertisement
		SaleAdvertisementEntity changeSaleAdvertisement; // sale advertisement for update image
		DefaultImageEntity image; // image to add and update attributes

		// Get all sale advertisements
		Iterable<DefaultSaleAdvertisementEntity> salesAdvertisementCollection = saleService.getAllSaleAdvertisements();
		Iterator<DefaultSaleAdvertisementEntity> iterator = salesAdvertisementCollection.iterator();

		saleAdvertisement = iterator.next();
		changeSaleAdvertisement = iterator.next();

		// Create image with attributes
		image = new DefaultImageEntity();

		image.setTitle("Initial image title");
		image.setImagePath("initial image path");
		image.setSale_advertisement(saleAdvertisement);
		// Save image
		ImageEntity storedImage = service.add(image);

		// Change saved image attributes and update image
		ImageEntity changeImage = service.findById(storedImage.getId());
		changeImage.setTitle("change image title");
		changeImage.setImagePath("change image path");
		changeImage.setSale_advertisement((DefaultSaleAdvertisementEntity) changeSaleAdvertisement);

		// Update changed image
		DefaultImageEntity updatedStoredImage = (DefaultImageEntity) service.update((DefaultImageEntity) changeImage);

		Assert.assertEquals(updatedStoredImage.getTitle(), "change image title");
		Assert.assertEquals(updatedStoredImage.getImagePath(), "change image path");
		Assert.assertEquals(updatedStoredImage.getSale_advertisement().getId(), changeSaleAdvertisement.getId());

	}

	/**
	 * Verifies that updating an image that not exists returns exception
	 * 
	 * @throws ImageServiceException
	 * @throws SaleAdvertisementServiceException
	 * @throws SaleAdvertisementNotFoundException
	 * 
	 */
	@Test
	public void testUpdate_NotExisting_Image_Fails()
			throws ImageServiceException, SaleAdvertisementServiceException, SaleAdvertisementNotFoundException {
		DefaultSaleAdvertisementEntity saleAdvertisement; // Initial image's sale advertisement
		DefaultImageEntity image; // image to update attributes

		saleAdvertisement = (DefaultSaleAdvertisementEntity) saleService.findById(1);
		// Create image with attributes
		image = new DefaultImageEntity();
		image.setId(100); // Image's id that not exist
		image.setTitle("Initial image title");
		image.setImagePath("initial image path");
		image.setSale_advertisement(saleAdvertisement);
		// Update image returns exception
		assertThrows(ImageNotFoundException.class, () -> {
			service.update(image);
		});
	}

}
