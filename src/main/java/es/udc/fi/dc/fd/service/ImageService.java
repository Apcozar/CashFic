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

import es.udc.fi.dc.fd.model.ImageEntity;
import es.udc.fi.dc.fd.model.persistence.DefaultImageEntity;
import es.udc.fi.dc.fd.service.exceptions.ImageAlreadyExistsException;
import es.udc.fi.dc.fd.service.exceptions.ImageNotFoundException;

/**
 * Service for the image domain.
 * <p>
 * This is a domain service just to allow the endpoints querying the entities
 * they are asked for.
 *
 * @author Santiago
 */
public interface ImageService {

	/**
	 * Returns an image with the given id.
	 * <p>
	 * If no image exists with that id then throw exception
	 *
	 * @param identifier image's id to find
	 * @return the image for the given identifier
	 * @throws ImageNotFoundException exception when image not found
	 */
	public ImageEntity findById(final Integer identifier) throws ImageNotFoundException;

	/**
	 * Store an image which no exist.
	 * <p>
	 * Create an image with the entity parameters received. Returns the entity
	 * persisted with id assigned
	 * 
	 * @param image image to persist imageId should be -1
	 * @return the persisted image
	 * @throws ImageAlreadyExistsException when the image with id already exists
	 */
	public ImageEntity add(final DefaultImageEntity image) throws ImageAlreadyExistsException;

	/**
	 * Updates an image from persistence. If sale advertisement change, the sale
	 * advertisement change, the image must be removed from the initial sale
	 * advertisement
	 * 
	 * @param image image to update
	 * @return updated image
	 * @throws ImageNotFoundException if image with id not found
	 */
	ImageEntity update(DefaultImageEntity image) throws ImageNotFoundException;

	/**
	 * Removes an image from persistence.
	 * 
	 * @param image image to remove
	 * @throws ImageNotFoundException when image not found
	 */
	public void remove(final DefaultImageEntity image) throws ImageNotFoundException;

	/**
	 * Exists image path.
	 *
	 * @param imagePath the image path
	 * @return true, if successful
	 */
	public boolean existsImagePath(String imagePath);

	/**
	 * Returns all the images from the DB.
	 * 
	 * @return the persisted images
	 */
	public Iterable<DefaultImageEntity> getAllImages();

	/**
	 * Returns a paginated collection of images.
	 * 
	 * @param page pagination data
	 * @return a paginated collection of images
	 */
	public Iterable<DefaultImageEntity> getImages(final Pageable page);

}
