package org.fixwo.process;

import org.fixwo.domain.Ocorrencia;
import org.nextflow.owm.mapping.Process;
import org.nextflow.owm.mapping.WorkflowProcess;

@Process("org_fixwo_domain_Ocorrencia")
public interface FixwoProcess extends WorkflowProcess{
	
	void criarOcorrencia(long id);
	
	void classificarEEncaminharAoSetorResponsavel(String status, String setor);
	
	void enviarFeedbackAoSolicitante(String status, String feedback);
	
	void avaliarSolucao(boolean avaliacao);
	
	Ocorrencia getData();

}
