package br.usp.poli.labpoo2022.controladores;

import java.io.IOException;
import java.rmi.ServerException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.usp.poli.labpoo2022.servicos.ServicoDeBusca;

import org.springframework.web.bind.annotation.RequestMapping;

import se.michaelthelin.spotify.enums.Modality;
import se.michaelthelin.spotify.model_objects.specification.AlbumSimplified;
import se.michaelthelin.spotify.model_objects.specification.Artist;
import se.michaelthelin.spotify.model_objects.specification.PlaylistSimplified;
import se.michaelthelin.spotify.model_objects.specification.Track;

/**
 * 
 * Classe responsável por todas as funcionalidades de busca no spotify
 * <p>
 * Exemplo: Buscar por uma música na base de dados do spotify
 *
 */
@RestController
@Scope("singleton")
@RequestMapping("/menu")
public class ControladorDeBusca{
	
	@Autowired
    private ServicoDeBusca servicoDeBusca;
	
	/**
	 * A API do spotify retorna no máximo 50 músicas a cada busca
	 */
	private final int maximoPossivelDeMusicasBuscadas = 5;

	/**
	 * Método que requisita a busca por uma música a partir de uma string (Preferencialmente o nome da música) para o serviço responsável
	 * @param nomeBuscado parâmetro da busca
	 * @return Se a busca for bem-sucedida, retorna as músicas encontradas
	 * @throws ServerException 
	 */
	@RequestMapping("/busca-musica")
	public ResponseEntity<Track[]> buscaMusicaPadrao(
			@RequestParam(value = "nome-busca", required = true) String nomeBuscado) 
			throws ServerException
	{
		try {
			// O próximo bloco efetivamente executa a busca e manuseia o resultado para um formato de dados conveniente

			return new ResponseEntity<>(servicoDeBusca.buscaMusicaPadrao(nomeBuscado, 0), HttpStatus.CREATED);
		} 
		catch (IOException e) {
			System.out.println("Erro na busca por musica: " + e.getMessage());
		}
		
		throw new ServerException(nomeBuscado);	
	}
	
	/**
	 * Método que requisita a  busca por artistas a partir de uma string de referência para o serviço de busca
	 * @param nomeArtista
	 * @return Lista de Artistas organizadas de maneira que pode ser manipulada pelo front-end
	 * @throws ServerException
	 */
	@RequestMapping("/busca-artista")
	public ResponseEntity<Artist[]> buscaArtista(
			@RequestParam(value  ="nome-artista", required = true) String nomeArtista) 
			throws ServerException
	{
		try {
			return new ResponseEntity<>(servicoDeBusca.buscaArtista(nomeArtista), HttpStatus.CREATED);
		}
		catch (IOException e) {
			System.out.println("Erro na busca por artista: " + e.getMessage());
		}
		
		throw new ServerException(nomeArtista);
	}
	
	/**
	 * Método que requisita a busca por playlists públca para o serviço de busca
	 * @param nomePlaylist
	 * @return Lista de Playlists organizadas de maneira que pode ser manipulada pelo front-end
	 * @throws ServerException
	 */
	@RequestMapping("/busca-playlist")
	public ResponseEntity<PlaylistSimplified[]> buscaPlaylist(@RequestParam(value = "nome-playlist", required = true) String nomePlaylist) throws ServerException
	{
		try {
			return new ResponseEntity<>(servicoDeBusca.buscaPlaylist(nomePlaylist), HttpStatus.CREATED);
		}
		catch (IOException e) {
			System.out.println("Erro na busca por playlist: " + e.getMessage());
		}
		
		throw new ServerException(nomePlaylist);
	}
	
	/**
	 * Método que requisita a busca por um álbum a partir de uma string de referância para o serviço responsável
	 * @param nomeAlbum
	 * @return Lista de Álbums organizadas de maneira que pode ser manipulada pelo front-end
	 * @throws ServerException
	 */
	@RequestMapping("/busca-albums")
	public ResponseEntity<AlbumSimplified[]> buscaAlbum(
			@RequestParam(value = "nome-album", required = true) String nomeAlbum) 
			throws ServerException
	{
		try {
			return new ResponseEntity<>(servicoDeBusca.buscaAlbum(nomeAlbum), HttpStatus.CREATED);
		}
		catch (IOException e) {
			System.out.println("Erro na busca por album: " + e.getMessage());
		}
	
		throw new ServerException(nomeAlbum);
	}
	
	/**
	 * Método que manipula os parâmetros recebidos para formar uma string e requisita uma busca para o serviço de busca
	 * @param nomeMusica Caso desejado, nome da música
	 * @param nomeArtista Caso desejado, nome do artista
	 * @param nomeAlbum Caso desejado, nome do álbum
	 * @return  Lista de Tracks organizadas de maneira que pode ser manipulada pelo front-end
	 * @throws ServerException
	 */
	@RequestMapping("/busca-musica-por-parametro")
	public ResponseEntity<Track[]> buscaMusicaPorParametro(@RequestParam(value = "nome-musica-criterio", required = false, defaultValue = "") String nomeMusica,
			@RequestParam(value = "nome-artista-criterio", required = false, defaultValue = "") String nomeArtista,
			@RequestParam(value  = "nome-album-criterio", required = false, defaultValue = "") String nomeAlbum) 
					throws ServerException
	{
		String resultado = "";
		resultado += (nomeMusica.length() == 0 ? "" : ("track:" + nomeMusica));
		resultado += (nomeArtista.length() == 0 ? "" : (" artist:" + nomeArtista));
		resultado += (nomeAlbum.length() == 0 ? "" : (" album:" + nomeAlbum));
		
		try {
			return new ResponseEntity<>(servicoDeBusca.buscaMusicaPorParametro(resultado), HttpStatus.CREATED);
		} catch (ServerException e)
		{
			System.out.println("Erro na busca");
			throw e;
		}
	}
	
	@RequestMapping("/busca-musica-por-filtro")
	public ResponseEntity<Track[]> buscaMusicaPorFiltro(@RequestParam(value = "nome-busca", required = false) String nomeBuscado,
			@RequestParam(value = "tonalidade", required = false) Integer tonalidade,
			@RequestParam(value = "modo", required = false) Integer modo,
			@RequestParam(value = "formula-de-compasso", required = false) Integer formulaDeCompasso) throws ServerException
	{
		List<Track> resultadoDaBusca = new ArrayList<>();
		
		try {
			while(resultadoDaBusca.size() < this.maximoPossivelDeMusicasBuscadas)
			{
				
				resultadoDaBusca.addAll(Arrays.asList(servicoDeBusca.buscaMusicaPorFiltro(nomeBuscado, tonalidade, Modality.keyOf((modo != null ? modo.intValue() : 0)), formulaDeCompasso, resultadoDaBusca.size())));
			}

			return new ResponseEntity<>(resultadoDaBusca.toArray(new Track[resultadoDaBusca.size()]), HttpStatus.CREATED);
		} catch (ServerException e)
		{
			System.out.println("Erro na busca");
			throw e;
		}		
	}
}
