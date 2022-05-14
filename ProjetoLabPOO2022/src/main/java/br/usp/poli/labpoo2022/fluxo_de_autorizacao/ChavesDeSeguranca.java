package br.usp.poli.labpoo2022.fluxo_de_autorizacao;

/**
 * Guarda o ID e segredo da aplicação web.
 *
 */
public enum ChavesDeSeguranca {
	idDeCliente ("031634f04918436395bbb0c8eb4a57e5"),
	segredoDoCliente("097f43d0919a40348a05fc9280eee64c");

	private final String chave;
	
	/**
	 * Construtor do objeto que armazena e omite chaves de segurança.
	 * @param chave chave que será armazenada no objeto
	 */
	ChavesDeSeguranca(String chave)
	{
		this.chave = chave;
	}
	
	/**
	 * Retorna chave de segurança armazenada no objeto.
	 * 
	 * @return chave de segurança.
	 */
	public String getChave()
	{
		return this.chave;
	}
}
