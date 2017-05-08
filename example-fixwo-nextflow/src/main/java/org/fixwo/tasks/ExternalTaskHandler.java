package org.fixwo.tasks;

import org.fixwo.domain.Ocorrencia;
import org.fixwo.process.FixwoProcess;

public interface ExternalTaskHandler {
	public void executeUserTask(Ocorrencia ocorrencia,FixwoProcess fixwoProcess);
}
