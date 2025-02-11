package uk.anbu.spring.sample.petclinic.ui.system.controller;

import gg.jte.TemplateEngine;
import gg.jte.TemplateOutput;
import gg.jte.output.StringOutput;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.junit.Ignore;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import uk.anbu.spring.sample.petclinic.dto.OwnerDto;
import uk.anbu.spring.sample.petclinic.service.PetClinicServiceFacade;
import uk.anbu.spring.sample.petclinic.service.internal.model.OwnerModel;
import uk.anbu.spring.sample.petclinic.service.internal.entity.OwnerEntity;
import uk.anbu.spring.sample.petclinic.ui.system.WelcomeController;

import java.util.List;
import java.util.Map;

@Controller
@Ignore
@RequiredArgsConstructor
public class OwnerController {

	private static final String VIEWS_OWNER_CREATE_OR_UPDATE_FORM = "createOrUpdateOwnerForm.jte";

	private final OwnerModel owners;

	private final PetClinicServiceFacade petClinicService;

	private final TemplateEngine templateEngine;

	@InitBinder
	public void setAllowedFields(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

	@ModelAttribute("owner")
	public OwnerDto findOwner(@PathVariable(name = "ownerId", required = false) Integer ownerId) {
		return ownerId == null ? OwnerDto.builder().build() : this.owners.findOwnerById(ownerId).get();
	}

	@GetMapping("/owners/new")
	public String initCreationForm(Map<String, Object> model) {
		OwnerEntity owner = new OwnerEntity();
		model.put("owner", owner);
		return VIEWS_OWNER_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping("/owners/new")
	public String processCreationForm(@Valid OwnerDto owner, BindingResult result, RedirectAttributes redirectAttributes) {
		if (result.hasErrors()) {
			redirectAttributes.addFlashAttribute("error", "There was an error in creating the owner.");
			return VIEWS_OWNER_CREATE_OR_UPDATE_FORM;
		}

		var id = petClinicService.registerNewOwner(owner);
		redirectAttributes.addFlashAttribute("message", "New Owner Created");
		return "redirect:/owners/" + id;
	}

	@GetMapping("/owners/find")
	public ResponseEntity<String> initFindForm() {
		return WelcomeController.jteTemplate(templateEngine, "owners/findOwners", Map.of());
	}

	@GetMapping("/owners")
	public ResponseEntity<Object> processFindForm(@RequestParam(defaultValue = "1") int page, OwnerDto owner, BindingResult result,
												  Model model) {
		// allow parameterless GET request for /owners to return all records
		if (owner.getLastName() == null) {
			owner.setLastName(""); // empty string signifies broadest possible search
		}

		// find owners by last name
		var ownersResults = findPaginatedForOwnersLastName(page, owner.getLastName());
		if (ownersResults.isEmpty()) {
			// no owners found
			result.rejectValue("lastName", "notFound", "not found");
			return ResponseEntity.status(HttpStatus.FOUND)
				.header(HttpHeaders.LOCATION, "/owners/findOwners")
				.build();
		}

		if (ownersResults.getTotalElements() == 1) {
			// 1 owner found
			owner = ownersResults.iterator().next();

			return ResponseEntity.status(HttpStatus.FOUND)
				.header(HttpHeaders.LOCATION, "/owners/" + owner.getEid())
				.build();
		}

		// multiple owners found
		return addPaginationModel(page, model, ownersResults);
	}

	private ResponseEntity<Object> addPaginationModel(int page, Model model, Page<OwnerDto> paginated) {
		List<OwnerDto> listOwners = paginated.getContent();
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", paginated.getTotalPages());
		model.addAttribute("totalItems", paginated.getTotalElements());
		model.addAttribute("listOwners", listOwners);


		TemplateOutput output = new StringOutput();
		templateEngine.render("owners/ownersList.jte", model, output);

		return ResponseEntity.ok()
			.contentType(org.springframework.http.MediaType.TEXT_HTML)
			.body(output.toString());
	}

	private Page<OwnerDto> findPaginatedForOwnersLastName(int page, String lastname) {
		int pageSize = 5;
		Pageable pageable = PageRequest.of(page - 1, pageSize);
		return petClinicService.findOwnerByLastName(lastname, pageable);
	}

	@GetMapping("/owners/{ownerId}/edit")
	public String initUpdateOwnerForm(@PathVariable("ownerId") int ownerId, Model model) {
		OwnerDto owner = this.owners.findOwnerById(ownerId).get();
		model.addAttribute(owner);
		return VIEWS_OWNER_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping("/owners/{ownerId}/edit")
	public String processUpdateOwnerForm(@Valid OwnerDto owner, BindingResult result, @PathVariable("ownerId") int ownerId,
										 RedirectAttributes redirectAttributes) {
		if (result.hasErrors()) {
			redirectAttributes.addFlashAttribute("error", "There was an error in updating the owner.");
			return VIEWS_OWNER_CREATE_OR_UPDATE_FORM;
		}

		owner.setEid(ownerId);
		petClinicService.updateOwner(owner);
		redirectAttributes.addFlashAttribute("message", "Owner Values Updated");
		return "redirect:/owners/{ownerId}";
	}

	/**
	 * Custom handler for displaying an owner.
	 *
	 * @param ownerId the ID of the owner to display
	 * @return a ModelMap with the model attributes for the view
	 */
	@GetMapping("/owners/{ownerId}")
	public ModelAndView showOwner(@PathVariable("ownerId") int ownerId) {
		ModelAndView mav = new ModelAndView("ownerDetails.jte");
		OwnerDto owner = petClinicService.findOwnerById(ownerId).get();
		mav.addObject(owner);
		return mav;
	}

}
