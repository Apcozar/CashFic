package es.udc.fi.dc.fd.controller.saleAdvertisement;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.annotation.MultipartConfig;
import javax.validation.Valid;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import es.udc.fi.dc.fd.controller.ViewConstants;
import es.udc.fi.dc.fd.model.SaleAdvertisementEntity;
import es.udc.fi.dc.fd.model.form.SaleAdvertisementForm;
import es.udc.fi.dc.fd.model.persistence.DefaultImageEntity;
import es.udc.fi.dc.fd.model.persistence.DefaultSaleAdvertisementEntity;
import es.udc.fi.dc.fd.model.persistence.DefaultUserEntity;
import es.udc.fi.dc.fd.service.ImageService;
import es.udc.fi.dc.fd.service.SaleAdvertisementService;
import es.udc.fi.dc.fd.service.UserService;
import es.udc.fi.dc.fd.service.securityService.SecurityService;
import es.udc.fi.dc.fd.service.user.exceptions.UserNotFoundException;
import es.udc.fi.dc.service.exceptions.ImageAlreadyExistsException;
import es.udc.fi.dc.service.exceptions.ImageServiceException;
import es.udc.fi.dc.service.exceptions.SaleAdvertisementAlreadyExistsException;
import es.udc.fi.dc.service.exceptions.SaleAdvertisementNotFoundException;
import es.udc.fi.dc.service.exceptions.SaleAdvertisementServiceException;

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
	 * Show add sale advertisement view when de "get" petition is done.
	 *
	 * @param model the model
	 * @return the string
	 */

	@GetMapping(path = "/add")
	public String showSaleAdvertisementView(final Model model) {
		model.addAttribute("saleAdvertisementForm", new SaleAdvertisementForm());

		return SaleAdvertisementViewConstants.VIEW_SALE_ADVERTISEMENT_FORM;
	}

	/**
	 * Shows the page when the "post" petition is done.
	 *
	 * @param saleAdvertisementForm the sale add form
	 * @param bindingResult         the binding result
	 * @return the welcome view
	 * @throws SaleAdvertisementAlreadyExistsException exception
	 */

	@PostMapping(path = "/addSaleAdvertisement")
	public String addSaleAdvertisement(
			@Valid @ModelAttribute("saleAdvertisementForm") SaleAdvertisementForm saleAdvertisementForm,
			BindingResult bindingResult) throws SaleAdvertisementAlreadyExistsException {

		try {
			if (bindingResult.hasErrors()) {
				return SaleAdvertisementViewConstants.VIEW_SALE_ADVERTISEMENT_FORM;
			}

			String username = securityService.findLoggedInUsername();

			DefaultUserEntity user = userService.findByLogin(username);

			SaleAdvertisementEntity saleAdvertisement = saleAdvertisementService
					.add(new DefaultSaleAdvertisementEntity(saleAdvertisementForm.getProductTitle(),
							saleAdvertisementForm.getProductDescription(), user, LocalDateTime.now()));
			if (!saleAdvertisementForm.getImageFile().get(0).isEmpty())
				uploadImage(saleAdvertisement.getId(), saleAdvertisementForm.getImageFile());
		} catch (UserNotFoundException e) {
			return ViewConstants.VIEW_SIGNIN;
		} catch (SaleAdvertisementNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ImageServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ImageAlreadyExistsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return ViewConstants.WELCOME;
	}

	/**
	 * Update sale add.
	 *
	 * @param id                    the sale id
	 * @param saleAdvertisementForm the sale add form
	 * @param bindingResult         the binding result
	 * @param model                 the model
	 * 
	 * @return the welcome view
	 * @throws SaleAdvertisementNotFoundException exception
	 */
	@PutMapping(path = "/{id}")
	public String updateSaleAdvertisement(@PathVariable Integer id,
			@Valid @ModelAttribute("saleAdvertisementForm") SaleAdvertisementForm saleAdvertisementForm,
			BindingResult bindingResult, Model model) throws SaleAdvertisementNotFoundException {

		try {
			if (bindingResult.hasErrors()) {
				checkSaleAdvertisement(id, model);
				return SaleAdvertisementViewConstants.UPDATE_SALE_ADVERTISEMENT;
			}

			String username = securityService.findLoggedInUsername();

			DefaultUserEntity user = userService.findByLogin(username);

			saleAdvertisementService
					.update(new DefaultSaleAdvertisementEntity(id, saleAdvertisementForm.getProductTitle(),
							saleAdvertisementForm.getProductDescription(), user, LocalDateTime.now()));

			return ViewConstants.WELCOME;
		} catch (SaleAdvertisementServiceException e) {
			model.addAttribute(SaleAdvertisementViewConstants.SALE_ADVERTISEMENT_NOT_EXIST,
					SaleAdvertisementViewConstants.SALE_ADVERTISEMENT_NOT_EXIST);
			return SaleAdvertisementViewConstants.UPDATE_SALE_ADVERTISEMENT;
		} catch (UserNotFoundException e) {
			return ViewConstants.VIEW_SIGNIN;
		}
	}

	/**
	 * Check if the sale exists or not.
	 *
	 * @param id    the sale id
	 * @param model the model
	 * @throws SaleAdvertisementNotFoundException the sale advertisement not found
	 *                                            exception
	 */

	private void checkSaleAdvertisement(Integer id, Model model) throws SaleAdvertisementNotFoundException {
		saleAdvertisementService.findById(id);
		model.addAttribute(SaleAdvertisementViewConstants.SALE_ADVERTISEMENT_NOT_EXIST,
				SaleAdvertisementViewConstants.SALE_ADVERTISEMENT_NOT_EXIST);
	}

	private void uploadImage(Integer saleId, List<MultipartFile> files)
			throws SaleAdvertisementNotFoundException, ImageServiceException, ImageAlreadyExistsException {
		InputStream inputStream = null;
		File newFile = null;
		String finalFileName = null;
		String folderPath = "";
		String login = this.securityService.findLoggedInUsername();

		folderPath = context.getRealPath("/") + ViewConstants.UPLOADS_FOLDER_NAME + "/" + login;
		DefaultSaleAdvertisementEntity saleAdvertisement = saleAdvertisementService.findByIdDefault(saleId);

		for (MultipartFile file : files) {
			try {

				// Create folder if not exits
				File folder = new File(folderPath);
				if ((!folder.exists()) && (!folder.mkdirs())) {
					// TODO lanzar excepcion
				}

				// Create new file
				String originalFileName = file.getOriginalFilename();
				String fileName = FilenameUtils.removeExtension(originalFileName);
				String extension = FilenameUtils.getExtension(originalFileName);
				finalFileName = fileName + "." + extension;

				inputStream = file.getInputStream();
				newFile = new File(folderPath, finalFileName);

				// If file exists, new version
				int version = 1;
				while (newFile.exists()) {
					finalFileName = fileName + version + "." + extension;
					newFile = new File(folderPath, finalFileName);
					version++;
				}

				if (!newFile.createNewFile()) {
					// TODO lanzar excepcion
				}

			} catch (IOException e) {
				// TODO lanzar excepcion
			}

			try (OutputStream outputStream = new FileOutputStream(newFile)) {
				int read = 0;
				byte[] bytes = new byte[1024];

				while ((read = inputStream.read(bytes)) != -1) {
					outputStream.write(bytes, 0, read);
				}

			} catch (IOException e) {
				// TODO crear excepcion
			}

			DefaultImageEntity image = new DefaultImageEntity(finalFileName, finalFileName, saleAdvertisement);

			imageService.add(image);
		}

	}

}