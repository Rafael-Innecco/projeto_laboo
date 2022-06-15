package br.usp.poli.labpoo2022.servicos;

import java.io.IOException;
import java.rmi.ServerException;

import org.apache.hc.core5.http.ParseException;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.specification.Paging;
import se.michaelthelin.spotify.model_objects.specification.TrackSimplified;
import se.michaelthelin.spotify.requests.data.albums.GetAlbumsTracksRequest;

/**
 * Gerencia as funcionalidades relacionadas a albums
 */
@Service
@Scope("singleton")
public class ServicoDeAlbums extends ServicoBase{
	
	public TrackSimplified[] listaMusicas (String id) throws ServerException {
		final GetAlbumsTracksRequest requisicaoDeMusicasNoAlbum = servicoDeAutorizacao.getSpotifyApi()
				.getAlbumsTracks(id)
				.limit(50)
				.build();
		
		try {
			final Paging<TrackSimplified> listaDeMusicasNoAlbum = requisicaoDeMusicasNoAlbum.execute();
			
			System.out.println("Total de Músicas = " + listaDeMusicasNoAlbum.getTotal());
			
			return listaDeMusicasNoAlbum.getItems();
		} catch (IOException | SpotifyWebApiException | ParseException e) {
      		throw new ServerException(e.getMessage());
		}
	}
	
}
