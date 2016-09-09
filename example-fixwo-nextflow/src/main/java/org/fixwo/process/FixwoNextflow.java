
  /*Copyright (C) 2013 Rógel Garcia
 
  Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated 
  documentation files (the "Software"), to deal in the Software without restriction, including without limitation 
  the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and 
  to permit persons to whom the Software is furnished to do so, subject to the following conditions:
  
  The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 
  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO 
  THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS 
  OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, 
  ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.*/
 
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
	
	private static String url = "jwfc:jbpm:fixwo.bpmn2";
	private static WorkflowObjectFactory factory = FixwoNextflow.getFactory(url, FixwoCallback.class);
	
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
		handler.executeTask(ocorrencia,fixwoProcess);
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
			logInfo(url, callbacks);
			factory = createFactory(url, callbacks);
			System.err.println("Factory initialized!");
		}
		return factory;
	}

	private static WorkflowObjectFactory createFactory(String url, Class<?>... callbacks) {
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
	}
}
