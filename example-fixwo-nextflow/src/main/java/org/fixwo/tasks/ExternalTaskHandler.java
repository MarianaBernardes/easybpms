package org.fixwo.tasks;

import org.fixwo.domain.Ocorrencia;
import org.fixwo.process.FixwoProcess;

public interface ExternalTaskHandler {
	public void executeTask(Ocorrencia ocorrencia,FixwoProcess fixwoProcess);
}
