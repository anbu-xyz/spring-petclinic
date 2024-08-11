package uk.anbu.spring.sample.petclinic.ui.system.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import uk.anbu.spring.sample.petclinic.ui.system.DemoModel;

import java.util.Map;

@Controller
public class HelloController {
	@GetMapping("/hello")
	public String hello(Map<String, Object> model) {
		model.put("model", new DemoModel());
		return "hello-view";
	}
}
