package br.usp.poli.labpoo2022.gerenciadores;

import java.io.IOException;

import br.usp.poli.labpoo2022.fluxo_de_autorizacao.ControladorDeAutorizacao;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.specification.User;
import se.michaelthelin.spotify.requests.data.users_profile.GetCurrentUsersProfileRequest;

/**
 * Gerencia funcionalidades do usuário atualmente logado.
 */
public class GerenciadorDoUsuarioAtual {
	
	private final GetCurrentUsersProfileRequest requisicaoDePerfilDeUsuario = ControladorDeAutorizacao.getSpotifyApi().getCurrentUsersProfile()
    		.build();
	
	/**
	 * Getter do ID do usuário atual.
	 * @return ID do usuário atual
	 */
	public String getIdDeUsuario()
	{
		try 
		{
			final User usuario = requisicaoDePerfilDeUsuario.execute();
			
			System.out.println("ID do usuário: " + usuario.getId());
			return usuario.getId();
			
		} catch (IOException | SpotifyWebApiException | org.apache.hc.core5.http.ParseException e)
		{
			System.out.println("Erro ao requisitar ID de usuário: " + e.getMessage());
		}
		
		return "Não foi possível requisitar o ID de usuário";
	}
}
