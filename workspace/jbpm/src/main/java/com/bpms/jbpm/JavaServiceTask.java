package com.bpms.jbpm;

import org.kie.api.runtime.process.WorkItem;
import org.kie.api.runtime.process.WorkItemHandler;
import org.kie.api.runtime.process.WorkItemManager;

import com.easybpms.bpms.ServiceTask;

public class JavaServiceTask implements WorkItemHandler {

	private ServiceTask serviceTask;
	
	public JavaServiceTask(ServiceTask serviceTask) {
		this.serviceTask = serviceTask;
	}
	public void executeWorkItem(WorkItem workItem, WorkItemManager manager) {
		this.serviceTask.run();
		manager.completeWorkItem(workItem.getId(), null);
	}

	public void abortWorkItem(WorkItem workItem, WorkItemManager manager) {
		manager.abortWorkItem(workItem.getId());
	}
}
