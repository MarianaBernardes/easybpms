package org.fixwo.dao;

import java.util.List;

import org.fixwo.domain.Ocorrencia;
import org.fixwo.process.Connection;
import org.fixwo.process.FixwoProcess;
import org.nextflow.owm.WorkflowObjectFactory;

public class OcorrenciaDAO {
	
	FixwoProcess fixwoProcess;
	WorkflowObjectFactory factory = Connection.getFactory();
	
	public void create (Ocorrencia o){
		fixwoProcess = getProcessForEntity(o.getId());
		fixwoProcess.criarOcorrencia(o.getId());
	}
	
	public void update (Ocorrencia o){
		fixwoProcess = getProcessForEntity(o.getId());
		List <String> availableTasks = fixwoProcess.getAvailableTasks ();
		String task = getAvailableTask(availableTasks);
		executeTask(o,task);
		
	}
	
	
	//Iniciar o processo
	private FixwoProcess startNewFixwoProcess() {
		return factory.start(FixwoProcess.class);
	}
	
	//Retorna o processo em execução
	private FixwoProcess getProcessForEntity(long id) {
		FixwoProcess selectedFP = null ;
		List <FixwoProcess> processes = factory.getRepository().getRunningProcesses (FixwoProcess.class);
		for (FixwoProcess fp : processes) {
			Ocorrencia o = fp.getData();
			long vId = o.getId();
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
	private String getAvailableTask(List<String> availableTasks) {
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
	private void executeTask(Ocorrencia o, String task){
		switch (task){
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

}
