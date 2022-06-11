package br.usp.poli.labpoo2022.controladores;

import java.io.IOException;
import java.rmi.ServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.usp.poli.labpoo2022.servicos.ServicoDeBusca;

import org.springframework.web.bind.annotation.RequestMapping;

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
	 * Método que busca por uma música a partir de uma string (Preferencialmente o nome da música)
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

			return new ResponseEntity<>(servicoDeBusca.buscaMusicaPadrao(nomeBuscado), HttpStatus.CREATED);
		} 
		catch (IOException e) {
			System.out.println("Erro na busca por musica: " + e.getMessage());
		}
		
		throw new ServerException(nomeBuscado);	
	}
	
	/**
	 * Método que busca por artistas a partir de uma string de referência
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
	 * Método que realiza uma busca por playlists públca
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
	 * Método que realiza a busca por um álbum a partir de uma string de referância
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
	 * Método que busca por uma música a partir de alguns parâmetros
	 * @param nomeMusica Caso desejado, nome da música
	 * @param nomeArtista Caso desejado, nome do artista
	 * @param nomeAlbum Caso desejado, nome do álbum
	 * @return  Lista de Tracks organizadas de maneira que pode ser manipulada pelo front-end
	 * @throws ServerException
	 */
	public ResponseEntity<Track[]> buscaMusicaPorParametro(@RequestParam(value = "nome-musica", required = false, defaultValue = "") String nomeMusica,
			@RequestParam(value = "nome-artista", required = false, defaultValue = "") String nomeArtista,
			@RequestParam(value  = "nome-album", required = false, defaultValue = "") String nomeAlbum) 
					throws ServerException
	{
		String resultado = "";
		resultado += (nomeMusica == "" ? "": "track:" + nomeMusica);
		resultado += (nomeArtista == "" ? "" : " artist:" + nomeArtista);
		resultado += (nomeAlbum == "" ? "" : " album:" + nomeAlbum);
		
		try {
			return new ResponseEntity<>(servicoDeBusca.buscaMusicaPorParametro(resultado), HttpStatus.CREATED);
		} catch (ServerException e)
		{
			System.out.println("Erro na busca");
			throw new ServerException(e.getMessage());
		}
	}
}
