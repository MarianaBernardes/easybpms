package org.fixwo.services;

public class AssociarCliente {
	
	public static String run(long idOcorrencia){
		/*Receber o id da ocorrencia para conseguir buscar a area cadastrada para ela.
		  Buscar na entidade area o cliente responsavel por ela. Setar na entidade
		  ocorrencia, no atributo tenancy o id do cliente. Retornar o tenancy*/
		System.out.println("Cliente associado");
		return "1";
	}

}
