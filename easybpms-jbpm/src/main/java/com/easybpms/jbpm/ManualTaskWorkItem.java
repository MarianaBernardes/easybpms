package com.easybpms.jbpm;

import org.jbpm.process.instance.impl.demo.SystemOutWorkItemHandler;

import org.kie.api.runtime.process.WorkItem;
import org.kie.api.runtime.process.WorkItemManager;

public class ManualTaskWorkItem extends SystemOutWorkItemHandler{
	
	@Override
	public void executeWorkItem(WorkItem workItem, WorkItemManager manager) {
		manager.completeWorkItem(workItem.getId(), null);
	}
	
	@Override
	public void abortWorkItem(WorkItem workItem, WorkItemManager manager) {
		manager.abortWorkItem(workItem.getId());
	}

	

}
