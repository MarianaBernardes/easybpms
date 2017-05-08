package org.fixwo.tasks;

import org.fixwo.domain.Ocorrencia;
import org.fixwo.process.FixwoProcess;

public class AvaliarSolucao implements ExternalTaskHandler{

	public void executeUserTask(Ocorrencia ocorrencia, FixwoProcess fixwoProcess) {
		fixwoProcess.avaliarSolucao(ocorrencia.getAvaliacao());	
	}

}
