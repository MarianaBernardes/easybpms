package org.fixwo.process;

import org.fixwo.domain.Ocorrencia;
import org.fixwo.services.AssociarCliente;
import org.fixwo.services.BuscarArea;
import org.fixwo.services.PublicarServico;
import org.nextflow.owm.mapping.Process;

@Process("org_fixwo_domain_Ocorrencia")
public class FixwoCallback {
	
	Ocorrencia ocorrencia;
	
	//UserTask
	
	public void criarOcorrencia(Long id){
		ocorrencia.setId(id);
	}
	
	public void classificarEEncaminharAoSetorResponsavel(String status, String setor){
		ocorrencia.setStatus(status);
		ocorrencia.setSetor(setor);
		System.out.println("Tarefa Classificar e Encaminhar ao Setor Responsavel executada");
	}
	
	public void enviarFeedbackAoSolicitante(String status, String feedback){
		ocorrencia.setStatus(status);
		ocorrencia.setFeedback(feedback);
		System.out.println("Enviar Feedback ao Solicitante executada");
	}
	
	public void avaliarSolucao(Boolean avaliacao){
		ocorrencia.setAvaliacao(avaliacao);
		System.out.println("Avaliar Solucao executada");
	}
	
	//ScriptTask
	
	public void buscarArea(){
		Boolean area = BuscarArea.run(ocorrencia.getId());
		ocorrencia.setExisteArea(area);
	}
	
	public void associarCliente(){
		String tenancy = AssociarCliente.run(ocorrencia.getId());
		ocorrencia.setTenancy(tenancy);
	}
	
	public void publicarOrdemDeServico(){
		String tenancy = PublicarServico.run(ocorrencia.getId());
		ocorrencia.setTenancy(tenancy);
	}
	
	public void enviarMensagemAreaInvalida(){
		
	}
	
	public void delegarAoServicoTerceirizado(){
		System.out.println("Saiu da Atividade Delegar ao Servico Terceirizado");
	}
	
	public void executarOrdemDeServico(){
		System.out.println("Saiu da Atividade Executar Ordem de Servico");
	}
	
	public void enviarMensagemSolucaoRejeitada(){
		
	}

}
