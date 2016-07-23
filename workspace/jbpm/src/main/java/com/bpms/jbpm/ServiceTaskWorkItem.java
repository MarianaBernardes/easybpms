package com.bpms.jbpm;

import org.jbpm.bpmn2.handler.ServiceTaskHandler;
import org.kie.api.runtime.process.WorkItem;
import org.kie.api.runtime.process.WorkItemManager;

import com.easybpms.bpms.ServiceTask;

public class ServiceTaskWorkItem extends ServiceTaskHandler {
	
	private ServiceTask serviceTask;
	
	public ServiceTaskWorkItem(ServiceTask serviceTask) {
		this.serviceTask = serviceTask;
	}
	@Override
	public void executeWorkItem(WorkItem workItem, WorkItemManager manager) {
		this.serviceTask.run();
		manager.completeWorkItem(workItem.getId(), null);
	}
	@Override
	public void abortWorkItem(WorkItem workItem, WorkItemManager manager) {
		manager.abortWorkItem(workItem.getId());
	}
}
