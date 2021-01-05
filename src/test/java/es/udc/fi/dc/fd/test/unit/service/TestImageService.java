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

package es.udc.fi.dc.fd.test.unit.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import es.udc.fi.dc.fd.model.ImageEntity;
import es.udc.fi.dc.fd.model.persistence.DefaultImageEntity;
import es.udc.fi.dc.fd.model.persistence.DefaultSaleAdvertisementEntity;
import es.udc.fi.dc.fd.repository.ImageRepository;
import es.udc.fi.dc.fd.repository.SaleAdvertisementRepository;
import es.udc.fi.dc.fd.service.DefaultImageService;
import es.udc.fi.dc.fd.service.ImageService;
import es.udc.fi.dc.fd.service.exceptions.ImageAlreadyExistsException;
import es.udc.fi.dc.fd.service.exceptions.ImageNotFoundException;

/**
 * Unit tests for the {@link ImageService}.
 * <p>
 * As this service doesn't contain any actual business logic, and it just wraps
 * the example entities repository, these tests are for verifying everything is
 * set up correctly and working.
 */
@RunWith(JUnitPlatform.class)
final class TestImageService {

	@Mock
	private ImageRepository imageRepository;

	@Mock
	private SaleAdvertisementRepository saleAdvertisementRepository;

	/**
	 * Service being tested.
	 */
	@InjectMocks
	private DefaultImageService imageService;

	static final private Integer imageA_ID = 1;
	static final private Integer imageB_ID = 2;
	static final private Integer imageC_ID = 3;
	static final private Integer imageSaved_ID = 4;

	static final private Integer noImage_ID = 25;

	static final private String imageA_filename = "filenameA";
	static final private String imageB_filename = "filenameB";
	static final private String imageC_filename = "filenameC";
	static final private String imageC_updatedFilename = "updatedFilenameC";
	static final private String filenameNotExisting = "filenameNotExisting";

	static final private String imageA_title = "titleA";
	static final private String imageB_title = "titleB";
	static final private String imageC_title = "titleC";
	static final private String imageC_updatedTitle = "updatedTitleC";

	private DefaultSaleAdvertisementEntity saleAdvertisementImages;
	static final private Integer saleAdvertisement_ID = 1;

	private ArrayList<DefaultImageEntity> images;
	private DefaultImageEntity imageA;
	private DefaultImageEntity imageB;
	private DefaultImageEntity imageC;
	private DefaultImageEntity updatedImageC;

	private DefaultImageEntity imageNotAdded;
	private DefaultImageEntity imageSaved;

	/**
	 * Default constructor.
	 */
	public TestImageService() {
		super();

	}

	@BeforeEach
	public void initialize() {
		saleAdvertisementImages = new DefaultSaleAdvertisementEntity();
		saleAdvertisementImages.setId(saleAdvertisement_ID);
		imageA = new DefaultImageEntity(imageA_filename, imageA_title, saleAdvertisementImages);
		imageA.setId(imageA_ID);

		imageB = new DefaultImageEntity(imageB_filename, imageB_title, saleAdvertisementImages);
		imageB.setId(imageB_ID);

		imageC = new DefaultImageEntity(imageC_filename, imageC_title, saleAdvertisementImages);
		imageC.setId(imageC_ID);

		imageNotAdded = new DefaultImageEntity(imageC_filename, imageC_title, saleAdvertisementImages);
		imageNotAdded.setId(noImage_ID);

		imageSaved = new DefaultImageEntity(imageC_filename, imageC_title, saleAdvertisementImages);
		imageSaved.setId(imageSaved_ID);

		updatedImageC = new DefaultImageEntity(imageC_updatedFilename, imageC_updatedTitle, saleAdvertisementImages);
		updatedImageC.setId(imageC_ID);

		MockitoAnnotations.initMocks(this);

		Mockito.when(imageRepository.existsById(imageA_ID)).thenReturn(true);
		Mockito.when(imageRepository.existsById(imageB_ID)).thenReturn(true);
		Mockito.when(imageRepository.existsById(imageC_ID)).thenReturn(true);

		Mockito.when(imageRepository.getOne(imageA_ID)).thenReturn(imageA);
		Mockito.when(imageRepository.getOne(imageB_ID)).thenReturn(imageB);
		Mockito.when(imageRepository.getOne(imageC_ID)).thenReturn(imageC);

		Mockito.when(imageRepository.existsById(noImage_ID)).thenReturn(false);

		Mockito.when(saleAdvertisementRepository.getOne(saleAdvertisement_ID)).thenReturn(saleAdvertisementImages);

		Mockito.when(imageRepository.save(imageNotAdded)).thenReturn(imageSaved);

		Mockito.when(saleAdvertisementRepository.save(saleAdvertisementImages)).thenReturn(saleAdvertisementImages);

		Mockito.when(imageRepository.existsImagePath(imageA_filename)).thenReturn(true);
		Mockito.when(imageRepository.existsImagePath(filenameNotExisting)).thenReturn(false);

		images = new ArrayList<>();
		images.add(imageA);
		images.add(imageB);
		images.add(imageC);

		Mockito.when(imageRepository.findAll()).thenReturn(images);
		PageImpl<DefaultImageEntity> imagesPages = new PageImpl<>(images, PageRequest.of(1, 3), 10);
		Mockito.when(imageRepository.findAll(PageRequest.of(1, 3))).thenReturn(imagesPages);

		Mockito.doNothing().when(imageRepository).delete(imageA);

		Mockito.when(imageRepository.save(imageC)).thenReturn(updatedImageC);

	}

