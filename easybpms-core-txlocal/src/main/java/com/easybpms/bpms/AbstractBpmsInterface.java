package com.easybpms.bpms;

import java.util.Map;

public abstract class AbstractBpmsInterface {

	public static AbstractBpmsInterface bpmsInterface = null;

	public AbstractBpmsInterface() {
		bpmsInterface = this;
	}

	public static void setBpmsInterface(AbstractBpmsInterface bpmsInterface) {
		AbstractBpmsInterface.bpmsInterface = bpmsInterface;
	}

	public static AbstractBpmsInterface getBpmsInterface() {
		return bpmsInterface;
	}

	//public abstract void startBPMS(List<String> bpmnProcessDefinitions);

	public abstract void startProcess(String processId, Map<String, Object> params);
	
	/**
	 * @param taskId
	 * @param user
	 * @param params
	 * @return the new status of task <code>taskId</code>
	 */
	public abstract String executeTask(long taskId, String user, Map<String, Object> params);

}
