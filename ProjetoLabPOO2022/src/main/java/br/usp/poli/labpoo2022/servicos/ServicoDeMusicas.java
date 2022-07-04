package br.usp.poli.labpoo2022.servicos;

import java.io.IOException;
import java.rmi.ServerException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.hc.core5.http.ParseException;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import se.michaelthelin.spotify.enums.Modality;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.miscellaneous.AudioAnalysis;
import se.michaelthelin.spotify.model_objects.specification.AudioFeatures;
import se.michaelthelin.spotify.model_objects.specification.Track;
import se.michaelthelin.spotify.requests.data.tracks.GetAudioAnalysisForTrackRequest;
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
    
    public AudioAnalysis requisitaAnaliseDeMusica(String id) throws ServerException
	{
		final GetAudioAnalysisForTrackRequest requisicaoDeAnaliseDeAudio = servicoDeAutorizacao.getSpotifyApi()
			    .getAudioAnalysisForTrack(id)
			    .build();
		
		try {
		      final AudioAnalysis analiseDaMusica = requisicaoDeAnaliseDeAudio.execute();

		      System.out.println("Sucesso na requisicao: " + analiseDaMusica.getTrack().toString());
		      
		      return analiseDaMusica;
		    } catch (IOException | SpotifyWebApiException | ParseException e) {
		    	throw new ServerException(e.getMessage());
		    }
	}
    
    public Track[] filtraMusicasPorTom(Track[] musicas, Integer tonalidade) throws ServerException
	{
    	if (tonalidade == null)
    		return musicas;
    	
    	List<Track> musicasFiltradas = new ArrayList<>(Arrays.asList(musicas));
    	
    	musicasFiltradas.removeIf(musica -> {
    		try {
    			
				return this.requisitaAnaliseDeMusica(musica.getId()).getTrack().getKey() != tonalidade;
				
    		} catch (ServerException e) {
				System.out.println("Falha na filtragem por tom: " + e.getMessage());
    		}
    		
    		return true;
    	});
    	
		return musicasFiltradas.toArray(musicas);
	}
	
	public Track[] filtraMusicasPorModo(Track[] musicas, Modality modo) throws ServerException
	{
		if(modo == null)
			return musicas;
		
		List<Track> musicasFiltradas = new ArrayList<>(Arrays.asList(musicas));
    	
    	musicasFiltradas.removeIf(musica -> {
    		try {
    			if(musica != null)
    				return this.requisitaAnaliseDeMusica(musica.getId()).getTrack().getMode() != modo;
				
    		} catch (ServerException e) {
				System.out.println("Falha na filtragem por modo: " + e.getMessage());
    		}
    		
    		return true;
    	});
		
    	return musicasFiltradas.toArray(musicas);	
	}
	
	public Track[] filtraMusicasPorCompasso(Track[] musicas, Integer formulaDeCompasso) throws ServerException
	{
		if(formulaDeCompasso == null)
			return musicas;
		
		List<Track> musicasFiltradas = new ArrayList<>(Arrays.asList(musicas));
    	
    	musicasFiltradas.removeIf(musica -> {
    		try {
    			
				return this.requisitaAnaliseDeMusica(musica.getId()).getTrack().getTimeSignature() != formulaDeCompasso;
				
    		} catch (ServerException e) {
				System.out.println("Falha na filtragem por fórmula de compasso: " + e.getMessage());
    		}
    		
    		return true;
    	});
		
    	return musicasFiltradas.toArray(musicas);
	}

}
