package com.bpms.jbpm;

import org.jbpm.process.instance.impl.demo.SystemOutWorkItemHandler;

import org.kie.api.runtime.process.WorkItem;
import org.kie.api.runtime.process.WorkItemManager;

public class ManualTaskWorkItemHandler extends SystemOutWorkItemHandler{
	
	@Override
	public void abortWorkItem(WorkItem workItem, WorkItemManager manager) {
		super.abortWorkItem(workItem, manager);
	}

	@Override
	public void executeWorkItem(WorkItem workItem, WorkItemManager manager) {
		super.executeWorkItem(workItem, manager);
	}

}
