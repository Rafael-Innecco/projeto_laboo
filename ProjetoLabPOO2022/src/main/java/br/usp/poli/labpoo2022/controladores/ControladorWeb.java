package br.usp.poli.labpoo2022.controladores;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.stereotype.Controller;

/**
 * 
 * Classe que controla o acesso às páginas HTML web do projeto.
 *
 */
@Controller
public class ControladorWeb {
	
	@GetMapping(value="/")
	public String home() {
		return "home";
	}
	
	@GetMapping(value="/menu")
	public String menu() {
		return "menu";
	}
}
