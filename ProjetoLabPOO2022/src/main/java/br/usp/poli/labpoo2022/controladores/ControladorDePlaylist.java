package br.usp.poli.labpoo2022.controladores;

import java.io.IOException;
import java.rmi.ServerException;

import javax.servlet.http.HttpServletResponse;

import org.apache.hc.core5.http.ParseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

import br.usp.poli.labpoo2022.fluxo_de_autorizacao.ControladorDeAutorizacao;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.special.SnapshotResult;
import se.michaelthelin.spotify.model_objects.specification.Paging;
import se.michaelthelin.spotify.model_objects.specification.PlaylistTrack;
import se.michaelthelin.spotify.model_objects.specification.Playlist;
import se.michaelthelin.spotify.model_objects.specification.PlaylistSimplified;
import se.michaelthelin.spotify.requests.data.follow.UnfollowPlaylistRequest;
import se.michaelthelin.spotify.requests.data.playlists.AddItemsToPlaylistRequest;
import se.michaelthelin.spotify.requests.data.playlists.CreatePlaylistRequest;
import se.michaelthelin.spotify.requests.data.playlists.GetListOfCurrentUsersPlaylistsRequest;
import se.michaelthelin.spotify.requests.data.playlists.GetPlaylistsItemsRequest;
import se.michaelthelin.spotify.requests.data.playlists.RemoveItemsFromPlaylistRequest;

/**
 * Gerencia todas as funcionalidades relacionadas à playlists do usuário atual.
 * <p>
 * Exemplos: adição, remoção e listagem de músicas; criação e remoção de playlists.
 * </p>
 */
@Controller
public class ControladorDePlaylist {
	
	/**
	 * Cria uma playlist vazia.
	 * 
	 * @param nomeDaPlaylist Nome da playlist escolhida pelo usuário atual.
	 */
	@GetMapping("/menu/cria-playlist")
	@ResponseBody
	public void criaPlaylist(
			@RequestParam(value = "nome-da-playlist", required = true) String nomeDaPlaylist, HttpServletResponse resposta) throws IOException
	{
		final ControladorDoUsuarioAtual usuarioAtual = new ControladorDoUsuarioAtual();
	
		final CreatePlaylistRequest requisicaoDeCriacaoDePlaylist = ControladorDeAutorizacao.getSpotifyApi().createPlaylist(usuarioAtual.getIdDeUsuario(), nomeDaPlaylist)
			.build();
		
		try {
			final Playlist playlist = requisicaoDeCriacaoDePlaylist.execute();
			
			System.out.println("Nome da playlist: " + playlist.getName());
			resposta.sendRedirect("/menu/cria-Playlist/playlistCriada");
		} catch (IOException | SpotifyWebApiException | ParseException e) {
			System.out.println("Erro ao criar playlist: "  + e.getMessage());
		}
	}
	
	/**
	* Remove playlist de usuário.
	*
	* @param idDaPlaylistSelecionada ID da playlist a ser removida.
	*/
	@GetMapping("/menu/remove-playlist")
	//@ResponseBody
	public ResponseEntity<String> removePlaylist(@RequestParam(value = "playlist-selecionada", required = true) String idDaPlaylistSelecionada) throws ServerException
	{	
		ControladorDoUsuarioAtual usuarioAtual = new ControladorDoUsuarioAtual();
		String idDoUsuario = usuarioAtual.getIdDeUsuario();
		final se.michaelthelin.spotify.requests.data.follow.legacy.UnfollowPlaylistRequest requisicaoDeRemocaoDePlaylist = ControladorDeAutorizacao.getSpotifyApi().unfollowPlaylist(idDoUsuario, idDaPlaylistSelecionada)
			.build();
		
		try
		{
			final String stringDeResposta = requisicaoDeRemocaoDePlaylist.execute();

			System.out.println("String nula: " + stringDeResposta);

			return new ResponseEntity<>(new String ("[{\"status\": \"success\"}]"), HttpStatus.CREATED);
		} catch (IOException | SpotifyWebApiException | ParseException e)
		{
			System.out.println("Erro na remoção de playlist: " + e.getMessage());
			throw new ServerException(e.getMessage());
		}
	}
	
