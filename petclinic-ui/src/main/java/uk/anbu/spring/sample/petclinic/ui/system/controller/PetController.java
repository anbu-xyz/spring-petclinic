package uk.anbu.spring.sample.petclinic.ui.system.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import uk.anbu.spring.sample.petclinic.dto.PetDto;
import uk.anbu.spring.sample.petclinic.model.Pet;
import uk.anbu.spring.sample.petclinic.service.PetClinicServiceFacade;
import uk.anbu.spring.sample.petclinic.service.internal.entity.OwnerEntity;
import uk.anbu.spring.sample.petclinic.service.internal.repository.OwnerRepository;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/owners/{ownerId}")
@RequiredArgsConstructor
public class PetController {

	private static final String VIEWS_PETS_CREATE_OR_UPDATE_FORM = "pets/createOrUpdatePetForm";

	private final OwnerRepository owners;

	private final PetClinicServiceFacade facade;

	@ModelAttribute("types")
	public static Collection<Pet.PetType> populatePetTypes() {
		return Arrays.stream(Pet.Type.values())
			.map(Pet.PetType::of)
			.toList();
	}

	@ModelAttribute("owner")
	public OwnerEntity findOwner(@PathVariable("ownerId") int ownerId) {

		var owner = this.owners.findById(ownerId);
		if (owner.isEmpty()) {
			throw new IllegalArgumentException("Owner ID not found: " + ownerId);
		}
		return owner.get();
	}

	@ModelAttribute("pet")
	public Optional<PetDto> findPet(@PathVariable(name = "eid", required = false) Integer petId) {
		if (petId == null) {
			return Optional.empty();
		}

		return facade.findPetById(petId);
	}

	@InitBinder("owner")
	public void initOwnerBinder(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

	@InitBinder("pet")
	public void initPetBinder(WebDataBinder dataBinder) {
//		dataBinder.setValidator(new PetValidator()); // TODO: fix this
	}

	@GetMapping("/pets/new")
	public String initCreationForm(OwnerEntity owner, ModelMap model) {
		PetDto pet = new PetDto();
		pet.setOwnerId(owner.getEid());
		model.put("pet", pet);

		var typeStrings = ((List<Pet.PetType>) model.get("types"))
			.stream()
			.map(Pet.PetType::code)
			.toList();
		model.put("types", typeStrings);
		return VIEWS_PETS_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping("/pets/new")
	public String processCreationForm(OwnerEntity owner, @Valid PetDto petDto, BindingResult result, ModelMap model,
									  RedirectAttributes redirectAttributes) {
		var storedPetDetails = facade.getPet(petDto.getOwnerId(), petDto.getName());
		if (storedPetDetails != null && StringUtils.hasText(storedPetDetails.getName()) && storedPetDetails.isNew()) {
			result.rejectValue("name", "duplicate", "already exists");
		}

		LocalDate currentDate = facade.currentDate();
		if (petDto.getBirthDate() != null && petDto.getBirthDate().isAfter(currentDate)) {
			result.rejectValue("birthDate", "typeMismatch.birthDate");
		}

		if (result.hasErrors()) {
			model.put("pet", petDto);
			return VIEWS_PETS_CREATE_OR_UPDATE_FORM;
		}

		facade.registerNewPet(petDto);
		redirectAttributes.addFlashAttribute("message", "New Pet has been Added");
		return "redirect:/owners/{ownerId}";
	}

	@GetMapping("/pets/{petId}/edit")
	public String initUpdateForm(@PathVariable("petId") int petId, ModelMap model) {
		var pet = facade.findPetById(petId).orElseThrow();
		model.put("pet", pet);
		return VIEWS_PETS_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping("/pets/{petId}/edit")
	public String processUpdateForm(@Valid PetDto petDto, @PathVariable("petId") int petId, BindingResult result, ModelMap model,
									RedirectAttributes redirectAttributes) {
		// Big lesson: Only traditional bean classes can be bound, not records.
		var pet = facade.findPetById(petId).orElseThrow();

		LocalDate currentDate = LocalDate.now();
		if (pet.getBirthDate() != null && pet.getBirthDate().isAfter(currentDate)) {
			result.rejectValue("birthDate", "typeMismatch.birthDate");
		}

		if (result.hasErrors()) {
			model.put("pet", pet);
			return VIEWS_PETS_CREATE_OR_UPDATE_FORM;
		}

		facade.updatePet(new Pet(petId, petDto.getOwnerId(), petDto.getType(), petDto.getName(), petDto.getBirthDate()));

		redirectAttributes.addFlashAttribute("message", "Pet details has been edited");
		return "redirect:/owners/{ownerId}";
	}
}
