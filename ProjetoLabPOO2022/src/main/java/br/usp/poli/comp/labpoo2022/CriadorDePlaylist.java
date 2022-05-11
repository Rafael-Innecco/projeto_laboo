package br.usp.poli.comp.labpoo2022;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import se.michaelthelin.spotify.model_objects.specification.Playlist;
import se.michaelthelin.spotify.requests.data.playlists.CreatePlaylistRequest;

@RestController
@RequestMapping("/playlist")
public class CriadorDePlaylist {

	@GetMapping(value = "criador-de-playlist")
	public void setPlaylist()
	{
		final PerfilDeUsuario usuarioAtual = new PerfilDeUsuario();
		final String nomeDaPlaylist;
		//TODO: capturar nome da playlist digitada pelo usuário no front-end e entregar como parâmetro ao código abaixo
		
		final CreatePlaylistRequest requisicaoDePlaylist = ControladorDeAutorizacao.getSpotifyApi().createPlaylist(usuarioAtual.getIdDeUsuario(), nomeDaPlaylist)
				.build();
	}
}
