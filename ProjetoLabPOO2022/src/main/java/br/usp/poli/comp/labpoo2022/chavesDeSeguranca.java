package br.usp.poli.comp.labpoo2022;

public enum chavesDeSeguranca {
	idDeCliente ("031634f04918436395bbb0c8eb4a57e5"),
	segredoDoCliente("097f43d0919a40348a05fc9280eee64c");

	private final String chave;
	
	chavesDeSeguranca(String chave)
	{
		this.chave = chave;
	}
	
	public String getChave()
	{
		return this.chave;
	}
}
