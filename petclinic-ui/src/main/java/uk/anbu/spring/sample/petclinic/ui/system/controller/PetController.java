/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package uk.anbu.spring.sample.petclinic.ui.system.controller;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

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

import jakarta.validation.Valid;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import uk.anbu.spring.sample.petclinic.dto.PetDto;
import uk.anbu.spring.sample.petclinic.model.Pet;
import uk.anbu.spring.sample.petclinic.service.PetClinicService;
import uk.anbu.spring.sample.petclinic.service.internal.entity.OwnerEntity;
import uk.anbu.spring.sample.petclinic.service.internal.entity.PetEntity;
import uk.anbu.spring.sample.petclinic.service.internal.repository.OwnerRepository;

/**
 * @author Juergen Hoeller
 * @author Ken Krebs
 * @author Arjen Poutsma
 */
@Controller
@RequestMapping("/owners/{ownerId}")
@RequiredArgsConstructor
public class PetController {

    private static final String VIEWS_PETS_CREATE_OR_UPDATE_FORM = "pets/createOrUpdatePetForm";

    private final OwnerRepository owners;

	private final PetClinicService petClinicService;

    @ModelAttribute("types")
    public static Collection<Pet.PetType> populatePetTypes() {
        return Arrays.stream(Pet.Type.values())
			.map(Pet.PetType::of)
			.toList();
    }

    @ModelAttribute("owner")
    public OwnerEntity findOwner(@PathVariable("ownerId") int ownerId) {

        OwnerEntity owner = this.owners.findById(ownerId).get();
        if (owner == null) {
    		throw new IllegalArgumentException("Owner ID not found: " + ownerId);
		}
		return owner;
	}

	@ModelAttribute("pet")
	public PetEntity findPet(@PathVariable("ownerId") int ownerId,
							 @PathVariable(name = "petId", required = false) Integer petId) {

		if (petId == null) {
			return new PetEntity();
		}

		OwnerEntity owner = this.owners.findById(ownerId).get();
		if (owner == null) {
			throw new IllegalArgumentException("Owner ID not found: " + ownerId);
		}
		return owner.getPet(petId);
	}

	@InitBinder("owner")
	public void initOwnerBinder(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

	@InitBinder("pet")
	public void initPetBinder(WebDataBinder dataBinder) {
		dataBinder.setValidator(new PetValidator());
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
		var storedPetDetails = petClinicService.getPet(petDto.getOwnerId(), petDto.getName(), true);
		if (storedPetDetails != null && StringUtils.hasText(storedPetDetails.getName()) && storedPetDetails.isNew()) {
			result.rejectValue("name", "duplicate", "already exists");
		}

		LocalDate currentDate = petClinicService.currentDate();
		if (petDto.getBirthDate() != null && petDto.getBirthDate().isAfter(currentDate)) {
			result.rejectValue("birthDate", "typeMismatch.birthDate");
		}

		if (result.hasErrors()) {
			model.put("pet", petDto);
			return VIEWS_PETS_CREATE_OR_UPDATE_FORM;
		}

		petClinicService.registerNewPet(petDto);
		redirectAttributes.addFlashAttribute("message", "New Pet has been Added");
		return "redirect:/owners/{ownerId}";
	}

	@GetMapping("/pets/{petId}/edit")
	public String initUpdateForm(OwnerEntity owner, @PathVariable("petId") int petId, ModelMap model,
								 RedirectAttributes redirectAttributes) {
		PetEntity pet = owner.getPet(petId);
		model.put("pet", pet);
		return VIEWS_PETS_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping("/pets/{petId}/edit")
	public String processUpdateForm(@Valid PetEntity pet, BindingResult result, OwnerEntity owner, ModelMap model,
									RedirectAttributes redirectAttributes) {

		String petName = pet.getName();

		// checking if the pet name already exist for the owner
		if (StringUtils.hasText(petName)) {
			PetEntity existingPet = owner.getPet(petName.toLowerCase(), false);
			if (existingPet != null && existingPet.getEid() != pet.getEid()) {
				result.rejectValue("name", "duplicate", "already exists");
			}
		}

		LocalDate currentDate = LocalDate.now();
		if (pet.getBirthDate() != null && pet.getBirthDate().isAfter(currentDate)) {
			result.rejectValue("birthDate", "typeMismatch.birthDate");
		}

		if (result.hasErrors()) {
			model.put("pet", pet);
			return VIEWS_PETS_CREATE_OR_UPDATE_FORM;
		}

		owner.addPet(pet);
		this.owners.save(owner);
		redirectAttributes.addFlashAttribute("message", "Pet details has been edited");
		return "redirect:/owners/{ownerId}";
	}

}
