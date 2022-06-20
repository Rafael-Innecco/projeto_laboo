package br.usp.poli.labpoo2022.servicos;

import java.io.IOException;

import org.apache.hc.core5.http.ParseException;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.specification.AudioFeatures;
import se.michaelthelin.spotify.requests.data.tracks.GetAudioFeaturesForSeveralTracksRequest;
import se.michaelthelin.spotify.requests.data.tracks.GetAudioFeaturesForTrackRequest;

@Service
@Scope("singleton")
public class ServicoDeMusicas extends ServicoBase{

	
	/**
	 * Retorna características de uma música (dançável, energia, andamento, força, etc.)
	 * @param idDaMusica
	 * @return características da música
	 * @throws Exception
	 */
    public AudioFeatures requisitaCaracteristicasDeMusica(String idDaMusica) throws Exception
    {
        final GetAudioFeaturesForTrackRequest requisicaoDeCaracteristicasDeMusica = servicoDeAutorizacao.getSpotifyApi()
            .getAudioFeaturesForTrack(idDaMusica)
            .build();

        try {
			
            final AudioFeatures audioFeatures = requisicaoDeCaracteristicasDeMusica.execute();

            return audioFeatures;

		} catch (ParseException | SpotifyWebApiException | IOException e) {
            throw e;
		}
    }
    
    /**
     * Retorna características de várias músicas (dançável, energia, andamento, força, etc.)
     * @param ids
     * @return Características das músicas requisitadas
     * @throws Exception
     */
    public AudioFeatures[] requisitaCaracteristicasDeVariasMusicas(String ids) throws Exception
    {
    	final GetAudioFeaturesForSeveralTracksRequest requisicaoDeCaracteristicas = servicoDeAutorizacao.getSpotifyApi()
    			.getAudioFeaturesForSeveralTracks(ids)
    			.build();
    	
    	try {
    		final AudioFeatures[] caracteristicasDasMusicas = requisicaoDeCaracteristicas.execute();
    		
    		return caracteristicasDasMusicas;
    	} catch (ParseException | SpotifyWebApiException | IOException e) {
    		throw e;
    	}
    }
}
