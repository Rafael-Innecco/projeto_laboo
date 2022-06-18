package br.usp.poli.labpoo2022.controladores;

import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import br.usp.poli.labpoo2022.servicos.ServicoDeAutorizacao;

/**
 * Controla o fluxo de autorização para permitir o
 * requisições de acesso à conta do usuário.
 * 
 */
@RestController
@Scope("singleton")
@RequestMapping("/autorizacao")
public class ControladorDeAutorizacao {
	
	@Autowired
	private ServicoDeAutorizacao servicoDeAutorizacao;
	
	/**
	 * Cria pedido de permissão de acesso à funcionalidades da conta do usuário.
	 * 
	 * @return URI de redirecionamento do usuário.
	 */
	@GetMapping("login")
	@ResponseBody
	public void loginDoSpotify(HttpServletResponse resposta) throws IOException
	{	
		final String uri = servicoDeAutorizacao.loginDoSpotify(resposta);
		
		resposta.sendRedirect(uri);
	}
	
	/**
	 * Resgata código de usuário e retorna código de acesso.
	 * 
	 * @param codigoDeUsuario Código indicando aceitação ou recusa do pedido de acesso
	 * 		  à funcionalidades da conta do usuário. Usado para resgatar código de acesso.
	 * @param resposta Resposta do Servlet Sava ao acesso Web.
	 * @return Código de acesso em String.
	 * @throws IOException Caso haja erro durante a requisição do código de autorização.
	 */
	@GetMapping(value = "resgatar-codigo")
	public String getCodigoDeUsuarioSpotify(@RequestParam("code") String codigoDeUsuario, HttpServletResponse resposta) throws IOException
	{
		String codigoDeUsuarioRecebido = servicoDeAutorizacao.getCodigoDeUsuarioSpotify(codigoDeUsuario, resposta);
		
		resposta.sendRedirect("http://localhost:8080/menu");
		return codigoDeUsuarioRecebido;
	}
}
