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

import es.udc.fi.dc.fd.model.ImageEntity;
import es.udc.fi.dc.fd.model.persistence.DefaultImageEntity;
import es.udc.fi.dc.fd.model.persistence.DefaultSaleAdvertisementEntity;
import es.udc.fi.dc.fd.repository.ImageRepository;
import es.udc.fi.dc.fd.repository.SaleAdvertisementRepository;
import es.udc.fi.dc.service.exceptions.ImageAlreadyExistsException;
import es.udc.fi.dc.service.exceptions.ImageNotFoundException;
import es.udc.fi.dc.service.exceptions.ImageServiceException;

/**
 * Default implementation of the image service.
 * 
 * @author Santiago
 *
 */
@Service
public class DefaultImageService implements ImageService {

	/**
	 * Repository for the domain entities handled by the service.
	 */
	private final ImageRepository imageRepository;

	/**
	 * Repository for the domain entities handled by the service.
	 */
	private final SaleAdvertisementRepository saleAdvertisementRepository;

	/**
	 * Constructs an image service with the specified repository.
	 *
	 * @param repository     the repository for the image instances
	 * @param saleRepository the repository for the sale advertisement instances
	 */
	@Autowired
	public DefaultImageService(final ImageRepository repository, final SaleAdvertisementRepository saleRepository) {
		super();

		imageRepository = checkNotNull(repository, "Received a null pointer as repository");
		saleAdvertisementRepository = checkNotNull(saleRepository, "Received a null pointer as repository");
	}

	/**
	 * Returns an image with the given id.
	 * <p>
	 * If image no exists with that id then throw exception
	 *
	 * @param identifier image's id
	 * @return the image for the given identifier
	 * @throws ImageNotFoundException exception when image not found
	 */
	@Override
	public final ImageEntity findById(final Integer identifier) throws ImageNotFoundException {
		final ImageEntity image;
		checkNotNull(identifier, "Received a null pointer as identifier");

		if (!imageRepository.existsById(identifier)) {
			throw new ImageNotFoundException(identifier);
		}
		image = imageRepository.getOne(identifier);
		return image;
	}

	/**
	 * Returns an image with the given id.
	 * <p>
	 * Create an image with the entity parameters received. Returns the entity
	 * persisted with id assigned
	 * 
	 * @param image image with the parameters
	 * @return the image with the id associated
	 * @throws ImageAlreadyExistsException when the image id already exists
	 */
	@Override
	public final ImageEntity add(final DefaultImageEntity image) throws ImageAlreadyExistsException {
		checkNotNull(image, "Received a null pointer as image");
		if (imageRepository.existsById(image.getId())) {
			throw new ImageAlreadyExistsException(image.getId());
		}
		DefaultSaleAdvertisementEntity saleAdvertisementEntity = saleAdvertisementRepository
				.getOne(image.getSale_advertisement().getId());
		DefaultImageEntity savedImage = imageRepository.save(image);
		saleAdvertisementEntity.addImage(savedImage);
		saleAdvertisementRepository.save(saleAdvertisementEntity);

		return savedImage;
	}

	/**
	 * Updates an image from persistence.
	 * 
	 * @param image image to update
	 * @return updated image
	 * @throws ImageServiceException The ImageServiceException
	 */
	// CANT UPDATE SALE ADVERTISEMENT
	// imageService UPDATE NOT WORK (CHANGE image's sale advertisement, no delete
	// first sale advertise's image from the first sale advertise list
	// it add new sale advertisement to image and add image to new sale
	// advertisement list
	@Override
	public final ImageEntity update(final DefaultImageEntity image) throws ImageServiceException {
		DefaultImageEntity updatedImage;
		if ((image.getId() == null || image.getId() == -1)) {
			throw new ImageServiceException("The image id cannot be null or -1");
		}
		try {
			if (!imageRepository.existsById(image.getId())) {
				throw new ImageServiceException("Image not exists");
			}
			DefaultImageEntity imageStored = imageRepository.findById(Integer.valueOf(image.getId())).get();
			// if image update sale advertisement need remove image from sale advertisement
			if (!(imageStored.getSale_advertisement().equals(image.getSale_advertisement()))) {
				DefaultSaleAdvertisementEntity storedSaleAdvertisement = (DefaultSaleAdvertisementEntity) imageStored
						.getSale_advertisement();
				storedSaleAdvertisement.removeImage(imageStored);
				saleAdvertisementRepository.save(storedSaleAdvertisement);
			}

			updatedImage = imageRepository.save(image);
			DefaultSaleAdvertisementEntity newImageSaleAdvertisement = (DefaultSaleAdvertisementEntity) updatedImage
					.getSale_advertisement();
			newImageSaleAdvertisement.addImage(updatedImage);
			saleAdvertisementRepository.save(newImageSaleAdvertisement);
		} catch (Exception e) {
			throw new ImageServiceException("Cannot update this image");
		}
		return updatedImage;
	}

	/**
	 * Removes an image from persistence.
	 * 
	 * @param image image to remove
	 * @throws ImageNotFoundException when image not found
	 */
	@Override
	public final void remove(final DefaultImageEntity image) throws ImageNotFoundException {
		checkNotNull(image, "Received a null pointer as identifier");
		if (!imageRepository.existsById(image.getId())) {
			throw new ImageNotFoundException(image.getId());
		}
		DefaultSaleAdvertisementEntity saleAdvertisement = (DefaultSaleAdvertisementEntity) image
				.getSale_advertisement();
		saleAdvertisement.removeImage(image);
		saleAdvertisementRepository.save(saleAdvertisement);
		imageRepository.delete(image);
	}

	@Override
	public final Iterable<DefaultImageEntity> getAllImages() {
		return imageRepository.findAll();
	}

	@Override
	public final Iterable<DefaultImageEntity> getImages(final Pageable page) {
		return imageRepository.findAll(page);
	}

}
