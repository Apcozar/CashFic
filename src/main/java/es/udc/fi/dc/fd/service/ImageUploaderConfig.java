package es.udc.fi.dc.fd.service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import es.udc.fi.dc.fd.controller.saleAdvertisement.SaleAdvertisementFormController;

/**
 * The Class ImageUploaderConfig.
 */
@EnableWebMvc
@Configuration
@Import(SaleAdvertisementFormController.class)
public class ImageUploaderConfig {
	/**
	 * Multipart resolver.
	 *
	 * @return the multipart resolver
	 */
	@Bean
	public MultipartResolver multipartResolver() {
		CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver();
		commonsMultipartResolver.setDefaultEncoding("utf-8");
		commonsMultipartResolver.setMaxUploadSizePerFile(20000000);
		commonsMultipartResolver.setMaxUploadSize((long) 20000000 * 10);
		commonsMultipartResolver.setResolveLazily(false);
		return commonsMultipartResolver;
	}

}
