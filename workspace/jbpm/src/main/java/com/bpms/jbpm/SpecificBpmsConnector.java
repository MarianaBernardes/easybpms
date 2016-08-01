package com.bpms.jbpm;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.jbpm.services.task.exception.PermissionDeniedException;
import org.jbpm.services.task.utils.OnErrorAction;
import org.jbpm.services.task.wih.LocalHTWorkItemHandler;
import org.jbpm.workflow.core.node.HumanTaskNode;
import org.jbpm.workflow.instance.node.HumanTaskNodeInstance;
import org.kie.api.runtime.process.NodeInstance;
import org.jbpm.ruleflow.instance.RuleFlowProcessInstance;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.process.ProcessInstance;
import org.kie.api.runtime.process.WorkItem;
import org.kie.api.runtime.process.WorkItemManager;
import org.kie.api.task.TaskService;
import org.kie.api.task.model.Task;
import org.kie.internal.task.api.InternalTaskService;
import org.kie.internal.task.exception.TaskException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.easybpms.bpms.GenericBpmsConnector;


public class SpecificBpmsConnector extends LocalHTWorkItemHandler {
	
	private TaskService taskService;
	private KieSession ksession;
	private GenericBpmsConnector connector;
	
	private static final Logger logger = LoggerFactory.getLogger(SpecificBpmsConnector.class);
	
	public SpecificBpmsConnector (TaskService taskService, KieSession ksession){
		this.taskService = taskService;
		this.ksession = ksession;
		this.connector = new GenericBpmsConnector();
	}
	
	@Override
	public void executeWorkItem(WorkItem workItem, WorkItemManager manager) {
		
		long processInstanceId = workItem.getProcessInstanceId();
		ProcessInstance processInstance = ksession.getProcessInstance(processInstanceId);
		
		long taskInstanceId = this.createTask(workItem, manager);
		Task taskInstance = taskService.getTaskById(taskInstanceId);
		String status = taskInstance.getTaskData().getStatus().name();

		Map<String, Object> params = workItem.getParameters();
		
		RuleFlowProcessInstance pi = (RuleFlowProcessInstance)processInstance;
		
		
		ArrayList<NodeInstance> nodeInstances = (ArrayList<NodeInstance>) pi.getNodeInstances();
		/**
		 * Foi implementado esse loop pois quando existem tarefas paralelas no jbpm a lista
		 * "nodeInstances" retorna as duas atividades. Assim, precisa chamar o connector para 
		 * a tarefa correta, de mesmo nome
		 */
		for (NodeInstance node : nodeInstances){
			if (node.getNodeName().equals(taskInstance.getName())){
				HumanTaskNodeInstance nodeInstanceHumanTask = (HumanTaskNodeInstance) node;
				HumanTaskNode nodeHumanTask = nodeInstanceHumanTask.getHumanTaskNode();
				Object aux = nodeHumanTask.getMetaData("UniqueId");
				String taskIdBpms = aux.toString();
				this.connector.execute(taskIdBpms,taskInstance.getName(), String.valueOf(taskInstance.getId()), status, params, processInstance.getProcessId(), String.valueOf(processInstance.getId()));
			}
		}
	
	}
	
	private long createTask(WorkItem workItem, WorkItemManager manager) {

		long taskId = (long) 0;

		Task task = createTaskBasedOnWorkItemParams(ksession, workItem);
		Map<String, Object> content = createTaskDataBasedOnWorkItemParams(ksession, workItem);
		
		try {
			taskId = ((InternalTaskService)taskService).addTask(task, content);
			if (isAutoClaim(workItem, task)) {
				try {
					//taskService.claim(taskId,(String) workItem.getParameter("SwimlaneActorId"));
					taskService.claim(taskId,"Administrator");
				} catch (PermissionDeniedException e) {
					logger.warn(
							"User {} is not allowed to auto claim task due to permission violation",
							workItem.getParameter("SwimlaneActorId"));
				}
			}
		} catch (Exception e) {
			if (action.equals(OnErrorAction.ABORT)) {
				manager.abortWorkItem(workItem.getId());
			} else if (action.equals(OnErrorAction.RETHROW)) {
				if (e instanceof RuntimeException) {
					throw (RuntimeException) e;
				} else {
					throw new RuntimeException(e);
				}
			} else if (action.equals(OnErrorAction.LOG)) {
				StringBuilder logMsg = new StringBuilder();
				logMsg.append(new Date())
						.append(": Error when creating task on task server for work item id ")
						.append(workItem.getId());
				logMsg.append(". Error reported by task server: ").append(e.getMessage());
				logger.error(logMsg.toString(), e);
				if (!(e instanceof TaskException)
						|| ((e instanceof TaskException) && !((TaskException) e)
								.isRecoverable())) {
					if (e instanceof RuntimeException) {
						throw (RuntimeException) e;
					} else {
						throw new RuntimeException(e);
					}
				}
			}
		}

		return taskId;
	}
	
	//método da versão 6.3.0
	protected Map<String, Object> createTaskDataBasedOnWorkItemParams(KieSession session, WorkItem workItem) {
        Map<String, Object> data = new HashMap<String, Object>();
        Object contentObject = workItem.getParameter("Content");
        if (contentObject == null) {
            data = new HashMap<String, Object>(workItem.getParameters());
        } else {
            data.put("Content", contentObject);
        }
        
        return data;
    }
}
