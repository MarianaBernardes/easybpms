package org.fixwo.services;

public class AssociarCliente {
	
	public String run(Long idOcorrencia){
		/*Receber o id da ocorrencia para conseguir buscar a area cadastrada para ela.
		  Buscar na entidade area o cliente responsavel por ela. Setar na entidade
		  ocorrencia, no atributo cliente o id do cliente. Retornar o cliente*/
		System.out.println("Cliente associado");
		return "1";
	}

}
