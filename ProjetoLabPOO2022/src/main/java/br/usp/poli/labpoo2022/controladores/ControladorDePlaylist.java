package br.usp.poli.labpoo2022.controladores;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hc.core5.http.ParseException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import br.usp.poli.labpoo2022.fluxo_de_autorizacao.ControladorDeAutorizacao;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.special.SnapshotResult;
import se.michaelthelin.spotify.model_objects.specification.Paging;
import se.michaelthelin.spotify.model_objects.specification.Playlist;
import se.michaelthelin.spotify.model_objects.specification.PlaylistSimplified;
import se.michaelthelin.spotify.requests.data.playlists.AddItemsToPlaylistRequest;
import se.michaelthelin.spotify.requests.data.playlists.CreatePlaylistRequest;
import se.michaelthelin.spotify.requests.data.playlists.GetListOfCurrentUsersPlaylistsRequest;

/**
 * Gerencia todas as funcionalidades relacionadas à playlists do usuário atual.
 * <p>
 * Exemplos: adição, remoção e listagem de músicas; criação e remoção de playlists.
 */
@Controller
public class ControladorDePlaylist {
	
	/**
	 * Cria uma playlist vazia
	 * 
	 * @param nomeDaPlaylist Nome da playlist escolhida pelo usuário atual.
	 */
	@RequestMapping("/cria-playlist")
	public void criaPlaylist(
			@RequestParam(value = "nome-da-playlist", required = true) String nomeDaPlaylist)
	{
		final ControladorDoUsuarioAtual usuarioAtual = new ControladorDoUsuarioAtual();
	
		final CreatePlaylistRequest requisicaoDeCriacaoDePlaylist = ControladorDeAutorizacao.getSpotifyApi().createPlaylist(usuarioAtual.getIdDeUsuario(), nomeDaPlaylist)
			.build();
		
		try {
			final Playlist playlist = requisicaoDeCriacaoDePlaylist.execute();
			
			System.out.println("Name: " + playlist.getName());
		} catch (IOException | SpotifyWebApiException | ParseException e) {
			System.out.println("Error: "  + e.getMessage());
		}
	}
	
	/**
	 * Adiciona músicas à playlist selecionada.
	 * 
	 * @param playlistSelecionada Playlist selecionada pelo usuário.
	 * @param uris URIs das músicas selecionadas
	 */
	@RequestMapping("/adicona-itens")
	public void adicionaItens (
			@RequestParam(value = "playlist-selecionada", required = true) String playlistSelecionada, 
			@RequestParam(value = "uris", required = true) String[] uris) 
	{
		final AddItemsToPlaylistRequest requisicaoDeAdicaoDeItens = ControladorDeAutorizacao.getSpotifyApi()
				.addItemsToPlaylist(playlistSelecionada, uris)
				.build(); //Omite a posicao
		
		try 
		{
			final SnapshotResult snapshotResult = requisicaoDeAdicaoDeItens.execute();	
			System.out.println("ID do snapshot: " + snapshotResult.getSnapshotId());
		} catch (IOException |SpotifyWebApiException | ParseException e) {
			System.out.println("Erro: " + e.getMessage());
		}
	}

	@RequestMapping("/lista-playlists")
	public String[] listaPlaylists()
	{
		
		final GetListOfCurrentUsersPlaylistsRequest requisicaoDeListarPlaylists = ControladorDeAutorizacao.getSpotifyApi().getListOfCurrentUsersPlaylists()
	          // .limit(20)
	          //.offset(0) //damos ja com limite e offset (poderia tirar)
	          .build();
				
		try {
			final Paging<PlaylistSimplified> listaSimplesDePlaylist = requisicaoDeListarPlaylists.execute(); // traducao correta eh?
			System.out.println("Total: " + listaSimplesDePlaylist.getTotal());
			
			List<String> listaDePlaylists = new ArrayList<>();;
			for (PlaylistSimplified playlist : listaSimplesDePlaylist.getItems())
				listaDePlaylists.add(playlist.toString());
			
			return (String[]) listaDePlaylists.toArray();
			 
		}
		catch (IOException | SpotifyWebApiException | ParseException e) {
		      System.out.println("Erro ao listar playlists: " + e.getMessage());
		}
		
	}
	
	public void 
}
