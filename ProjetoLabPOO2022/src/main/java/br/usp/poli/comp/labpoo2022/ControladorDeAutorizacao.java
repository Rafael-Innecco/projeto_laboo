package br.usp.poli.comp.labpoo2022;

import java.io.IOException;
import java.net.URI;

import javax.servlet.http.HttpServletResponse;

import org.apache.hc.core5.http.ParseException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.SpotifyHttpManager;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeRequest;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeUriRequest;

@RestController
@RequestMapping("/autorizacao")
public class ControladorDeAutorizacao {
	
	private static final URI enderecoDeRedirecionamento = SpotifyHttpManager.makeUri("http://localhost:8080/autorizacao/resgatar-codigo");
	private String codigoDeUsuario = "";
	
	private static final SpotifyApi spotifyApi = new SpotifyApi.Builder()
			.setClientId(chavesDeSeguranca.idDeCliente.getChave())
			.setClientSecret(chavesDeSeguranca.segredoDoCliente.getChave())
			.setRedirectUri(enderecoDeRedirecionamento)
			.build();

	@GetMapping("login")
	@ResponseBody
	public String loginDoSpotify()
	{
		AuthorizationCodeUriRequest requisicaoDoCodigoDeAutorizacao = spotifyApi.authorizationCodeUri()
				.scope("playlist-read-collaborative playlist-modify-public playlist-read-private playlist-modify-private")
				.show_dialog(true)
				.build();
		final URI uri = requisicaoDoCodigoDeAutorizacao.execute();
		
		return uri.toString();
	}

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
		
		resposta.sendRedirect("http://localhost:8080/playlist/criador-de-playlist");
		return spotifyApi.getAccessToken();
	}

	public static SpotifyApi getSpotifyApi()
	{
		return ControladorDeAutorizacao.spotifyApi;
	}
}
