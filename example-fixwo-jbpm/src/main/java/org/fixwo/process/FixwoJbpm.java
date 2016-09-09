package org.fixwo.process;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.fixwo.domain.Ocorrencia;
import org.fixwo.tasks.AvaliarSolucao;
import org.fixwo.tasks.ClassificarEEncaminharAoSetorResponsavel;
import org.fixwo.tasks.CriarOcorrencia;
import org.fixwo.tasks.EnviarFeedbackAoSolicitante;
import org.fixwo.tasks.ExternalTaskHandler;
import org.jbpm.bpmn2.handler.ServiceTaskHandler;
import org.jbpm.process.instance.impl.demo.SystemOutWorkItemHandler;
import org.jbpm.test.JBPMHelper;
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

public class FixwoJbpm {
	
	private static KieSession ksession = FixwoJbpm.getSession();
	private static TaskService taskService = FixwoJbpm.getTaskService();
	
	private static final String PROCESS_ID = "org_fixwo_domain_Ocorrencia";
	private static ProcessInstance processInstance; 
	private static Task task;
	private Map <String, ExternalTaskHandler> mapTasks;
	
	public FixwoJbpm(){
		mapTasks = new HashMap <String, ExternalTaskHandler>();
		mapTasks.put ("Criar Ocorrencia", new CriarOcorrencia());
		mapTasks.put ("Classificar e Encaminhar ao Setor Responsavel", new ClassificarEEncaminharAoSetorResponsavel());
		mapTasks.put ("Enviar Feedback ao Solicitante", new EnviarFeedbackAoSolicitante());
		mapTasks.put ("Avaliar Solucao", new AvaliarSolucao());
	}
	
	public void executeFlow(Ocorrencia ocorrencia){
		processInstance = getProcessForEntity(ocorrencia.getId());
		List<Long> availableTasks = taskService.getTasksByProcessInstanceId(processInstance.getId());
		ExternalTaskHandler handler = getExternalTaskHandler(availableTasks);
		handler.executeTask(ocorrencia,taskService,task);
	}
	
	//Iniciar o processo
	private ProcessInstance startNewFixwoProcess(long idOcorrencia) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("org_fixwo_domain_Ocorrencia_id", idOcorrencia);
		return ksession.startProcess (PROCESS_ID,params);
	}
		
	//Retorna o processo em execução
	private ProcessInstance getProcessForEntity(long idOcorrencia) {
		ProcessInstance selectedFP = null;
		
		/*Collection <ProcessInstance> processInstances = ksession.getProcessInstances();
		for (ProcessInstance pi : processInstances) {
			RuleFlowProcessInstance rfpi = (RuleFlowProcessInstance)pi;
			long idVariable = (long) rfpi.getVariable("org_fixwo_domain_Ocorrencia_id");
			if(idVariable == idOcorrencia){
				processInstance = rfpi;
			}
		}*/
		
		/*ProcessInstance pi = ksession.getProcessInstance(processInstance.getId());
		RuleFlowProcessInstance rfpi = (RuleFlowProcessInstance)pi;
		Long idVariable = (Long) rfpi.getVariable("org_fixwo_domain_Ocorrencia_id");
		if(idVariable.equals(idOcorrencia)){
			processInstance = rfpi;
		}*/
		
		selectedFP = processInstance;
		if(selectedFP == null){
			selectedFP = startNewFixwoProcess(idOcorrencia);
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
		RuntimeManager manager = createRuntimeManager();
		RuntimeEngine engine = manager.getRuntimeEngine(EmptyContext.get());
		ksession = engine.getKieSession();
		taskService = engine.getTaskService();
		addTaskConnector();
	}
	
	private static void addTaskConnector(){
		ksession.getWorkItemManager().registerWorkItemHandler("Manual Task",new SystemOutWorkItemHandler());
		ksession.getWorkItemManager().registerWorkItemHandler("Service Task",new ServiceTaskHandler());
		ksession.getWorkItemManager().registerWorkItemHandler("Send Task",new SystemOutWorkItemHandler());
	}
	private static RuntimeManager createRuntimeManager() {
		JBPMHelper.startH2Server();
		JBPMHelper.setupDataSource();
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("org.jbpm.persistence.jpa");
		RuntimeEnvironmentBuilder builder = RuntimeEnvironmentBuilder.Factory.get()
			.newDefaultBuilder().entityManagerFactory(emf)
			.addAsset(ResourceFactory.newClassPathResource("fixwoProcess.bpmn2"),ResourceType.BPMN2);
		return RuntimeManagerFactory.Factory.get()
			.newSingletonRuntimeManager(builder.get());
	}
}
