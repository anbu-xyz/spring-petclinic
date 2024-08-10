package uk.anbu.spring.sample.petclinic.ui.system.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import uk.anbu.spring.sample.petclinic.dto.VisitDto;
import uk.anbu.spring.sample.petclinic.service.PetClinicServiceFacade;
import uk.anbu.spring.sample.petclinic.service.internal.entity.VisitEntity;
import uk.anbu.spring.sample.petclinic.service.internal.repository.OwnerRepository;

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class VisitController {

    private final OwnerRepository owners;

	private final PetClinicServiceFacade petClinicFacade;

    @InitBinder
    public void setAllowedFields(WebDataBinder dataBinder) {
        dataBinder.setDisallowedFields("id");
    }

    @ModelAttribute("visit")
    public VisitEntity loadPetWithVisit(@PathVariable("ownerId") int ownerId, @PathVariable("petId") int petId,
										Map<String, Object> model) {
        var owner = this.owners.findById(ownerId).get();
        var pet = petClinicFacade.findPetById(petId).get();

        model.put("pet", pet);
        model.put("owner", owner);
		model.put("visits", List.of());

        VisitEntity visit = new VisitEntity();

        // pet.get().addVisit(visit); // TODO: Fix it
        return visit;
    }

    // Spring MVC calls method loadPetWithVisit(...) before initNewVisitForm is
    // called
    @GetMapping("/owners/{ownerId}/pets/{petId}/visits/new")
    public String initNewVisitForm() {
        return "pets/createOrUpdateVisitForm";
    }

    // Spring MVC calls method loadPetWithVisit(...) before processNewVisitForm is
    // called
    @PostMapping("/owners/{ownerId}/pets/{petId}/visits/new")
    public String processNewVisitForm(@PathVariable int ownerId, @PathVariable int petId, @Valid VisitDto visit,
									  BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "pets/createOrUpdateVisitForm";
        }

		petClinicFacade.addVisit(visit);
        redirectAttributes.addFlashAttribute("message", "Your visit has been booked");
        return "redirect:/owners/{ownerId}";
    }
}
