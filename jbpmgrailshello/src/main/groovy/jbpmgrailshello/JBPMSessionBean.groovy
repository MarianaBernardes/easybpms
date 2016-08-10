package jbpmgrailshello;

import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.manager.RuntimeEngine;
import org.kie.api.runtime.manager.RuntimeManager;
import org.kie.api.task.TaskService;
import org.kie.internal.runtime.manager.context.EmptyContext;

public class JBPMSessionBean {
	private RuntimeManager runtimeManager;
	private RuntimeEngine runtimeEngine;
	private KieSession kieSession;
	private TaskService taskService;

	public RuntimeEngine getRuntimeEngine() {
		return this.runtimeEngine;
	}

	public RuntimeManager getRuntimeManager() {
		return runtimeManager;
	}

	public void setRuntimeManager(RuntimeManager runtimeManager) {
		this.runtimeManager = runtimeManager;
		this.runtimeEngine = runtimeManager.getRuntimeEngine(EmptyContext.get());
		this.kieSession = this.runtimeEngine.getKieSession();
		this.taskService = this.runtimeEngine.getTaskService();
	}

	public KieSession getKieSession() {
		return kieSession;
	}

	public TaskService getTaskService() {
		return taskService;
	}
}
