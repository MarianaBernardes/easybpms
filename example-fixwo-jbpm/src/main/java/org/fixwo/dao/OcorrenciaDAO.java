package org.fixwo.dao;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.fixwo.domain.Ocorrencia;
import org.jbpm.bpmn2.handler.ServiceTaskHandler;
import org.jbpm.process.instance.impl.demo.SystemOutWorkItemHandler;
import org.jbpm.ruleflow.instance.RuleFlowProcessInstance;
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

public class OcorrenciaDAO {
	
	private static ProcessInstance processInstance; 
	private static final String PROCESS_ID = "org_fixwo_domain_Ocorrencia";
	
	private static KieSession ksession;
	private static TaskService taskService;
	private static RuntimeManager manager;
	private static RuntimeEngine engine;
	
	
	public void create (Ocorrencia o){
		executeTask(o);
	}
	
	public void update (Ocorrencia o){
		executeTask(o);
	}
	
	
	//Iniciar o processo
	private void startNewFixwoProcess(long idOcorrencia) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("org_fixwo_domain_Ocorrencia_id", idOcorrencia);
		processInstance = ksession.startProcess (PROCESS_ID,params);
	}
		
	//Retorna o processo em execução
	private void getProcessForEntity(long idOcorrencia) {
		
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
		
		if(processInstance == null){
			startNewFixwoProcess(idOcorrencia);
		}
	}
		
	//Retorna a tarefa disponível
	private Task getAvailableTask(long idProcess) {
		List<Long> listTask = taskService.getTasksByProcessInstanceId(idProcess);
		for (Long idTask : listTask){
			Task task = taskService.getTaskById(idTask);
			String status = task.getTaskData().getStatus().name();
			if (status.equals("Ready") || status.equals("Reserved")){
				return task;
			}
		}
		return null;
	}
		
	//Executar tarefa
	private void executeTask(Ocorrencia o){
		getProcessForEntity(o.getId());
		Task task = getAvailableTask(processInstance.getId());
		Map <String,Object> results = new HashMap <String,Object>();;
		
		switch (task.getName()){
		case "Criar Ocorrencia":
			System.out.println("Tarefa " + task.getName() + " executada");
			executeTask(task.getId(),results);
			break;
		case "Classificar e Encaminhar ao Setor Responsavel": 
			results.put("easybpms_org_fixwo_domain_Ocorrencia_status", o.getStatus());
			results.put("easybpms_org_fixwo_domain_Ocorrencia_setor", o.getSetor());
			System.out.println("Tarefa " + task.getName() + " executada");
			executeTask(task.getId(),results);
			break;
		case "Enviar Feedback ao Solicitante":
			results.put("easybpms_org_fixwo_domain_Ocorrencia_status", o.getStatus());
			results.put("easybpms_org_fixwo_domain_Ocorrencia_feedback", o.getFeedback());
			System.out.println("Tarefa " + task.getName() + " executada");
			executeTask(task.getId(),results);
			break;
		case "Avaliar Solucao":
			results.put("easybpms_org_fixwo_domain_Ocorrencia_avaliacao", o.getAvaliacao());
			System.out.println("Tarefa " + task.getName() + " executada");
			executeTask(task.getId(),results);
			break;
	}
	}
	
	private void executeTask (long idTask, Map <String,Object> results){
		taskService.start(idTask, "Administrator");
		taskService.complete(idTask, "Administrator", results);
	}
	
	public void connect(){
		manager = createRuntimeManager();
		engine = manager.getRuntimeEngine(EmptyContext.get());
		ksession = engine.getKieSession();
		taskService = engine.getTaskService();
		addTaskConnector();
	}
	
	private void addTaskConnector(){
		ksession.getWorkItemManager().registerWorkItemHandler("Manual Task",new SystemOutWorkItemHandler());
		ksession.getWorkItemManager().registerWorkItemHandler("Service Task",new ServiceTaskHandler());
		ksession.getWorkItemManager().registerWorkItemHandler("Send Task",new SystemOutWorkItemHandler());
	}
	private RuntimeManager createRuntimeManager() {
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
