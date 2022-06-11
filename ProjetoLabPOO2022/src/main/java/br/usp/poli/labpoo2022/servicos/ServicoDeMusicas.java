package br.usp.poli.labpoo2022.servicos;

import java.io.IOException;

import org.apache.hc.core5.http.ParseException;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.specification.AudioFeatures;
import se.michaelthelin.spotify.requests.data.tracks.GetAudioFeaturesForTrackRequest;

@Service
@Scope("singleton")
public class ServicoDeMusicas extends ServicoBase{

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
}