	@Test
	void validIdFindsImage() {

		try {
			assertEquals(imageService.findById(imageA_ID), (imageA));
			assertEquals(imageService.findById(imageB_ID), (imageB));
			assertEquals(imageService.findById(imageC_ID), (imageC));
			assertEquals(imageA_ID, imageService.findById(imageA_ID).getId());
			assertEquals(imageA_filename, imageService.findById(imageA_ID).getImagePath());
			assertEquals(imageA_title, imageService.findById(imageA_ID).getTitle());
			assertEquals(saleAdvertisementImages, imageService.findById(imageA_ID).getSale_advertisement());
		} catch (ImageNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Test
	void findByIdImageNotFound() {
		assertThrows(ImageNotFoundException.class, () -> {
			imageService.findById(noImage_ID);
		});
	}

	@Test
	void findByIdNullId() {
		assertThrows(NullPointerException.class, () -> {
			imageService.findById(null);
		});
	}

	@Test
	void addNullImage() {
		assertThrows(NullPointerException.class, () -> {
			imageService.add(null);
		});
	}

	@Test
	void addImageThatExists() {
		assertThrows(ImageAlreadyExistsException.class, () -> {
			imageService.add(imageC);
		});
	}

	@Test
	void addImageReturnImageWithId() {
		try {
			DefaultImageEntity imageStored = (DefaultImageEntity) imageService.add(imageNotAdded);
			assertEquals(imageSaved, imageStored);
		} catch (ImageAlreadyExistsException e) {
			e.printStackTrace();
		}

	}

	@Test
	void updateImageNullImageThrowException() {
		assertThrows(NullPointerException.class, () -> {
			imageService.update(null);
		});
	}

	@Test
	void updateImageNotFoundThrowsException() {
		assertThrows(ImageNotFoundException.class, () -> {
			imageService.update(imageNotAdded);
		});
	}

	@Test
	void updateImageUpdated() {
		try {
			ImageEntity imageUpdated = imageService.update(imageC);
			assertEquals(imageUpdated, updatedImageC);
		} catch (ImageNotFoundException e) {
			e.printStackTrace();
		}

	}

	@Test
	void removeImageNullThrowsException() {
		assertThrows(NullPointerException.class, () -> {
			imageService.remove(null);
		});
	}

	@Test
	void removeImageNotFoundThrowsException() {
		assertThrows(ImageNotFoundException.class, () -> {
			imageService.remove(imageNotAdded);
		});
	}

	@Test
	void removeImageRemoved() {
		try {
			imageService.remove(imageA);
			assertFalse(saleAdvertisementImages.getImages().contains(imageA));
		} catch (ImageNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Test
	void imagePathExists() {
		assertTrue(imageService.existsImagePath(imageA_filename));
	}

	@Test
	void imagePathNotExists() {
		assertFalse(imageService.existsImagePath(filenameNotExisting));
	}

	@Test
	void getAllImages() {
		Iterable<DefaultImageEntity> allImages = imageService.getAllImages();
		assertEquals(images, allImages);
	}

	@Test
	void getImages() {
		Iterable<DefaultImageEntity> allImages = imageService.getImages(PageRequest.of(1, 3));
		allImages.forEach(image -> {
			assertTrue(images.contains(image));
		});
	}

}
