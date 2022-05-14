package br.usp.poli.labpoo2022.controladores;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hc.core5.http.ParseException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
	 * @return Se a busca for bem-sucedida, retorna uma array com strings formadas a partir das músicas encontradas, se não retorna null
	 */
	@RequestMapping("/busca-musica")
	public static String [] buscaMusica(
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
			
			List<String> listaDeMusicas = new ArrayList<>();
			
			for (Track musica: musicasEncontradas.getItems()) {
				listaDeMusicas.add(musica.toString());
			}
			
			System.out.println("Total: " + musicasEncontradas.getTotal());
			return listaDeMusicas.toArray(new String[listaDeMusicas.size()]);
		} catch (IOException | SpotifyWebApiException | ParseException e) {
			System.out.println("Erro na busca por musica: " + e.getMessage());
		}
		
		return null;
				
	}
}
