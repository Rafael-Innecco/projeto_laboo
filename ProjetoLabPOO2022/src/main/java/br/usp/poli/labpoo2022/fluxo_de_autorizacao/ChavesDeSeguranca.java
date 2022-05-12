package br.usp.poli.labpoo2022.fluxo_de_autorizacao;

public enum ChavesDeSeguranca {
	idDeCliente ("031634f04918436395bbb0c8eb4a57e5"),
	segredoDoCliente("097f43d0919a40348a05fc9280eee64c");

	private final String chave;
	
	ChavesDeSeguranca(String chave)
	{
		this.chave = chave;
	}
	
	public String getChave()
	{
		return this.chave;
	}
}
