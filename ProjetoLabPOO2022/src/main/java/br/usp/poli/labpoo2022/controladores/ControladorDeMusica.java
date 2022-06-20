package br.usp.poli.labpoo2022.controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.usp.poli.labpoo2022.servicos.ServicoDeMusicas;
import se.michaelthelin.spotify.model_objects.specification.AudioFeatures;

@RestController
@Scope("singleton")
@RequestMapping("/menu")
public class ControladorDeMusica {

    @Autowired
    private ServicoDeMusicas servicoDeMusicas;

    /**
     * Método que requisita as características de uma música para o serviço responsável
     * @param idDeMusica
     * @return Objeto com as características do música
     */
    @GetMapping("/parametros-de-musica")
    public ResponseEntity<AudioFeatures> getCaracteristicasDeMusicas(@RequestParam("id-da-musica") String idDeMusica)
    {
        try 
        {
			return new ResponseEntity<>(servicoDeMusicas.requisitaCaracteristicasDeMusica(idDeMusica), HttpStatus.OK);
			
        } catch (Exception e) 
        {	
			System.out.println("Erro ao requisitar características de músicas: " + e.getMessage());
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        
        }
    }
    
    /**
     * Método que requisita as características de várias músicas de uma vez para o serviço responsável
     * @param idsDasMusicas
     * @return Array de objetos com as caracterpisticas das músicas
     */
    @GetMapping("/caracteristicas-de-varias-musicas")
    public ResponseEntity<AudioFeatures[]> getCaracteristicasDeVariasMusicas(@RequestParam(value = "ids-das-musicas", required = true) String idsDasMusicas)
    {
    	try {
    		return new ResponseEntity<>(servicoDeMusicas.requisitaCaracteristicasDeVariasMusicas(idsDasMusicas), HttpStatus.OK);
    	} catch (Exception e) {
    		System.out.println("Erro ao requisitar características de várias músicas: " + e.getMessage());
    		return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    	}
    }
}
