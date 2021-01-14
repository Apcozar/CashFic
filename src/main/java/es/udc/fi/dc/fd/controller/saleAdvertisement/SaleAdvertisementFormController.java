package es.udc.fi.dc.fd.controller.saleAdvertisement;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import es.udc.fi.dc.fd.controller.ViewConstants;
import es.udc.fi.dc.fd.controller.saleAdvertisement.exceptions.InternalServerErrorException;
import es.udc.fi.dc.fd.model.SaleAdvertisementEntity;
import es.udc.fi.dc.fd.model.form.SaleAdvertisementForm;
import es.udc.fi.dc.fd.model.persistence.DefaultImageEntity;
import es.udc.fi.dc.fd.model.persistence.DefaultSaleAdvertisementEntity;
import es.udc.fi.dc.fd.model.persistence.DefaultUserEntity;
import es.udc.fi.dc.fd.service.ImageService;
import es.udc.fi.dc.fd.service.SaleAdvertisementService;
import es.udc.fi.dc.fd.service.UserService;
import es.udc.fi.dc.fd.service.exceptions.ImageAlreadyExistsException;
import es.udc.fi.dc.fd.service.exceptions.SaleAdvertisementAlreadyExistsException;
import es.udc.fi.dc.fd.service.exceptions.SaleAdvertisementNotFoundException;
import es.udc.fi.dc.fd.service.securityService.SecurityService;
import es.udc.fi.dc.fd.service.user.exceptions.UserNotFoundException;

/**
 * The Class SaleAdvertisementFormController.
 */
@Controller
@RequestMapping("/saleAdvertisement")
@MultipartConfig
public class SaleAdvertisementFormController {

	/** The sale add service. */
	private SaleAdvertisementService saleAdvertisementService;

	/** The security service. */
	private SecurityService securityService;

	/** The user service. */
	private UserService userService;

	/** The image service. */
	private ImageService imageService;

	/** The context. */
	private ServletContext context;

	/**
	 * Instantiates a new sale add controller.
	 *
	 * @param saleAdvertisementService the sale advertisement service
	 * @param securityService          the security service
	 * @param userService              the user service
	 * @param context                  the context
	 * @param imageService             the image service
	 */
	@Autowired
	public SaleAdvertisementFormController(final SaleAdvertisementService saleAdvertisementService,
			final SecurityService securityService, final UserService userService, final ServletContext context,
			final ImageService imageService) {
		super();
		this.saleAdvertisementService = checkNotNull(saleAdvertisementService, ViewConstants.NULL_POINTER);
		this.securityService = checkNotNull(securityService, ViewConstants.NULL_POINTER);
		this.userService = checkNotNull(userService, ViewConstants.NULL_POINTER);
		this.imageService = checkNotNull(imageService, ViewConstants.NULL_POINTER);
		this.context = checkNotNull(context, ViewConstants.NULL_POINTER);
	}

	/**
	 * Show add sale advertisement view when the "get" petition is done.
	 *
	 * @param model the model
	 * @return the string
	 */

	@GetMapping(path = "/add")
	public String showSaleAdvertisementView(final Model model) {
		model.addAttribute(SaleAdvertisementViewConstants.SALE_ADVERTISEMENT_FORM, new SaleAdvertisementForm());

		return SaleAdvertisementViewConstants.VIEW_SALE_ADVERTISEMENT_FORM;
	}

	/**
	 * Shows the page when the "post" petition is done.
	 *
	 * @param saleAdvertisementForm the sale add form
	 * @param bindingResult         the binding result
	 * @param model                 the model
	 * @return the view of the add
	 * @throws SaleAdvertisementAlreadyExistsException exception
	 */

	@PostMapping(path = "/addSaleAdvertisement")
	public String addSaleAdvertisement(
			@Valid @ModelAttribute(SaleAdvertisementViewConstants.SALE_ADVERTISEMENT_FORM) SaleAdvertisementForm saleAdvertisementForm,
			BindingResult bindingResult, Model model, final HttpServletResponse response)
			throws SaleAdvertisementAlreadyExistsException {

		try {
			if (bindingResult.hasErrors()) {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				return SaleAdvertisementViewConstants.VIEW_SALE_ADVERTISEMENT_FORM;
			}

			String username = securityService.findLoggedInUsername();

			DefaultUserEntity user = userService.findByLogin(username);

			double price = Double.parseDouble(saleAdvertisementForm.getPrice());

			DefaultSaleAdvertisementEntity defaultSaleAdvertisement = new DefaultSaleAdvertisementEntity(
					saleAdvertisementForm.getProductTitle(), saleAdvertisementForm.getProductDescription(), user);

			defaultSaleAdvertisement.setPrice(BigDecimal.valueOf(price));

			SaleAdvertisementEntity saleAdvertisement = saleAdvertisementService.add(defaultSaleAdvertisement);

			if (saleAdvertisementForm.getImageFile() != null) {
				if (!saleAdvertisementForm.getImageFile().get(0).isEmpty())
					uploadImages(saleAdvertisement.getId(), saleAdvertisementForm.getImageFile(), model);
			}
			return "redirect:" + "/saleAdvertisement/" + saleAdvertisement.getId();

		} catch (UserNotFoundException e) {
			return ViewConstants.VIEW_SIGNIN;
		} catch (SaleAdvertisementNotFoundException e) {
			return ViewConstants.WELCOME;
		}
	}

	

