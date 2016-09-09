package org.fixwo.tasks;

import org.fixwo.domain.Ocorrencia;
import org.fixwo.process.FixwoProcess;

public class EnviarFeedbackAoSolicitante implements ExternalTaskHandler{

	@Override
	public void executeTask(Ocorrencia ocorrencia, FixwoProcess fixwoProcess) {
		fixwoProcess.enviarFeedbackAoSolicitante(ocorrencia.getStatus(), ocorrencia.getFeedback());
		
	}

}
