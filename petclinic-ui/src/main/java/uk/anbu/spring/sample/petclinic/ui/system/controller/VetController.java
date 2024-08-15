package uk.anbu.spring.sample.petclinic.ui.system.controller;

import java.util.List;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import uk.anbu.spring.sample.petclinic.dto.VetDto;
import uk.anbu.spring.sample.petclinic.service.PetClinicServiceFacade;
import uk.anbu.spring.sample.petclinic.service.internal.entity.VetEntity;
import uk.anbu.spring.sample.petclinic.service.internal.entity.Vets;
import uk.anbu.spring.sample.petclinic.service.internal.repository.VetRepository;

@Controller
@RequiredArgsConstructor
@Slf4j
public class VetController {

    private final VetRepository vetRepository;

	private final PetClinicServiceFacade facade;

    @GetMapping("/vets.html")
    public String showVetList(@RequestParam(defaultValue = "1") int page, Model model) {
        // Here we are returning an object of type 'Vets' rather than a collection of Vet
        // objects so it is simpler for Object-Xml mapping
        Vets vets = new Vets();
        Page<VetEntity> paginated = findPaginated(page);
        vets.getVetList().addAll(paginated.toList());
        return addPaginationModel(page, paginated, model);
    }

	@GetMapping("/registerNewVet.html")
	public String registerNewVet(Model model) {
		return "registerNewVet.jte";
	}

	@PostMapping("/registerNewVet.html")
	public String registerNewVet(@Valid VetDto vetDto, BindingResult result,
								 RedirectAttributes redirectAttributes, Model model) {
		if (result.hasErrors()) {
			log.debug("There was an error in creating the vet. Errors: {}", result.getAllErrors());
			return "registerNewVet.jte";
		}

		var id = facade.registerNewVet(vetDto);
		redirectAttributes.addFlashAttribute("message", "New Vet Created");
		return "redirect:/vets/" + id;
	}

    private String addPaginationModel(int page, Page<VetEntity> paginated, Model model) {
        List<VetEntity> listVets = paginated.getContent();
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", paginated.getTotalPages());
        model.addAttribute("totalItems", paginated.getTotalElements());
        model.addAttribute("listVets", listVets);
        return "vets/vetList";
    }

    private Page<VetEntity> findPaginated(int page) {
        int pageSize = 5;
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        return vetRepository.findAll(pageable);
    }

    @GetMapping({ "/vets" })
    public @ResponseBody Vets showResourcesVetList() {
        // Here we are returning an object of type 'Vets' rather than a collection of Vet
        // objects so it is simpler for JSon/Object mapping
    	Vets vets = new Vets();
		vets.getVetList().addAll(this.vetRepository.findAll());
		return vets;
	}

}
