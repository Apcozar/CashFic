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
import es.udc.fi.dc.fd.model.SaleAdvertisementEntity;
import es.udc.fi.dc.fd.model.persistence.DefaultImageEntity;
import es.udc.fi.dc.fd.model.persistence.DefaultSaleAdvertisementEntity;
import es.udc.fi.dc.fd.repository.ImageRepository;
import es.udc.fi.dc.fd.repository.SaleAdvertisementRepository;
import es.udc.fi.dc.fd.service.exceptions.ImageAlreadyExistsException;
import es.udc.fi.dc.fd.service.exceptions.ImageNotFoundException;

/**
 * Default implementation of the image service.
 * 
 * @author Santiago
 *
 */
@Service
public class DefaultImageService implements ImageService {

	/**
	 * Repositories for the domain entities handled by the service.
	 */
	private final ImageRepository imageRepository;
	private final SaleAdvertisementRepository saleAdvertisementRepository;

	/**
	 * Constructs an image service with the specified repositories.
	 *
	 * @param imageRepository             the image repository
	 * @param saleAdvertisementRepository the sale advertisement repository
	 */
	@Autowired
	public DefaultImageService(final ImageRepository imageRepository,
			final SaleAdvertisementRepository saleAdvertisementRepository) {
		super();

		this.imageRepository = checkNotNull(imageRepository, "Received a null pointer as imageRepository");
		this.saleAdvertisementRepository = checkNotNull(saleAdvertisementRepository,
				"Received a null pointer as saleAdvertisementRepository");
	}

	/**
	 * Returns an image with the given id.
	 * <p>
	 * If image does not exist with that id then throw ImageNotFoundException
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
	 * Store an image which no exist.
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
	 * Updates an image from persistence. If sale advertisement change, the sale
	 * advertisement change, the image must be removed from the initial sale
	 * advertisement
	 * 
	 * @param image image to update
	 * @return updated image
	 * @throws ImageNotFoundException if image with id not found
	 */
	@Override
	public final ImageEntity update(final DefaultImageEntity image) throws ImageNotFoundException {
		DefaultImageEntity updatedImage;
		checkNotNull(image, "Received a null pointer as image");

		if (!imageRepository.existsById(image.getId())) {
			throw new ImageNotFoundException(image.getId());
		}

		updatedImage = imageRepository.save(image);
		SaleAdvertisementEntity saleAdvertisementToUpdate = updatedImage.getSale_advertisement();
		saleAdvertisementToUpdate.addImage(updatedImage);
		saleAdvertisementRepository.save((DefaultSaleAdvertisementEntity) saleAdvertisementToUpdate);
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

	/**
	 * Exists image path.
	 *
	 * @param imagePath the image path
	 * @return true, if successful
	 */
	public boolean existsImagePath(String imagePath) {
		return imageRepository.existsImagePath(imagePath);
	}

	/**
	 * Returns all the images from the DB.
	 * 
	 * @return the persisted images
	 */
	@Override
	public final Iterable<DefaultImageEntity> getAllImages() {
		return imageRepository.findAll();
	}

	/**
	 * Returns a paginated collection of images.
	 * 
	 * @param page pagination data
	 * @return a paginated collection of images
	 */
	@Override
	public final Iterable<DefaultImageEntity> getImages(final Pageable page) {
		return imageRepository.findAll(page);
	}

}
