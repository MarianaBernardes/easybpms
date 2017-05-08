package org.fixwo.process;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.fixwo.domain.Occurrence;
import org.fixwo.tasks.ClassifyAndForwardToResponsibleSector;
import org.fixwo.tasks.EvaluateSolution;
import org.fixwo.tasks.ExternalTaskHandler;
import org.fixwo.tasks.RegisterOccurrence;
import org.fixwo.tasks.SendFeedbackToRequester;
import org.jbpm.bpmn2.handler.ServiceTaskHandler;
import org.jbpm.process.instance.impl.demo.SystemOutWorkItemHandler;
//import org.jbpm.test.JBPMHelper;
import org.jbpm.workflow.instance.WorkflowProcessInstance;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.manager.RuntimeEngine;
import org.kie.api.runtime.manager.RuntimeEnvironmentBuilder;
import org.kie.api.runtime.manager.RuntimeManager;
import org.kie.api.runtime.manager.RuntimeManagerFactory;
import org.kie.api.runtime.process.ProcessInstance;
import org.kie.api.task.TaskService;
import org.kie.api.task.model.Task;
import org.kie.internal.io.ResourceFactory;
import org.kie.internal.runtime.manager.context.EmptyContext;

import bitronix.tm.resource.jdbc.PoolingDataSource;

public class FixwoJbpm {
	
	private static KieSession ksession = FixwoJbpm.getSession();
	private static TaskService taskService = FixwoJbpm.getTaskService();
	
	private static final String PROCESS_ID = "org_fixwo_domain_Occurrence";
	private static ProcessInstance processInstance; 
	private static Task task;
	private Map <String, ExternalTaskHandler> mapTasks;
	
	public FixwoJbpm(){
		mapTasks = new HashMap <String, ExternalTaskHandler>();
		mapTasks.put ("Register Occurrence", new RegisterOccurrence());
		mapTasks.put ("Classify and Forward to Responsible Sector", new ClassifyAndForwardToResponsibleSector());
		mapTasks.put ("Send Feedback to Requester", new SendFeedbackToRequester());
		mapTasks.put ("Evaluate Solution", new EvaluateSolution());
	}
	
	public void executeFlow(Occurrence occurrence){
		processInstance = getProcessForEntity(occurrence.getId());
		List<Long> availableTasks = taskService.getTasksByProcessInstanceId(processInstance.getId());
		ExternalTaskHandler handler = getExternalTaskHandler(availableTasks);
		handler.executeUserTask(occurrence,taskService,task);
	}
	
	//Iniciar o processo
	private ProcessInstance startNewFixwoProcess() {
		return ksession.startProcess (PROCESS_ID);
	}
		
	//Retorna o processo em execução
	private ProcessInstance getProcessForEntity(long id) {
		ProcessInstance selectedFP = null;
		
		Collection <ProcessInstance> processInstances = ksession.getProcessInstances();
		for (ProcessInstance pi : processInstances) {
			//RuleFlowProcessInstance rfpi = (RuleFlowProcessInstance)pi;
			WorkflowProcessInstance wpi = (WorkflowProcessInstance)pi;
			long idVariable = (Long) wpi.getVariable("id");
			if(idVariable == id){
				selectedFP = wpi;
			}
		}
		
		/*ProcessInstance pi = ksession.getProcessInstance(processInstance.getId());
		RuleFlowProcessInstance rfpi = (RuleFlowProcessInstance)pi;
		Long idVariable = (Long) rfpi.getVariable("org_fixwo_domain_Ocorrencia_id");
		if(idVariable.equals(idOcorrencia)){
			processInstance = rfpi;
		}*/
		
		selectedFP = processInstance;
		if(selectedFP == null){
			selectedFP = startNewFixwoProcess();
		}
		return selectedFP;
	}
		
	//Retorna a tarefa disponível
	private ExternalTaskHandler getExternalTaskHandler(List<Long> availableTasks) {
		
		for (Long idTask : availableTasks){
			task = taskService.getTaskById(idTask);
			String status = task.getTaskData().getStatus().name();
			if (status.equals("Ready") || status.equals("Reserved")){
				return mapTasks.get(task.getName());
			}
		}
		return null;
	}
	
	private static TaskService getTaskService(){
		if (taskService == null){
			connect();
		}
		return taskService;
	}
	
	private static KieSession getSession(){
		if (ksession == null){
			connect();
		}
		return ksession;
	}
	
	private static void connect(){
		//JBPMHelper.startH2Server();
		//JBPMHelper.setupDataSource();
		EntityManagerFactory emf = setupDataSource();
		//EntityManagerFactory emf = Persistence.createEntityManagerFactory("org.jbpm.persistence.jpa");
		RuntimeEnvironmentBuilder builder = RuntimeEnvironmentBuilder.Factory.get().newDefaultBuilder().entityManagerFactory(emf).addAsset(ResourceFactory.newClassPathResource("fixwoProcess.bpmn2"),ResourceType.BPMN2);
		RuntimeManager manager = RuntimeManagerFactory.Factory.get().newSingletonRuntimeManager(builder.get());
		
		//RuntimeManager manager = createRuntimeManager();
		RuntimeEngine engine = manager.getRuntimeEngine(EmptyContext.get());
		ksession = engine.getKieSession();
		taskService = engine.getTaskService();
		ksession.getWorkItemManager().registerWorkItemHandler("Manual Task",new SystemOutWorkItemHandler());
		ksession.getWorkItemManager().registerWorkItemHandler("Service Task",new ServiceTaskHandler());
		ksession.getWorkItemManager().registerWorkItemHandler("Send Task",new SystemOutWorkItemHandler());
		//addTaskConnector();
	}
	
	/*private static void addTaskConnector(){
		ksession.getWorkItemManager().registerWorkItemHandler("Manual Task",new SystemOutWorkItemHandler());
		ksession.getWorkItemManager().registerWorkItemHandler("Service Task",new ServiceTaskHandler());
		ksession.getWorkItemManager().registerWorkItemHandler("Send Task",new SystemOutWorkItemHandler());
	}
	private static RuntimeManager createRuntimeManager() {
		JBPMHelper.startH2Server();
		JBPMHelper.setupDataSource();
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("org.jbpm.persistence.jpa");
		RuntimeEnvironmentBuilder builder = RuntimeEnvironmentBuilder.Factory.get().newDefaultBuilder().entityManagerFactory(emf).addAsset(ResourceFactory.newClassPathResource("fixwoProcess.bpmn2"),ResourceType.BPMN2);
		return RuntimeManagerFactory.Factory.get().newSingletonRuntimeManager(builder.get());
	}*/
	

	private static EntityManagerFactory setupDataSource() {
        // create data source
        PoolingDataSource pds = new PoolingDataSource();
        pds.setUniqueName("jdbc/jbpm-ds");
        pds.setClassName("bitronix.tm.resource.jdbc.lrc.LrcXADataSource");
        pds.setMaxPoolSize(5);
        pds.setAllowLocalTransactions(true);
        pds.getDriverProperties().put("user", "sa");
        pds.getDriverProperties().put("password", "");
        pds.getDriverProperties().put("url", "jdbc:h2:mem:jbpm-db");
        pds.getDriverProperties().put("driverClassName", "org.h2.Driver");
        pds.init();
        
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("org.jbpm.persistence.jpa");
		
		return emf;
    }
}
