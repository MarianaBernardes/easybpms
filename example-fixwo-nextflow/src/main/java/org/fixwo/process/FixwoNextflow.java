package org.fixwo.process;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.fixwo.domain.Ocorrencia;
import org.fixwo.tasks.AvaliarSolucao;
import org.fixwo.tasks.ClassificarEEncaminharAoSetorResponsavel;
import org.fixwo.tasks.CriarOcorrencia;
import org.fixwo.tasks.EnviarFeedbackAoSolicitante;
import org.fixwo.tasks.ExternalTaskHandler;
import org.nextflow.owm.Configuration;
import org.nextflow.owm.WorkflowObjectFactory;

public class FixwoNextflow {
	
	private static WorkflowObjectFactory factory = FixwoNextflow.getFactory("jwfc:jbpm:fixwo.bpmn2", FixwoCallback.class);
	
	private static FixwoProcess fixwoProcess;
	private Map <String, ExternalTaskHandler> mapTasks;
	
	public FixwoNextflow(){
		mapTasks = new HashMap <String, ExternalTaskHandler>();
		mapTasks.put ("Criar Ocorrencia", new CriarOcorrencia());
		mapTasks.put ("Classificar e Encaminhar ao Setor Responsavel", new ClassificarEEncaminharAoSetorResponsavel());
		mapTasks.put ("Enviar Feedback ao Solicitante", new EnviarFeedbackAoSolicitante());
		mapTasks.put ("Avaliar Solucao", new AvaliarSolucao());
	}
	
	public void executeFlow(Ocorrencia ocorrencia){
		fixwoProcess = getProcessForEntity(ocorrencia.getId());
		List <String> availableTasks = fixwoProcess.getAvailableTasks();
		ExternalTaskHandler handler = getExternalTaskHandler(availableTasks);
		handler.executeUserTask(ocorrencia,fixwoProcess);
	}
	
	//Iniciar o processo
	private FixwoProcess startNewFixwoProcess() {
		return factory.start(FixwoProcess.class);
	}
	
	//Retorna o processo em execução
	private FixwoProcess getProcessForEntity(Long id) {
		FixwoProcess selectedFP = null;
		List <FixwoProcess> processes = factory.getRepository().getRunningProcesses (FixwoProcess.class);
		for (FixwoProcess fp : processes) {
			Ocorrencia o = fp.getData();
			Long vId = o.getId();
			if(vId == id){
				selectedFP = fp ;
			}
		}
		if(selectedFP == null ){
			selectedFP = startNewFixwoProcess();
		}
		return selectedFP;
	}
	
	//Retorna a tarefa disponível
	private ExternalTaskHandler getExternalTaskHandler(List<String> availableTasks) {
		for (String task : availableTasks){
			if (fixwoProcess.isTaskAvailable(task)){
				return mapTasks.get(task);
			}
		}
		return null;
	}
	
	//Conecta ao bpms
	private static WorkflowObjectFactory getFactory(String url, Class<?>...callbacks) {
		if(factory == null){
			//logInfo(url, callbacks);
			//factory = createFactory(url, callbacks);
			Configuration configuration = new Configuration(url);
			for (Class<?> class1 : callbacks) {
				configuration.addCallbackClass(class1);
			}
			return configuration.createFactory();
		}
		return factory;
	}

	/*private static WorkflowObjectFactory createFactory(String url, Class<?>... callbacks) {
		Configuration configuration = new Configuration(url);
		for (Class<?> class1 : callbacks) {
			configuration.addCallbackClass(class1);
		}
		return configuration.createFactory();
	}

	private static void logInfo(String url, Class<?>... callbacks) {
		System.err.println("Initializing Workflow Object Factory...");
		System.err.println("  + URL: "+url);
		for (Class<?> class1 : callbacks) {
			System.err.println("  + Callback: "+class1.getName());
		}
	}*/
}
