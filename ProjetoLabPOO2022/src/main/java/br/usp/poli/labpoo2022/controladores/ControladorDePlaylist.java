
package br.usp.poli.labpoo2022.controladores;

import java.rmi.ServerException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.usp.poli.labpoo2022.servicos.ServicoDePlaylist;
import se.michaelthelin.spotify.model_objects.specification.PlaylistTrack;
import se.michaelthelin.spotify.model_objects.specification.PlaylistSimplified;

/**
 * Gerencia todas as funcionalidades relacionadas à playlists do usuário atual.
 * <p>
 * Exemplos: adição, remoção e listagem de músicas; criação e remoção de playlists.
 * </p>
 */
@RestController
@Scope("singleton")
@RequestMapping("/menu")
public class ControladorDePlaylist{
	
	@Autowired
	private ServicoDePlaylist servicoDePlaylist;
	
	/**
	 * Cria uma playlist vazia.
	 * 
	 * @param nomeDaPlaylist Nome da playlist escolhida pelo usuário atual.
	 * @param resposta resposta do servlet à requisição de criação de playlist.
	 */
	@GetMapping("/cria-playlist")
	public ResponseEntity<Boolean> criaPlaylist(
			@RequestParam(value = "nome-da-playlist", required = true) String nomeDaPlaylist, HttpServletResponse resposta)
	{
		try {
			Boolean respostaDaRequisicao = Boolean.valueOf(servicoDePlaylist.criaPlaylist(nomeDaPlaylist, resposta));
			return new ResponseEntity<>(respostaDaRequisicao, HttpStatus.OK);
			
		} catch (ServerException e) 
		{
			System.out.println("Erro ao criar playlist: " + e.getMessage());
			return new ResponseEntity<>(Boolean.valueOf(false), HttpStatus.BAD_REQUEST);
		}
	}
	
	/**
	* Remove playlist de usuário.
	*
	* @param idDaPlaylistSelecionada ID da playlist a ser removida.
	* @return String JSON indicando sucesso na requisição de remoção.
	* @throws ServerException
	*/
	@GetMapping("/remove-playlist")
	public ResponseEntity<Boolean> removePlaylist(
			@RequestParam(value = "playlist-selecionada", required = true) String idDaPlaylistSelecionada) throws ServerException
	{
		try {
			Boolean respostaDaRequisicao = Boolean.valueOf(servicoDePlaylist.removePlaylist(idDaPlaylistSelecionada));
			return new ResponseEntity<>(respostaDaRequisicao, HttpStatus.OK);
			
		}catch(ServerException e)
		{
			System.out.println("Erro ao remover playlist: " + e.getMessage());
			throw new ServerException(e.getMessage());
		}
	}
	
	/**
	 * Adiciona músicas à playlist selecionada.
	 * 
	 * @param playlistSelecionada Playlist selecionada pelo usuário.
	 * @param uris URIs das músicas selecionadas
	 */
	@GetMapping("/adiciona-itens")
	public ResponseEntity<Boolean> adicionaItensEmPlaylist (
			@RequestParam(value = "playlist-selecionada", required = true) String idDaplaylistSelecionada, 
			@RequestParam(value = "uri", required = true) String uri) throws ServerException
	{
		try {
			Boolean respostaDaRequisicao = Boolean.valueOf(servicoDePlaylist.adicionaItensEmPlaylist(idDaplaylistSelecionada, uri));
			return new ResponseEntity<>(respostaDaRequisicao, HttpStatus.OK);
			
		}catch(ServerException e)
		{
			System.out.println("Erro ao adicionar item em playlist: " + e.getMessage());
			throw new ServerException(e.getMessage());
		}
	}

	/**
	 * Captura a lista de playlists do usuário atual.
	 * 
	 * @return Array de playlists contendo informações sobre as playlists.
	 * @throws ServerException 
	 */
	@GetMapping("/lista-playlists")
	public ResponseEntity<PlaylistSimplified[]> listaPlaylists() throws ServerException
	{
		try
		{
			PlaylistSimplified[] playlistsDoUsuarioAtual = servicoDePlaylist.listaPlaylists();
			return new ResponseEntity<>(playlistsDoUsuarioAtual, HttpStatus.OK);
		} catch(ServerException e)
		{
			System.out.println("Erro ao listar playlists do usuário atual: " + e.getMessage());
			throw new ServerException(e.getMessage());
		}
	}
	
	/**
	 * Lista os itens de uma playlist do usuário atual.
	 * 
	 * @param idDaPlaylist ID da playlist cujos itens serão listados.
	 * @return Array de músicas de playlists
	 */
	@GetMapping("/lista-itens-de-playlist")
	public ResponseEntity<PlaylistTrack[]> listaItensDePlaylist(
			@RequestParam(value = "playlist-selecionada", required = true) String idDaPlaylistSelecionada) throws ServerException
	{
		try {
			
			PlaylistTrack [] musicasDePlaylist = servicoDePlaylist.listaItensDePlaylist(idDaPlaylistSelecionada);
			return new ResponseEntity<>(musicasDePlaylist, HttpStatus.OK);
	
		} catch(ServerException e)
		{
			System.out.println("Erro ao listar itens de uma playlist: " + e.getMessage());
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
	@GetMapping("/remove-itens-de-playlist")
	public ResponseEntity<Boolean> removeItensDePlaylist(
			@RequestParam(value = "playlist-selecionada", required = true) String idDaPlaylistSelecionada,
			@RequestParam(value = "uris", required = true) String musica) throws ServerException
	{
		try {
			Boolean respostaDaRequisicao = Boolean.valueOf(servicoDePlaylist.removeItensDePlaylist(idDaPlaylistSelecionada, musica));
			return new ResponseEntity<>(respostaDaRequisicao, HttpStatus.OK);
			
		}catch(ServerException e)
		{
			System.out.println("Erro ao remover playlist: " + e.getMessage());
			throw new ServerException(e.getMessage());
		}
	}
}
