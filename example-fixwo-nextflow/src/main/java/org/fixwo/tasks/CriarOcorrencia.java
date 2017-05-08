package org.fixwo.tasks;

import org.fixwo.domain.Ocorrencia;
import org.fixwo.process.FixwoProcess;

public class CriarOcorrencia implements ExternalTaskHandler{

	public void executeUserTask(Ocorrencia ocorrencia, FixwoProcess fixwoProcess) {
		fixwoProcess.criarOcorrencia(ocorrencia.getId());
		
	}

}
