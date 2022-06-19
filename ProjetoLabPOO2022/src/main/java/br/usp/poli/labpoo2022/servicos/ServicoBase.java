package br.usp.poli.labpoo2022.servicos;

import org.springframework.beans.factory.annotation.Autowired;

public abstract class ServicoBase {
	
	@Autowired
    protected ServicoDeAutorizacao servicoDeAutorizacao;

	/**
	 * @return o objeto controladorDeAutorizacao, que é usado para autorizar 
	 * 			requisições à API do Spotify.
	 */
	public ServicoDeAutorizacao getControladorDeAutorizacao() {
		return servicoDeAutorizacao;
	}
}
