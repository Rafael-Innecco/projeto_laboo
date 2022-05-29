package br.usp.poli.labpoo2022.controladores;

import java.io.IOException;
import java.rmi.ServerException;
import org.apache.hc.core5.http.ParseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;

import br.usp.poli.labpoo2022.fluxo_de_autorizacao.ControladorDeAutorizacao;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.special.SearchResult;
import se.michaelthelin.spotify.model_objects.specification.AlbumSimplified;
import se.michaelthelin.spotify.model_objects.specification.Artist;
import se.michaelthelin.spotify.model_objects.specification.ArtistSimplified;
import se.michaelthelin.spotify.model_objects.specification.Paging;
import se.michaelthelin.spotify.model_objects.specification.PlaylistSimplified;
import se.michaelthelin.spotify.model_objects.specification.Track;
import se.michaelthelin.spotify.requests.data.search.SearchItemRequest;
import se.michaelthelin.spotify.requests.data.search.simplified.SearchAlbumsRequest;
import se.michaelthelin.spotify.requests.data.search.simplified.SearchArtistsRequest;
import se.michaelthelin.spotify.requests.data.search.simplified.SearchPlaylistsRequest;
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
	
	private Track[] buscaMusicaInterno(String nomeBuscado, int offset) {
		final SearchTracksRequest requisicaoBuscaDeMusicas = ControladorDeAutorizacao.getSpotifyApi()
				.searchTracks(nomeBuscado)
				.limit(50)
				.offset(offset)
				.build();
		try {
			// O próximo bloco efetivamente executa a busca e manuseia o resultado para um formato de dados conveniente
			final Paging<Track> musicasEncontradas = requisicaoBuscaDeMusicas.execute();

			System.out.println("buscando...");
			//return musicasEncontradas.getItems();
			return musicasEncontradas.getItems();
		} 
		catch (IOException | SpotifyWebApiException | ParseException e) {
			System.out.println("Erro na busca por musica: " + e.getMessage());
		}
		
		return null;
	}
