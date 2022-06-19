package br.usp.poli.labpoo2022.controladores;

import java.io.IOException;
import java.rmi.ServerException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.usp.poli.labpoo2022.servicos.ServicoDeAlbums;
import se.michaelthelin.spotify.model_objects.specification.TrackSimplified;

/**
 * Controlador responsável por conectar a classe de serviço de albums e o front-end
 */

@RestController
@Scope("singleton")
@RequestMapping("/menu")
public class ControladorDeAlbums {
	@Autowired
	private ServicoDeAlbums servicoDeAlbums;
	
	/**
	 * Método que solicita a listagem das músicas de um álbum para o Serviço responsável e devolve a resposta em formato conveniente
	 * @param idDoAlbum id do álbum a ser listado
	 * @return Response Entity com lista de músicas
	 * @throws ServerException
	 */
	@RequestMapping("/lista-musicas-de-album")
	public ResponseEntity<TrackSimplified[]> listaMusicas (@RequestParam(value = "album-selecionado", required = true) String idDoAlbum)
		throws ServerException {
		try {
			return new ResponseEntity<>(servicoDeAlbums.listaMusicas(idDoAlbum), HttpStatus.CREATED);
		} 
		catch (IOException e) {
			System.out.println("Erro na busca por musica: " + e.getMessage());
		}
		
		throw new ServerException(idDoAlbum);	
	}
}
