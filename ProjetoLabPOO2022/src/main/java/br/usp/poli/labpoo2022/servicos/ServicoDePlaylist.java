package br.usp.poli.labpoo2022.servicos;

import java.io.IOException;
import java.rmi.ServerException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
	
	@Autowired
	private ServicoDeMusicas servicoDeMusicas;
	
	/**
	 * Cria uma playlist vazia.
	 * 
	 * @param nomeDaPlaylist Nome da playlist escolhida pelo usuário atual.
	 * @param resposta resposta do servlet à requisição de criação de playlist.
	 * @return booleano verdadeiro se a playlist foi criada com sucesso e falso, caso contrário.
	 * @throws ServerException
	 */
	public boolean criaPlaylist(String nomeDaPlaylist, HttpServletResponse resposta) throws ServerException
	{
	
		final CreatePlaylistRequest requisicaoDeCriacaoDePlaylist = servicoDeAutorizacao.getSpotifyApi().createPlaylist(usuarioAtual.getIdDeUsuario(), nomeDaPlaylist)
			.build();
		
		try {
			final Playlist playlist = requisicaoDeCriacaoDePlaylist.execute();
			
			System.out.println("Nome da playlist: " + playlist.getName());
			resposta.sendRedirect("/menu");
			
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
	* @return booleano verdadeiro se a playlist foi removida com sucesso e falso, caso contrário
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
	 * Adiciona música à playlist selecionada.
	 * 
	 * @param idDaplaylistSelecionada Playlist selecionada pelo usuário.
	 * @param uri URI da música selecionada
	 * @return booleano verdadeiro se o item foi adicionado com sucesso e falso, caso contrário
	 * @throws ServerException
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
	 * @param idDaPlaylistSelecionada ID da playlist cujos itens serão listados.
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
	 * @return booleano verdadeiro se o item foi removido com sucesso e falso, caso contrário
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
	
	
	/**
	 * Itera pelas musicas de uma playlist e separa as que se encixam na especificação de acusticidade
	 * @param musicas
	 * @param minimoAcustico
	 * @param maximoAcustico
	 * @return Array com músicas que satisfazem as condições
	 * @throws ServerException
	 */
	public PlaylistTrack[] filtraMusicasPorAcustiscidade(PlaylistTrack[] musicas, float minimoAcustico, float maximoAcustico) throws ServerException
	{		
		List<PlaylistTrack> musicasFiltradas = new ArrayList<>(Arrays.asList(musicas));
    	
    	musicasFiltradas.removeIf(musica -> {
    		try {
    			float acustiscidade = servicoDeMusicas.requisitaCaracteristicasDeMusica(musica.getTrack().getId()).getAcousticness();
				return  !(acustiscidade >= minimoAcustico && acustiscidade <= maximoAcustico);
				
    		} catch (Exception e) {
				System.out.println("Falha na filtragem por acustiscidade: " + e.getMessage());
    		}
    		
    		return true;
    	});
		
    	return musicasFiltradas.toArray(musicas);
	}
	
	/**
	 * Itera pelas musicas de uma playlist e separa as que se encixam na especificação
	 * @param musicas
	 * @param minimoAoVivo
	 * @param maximoAoVivo
	 * @return Array com músicas que satisfazem as condições
	 * @throws ServerException
	 */
	public PlaylistTrack[] filtraMusicasPorAoVivo(PlaylistTrack[] musicas, float minimoAoVivo, float maximoAoVivo) throws ServerException
	{		
		List<PlaylistTrack> musicasFiltradas = new ArrayList<>(Arrays.asList(musicas));
    	
    	musicasFiltradas.removeIf(musica -> {
    		try {
    			float aoVivo = servicoDeMusicas.requisitaCaracteristicasDeMusica(musica.getTrack().getId()).getLiveness();
				return  !(aoVivo >= minimoAoVivo && aoVivo <= maximoAoVivo);
				
    		} catch (Exception e) {
				System.out.println("Falha na filtragem por porcentagem de 'Ao vivo': " + e.getMessage());
    		}
    		
    		return true;
    	});
		
    	return musicasFiltradas.toArray(musicas);
	}
	
	/**
	 * Itera pelas musicas de uma playlist e separa as que se encixam na especificação de instrumentalidade
	 * @param musicas
	 * @param minimoInstrumental
	 * @param maximoInstrumental
	 * @return Array com músicas que satisfazem as condições
	 * @throws ServerException
	 */
	public PlaylistTrack[] filtraMusicasPorInstrumental(PlaylistTrack[] musicas, float minimoInstrumental, float maximoInstrumental) throws ServerException
	{		
		List<PlaylistTrack> musicasFiltradas = new ArrayList<>(Arrays.asList(musicas));
    	
    	musicasFiltradas.removeIf(musica -> {
    		try {
    			float instrumental = servicoDeMusicas.requisitaCaracteristicasDeMusica(musica.getTrack().getId()).getInstrumentalness();
				return  !(instrumental >= minimoInstrumental && instrumental <= maximoInstrumental);
				
    		} catch (Exception e) {
				System.out.println("Falha na filtragem por porcentagem de instrumentalidade: " + e.getMessage());
    		}
    		
    		return true;
    	});
		
    	return musicasFiltradas.toArray(musicas);
	}
	
	/**
	 * Itera pelas musicas de uma playlist e separa as que se encixam na especificação do parâmetro "fala"
	 * @param musicas
	 * @param minimoFala
	 * @param maximoFala
	 * @return Array com músicas que satisfazem as condições
	 * @throws ServerException
	 */
	public PlaylistTrack[] filtraMusicasPorFala(PlaylistTrack[] musicas, float minimoFala, float maximoFala) throws ServerException
	{		
		List<PlaylistTrack> musicasFiltradas = new ArrayList<>(Arrays.asList(musicas));
    	
    	musicasFiltradas.removeIf(musica -> {
    		try {
    			float fala = servicoDeMusicas.requisitaCaracteristicasDeMusica(musica.getTrack().getId()).getSpeechiness();
				return  !(fala >= minimoFala && fala <= maximoFala);
				
    		} catch (Exception e) {
				System.out.println("Falha na filtragem por porcentagem de nível de fala: " + e.getMessage());
    		}
    		
    		return true;
    	});
		
    	return musicasFiltradas.toArray(musicas);
	}
	
	/**
	 * Itera pelas musicas de uma playlist e separa as que se encixam na especificação de força
	 * @param musicas
	 * @param minimoForca
	 * @param maximoForca
	 * @return Array com músicas que satisfazem as condições
	 * @throws ServerException
	 */
	public PlaylistTrack[] filtraMusicasPorForca(PlaylistTrack[] musicas, float minimoForca, float maximoForca) throws ServerException
	{		
		List<PlaylistTrack> musicasFiltradas = new ArrayList<>(Arrays.asList(musicas));
    	
    	musicasFiltradas.removeIf(musica -> {
    		try {
    			float forca = servicoDeMusicas.requisitaCaracteristicasDeMusica(musica.getTrack().getId()).getLoudness();
				return  !(forca >= minimoForca && forca <= maximoForca);
				
    		} catch (Exception e) {
				System.out.println("Falha na filtragem por porcentagem de força: " + e.getMessage());
    		}
    		
    		return true;
    	});
		
    	return musicasFiltradas.toArray(musicas);
	}
	
	/**
	 * Itera pelas musicas de uma playlist e separa as que se encixam na especificação de andamento
	 * @param musicas
	 * @param minimoAndamento
	 * @param maximoAndamento
	 * @return Array com músicas que satisfazem as condições
	 * @throws ServerException
	 */
	public PlaylistTrack[] filtraMusicasPorAndamento(PlaylistTrack[] musicas, float minimoAndamento, float maximoAndamento) throws ServerException
	{		
		List<PlaylistTrack> musicasFiltradas = new ArrayList<>(Arrays.asList(musicas));
    	
    	musicasFiltradas.removeIf(musica -> {
    		try {
    			float andamento = servicoDeMusicas.requisitaCaracteristicasDeMusica(musica.getTrack().getId()).getTempo();
				return  !(andamento >= minimoAndamento && andamento <= maximoAndamento);
				
    		} catch (Exception e) {
				System.out.println("Falha na filtragem por porcentagem de andamento: " + e.getMessage());
    		}
    		
    		return true;
    	});
		
    	return musicasFiltradas.toArray(musicas);
	}
	
	/**
	 * Itera pelas musicas de uma playlist e separa as que se encixam na especificação de energia
	 * @param musicas
	 * @param minimoEnergia
	 * @param maximoEnergia
	 * @return Array com músicas que satisfazem as condições
	 * @throws ServerException
	 */
	public PlaylistTrack[] filtraMusicasPorEnergia(PlaylistTrack[] musicas, float minimoEnergia, float maximoEnergia) throws ServerException
	{		
		List<PlaylistTrack> musicasFiltradas = new ArrayList<>(Arrays.asList(musicas));
    	
    	musicasFiltradas.removeIf(musica -> {
    		try {
    			float energia = servicoDeMusicas.requisitaCaracteristicasDeMusica(musica.getTrack().getId()).getEnergy();
				return !(energia >= minimoEnergia && energia <= maximoEnergia);
				
    		} catch (Exception e) {
				System.out.println("Falha na filtragem por porcentagem de energia: " + e.getMessage());
    		}
    		
    		return true;
    	});
		
    	return musicasFiltradas.toArray(musicas);
	}
	
	/**
	 * Itera pelas musicas de uma playlist e separa as que se encixam na especificação de dançabilidade
	 * @param musicas
	 * @param minimoDancavel
	 * @param maximoDancavel
	 * @return Array com músicas que satisfazem as condições
	 * @throws ServerException
	 */
	public PlaylistTrack[] filtraMusicasPorDancavel(PlaylistTrack[] musicas, float minimoDancavel, float maximoDancavel) throws ServerException
	{		
		List<PlaylistTrack> musicasFiltradas = new ArrayList<>(Arrays.asList(musicas));
    	
    	musicasFiltradas.removeIf(musica -> {
    		try {
    			float dancavel = servicoDeMusicas.requisitaCaracteristicasDeMusica(musica.getTrack().getId()).getDanceability();
				return  !(dancavel >= minimoDancavel && dancavel <= maximoDancavel);
				
    		} catch (Exception e) {
				System.out.println("Falha na filtragem por porcentagem de dançável: " + e.getMessage());
    		}
    		
    		return true;
    	});
		
    	return musicasFiltradas.toArray(musicas);
	}
}
