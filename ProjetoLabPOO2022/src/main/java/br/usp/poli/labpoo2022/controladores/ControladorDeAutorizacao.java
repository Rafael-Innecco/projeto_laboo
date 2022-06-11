package br.usp.poli.labpoo2022.controladores;

import java.io.IOException;
import java.net.URI;

import javax.servlet.http.HttpServletResponse;

import org.apache.hc.core5.http.ParseException;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import br.usp.poli.labpoo2022.credenciais.ChavesDeSeguranca;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.SpotifyHttpManager;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeRequest;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeUriRequest;

/**
 * Controla o fluxo de autorização para permitir o
 * requisições de acesso à conta do usuário.
 * 
 */
@RestController
@Scope("singleton")
@RequestMapping("/autorizacao")
public class ControladorDeAutorizacao {
	/**
	 * Endereço para o qual o usuário será redirecionado após aceitar ou recusar a 
	 * permissão de acesso à funcionalidades de sua conta, por parte da aplicação web.
	 */
	private final URI enderecoDeRedirecionamento = SpotifyHttpManager.makeUri("http://localhost:8080/autorizacao/resgatar-codigo");
	
	/**
	 * Usado no resgate de um token de acesso para
	 * modificação/leitura dos dados da conta de um usuário spotify.
	 */
	private String codigoDeUsuario = "";
	
	/**
	 * Facilita utilização da API do Spotify.
	 */
	private final SpotifyApi spotifyApi = new SpotifyApi.Builder()
			.setClientId(ChavesDeSeguranca.idDeCliente.getChave())
			.setClientSecret(ChavesDeSeguranca.segredoDoCliente.getChave())
			.setRedirectUri(enderecoDeRedirecionamento)
			.build();

	/**
	 * Cria pedido de permissão de acesso à funcionalidades da conta do usuário.
	 * 
	 * @return URI de redirecionamento do usuário.
	 */
	@GetMapping("login")
	@ResponseBody
	public void loginDoSpotify(HttpServletResponse resposta) throws IOException
	{
		AuthorizationCodeUriRequest requisicaoDoCodigoDeAutorizacao = spotifyApi.authorizationCodeUri()
				.scope("playlist-read-collaborative playlist-modify-public playlist-read-private playlist-modify-private")
				.show_dialog(true)
				.build();
		final URI uri = requisicaoDoCodigoDeAutorizacao.execute();
		
		resposta.sendRedirect(uri.toString());
	}
	
	/**
	 * Resgata código de usuário e retorna código de acesso.
	 * 
	 * @param codigoDeUsuario Código indicando aceitação ou recusa do pedido de acesso
	 * 		  à funcionalidades da conta do usuário. Usado para resgatar código de acesso.
	 * @param resposta Resposta do Servlet Sava ao acesso Web.
	 * @return Código de acesso em String.
	 * @throws IOException Caso haja erro durante a requisição do código de autorização.
	 */
	@GetMapping(value = "resgatar-codigo")
	public String getCodigoDeUsuarioSpotify(@RequestParam("code") String codigoDeUsuario, HttpServletResponse resposta) throws IOException
	{
		this.codigoDeUsuario = codigoDeUsuario;
		AuthorizationCodeRequest requisicaoDeCodigoDeAutorizacao = spotifyApi.authorizationCode(this.codigoDeUsuario)
				.build();
		try
		{
			final AuthorizationCodeCredentials credenciaisDeCodigoDeAutorizacao = requisicaoDeCodigoDeAutorizacao.execute();
			
			spotifyApi.setAccessToken(credenciaisDeCodigoDeAutorizacao.getAccessToken());
			spotifyApi.setRefreshToken(credenciaisDeCodigoDeAutorizacao.getRefreshToken());
			
			System.out.println("Código de acesso expira em: " + credenciaisDeCodigoDeAutorizacao.getExpiresIn());
		} catch (IOException | SpotifyWebApiException | ParseException e)
		{
			System.out.println("Erro ao requisitar código do usuário spotify: " + e.getMessage());
		}
		
		resposta.sendRedirect("http://localhost:8080/menu");
		return spotifyApi.getAccessToken();
	}

	/**
	 * Getter do atributo que facilita o acesso à API do Spotify.
	 * 
	 * @return Atributo estático que facilita o acesso à API do Spotify.
	 */
	public SpotifyApi getSpotifyApi()
	{
		return this.spotifyApi;
	}
}
