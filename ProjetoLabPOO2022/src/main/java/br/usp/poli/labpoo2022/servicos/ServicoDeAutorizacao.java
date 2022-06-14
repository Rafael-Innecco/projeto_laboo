package br.usp.poli.labpoo2022.servicos;

import java.io.IOException;
import java.net.URI;

import javax.servlet.http.HttpServletResponse;

import org.apache.hc.core5.http.ParseException;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import br.usp.poli.labpoo2022.credenciais.ChavesDeSeguranca;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.SpotifyHttpManager;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeRefreshRequest;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeRequest;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeUriRequest;

@Service
@Scope("singleton")
public class ServicoDeAutorizacao {
	
	private  long tempoDaUltimaRenovacao;
	private String scope = new String(
			"playlist-read-collaborative playlist-modify-public playlist-read-private playlist-modify-private"
			+ " user-modify-playback-state user-library-modify user-read-private ugc-image-upload"
			);
	
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
	public String loginDoSpotify(HttpServletResponse resposta) throws IOException
	{
		AuthorizationCodeUriRequest requisicaoDoCodigoDeAutorizacao = spotifyApi.authorizationCodeUri()
				.scope(scope)
				.show_dialog(true)
				.build();
		final URI uri = requisicaoDoCodigoDeAutorizacao.execute();
		
		return uri.toString();
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
			
			tempoDaUltimaRenovacao = System.nanoTime();
			
			System.out.println("Código de acesso expira em: " + credenciaisDeCodigoDeAutorizacao.getExpiresIn());
		} catch (IOException | SpotifyWebApiException | ParseException e)
		{
			System.out.println("Erro ao requisitar código do usuário spotify: " + e.getMessage());
		}
		
		return spotifyApi.getAccessToken();
	}
	
	/**
	 * Método que renova o código de acesso à API
	 */
	private void renovaAcesso() {
		final AuthorizationCodeRefreshRequest requisicaoDeRenovacaoDeAcesso = spotifyApi.authorizationCodeRefresh()
			    .build();
		
		 try {
			 final AuthorizationCodeCredentials credenciaisDoCodigoDeAutorizacao = requisicaoDeRenovacaoDeAcesso.execute();

			 // Set access and refresh token for further "spotifyApi" object usage
			 spotifyApi.setAccessToken(credenciaisDoCodigoDeAutorizacao.getAccessToken());
			 
			 tempoDaUltimaRenovacao = System.nanoTime();

			 System.out.println("Expira em: " + credenciaisDoCodigoDeAutorizacao.getExpiresIn());
		 } catch (IOException | SpotifyWebApiException | ParseException e) {
			 System.out.println("Erro: " + e.getMessage());
		 }
	}

	public SpotifyApi getSpotifyApi() {
		if ((System.nanoTime() - tempoDaUltimaRenovacao) * 0.000000001 >= 1800)
			renovaAcesso();
		return spotifyApi;
	}

}
