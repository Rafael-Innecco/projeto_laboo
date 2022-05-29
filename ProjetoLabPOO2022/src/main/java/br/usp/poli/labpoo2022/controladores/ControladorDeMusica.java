package br.usp.poli.labpoo2022.controladores;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import br.usp.poli.labpoo2022.servicos.ServicoDeMusicas;
import se.michaelthelin.spotify.model_objects.specification.AudioFeatures;

@RestController
public class ControladorDeMusica {

    @Autowired
    private ServicoDeMusicas servicoDeMusicas;

    public ResponseEntity<AudioFeatures[]> getCaracteristicasDeMusicas(String [] idsDeMusicas)
    {
        List<AudioFeatures> listaDeCaracteristicasDasMusicas = new ArrayList<>();
        
        for(String idDeMusica : idsDeMusicas)
        {
            try {
				listaDeCaracteristicasDasMusicas.add(servicoDeMusicas.getCaracteristicasDeMusica(idDeMusica));
			} catch (Exception e) {
				System.out.println("Erro ao requisitar características de músicas: " + e.getMessage());
			}
        }

        return new ResponseEntity<>(
        		listaDeCaracteristicasDasMusicas.toArray(new AudioFeatures[listaDeCaracteristicasDasMusicas.size()]), 
        		HttpStatus.OK
                );
    
    }

}
