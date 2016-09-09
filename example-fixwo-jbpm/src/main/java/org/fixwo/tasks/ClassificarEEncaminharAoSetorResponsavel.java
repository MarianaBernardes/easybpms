package org.fixwo.tasks;

import java.util.HashMap;
import java.util.Map;

import org.fixwo.domain.Ocorrencia;
import org.kie.api.task.TaskService;
import org.kie.api.task.model.Task;

public class ClassificarEEncaminharAoSetorResponsavel implements ExternalTaskHandler{

	@Override
	public void executeTask(Ocorrencia ocorrencia, TaskService taskService, Task task) {
		Map <String,Object> results = new HashMap <String,Object>();
		results.put("easybpms_org_fixwo_domain_Ocorrencia_status", ocorrencia.getStatus());
		results.put("easybpms_org_fixwo_domain_Ocorrencia_setor", ocorrencia.getSetor());
		System.out.println("Tarefa " + task.getName() + " executada");
		taskService.start(task.getId(), "Administrator");
		taskService.complete(task.getId(), "Administrator", results);
	}
	
}
