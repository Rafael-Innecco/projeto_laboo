package br.usp.poli.labpoo2022.servicos;

import org.springframework.beans.factory.annotation.Autowired;

import br.usp.poli.labpoo2022.fluxo_de_autorizacao.ControladorDeAutorizacao;

public abstract class ServicoBase {
	
	@Autowired
    protected ControladorDeAutorizacao controladorDeAutorizacao;

	/**
	 * @return o objeto controladorDeAutorizacao, que é usado para autorizar 
	 * 			requisições à API do Spotify.
	 */
	public ControladorDeAutorizacao getControladorDeAutorizacao() {
		return controladorDeAutorizacao;
	}
}
