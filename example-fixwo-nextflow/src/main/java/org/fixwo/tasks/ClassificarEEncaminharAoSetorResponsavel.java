package org.fixwo.tasks;

import org.fixwo.domain.Ocorrencia;
import org.fixwo.process.FixwoProcess;

public class ClassificarEEncaminharAoSetorResponsavel implements ExternalTaskHandler{

	@Override
	public void executeTask(Ocorrencia ocorrencia, FixwoProcess fixwoProcess) {
		fixwoProcess.classificarEEncaminharAoSetorResponsavel(ocorrencia.getStatus(), ocorrencia.getSetor());
	}
	
}
