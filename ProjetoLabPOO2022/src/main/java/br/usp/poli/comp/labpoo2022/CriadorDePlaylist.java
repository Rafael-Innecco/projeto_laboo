package br.usp.poli.comp.labpoo2022;

import java.io.IOException;

import org.apache.hc.core5.http.ParseException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.specification.Playlist;
import se.michaelthelin.spotify.requests.data.playlists.CreatePlaylistRequest;

@RestController
@RequestMapping("/playlist")
public class CriadorDePlaylist {
	//Cria uma playlist vazia
	
	private static String nomeDaPlaylist = "placeholder"; //Será modificado a pelo usuário
	
	final static PerfilDeUsuario usuarioAtual = new PerfilDeUsuario();
	
	private static final CreatePlaylistRequest requisicaoDePlaylist = ControladorDeAutorizacao.getSpotifyApi().createPlaylist(usuarioAtual.getIdDeUsuario(), nomeDaPlaylist)
			.build();
	
	@GetMapping(value = "criador-de-playlist")
	public static void criaPlaylist()
	{
		try {
			final Playlist playlist = requisicaoDePlaylist.execute();
			
			System.out.println("Name: " + playlist.getName());
		} catch (IOException | SpotifyWebApiException | ParseException e) {
			System.out.println("Error: "  + e.getMessage());
		}
	}
}
