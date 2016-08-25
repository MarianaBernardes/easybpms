package com.easybpms.jbpm;

import java.util.Map;

import org.jbpm.bpmn2.handler.ReceiveTaskHandler;
import org.jbpm.bpmn2.handler.SendTaskHandler;
import org.kie.api.runtime.KieSession;
//import org.kie.api.runtime.manager.RuntimeEngine;
//import org.kie.api.runtime.manager.RuntimeManager;
import org.kie.api.runtime.process.ProcessInstance;
import org.kie.api.task.TaskService;

import com.easybpms.bpms.AbstractBpmsInterface;

public class ConcreteBpmsInterface extends AbstractBpmsInterface {

	// RuntimeManager - combinacao do motor e do processo de servico de tarefa humana
	//private RuntimeManager manager;
	//private RuntimeEngine engine;
	private KieSession ksession;
	private TaskService taskService;
	
	public void setBpmsSession(JbpmSession jbpmSession) {
		this.ksession = jbpmSession.getKieSession();
		this.taskService = jbpmSession.getTaskService();
		addTaskConnector();
	}
	
	//Metodo deve ser descomentado se os processos nao forem carregados via Spring
	
	/*public void startBPMS(List<String> processes) {
		manager = createRuntimeManager(processes);
		engine = manager.getRuntimeEngine(EmptyContext.get());
		ksession = engine.getKieSession();
		taskService = engine.getTaskService();
		addTaskConnector();
	}*/

	/**
	 * @param process - idBpms do processo registrado no BD da API
	 * @param params - variaveis do processo (properties)
	 * @return id da instancia processo criada no bpms listner - ouvinte do evento final adicionado a sessao 
	 * que sera chamado quando o processo terminar
	 */
	public void startProcess(String process, Map<String, Object> params) {
		EndEventListener listener = new EndEventListener();
		ksession.addEventListener(listener);
		ProcessInstance pi = ksession.startProcess(process, params);
	}

	/**
	 * myWorkItemHandler - conector especifico registrado na sessao que sera
	 * chamado quando o motor chegar em uma atividade de usuario
	 */
	public void addTaskConnector() {
		ksession.getWorkItemManager().registerWorkItemHandler("Human Task",new HumanTaskWorkItem(taskService, ksession));
		ksession.getWorkItemManager().registerWorkItemHandler("Manual Task",new ManualTaskWorkItem());
		ksession.getWorkItemManager().registerWorkItemHandler("Service Task",new ServiceTaskWorkItem());
		ksession.getWorkItemManager().registerWorkItemHandler("Send Task",new SendTaskHandler());
		ksession.getWorkItemManager().registerWorkItemHandler("Receive Task",new ReceiveTaskHandler(ksession));	
	}


	/**
	 * @param taskId - idBpms da tarefa registrado no BD da API
	 * @param user - usuario Administrador do bpms que executara a tarefa
	 * @param params - parametros de saida necessarios para executar a tarefa
	 * @return status da tarefa apos ser completada
	 */
	public String executeTask(long taskId, String user,Map<String, Object> params) {
		taskService.start(taskId, user);
		taskService.complete(taskId, user, params);
		return taskService.getTaskById(taskId).getTaskData().getStatus().name();
	}

	
	//Metodo deve ser descomentado se os processos nao forem carregados via Spring
	
	/*private RuntimeManager createRuntimeManager(
			List<String> bpmnProcessDefinitions) {
		
		RuntimeEnvironmentBuilder environmentBuilder;
		
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("org.jbpm.persistence.jpa");
		
		environmentBuilder = RuntimeEnvironmentBuilder.Factory.get()
				.newDefaultBuilder().entityManagerFactory(emf);
		
		for (String resource : bpmnProcessDefinitions) {
				
			// Caminho relativo
			// environmentBuilder.addAsset(ResourceFactory.newClassPathResource(resource),ResourceType.BPMN2);
			
			//Caminho absoluto
			environmentBuilder.addAsset(
					ResourceFactory.newFileResource(resource),
					ResourceType.BPMN2);
		}
		
		//Adicionar suporte a transacao
		//RuntimeEnvironment environment = environmentBuilder
		// .addEnvironmentEntry(EnvironmentName.TRANSACTION_MANAGER, tm).get();
		

		RuntimeEnvironment environment = environmentBuilder.get();

		//Sessao de conhecimento unica que ira executar todas as instancias de processo
		RuntimeManager manager = RuntimeManagerFactory.Factory.get().newSingletonRuntimeManager(environment);

		//Cada pedido (que esta na chamada de getRuntimeEngine) tera nova sessao de conhecimento 
		//RuntimeManager manager = RuntimeManagerFactory.Factory.get().newPerRequestRuntimeManager(environment);
		 

		//Cada instancia do processo tera sua sessao de conhecimento dedicada para todo o tempo de vida 
		//Precisa fornecer o ID da instancia do processo no getRuntimeEngine (engine = manager.getRuntimeEngine(ProcessInstanceIdContext.get())) 
		//Obs: a instancia processo precisa estar criada 
		//RuntimeManager manager = RuntimeManagerFactory.Factory.get().newPerProcessInstanceRuntimeManager(environment);
		 
		return manager;
	}*/

}
