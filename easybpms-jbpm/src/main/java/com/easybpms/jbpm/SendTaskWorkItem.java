package com.easybpms.jbpm;


import org.jbpm.bpmn2.handler.SendTaskHandler;
import org.kie.api.runtime.process.WorkItem;
import org.kie.api.runtime.process.WorkItemManager;

public class SendTaskWorkItem extends SendTaskHandler{
	
	@Override
    public void executeWorkItem(WorkItem workItem, WorkItemManager manager) {
        manager.completeWorkItem(workItem.getId(), null);
    }
	
	@Override
    public void abortWorkItem(WorkItem workItem, WorkItemManager manager) {
        // Do nothing, cannot be aborted
    }

}
