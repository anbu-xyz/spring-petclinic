package uk.anbu.spring.sample.petclinic.ui.system;

import gg.jte.TemplateEngine;
import gg.jte.TemplateOutput;
import gg.jte.output.StringOutput;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;

@Controller
@RequiredArgsConstructor
public class WelcomeController {

	private final TemplateEngine templateEngine;

    @GetMapping("/")
    public ResponseEntity<String> welcome() {
		String templateName = "welcome";
		var model = Map.<String, Object>of("welcomeMessage", "Welcome to the Spring PetClinic!");

		return jteTemplate(templateEngine, templateName, model);
	}

	public static ResponseEntity<String> jteTemplate(TemplateEngine templateEngine, String templateName,
													 Map<String, Object> model) {
		TemplateOutput output = new StringOutput();
		templateEngine.render(templateName + ".jte", model, output);
		return ResponseEntity.ok()
			.contentType(org.springframework.http.MediaType.TEXT_HTML)
			.body(output.toString());
	}

}
