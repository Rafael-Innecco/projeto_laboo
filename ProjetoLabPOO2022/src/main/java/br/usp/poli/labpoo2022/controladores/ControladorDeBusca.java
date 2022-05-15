package br.usp.poli.labpoo2022.controladores;

import java.io.IOException;
import org.apache.hc.core5.http.ParseException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import br.usp.poli.labpoo2022.fluxo_de_autorizacao.ControladorDeAutorizacao;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.specification.Paging;
import se.michaelthelin.spotify.model_objects.specification.Track;
import se.michaelthelin.spotify.requests.data.search.simplified.SearchTracksRequest;

/**
 * 
 * Classe responsável por todas as funcionalidades de busca no spotify
 * <p>
 * Exemplo: Buscar por uma música na base de dados do spotify
 *
 */
@Controller
public class ControladorDeBusca {
	
	/**
	 * Método que busca por uma música a partir de uma string (Preferencialmente o nome da música)
	 * @param nomeBuscado parâmetro da busca
	 * @return Se a busca for bem-sucedida, retorna uma lista de Strings formada a partir das músicas encontradas, se não retorna null
	 */
	@GetMapping("/menu/busca-musica")
	@ResponseBody
	public static Track [] buscaMusica(
			@RequestParam(value = "nome-busca", required = true) String nomeBuscado
			)
	{
		final SearchTracksRequest requisicaoBuscaDeMusicas = ControladorDeAutorizacao.getSpotifyApi()
				.searchTracks(nomeBuscado)
				.limit(50)
				.build();
		try {
			// O próximo bloco efetivamente executa a busca e manuseia o resultado para um formato de dados conveniente
			final Paging<Track> musicasEncontradas = requisicaoBuscaDeMusicas.execute();
			
			return musicasEncontradas.getItems();
		} catch (IOException | SpotifyWebApiException | ParseException e) {
			System.out.println("Erro na busca por musica: " + e.getMessage());
		}
		
		return null;
	}
}
