package br.usp.poli.labpoo2022.servicos;

import java.io.IOException;
import java.rmi.ServerException;
import org.apache.hc.core5.http.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import se.michaelthelin.spotify.enums.Modality;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.special.SearchResult;
import se.michaelthelin.spotify.model_objects.specification.AlbumSimplified;
import se.michaelthelin.spotify.model_objects.specification.Artist;
import se.michaelthelin.spotify.model_objects.specification.Paging;
import se.michaelthelin.spotify.model_objects.specification.PlaylistSimplified;
import se.michaelthelin.spotify.model_objects.specification.Track;
import se.michaelthelin.spotify.requests.data.search.SearchItemRequest;
import se.michaelthelin.spotify.requests.data.search.simplified.SearchAlbumsRequest;
import se.michaelthelin.spotify.requests.data.search.simplified.SearchArtistsRequest;
import se.michaelthelin.spotify.requests.data.search.simplified.SearchPlaylistsRequest;
import se.michaelthelin.spotify.requests.data.search.simplified.SearchTracksRequest;

/**
 * Classe que realiza as interações com a api do spotify relacionadas com as funções de busca
 *
 */
@Service
@Scope("singleton")
public class ServicoDeBusca extends ServicoBase {
	
	@Autowired
	private ServicoDeMusicas servicoDeMusicas;
	
	/**
	 * Método que acessa a api do spotify e busca por uma música
	 * @param nomeBuscado
	 * @param offset deslocamento dos resultados da busca (o método retorna 25 músicas, mas a API do Spotify retorna 50)
	 * @return Array de Tracks encontradas
	 * @throws ServerException
	 */
	public Track[] buscaMusicaPadrao (String nomeBuscado, int offset) throws ServerException
	{
		final SearchTracksRequest requisicaoDeBuscaMusica = servicoDeAutorizacao.getSpotifyApi()
				.searchTracks(nomeBuscado)
				.limit(25)
				.offset(offset)
				.build();
		
		try {
			// Tenta realizar a busca
			final Paging<Track> musicasEncontradas = requisicaoDeBuscaMusica.execute();
			
			System.out.println("Buscando musicas...");
			
			return musicasEncontradas.getItems();
		} catch (IOException | SpotifyWebApiException | ParseException e)
		{
			throw new ServerException(e.getMessage());
		}
	}
	
	/**
	 * Método que acessa a api do spotify e busca por um artista
	 * @param nomeArtista
	 * @return Array de Artists encontrados
	 * @throws ServerException
	 */
	public Artist[] buscaArtista(String nomeArtista) throws ServerException
	{
		final SearchArtistsRequest requisicaoDeBuscaArtista = servicoDeAutorizacao.getSpotifyApi()
				.searchArtists(nomeArtista)
				.limit(50)
				.build();
		
		try {
			final Paging<Artist> artistasEncontrados = requisicaoDeBuscaArtista.execute();
			
			System.out.println("Buscando artists ... ");
			
			return artistasEncontrados.getItems();
		} catch (IOException | SpotifyWebApiException | ParseException e)
		{
			throw new ServerException (e.getMessage());
		}
	}
	
	/**
	 * Método que acessa a api do spotify e busca por uma playlist pública
	 * @param nomePlaylist
	 * @return Array com Playlists públicas encontradas
	 * @throws ServerException
	 */
	public PlaylistSimplified[] buscaPlaylist(String nomePlaylist) throws ServerException
	{
		final SearchPlaylistsRequest requisicaoDeBuscaPlaylist = servicoDeAutorizacao.getSpotifyApi()
				.searchPlaylists(nomePlaylist)
				.limit(50)
				.build();
		
		try {
			// Tenta realizar a busca por playlists públicas
			final Paging<PlaylistSimplified> playlistsEncontradas = requisicaoDeBuscaPlaylist.execute();
			
			System.out.println("Buscando playlists ... ");
			
			return playlistsEncontradas.getItems();
		} catch (IOException | SpotifyWebApiException | ParseException e)
		{
			throw new ServerException (e.getMessage());
		}
	}
	
	/**
	 * Método que acessa a api do Spotify e busca por um álbum
	 * @param nomeAlbum
	 * @return Array de Albums encontrados
	 * @throws ServerException
	 */
	public AlbumSimplified[] buscaAlbum(String nomeAlbum) throws ServerException
	{
		final SearchAlbumsRequest requisicaoDeBuscaAlbum = servicoDeAutorizacao.getSpotifyApi()
				.searchAlbums(nomeAlbum)
				.limit(50)
				.build();
		
		try {
			// Tenta executar a busca
			final Paging<AlbumSimplified> albumsEncontrados = requisicaoDeBuscaAlbum.execute();
			
			System.out.println("Buscando album... ");
			
			return albumsEncontrados.getItems();
		} catch (IOException | SpotifyWebApiException | ParseException e)
		{
			throw new ServerException(e.getMessage());
		}
	}
	
	/**
	 * Método que acessa a api do spotify e realiza um busca por musica a partir de uma série de parâmetros. Ex: nome do artista
	 * @param q
	 * @return Array de Tracks que satfazem as condições escolhidas
	 * @throws ServerException
	 */
	public Track[] buscaMusicaPorParametro (String q) throws ServerException
	{
		final SearchItemRequest requisicaoDeBusca = servicoDeAutorizacao.getSpotifyApi()
				.searchItem(q, "track")
				.limit(50)
				.build();
		
		try {
			SearchResult resultados = requisicaoDeBusca.execute();
			
			System.out.println("Buscando música ... ");
			return resultados.getTracks().getItems();
		} catch (IOException | SpotifyWebApiException | ParseException e)
		{
			throw new ServerException(e.getMessage());
		}
	}
	
	public Track[] buscaMusicaPorFiltro(String nomeBuscado, Integer tonalidade, Modality modo, Integer formulaDeCompasso, int offset) throws ServerException
	{
		Track[] resultadoFiltrado = servicoDeMusicas.filtraMusicasPorTom(this.buscaMusicaPadrao(nomeBuscado, offset), tonalidade);
		resultadoFiltrado = servicoDeMusicas.filtraMusicasPorModo(resultadoFiltrado, modo);
		resultadoFiltrado = servicoDeMusicas.filtraMusicasPorCompasso(resultadoFiltrado, formulaDeCompasso);
		
		return resultadoFiltrado;
	
	}
}
