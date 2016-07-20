package com.easybpms.event;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import com.easybpms.bd.CRUDException;
import com.easybpms.bd.dao.CRUDActivityInstance;
import com.easybpms.bd.dao.CRUDParameterInstance;
import com.easybpms.bpms.AbstractBpmsInterface;
import com.easybpms.domain.ActivityInstance;
import com.easybpms.domain.Parameter;
import com.easybpms.domain.ParameterInstance;

public class TaskExecutedObserver implements Observer{
	private String taskIdBpms;
	
	/**
	 * TaskExecutedObserver instanciado no Context
	 * @param taskIdBpms - id da tarefa de usuário no bpmn
	 */
	public TaskExecutedObserver (String taskIdBpms) {
		this.taskIdBpms = taskIdBpms;
	}
	
	/**
	 * Observador notificado todas as vezes que o método notifyObservers for chamado na aplicação
	 * Observador será escolhido quando a atividade parada no bpms for identificada
	 * @param arg - Instância da classe que executa a tarefa na aplicação
	 */
	public void update(Observable o, Object arg) {
		System.out.println("\nTaskExecuted da tarefa " + this.taskIdBpms + " notificado. Objeto passado: " + arg.getClass().getName());
		
		String className = arg.getClass().getName(); //nomedopacote.nomedaclasse
		
		
		/**
		 * @param inputParamName - parâmetro de entrada que deve ser inserido para todas as atividades de usuário
		 * do processo ne negócio. 
		 * Para executar a atividade correta, esse parâmetro tem que ser correspondente ao id da entidade de domínio 
		 * que executa a atividade na aplicação. Exemplo: parâmetro de entrada easybpms_com_solicitarviagem_domain_Viagem_id 
		 */
		String inputParamName = "easybpms." + className + ".id";
		inputParamName = inputParamName.replace(".","_"); 	
        
		/**
		 * @param inputParamValue - valor do parâmetro de entrada buscado na aplicação. Exemplo: valor do id da entidade Viagem
		 * Obs: Utiliza API de reflexão para invocar o método da instância da entidade de domínio da aplicação e seu respectivo valor
		 */
		String inputParamValue = null;
		try {
			Method m = arg.getClass().getMethod("getId");
			Integer i = (Integer)m.invoke(arg);
			inputParamValue = i.toString(); 
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		/**
		 * @param - listIp - lista de todas as instâncias parâmetro do easybpms
		 */
		List<ParameterInstance> listIp = null;
		try {
			listIp = CRUDParameterInstance.readAll();
		} catch (CRUDException e) {
			e.printStackTrace();
		}
		
		List<ActivityInstance> taskInstances = new ArrayList<ActivityInstance>();
		
		/**
		 * Para cada instância parâmetro da lista identifica as instâncias atividades que tem parâmetro de 
		 * entrada igual ao id da entidade de dominio e que estão paradas no bpms (status = "Reserved"). 
		 * Em outras palavras, em que o inputParamName é igual à algum parâmetro de entrada do easybpms e o 
		 * inputParamValue é igual a alguma instância parâmetro do easybpms.
		 */
		
		for (ParameterInstance ip : listIp){
			if(ip.getActivityInstance().getActivity().getIdBpms().equals(taskIdBpms) && 
					ip.getActivityInstance().getStatus().equals("Reserved") && ip.getType().equals("input") && 
					ip.getParameter().getName().equals(inputParamName) && ip.getValue().equals(inputParamValue)){ 
				taskInstances.add(ip.getActivityInstance()); 
				break;
			}
		}
		
		/**
		 * Para cada instância atividade identificada, cria o parâmetro de saída
		 * O parâmetro de saída corresponde à algum atributo da entidade de domínio da aplicação que é necessário
		 * para executar a atividade no processo. Ele é buscado por meio da API de reflexão. Exemplo: Para executar
		 * a atividade Analisar Solicitação de Viagem, o atributo aprovar tem que ser true ou false. Ele será um
		 * parâmetro de saída para a atividade para que o bpms dê um passo no processo
		 */

		for (ActivityInstance task : taskInstances) {  
			for (Parameter p : task.getActivity().getParameters()) { 
				if (p.getType().equals("output")){
					ParameterInstance pi = new ParameterInstance();
					pi.setType("output");
					pi.setParameter(p);
					pi.setActivityInstance(task);
					
					String[] entityParts = p.getName().split("\\_");  
					String mName = entityParts[entityParts.length - 1]; 
					mName = "get" + mName.substring(0,1).toUpperCase() + mName.substring(1); 
					try {
						Method m = arg.getClass().getMethod(mName); 
						Object i = m.invoke(arg); 
						pi.setValue(i.toString()); 
					} catch (Exception e) {
						e.printStackTrace();
					}					
					task.addParameterInstance(pi);
				}
			}
		}
		
		/**
		 * Para executar as instâncias atividades, é necessário enviar o valor dos parâmetros de saída
		 * @param params - mapa que armazena o nome dos parâmetros de saída e seus respectivos valores
		 */
		for (ActivityInstance task : taskInstances) {
			Map<String,Object> params = new HashMap<String,Object>(); 
			for (ParameterInstance pi : task.getParameterInstances()) {
				if (pi.getType().equals("output")){
					params.put(pi.getParameter().getName(), pi.getValue());
				}
			}
			if (params.isEmpty()){
				params = null;
			}
			
			String statusTask;
			/**
			 * @param statusTask - recebe o status da tarefa executada no bpms
			 */
			statusTask = "" + AbstractBpmsInterface.getBpmsInterface().executeTask(Long.valueOf(task.getIdBpms()), "Administrator", params);
			
			/**
			 * Atualiza o status da tarefa no easybpms para completada
			 */
			try {
				CRUDActivityInstance.update(task,statusTask);
			} catch (CRUDException e) {
				e.printStackTrace();
			}
			
		}
	}

}
