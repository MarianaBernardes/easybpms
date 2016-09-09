package org.fixwo.tasks;

import org.fixwo.domain.Ocorrencia;
import org.kie.api.task.TaskService;
import org.kie.api.task.model.Task;

public interface ExternalTaskHandler {
	
	public void executeTask(Ocorrencia ocorrencia,TaskService taskService, Task task);
}
