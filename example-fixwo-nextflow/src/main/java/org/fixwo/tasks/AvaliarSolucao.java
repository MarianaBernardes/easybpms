package org.fixwo.tasks;

import org.fixwo.domain.Ocorrencia;
import org.fixwo.process.FixwoProcess;

public class AvaliarSolucao implements ExternalTaskHandler{

	@Override
	public void executeTask(Ocorrencia ocorrencia, FixwoProcess fixwoProcess) {
		fixwoProcess.avaliarSolucao(ocorrencia.getAvaliacao());	
	}

}
