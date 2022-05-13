package br.usp.poli.comp.labpoo2022;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.stereotype.Controller;

@Controller
public class WebHomeController {
	@GetMapping(value="/")
	public String home() {
		return "home";
	}
	
}
