package br.usp.poli.labpoo2022.controladores;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hc.core5.http.ParseException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.JsonArray;

import br.usp.poli.labpoo2022.fluxo_de_autorizacao.ControladorDeAutorizacao;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.special.SnapshotResult;
import se.michaelthelin.spotify.model_objects.specification.Paging;
import se.michaelthelin.spotify.model_objects.specification.PlaylistTrack;
import se.michaelthelin.spotify.model_objects.specification.Playlist;
import se.michaelthelin.spotify.model_objects.specification.PlaylistSimplified;
import se.michaelthelin.spotify.requests.data.playlists.AddItemsToPlaylistRequest;
import se.michaelthelin.spotify.requests.data.playlists.CreatePlaylistRequest;
import se.michaelthelin.spotify.requests.data.playlists.GetListOfCurrentUsersPlaylistsRequest;
import se.michaelthelin.spotify.requests.data.playlists.GetPlaylistsItemsRequest;
import se.michaelthelin.spotify.requests.data.playlists.RemoveItemsFromPlaylistRequest;

/**
 * Gerencia todas as funcionalidades relacionadas à playlists do usuário atual.
 * <p>
 * Exemplos: adição, remoção e listagem de músicas; criação e remoção de playlists.
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
			@RequestParam(value = "nome-da-playlist", required = true) String nomeDaPlaylist)
	{
		final ControladorDoUsuarioAtual usuarioAtual = new ControladorDoUsuarioAtual();
	
		final CreatePlaylistRequest requisicaoDeCriacaoDePlaylist = ControladorDeAutorizacao.getSpotifyApi().createPlaylist(usuarioAtual.getIdDeUsuario(), nomeDaPlaylist)
			.build();
		
		try {
			final Playlist playlist = requisicaoDeCriacaoDePlaylist.execute();
			
			System.out.println("Nome da playlist: " + playlist.getName());
		} catch (IOException | SpotifyWebApiException | ParseException e) {
			System.out.println("Erro ao criar playlist: "  + e.getMessage());
		}
	}
	
	/**
	 * Adiciona músicas à playlist selecionada.
	 * 
	 * @param playlistSelecionada Playlist selecionada pelo usuário.
	 * @param uris URIs das músicas selecionadas
	 */
	@RequestMapping("/adiciona-itens")
	public void adicionaItens (
			@RequestParam(value = "playlist-selecionada", required = true) String playlistSelecionada, 
			@RequestParam(value = "uris", required = true) String[] uris) 
	{
		final AddItemsToPlaylistRequest requisicaoDeAdicaoDeItens = ControladorDeAutorizacao.getSpotifyApi()
				.addItemsToPlaylist(playlistSelecionada, uris)
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
	 * @return Array de strings contendo informações sobre as playlists.
	 */
	@RequestMapping("/lista-playlists")
	public String[] listaPlaylists()
	{
		
		final GetListOfCurrentUsersPlaylistsRequest requisicaoDeListarPlaylists = ControladorDeAutorizacao.getSpotifyApi().getListOfCurrentUsersPlaylists()
	          .build();
				
		try {
			final Paging<PlaylistSimplified> listaSimplesDePlaylist = requisicaoDeListarPlaylists.execute(); // traducao correta eh?

			System.out.println("Total: " + listaSimplesDePlaylist.getTotal());
			
			List<String> listaDePlaylists = new ArrayList<>();
			for (PlaylistSimplified playlist : listaSimplesDePlaylist.getItems())
				listaDePlaylists.add(playlist.toString());
			
			return listaDePlaylists.toArray(new String[listaDePlaylists.size()]);
			
			 
		}
		catch (IOException | SpotifyWebApiException | ParseException e) {
		      System.out.println("Erro ao listar playlists: " + e.getMessage());
		}

		return null;
		
	}
	
	/**
	 * Lista os itens de uma playlist do usuário atual.
	 * 
	 * @param idDaPlaylist ID da playlist cujos itens serão listados.
	 */
	@RequestMapping("/lista-itens-de-playlist")
	public void listaItensDeUmaPlaylist(
			@RequestParam(value = "id-da-playlist-selecionada", required = true) String idDaPlaylist)
	{
		final GetPlaylistsItemsRequest requisicaoDeListarItensDeUmaPlaylist = ControladorDeAutorizacao.getSpotifyApi().getPlaylistsItems(idDaPlaylist)
			.build();

		try
		{
			final Paging<PlaylistTrack> listaDasMusicasDePlaylist = requisicaoDeListarItensDeUmaPlaylist.execute();	
					
			System.out.println("Total de músicas: " + listaDasMusicasDePlaylist.getTotal());
					
		} catch (IOException | SpotifyWebApiException | ParseException e) {
      		System.out.println("Erro com a listagem de músicas de uma playlist: " + e.getMessage());
		}
	} 

	/**
	 * Remove itens de determinada playlist do usuário atual.
	 * 
	 * @param idDaPlaylistSelecionada ID da playlist cujas músicas serão removidas.
	 * @param musicas JSON contendo URIs das músicas a serem removidas.
	 */
	@RequestMapping("/remove-itens-de-playlist")
	public void removeItensDePlaylist(
			@RequestParam(value = "id-da-playlist-selecionada", required = true) String idDaPlaylistSelecionada,
			@RequestParam(value = "uris", required = true) JsonArray musicas)
	{
		final RemoveItemsFromPlaylistRequest requisicaoDeRemocaoDeItens = ControladorDeAutorizacao.getSpotifyApi().removeItemsFromPlaylist(idDaPlaylistSelecionada, musicas)
				.build();
		
		try
		{
			final SnapshotResult resultadoDaRequisicao = requisicaoDeRemocaoDeItens.execute();
			
			System.out.println("ID do resultado da requisição: " + resultadoDaRequisicao.getSnapshotId());

		} catch (IOException | SpotifyWebApiException | ParseException e) {
		  System.out.println("Erro na remoção de música: " + e.getMessage());
		}
	}
}
