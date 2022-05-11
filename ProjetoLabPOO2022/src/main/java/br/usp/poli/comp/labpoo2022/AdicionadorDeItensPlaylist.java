package br.usp.poli.comp.labpoo2022;

import java.io.IOException;

import org.apache.hc.core5.http.ParseException;

import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.special.SnapshotResult;
import se.michaelthelin.spotify.requests.data.playlists.AddItemsToPlaylistRequest;

public class AdicionadorDeItensPlaylist {
	private static String playlistSelecionada = "playlistURI"; // Deve ser substituido pelo uri correto
	private static String[] uris; //URIs das musicas a serem adicionadas (1 ou mais)
	// uris pode ser transformado em um JSON array (se for mais simples a transferencia do front-end)
	
	private static final AddItemsToPlaylistRequest requisicaoAdicaoItens = ControladorDeAutorizacao.getSpotifyApi()
			.addItemsToPlaylist(playlistSelecionada, uris)
			.build(); //Omite a posicao
	
	public static void adicionaItens () {
		try {
			final SnapshotResult snapshotResult = requisicaoAdicaoItens.execute();
			
			System.out.println("ID do snapshot: " + snapshotResult.getSnapshotId());
		} catch (IOException |SpotifyWebApiException | ParseException e) {
			System.out.println("Erro: " + e.getMessage());
		}
	}
}
