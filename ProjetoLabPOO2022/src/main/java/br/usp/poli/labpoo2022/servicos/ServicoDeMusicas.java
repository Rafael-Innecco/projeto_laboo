package br.usp.poli.labpoo2022.servicos;

import java.io.IOException;

import org.apache.hc.core5.http.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.usp.poli.labpoo2022.fluxo_de_autorizacao.ControladorDeAutorizacao;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.specification.AudioFeatures;
import se.michaelthelin.spotify.requests.data.tracks.GetAudioFeaturesForTrackRequest;

@Service
public class ServicoDeMusicas {

    @Autowired
    private ControladorDeAutorizacao controladorDeAutorizacao;

    public AudioFeatures getCaracteristicasDeMusica(String idDaMusica) throws Exception
    {
        final GetAudioFeaturesForTrackRequest requisicaoDeCaracteristicasDeMusica = ControladorDeAutorizacao.getSpotifyApi()
            .getAudioFeaturesForTrack(idDaMusica)
            .build();

        try {
			
            final AudioFeatures audioFeatures = requisicaoDeCaracteristicasDeMusica.execute();

            return audioFeatures;

		} catch (ParseException | SpotifyWebApiException | IOException e) {
			System.out.println("Erro ao requisitar características de música: " + e.getMessage());
            throw e;
		}
    }
}