	/**
	 * Adiciona músicas à playlist selecionada.
	 * 
	 * @param playlistSelecionada Playlist selecionada pelo usuário.
	 * @param uris URIs das músicas selecionadas
	 */
	@GetMapping("/menu/adiciona-itens")
	@ResponseBody
	public void adicionaItensEmPlaylist (
			@RequestParam(value = "playlist-selecionada", required = true) String idDaplaylistSelecionada, 
			@RequestParam(value = "uri", required = true) String uri) 
	{
		String uri_em_array[] = {uri};
		final AddItemsToPlaylistRequest requisicaoDeAdicaoDeItens = ControladorDeAutorizacao.getSpotifyApi()
				.addItemsToPlaylist(idDaplaylistSelecionada, uri_em_array)
				.build(); 
		
		try 
		{
			final SnapshotResult snapshotResult = requisicaoDeAdicaoDeItens.execute();	
			System.out.println("ID do snapshot: " + snapshotResult.getSnapshotId());
		} catch (IOException |SpotifyWebApiException | ParseException e) {
			System.out.println("Erro: " + e.getMessage());
		}
	}

	/**
	 * Captura a lista de playlists do usuário atual.
	 * 
	 * @return Array de playlists contendo informações sobre as playlists.
	 * @throws ServerException 
	 */
	@RequestMapping("/menu/lista-playlists")
	//@ResponseBody
	public static ResponseEntity<PlaylistSimplified[]> listaPlaylists() throws ServerException
	{
		
		final GetListOfCurrentUsersPlaylistsRequest requisicaoDeListarPlaylists = ControladorDeAutorizacao.getSpotifyApi().getListOfCurrentUsersPlaylists()
	          .build();
				
		try {
			final Paging<PlaylistSimplified> listaSimplesDePlaylist = requisicaoDeListarPlaylists.execute(); // traducao correta eh?

			System.out.println("Total: " + listaSimplesDePlaylist.getTotal());
			
			return new ResponseEntity<>(listaSimplesDePlaylist.getItems(), HttpStatus.CREATED);
		}
		catch (IOException | SpotifyWebApiException | ParseException e) {
		      System.out.println("Erro ao listar playlists: " + e.getMessage());
		}

		 throw new ServerException("lista");
		
	}
	
	/**
	 * Lista os itens de uma playlist do usuário atual.
	 * 
	 * @param idDaPlaylist ID da playlist cujos itens serão listados.
	 */
	@GetMapping("/menu/lista-itens-de-playlist")
	public ResponseEntity<PlaylistTrack[]> listaItensDePlaylist(
			@RequestParam(value = "playlist-selecionada", required = true) String idDaPlaylistSelecionada)
	{
		final GetPlaylistsItemsRequest requisicaoDeListarItensDeUmaPlaylist = ControladorDeAutorizacao.getSpotifyApi().getPlaylistsItems(idDaPlaylistSelecionada)
			.build();

		try
		{
			final Paging<PlaylistTrack> listaDasMusicasDePlaylist = requisicaoDeListarItensDeUmaPlaylist.execute();	
					
			System.out.println("Total de músicas: " + listaDasMusicasDePlaylist.getTotal());
			
			return new ResponseEntity<>(listaDasMusicasDePlaylist.getItems(), HttpStatus.CREATED);
		} catch (IOException | SpotifyWebApiException | ParseException e) {
      		System.out.println("Erro com a listagem de músicas de uma playlist: " + e.getMessage());
		}
		
		return null;
	} 

	/**
	 * Remove itens de determinada playlist do usuário atual.
	 * 
	 * @param idDaPlaylistSelecionada ID da playlist cujas músicas serão removidas.
	 * @param musica String contendo URI da música a ser removida.
	 * @return 
	 * @throws ServerException 
	 */
	@GetMapping("/menu/remove-itens-de-playlist")
	public ResponseEntity<String> removeItensDePlaylist(
			@RequestParam(value = "playlist-selecionada", required = true) String idDaPlaylistSelecionada,
			@RequestParam(value = "uris", required = true) String musica) throws ServerException
	{
		final JsonArray musicas = JsonParser.parseString("[{\"uri\":\"" + musica + "\"}]").getAsJsonArray();
		
		final RemoveItemsFromPlaylistRequest requisicaoDeRemocaoDeItens = ControladorDeAutorizacao.getSpotifyApi().removeItemsFromPlaylist(idDaPlaylistSelecionada, musicas)
				.build();
		
		try
		{
			final SnapshotResult resultadoDaRequisicao = requisicaoDeRemocaoDeItens.execute();
			
			System.out.println("ID do resultado da requisição: " + resultadoDaRequisicao.getSnapshotId());
			
			return new ResponseEntity<>(new String ("[{\"status\": \"success\"}]"), HttpStatus.CREATED);
		} catch (IOException | SpotifyWebApiException | ParseException e) {
		  System.out.println("Erro na remoção de música: " + e.getMessage());
		  
		  throw new ServerException(e.getMessage());
		}
	}
}