	/**
	 * Upload images into the sale.
	 *
	 * @param saleId the sale id
	 * @param files  the images to upload
	 * @param model  the model
	 * @throws SaleAdvertisementNotFoundException the sale advertisement not found
	 *                                            exception
	 */
	private void uploadImages(Integer saleId, List<MultipartFile> files, Model model)
			throws SaleAdvertisementNotFoundException {
		DefaultSaleAdvertisementEntity saleAdvertisement = (DefaultSaleAdvertisementEntity) saleAdvertisementService
				.findById(saleId);
		List<String> imageError = new ArrayList<>();

		for (MultipartFile file : files) {
			try {
				uploadImage(saleAdvertisement, file);
			} catch (InternalServerErrorException | DataIntegrityViolationException e) {
				imageError.add(file.getOriginalFilename());
			}
		}

		if (!imageError.isEmpty()) {
			model.addAttribute(SaleAdvertisementViewConstants.UPLOAD_IMAGE_ERROR, imageError);
		}
	}

	/**
	 * Upload one image into the saleAdvertisement.
	 *
	 * @param saleAdvertisement the sale advertisement
	 * @param file              the image
	 * @throws InternalServerErrorException the internal server error exception
	 */
	private void uploadImage(DefaultSaleAdvertisementEntity saleAdvertisement, MultipartFile file)
			throws InternalServerErrorException {

		try {
			final String slash = "/";
			InputStream inputStream = file.getInputStream();

			openFolder(getUserFolderAbsolutePath());

			File newFile = createFile(file);

			writeInputStreamIntoFile(inputStream, newFile);

			String finalFileName = newFile.getName();
			String filePath = getUserFolderRelativePath() + slash + finalFileName;

			imageService.add(new DefaultImageEntity(filePath, finalFileName, saleAdvertisement));
		} catch (IOException | InternalServerErrorException | ImageAlreadyExistsException e) {
			throw new InternalServerErrorException();
		}
	}

	/**
	 * Open the folder or create it if it does not exist.
	 *
	 * @param folderPath the folder path
	 * @return the file
	 * @throws InternalServerErrorException the internal server error exception if
	 *                                      can't open or create the folder
	 */
	private File openFolder(String folderPath) throws InternalServerErrorException {
		try {
			File folder = new File(folderPath);

			if ((!folder.exists()) && (!folder.mkdirs())) {
				throw new InternalServerErrorException();
			}

			return folder;
		} catch (SecurityException e) {
			throw new InternalServerErrorException();
		}
	}

	/**
	 * Creates the file.
	 *
	 * @param uploadFile the upload file
	 * @return the file
	 * @throws InternalServerErrorException the internal server error exception
	 */
	private File createFile(MultipartFile uploadFile) throws InternalServerErrorException {
		try {
			String folderPath = getUserFolderAbsolutePath();
			String originalFileName = uploadFile.getOriginalFilename();
			String fileName = FilenameUtils.removeExtension(originalFileName);
			String extension = FilenameUtils.getExtension(originalFileName);

			String finalFileName = fileName + "." + extension;
			final String slash = "/";

			File newFile = new File(folderPath, finalFileName);
			String filePath = getUserFolderRelativePath() + slash + finalFileName;

			// If file exists, new version
			int version = 1;
			while (newFile.exists() || imageService.existsImagePath(filePath)) {
				finalFileName = fileName + version + "." + extension;
				newFile = new File(folderPath, finalFileName);
				version++;
				filePath = getUserFolderRelativePath() + slash + finalFileName;
			}

			if (!newFile.createNewFile()) {
				throw new InternalServerErrorException();
			}

			return newFile;
		} catch (IOException | SecurityException e) {
			throw new InternalServerErrorException();
		}
	}

	/**
	 * Write input stream into file.
	 *
	 * @param inputStream the input stream
	 * @param file        the file
	 * @throws InternalServerErrorException the internal server error exception
	 */
	private void writeInputStreamIntoFile(InputStream inputStream, File file) throws InternalServerErrorException {
		try (OutputStream outputStream = new FileOutputStream(file)) {
			int read = 0;
			byte[] bytes = new byte[1024];

			while ((read = inputStream.read(bytes)) != -1) {
				outputStream.write(bytes, 0, read);
			}

		} catch (IOException e) {
			throw new InternalServerErrorException();
		}
	}

	/**
	 * Gets the user folder absolute path.
	 *
	 * @return the user folder absolute path
	 */
	private String getUserFolderAbsolutePath() {
		String login = this.securityService.findLoggedInUsername();

		return context.getRealPath("/") + ViewConstants.UPLOADS_FOLDER_NAME + "/" + login;
	}

	/**
	 * Gets the user folder relative path.
	 *
	 * @return the user folder relative path
	 */
	private String getUserFolderRelativePath() {
		String login = this.securityService.findLoggedInUsername();

		return ViewConstants.UPLOADS_FOLDER_NAME + "/" + login;
	}

}