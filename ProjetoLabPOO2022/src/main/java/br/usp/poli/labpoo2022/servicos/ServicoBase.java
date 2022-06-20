package br.usp.poli.labpoo2022.servicos;

import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * Serviço utilizado como base para outros serviços.
 * Possui atributo de autorização usado em toda e qualquer requisição à API do spotify.
 *
 */
public abstract class ServicoBase {

	@Autowired
    protected ServicoDeAutorizacao servicoDeAutorizacao;
}
