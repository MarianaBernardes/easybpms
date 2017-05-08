package org.fixwo.tasks;

import org.fixwo.domain.Occurrence;
import org.kie.api.task.TaskService;
import org.kie.api.task.model.Task;

public interface ExternalTaskHandler {
	
	public void executeUserTask(Occurrence occurrence,TaskService taskService, Task task);
}
