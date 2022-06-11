package br.usp.poli.labpoo2022.controladores;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

/**
 * 
 * Classe que controla o acesso às páginas HTML web do projeto.
 *
 */
@Controller
@Scope("singleton")
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