/*
	public ResponseEntity<Track[]> buscaMusicaPorParametro(String nomeBuscado, String[] filtros) throws ServerException{
		
		String tipos = "track"; // Variável inicializada dessa forma para evitar que sjam feitas buscas seu uma categoria definida, caso não seja informado o tipo de elemento que deve ser retornado será considerado apenas músicas
		
		for (String requisito : filtros)
			if(requisito != "track")
				tipos += "," + requisito;
		
		SearchItemRequest requisicaoDeBusca = ControladorDeAutorizacao.getSpotifyApi()
				.searchItem(nomeBuscado, tipos)
				.limit(50)
				.build();
		
		try {
			final SearchResult resultadoDaBuscaInicial = requisicaoDeBusca.execute();
			
			final Artist [] artistasAceitos = resultadoDaBuscaInicial.getArtists().getItems();
			final PlaylistSimplified [] playlistsAceitas = resultadoDaBuscaInicial.getPlaylists().getItems();
			final AlbumSimplified [] albumsAceitos = resultadoDaBuscaInicial.getAlbums().getItems();
			
			Track [] tracksEncontrados = resultadoDaBuscaInicial.getTracks().getItems();
			Track [] musicasAceitas = new Track [50];
			int numeroAceito = 0;
			boolean aceitarMusica = false;
			
			while (numeroAceito < 50) {
				for (Track musica : tracksEncontrados) {
					for (ArtistSimplified artista : musica.getArtists())
						for (Artist artistaEncontrado : artistasAceitos)
							if (artistaEncontrado.getName() == artista.getName())
								aceitarMusica = true;
					for (AlbumSimplified albumEncontrado : albumsAceitos)
						if (albumEncontrado.getName() == musica.getAlbum().getName())
							aceitarMusica = true;
					if (aceitarMusica == true) {
						musicasAceitas[numeroAceito] = musica;
						numeroAceito += 1;
					}
				}
				
				if (numeroAceito < 50)
					tracksEncontrados = buscaMusicaInterno(nomeBuscado, numeroAceito);
				
			}
			
			return new ResponseEntity<>(musicasAceitas, HttpStatus.CREATED);
			
		} catch (IOException | SpotifyWebApiException | ParseException e) {
			System.out.println("Erro na busca por musica: " + e.getMessage());
		}
		
		throw new ServerException(nomeBuscado);	
	}
	*/
	
	/**
	 * Método que busca por uma música a partir de uma string (Preferencialmente o nome da música)
	 * @param nomeBuscado parâmetro da busca
	 * @return Se a busca for bem-sucedida, retorna uma array das músicas encontradas, se não retorna, null
	 * @throws ServerException 
	 */
	@RequestMapping("/menu/busca-musica")
	public ResponseEntity<Track[]> buscaMusicaPorNome(@RequestParam(value = "nome-busca", required = true) String nomeBuscado) throws ServerException
	{
		final SearchTracksRequest requisicaoBuscaDeMusicas = ControladorDeAutorizacao.getSpotifyApi()
				.searchTracks(nomeBuscado)
				.limit(50)
				.build();
		try {
			// O próximo bloco efetivamente executa a busca e manuseia o resultado para um formato de dados conveniente
			final Paging<Track> musicasEncontradas = requisicaoBuscaDeMusicas.execute();

			System.out.println("buscando...");
			//return musicasEncontradas.getItems();
			return new ResponseEntity<>(musicasEncontradas.getItems(), HttpStatus.CREATED);
		} 
		catch (IOException | SpotifyWebApiException | ParseException e) {
			System.out.println("Erro na busca por musica: " + e.getMessage());
		}
		
		//return null;
		throw new ServerException(nomeBuscado);	
	}
	
	@RequestMapping("/menu/busca-artista")
	public ResponseEntity<Artist[]> buscaArtista(@RequestParam(value  ="nome-artista", required = true) String nomeArtista) throws ServerException
	{
		final SearchArtistsRequest requisicaDeBuscaArtista = ControladorDeAutorizacao.getSpotifyApi()
				.searchArtists(nomeArtista)
				.limit(50)
				.build();
		
		try {
			final Paging<Artist> artistasEncontrados = requisicaDeBuscaArtista.execute();
			
			System.out.println("buscando... ");
			
			return new ResponseEntity<>(artistasEncontrados.getItems(), HttpStatus.CREATED);
		}
		catch (IOException | SpotifyWebApiException | ParseException e) {
			System.out.println("Erro na busca por artista: " + e.getMessage());
		}
		
		throw new ServerException(nomeArtista);
	}
	
	@RequestMapping("/menu/busca-playlist")
	public ResponseEntity<PlaylistSimplified[]> buscaPlaylist(@RequestParam(value = "nome-playlist", required = true) String nomePlaylist) throws ServerException
	{
		final SearchPlaylistsRequest requisicaoDeBuscaPlaylist = ControladorDeAutorizacao.getSpotifyApi()
				.searchPlaylists(nomePlaylist)
				.limit(50)
				.build();
		
		try {
			final Paging<PlaylistSimplified> playlistsEncontradas = requisicaoDeBuscaPlaylist.execute();
			
			System.out.println("buscando.. ");
			
			return new ResponseEntity<>(playlistsEncontradas.getItems(), HttpStatus.CREATED);
		}
		catch (IOException | SpotifyWebApiException | ParseException e) {
			System.out.println("Erro na busca por playlist: " + e.getMessage());
		}
		
		throw new ServerException(nomePlaylist);
	}
	
	@RequestMapping("/menu/busca-albums")
	public ResponseEntity<AlbumSimplified[]> buscaAlbum(@RequestParam(value = "nome-album", required = true) String nomeAlbum) throws ServerException
	{
		final SearchAlbumsRequest requisicaoDeBuscaAlbum = ControladorDeAutorizacao.getSpotifyApi()
				.searchAlbums(nomeAlbum)
				.limit(50)
				.build();
		
		try {
			final Paging<AlbumSimplified> albumsEncontrados = requisicaoDeBuscaAlbum.execute();
			
			System.out.println("buscando... ");
			
			return new ResponseEntity<>(albumsEncontrados.getItems(), HttpStatus.CREATED);
		}
		catch (IOException | SpotifyWebApiException | ParseException e) {
			System.out.println("Erro na busca por album: " + e.getMessage());
		}
	
		throw new ServerException(nomeAlbum);
	}
}
