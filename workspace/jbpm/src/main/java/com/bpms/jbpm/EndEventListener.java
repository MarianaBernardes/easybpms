package com.bpms.jbpm;

import org.kie.api.event.process.DefaultProcessEventListener;
import org.kie.api.event.process.ProcessCompletedEvent;

import com.easybpms.bpms.GenericBpmsConnector;


public class EndEventListener extends DefaultProcessEventListener{
	
	@Override
	public void afterProcessCompleted(ProcessCompletedEvent event) {

		GenericBpmsConnector.endProcess(event.getProcessInstance().getProcessId(), String.valueOf(event.getProcessInstance().getId()));
	    
	    /*System.out.println("Process completed: processId=["
	            + processInstance.getProcessId() + "]; processInstanceId=["
	            + processInstance.getId() + "]");*/
	}

}

