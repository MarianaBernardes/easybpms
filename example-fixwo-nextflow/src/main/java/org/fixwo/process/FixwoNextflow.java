
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

import java.util.List;

import org.fixwo.domain.Ocorrencia;
import org.nextflow.owm.Configuration;
import org.nextflow.owm.WorkflowObjectFactory;

public class FixwoNextflow {
	
	private static String url = "jwfc:jbpm:fixwo.bpmn2";
	private static WorkflowObjectFactory factory = FixwoNextflow.getFactory(url, FixwoCallback.class);
	
	private static FixwoProcess fixwoProcess;
	
	
	//Iniciar o processo
	private void startNewFixwoProcess() {
		fixwoProcess =  factory.start(FixwoProcess.class);
	}
	
	//Retorna o processo em execução
	private void getProcessForEntity(long id) {
		
		List <FixwoProcess> processes = factory.getRepository().getRunningProcesses (FixwoProcess.class);
		for (FixwoProcess fp : processes) {
			Ocorrencia o = fp.getData();
			long vId = o.getId();
			if(vId == id){
				fixwoProcess = fp ;
			}
		}
		if(fixwoProcess == null ){
			startNewFixwoProcess();
		}
	}
	
	//Retorna a tarefa disponível
	private String getAvailableTask() {
		List <String> availableTasks = fixwoProcess.getAvailableTasks ();
		String taskHandler = null;
		
		for (String task : availableTasks){
			if (fixwoProcess.isTaskAvailable(task)){
				taskHandler = task;
				return taskHandler;
			}
		}
		return taskHandler;
		
	}
	
	//Executar tarefa
	protected void executeTask(Ocorrencia o){
		getProcessForEntity(o.getId());
		String task = getAvailableTask();
		
		switch (task){
		case "Criar Ocorrencia":
			fixwoProcess.criarOcorrencia(o.getId());
			break;
		case "Classificar e Encaminhar ao Setor Responsavel":
			fixwoProcess.classificarEEncaminharAoSetorResponsavel(o.getStatus(), o.getSetor());
			break;
		case "Enviar Feedback ao Solicitante":
			fixwoProcess.enviarFeedbackAoSolicitante(o.getStatus(), o.getFeedback());
			break;
		case "Avaliar Solucao":
			fixwoProcess.avaliarSolucao(o.getAvaliacao());
			break;
	}
	}
	
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
