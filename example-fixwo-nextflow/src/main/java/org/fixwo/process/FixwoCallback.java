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
		System.out.println("Tarefa Criar Ocorrencia executada");
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
		String cliente = AssociarCliente.run(ocorrencia.getId());
		ocorrencia.setCliente(cliente);
	}
	public void publicarOrdemDeServico(){
		PublicarServico.run(ocorrencia.getId());
	}
	public void enviarMensagemAreaInvalida(){}
	public void delegarAoServicoTerceirizado(){}
	public void executarOrdemDeServico(){}
	public void enviarMensagemSolucaoRejeitada(){}
}