package org.fixwo.services;

public class PublicarServico {
	public String run(Long idOcorrencia){
		/*Receber o id da ocorrencia. Buscar por meio do tenancy, o triador do cliente
		responsavel. Enviar uma solicitacao de ordem de servico ao triador. Retornar o
		tenancy*/
		System.out.println("Servico Publicado");
		return "1";
	}

}
