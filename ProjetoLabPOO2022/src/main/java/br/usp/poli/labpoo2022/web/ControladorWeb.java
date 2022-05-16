package br.usp.poli.labpoo2022.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.stereotype.Controller;

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

	@GetMapping(value="/menu/cria-Playlist/playlistCriada")
	public String playlistCriada() {
		return "playlistCriada";
	}

	@GetMapping(value="/menu/cria-Playlist/playlistRemovida")
	public String playlistRemovida() {
		return "playlistRemovida";
	}

	// @GetMapping(value="/menu/busca-musica/resultadoDaBusca")
	// public String resultadoDaBusca() {
	// 	return "resultadoDaBusca";
	// }
}
