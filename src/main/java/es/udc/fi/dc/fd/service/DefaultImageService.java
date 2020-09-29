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
import es.udc.fi.dc.fd.repository.ImageRepository;

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
     * Constructs an image service with the specified repository.
     *
     * @param repository
     *            the repository for the image instances
     */
    @Autowired
    public DefaultImageService(
            final ImageRepository repository) {
        super();

        imageRepository = checkNotNull(repository,
                "Received a null pointer as repository");
    }

    @Override
    public final ImageEntity add(final DefaultImageEntity image) {
        return imageRepository.save(image);
    }

    /**
     * Returns an image with the given id.
     * <p>
     * If no instance exists with that id then a image with a negative id is
     * returned.
     *
     * @param identifier
     *            identifier of the image to find
     * @return the image for the given id
     */
    @Override
    public final ImageEntity findById(final Integer identifier) {
        final ImageEntity image;

        checkNotNull(identifier, "Received a null pointer as identifier");

        if (imageRepository.existsById(identifier)) {
            image = imageRepository.getOne(identifier);
        } else {
            image = new DefaultImageEntity();
        }

        return image;
    }

    @Override
    public final Iterable<DefaultImageEntity> getAllImages() {
        return imageRepository.findAll();
    }

    @Override
    public final Iterable<DefaultImageEntity>
            getImages(final Pageable page) {
        return imageRepository.findAll(page);
    }

    @Override
    public final void remove(final DefaultImageEntity image) {
        imageRepository.delete(image);
    }

}
