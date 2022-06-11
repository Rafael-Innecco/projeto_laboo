package br.usp.poli.labpoo2022.servicos;

import java.io.IOException;
import java.rmi.ServerException;

import javax.servlet.http.HttpServletResponse;

import org.apache.hc.core5.http.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.special.SnapshotResult;
import se.michaelthelin.spotify.model_objects.specification.Paging;
import se.michaelthelin.spotify.model_objects.specification.PlaylistTrack;
import se.michaelthelin.spotify.model_objects.specification.Playlist;
import se.michaelthelin.spotify.model_objects.specification.PlaylistSimplified;
import se.michaelthelin.spotify.requests.data.follow.legacy.UnfollowPlaylistRequest;
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
@Service
@Scope("singleton")
public class ServicoDePlaylist extends ServicoBase{
	
	@Autowired
	private ServicoDoUsuarioAtual usuarioAtual;
	
	/**
	 * Cria uma playlist vazia.
	 * 
	 * @param nomeDaPlaylist Nome da playlist escolhida pelo usuário atual.
	 * @param resposta resposta do servlet à requisição de criação de playlist.
	 */
	public boolean criaPlaylist(String nomeDaPlaylist, HttpServletResponse resposta) throws ServerException
	{
	
		final CreatePlaylistRequest requisicaoDeCriacaoDePlaylist = servicoDeAutorizacao.getSpotifyApi().createPlaylist(usuarioAtual.getIdDeUsuario(), nomeDaPlaylist)
			.build();
		
		try {
			final Playlist playlist = requisicaoDeCriacaoDePlaylist.execute();
			
			System.out.println("Nome da playlist: " + playlist.getName());
			resposta.sendRedirect("/menu/cria-Playlist/playlistCriada");
			
			return true;
		} catch (IOException | SpotifyWebApiException | ParseException e) {
			throw new ServerException(e.getMessage());
		}
	}
	
	/**
	* Remove playlist de usuário.
	*
	* @param idDaPlaylistSelecionada ID da playlist a ser removida.
	* @return String JSON indicando sucesso na requisição de remoção.
	* @throws ServerException
	*/
	public boolean removePlaylist(String idDaPlaylistSelecionada) throws ServerException
	{	
		String idDoUsuario = usuarioAtual.getIdDeUsuario();
		final UnfollowPlaylistRequest requisicaoDeRemocaoDePlaylist = servicoDeAutorizacao.getSpotifyApi().unfollowPlaylist(idDoUsuario, idDaPlaylistSelecionada)
			.build();
		
		try
		{
			final String stringDeResposta = requisicaoDeRemocaoDePlaylist.execute();

			System.out.println("String nula: " + stringDeResposta);

			return true;
		} catch (IOException | SpotifyWebApiException | ParseException e)
		{
			throw new ServerException(e.getMessage());
		}
	}
	
	/**
	 * Adiciona músicas à playlist selecionada.
	 * 
	 * @param playlistSelecionada Playlist selecionada pelo usuário.
	 * @param uris URIs das músicas selecionadas
	 */
	public boolean adicionaItensEmPlaylist (String idDaplaylistSelecionada, String uri) throws ServerException
	{
		String uri_em_array[] = {uri};
		final AddItemsToPlaylistRequest requisicaoDeAdicaoDeItens = servicoDeAutorizacao.getSpotifyApi()
				.addItemsToPlaylist(idDaplaylistSelecionada, uri_em_array)
				.build(); 
		
		try 
		{
			final SnapshotResult snapshotResult = requisicaoDeAdicaoDeItens.execute();	
			System.out.println("ID do snapshot: " + snapshotResult.getSnapshotId());
			return true;
		} catch (IOException |SpotifyWebApiException | ParseException e) {
			throw new ServerException(e.getMessage());
		}
	}

	/**
	 * Captura a lista de playlists do usuário atual.
	 * 
	 * @return Array de playlists contendo informações sobre as playlists.
	 * @throws ServerException 
	 */
	public PlaylistSimplified[] listaPlaylists() throws ServerException
	{
		
		final GetListOfCurrentUsersPlaylistsRequest requisicaoDeListarPlaylists = servicoDeAutorizacao.getSpotifyApi().getListOfCurrentUsersPlaylists()
	          .build();
				
		try {
			final Paging<PlaylistSimplified> listaSimplesDePlaylist = requisicaoDeListarPlaylists.execute();

			System.out.println("Total: " + listaSimplesDePlaylist.getTotal());
			
			return listaSimplesDePlaylist.getItems();
		}
		catch (IOException | SpotifyWebApiException | ParseException e) {
			throw new ServerException("lista");
		}
	}
	
	/**
	 * Lista os itens de uma playlist do usuário atual.
	 * 
	 * @param idDaPlaylist ID da playlist cujos itens serão listados.
	 * @return Array de músicas de playlists
	 */
	public PlaylistTrack[] listaItensDePlaylist(String idDaPlaylistSelecionada) throws ServerException
	{
		final GetPlaylistsItemsRequest requisicaoDeListarItensDeUmaPlaylist = servicoDeAutorizacao.getSpotifyApi().getPlaylistsItems(idDaPlaylistSelecionada)
			.build();

		try
		{
			final Paging<PlaylistTrack> listaDasMusicasDePlaylist = requisicaoDeListarItensDeUmaPlaylist.execute();	
					
			System.out.println("Total de músicas: " + listaDasMusicasDePlaylist.getTotal());
			
			return listaDasMusicasDePlaylist.getItems();
		} catch (IOException | SpotifyWebApiException | ParseException e) {
      		throw new ServerException(e.getMessage());
		}
	} 

	/**
	 * Remove itens de determinada playlist do usuário atual.
	 * 
	 * @param idDaPlaylistSelecionada ID da playlist cuja música será removidas.
	 * @param musica String contendo URI da música a ser removida.
	 * @return String JSON confirmando a remoção do item
	 * @throws ServerException
	 */
	public boolean removeItensDePlaylist(String idDaPlaylistSelecionada, String musica) throws ServerException
	{
		final JsonArray musicas = JsonParser.parseString("[{\"uri\":\"" + musica + "\"}]").getAsJsonArray();
		
		final RemoveItemsFromPlaylistRequest requisicaoDeRemocaoDeItens = servicoDeAutorizacao.getSpotifyApi().removeItemsFromPlaylist(idDaPlaylistSelecionada, musicas)
				.build();
		
		try
		{
			final SnapshotResult resultadoDaRequisicao = requisicaoDeRemocaoDeItens.execute();
			
			System.out.println("ID do resultado da requisição: " + resultadoDaRequisicao.getSnapshotId());
			
			return true;
		} catch (IOException | SpotifyWebApiException | ParseException e) {
		  throw new ServerException(e.getMessage());
		}
	}
}
