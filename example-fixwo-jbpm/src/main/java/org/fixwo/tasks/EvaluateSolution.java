package org.fixwo.tasks;

import java.util.HashMap;
import java.util.Map;

import org.fixwo.domain.Occurrence;
import org.kie.api.task.TaskService;
import org.kie.api.task.model.Task;

public class EvaluateSolution implements ExternalTaskHandler{

	public void executeUserTask(Occurrence occurrence, TaskService taskService, Task task) {
		Map <String,Object> results = new HashMap <String,Object>();
		results.put("out_evaluation", occurrence.getEvaluation());
		System.out.println("Tarefa " + task.getName() + " executada");
		taskService.start(task.getId(), "Administrator");
		taskService.complete(task.getId(), "Administrator", results);
	}

}
