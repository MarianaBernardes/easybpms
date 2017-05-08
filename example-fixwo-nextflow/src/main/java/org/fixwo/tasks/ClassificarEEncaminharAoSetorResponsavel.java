package org.fixwo.tasks;

import org.fixwo.domain.Ocorrencia;
import org.fixwo.process.FixwoProcess;

public class ClassificarEEncaminharAoSetorResponsavel implements ExternalTaskHandler{

	public void executeUserTask(Ocorrencia ocorrencia, FixwoProcess fixwoProcess) {
		fixwoProcess.classificarEEncaminharAoSetorResponsavel(ocorrencia.getStatus(), ocorrencia.getSetor());
	}
	
}
