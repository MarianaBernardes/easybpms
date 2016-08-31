package com.easybpms.event;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import javax.persistence.NoResultException;

import com.easybpms.bpms.AbstractBpmsInterface;
import com.easybpms.db.CRUDException;
import com.easybpms.db.dao.CRUDActivityInstance;
import com.easybpms.db.dao.CRUDParameterInstance;
import com.easybpms.domain.ActivityInstance;
import com.easybpms.domain.Parameter;
import com.easybpms.domain.ParameterInstance;

public class TaskExecutedObserver implements Observer{
	private String taskIdBpms;
	
	/**
	 * TaskExecutedObserver instanciado no Context
	 * @param taskIdBpms - id da tarefa de usuario no bpmn
	 */
	public TaskExecutedObserver (String taskIdBpms) {
		this.taskIdBpms = taskIdBpms;
	}
	
	/**
	 * Observador notificado todas as vezes que o metodo notifyObservers for chamado na aplicacao
	 * Observador sera escolhido quando a atividade parada no bpms for identificada
	 * @param arg - Instancia da classe que executa a tarefa na aplicacao
	 */
	public void update(Observable o, Object arg) {
		
		String className = arg.getClass().getName(); //nomedopacote.nomedaclasse
		
		/**
		 * @param inputParamName - parametro de entrada que deve ser inserido para todas as atividades de usuario
		 * do processo ne negocio. 
		 * Para executar a atividade correta, esse parametro tem que ser correspondente ao id da entidade de dominio 
		 * que executa a atividade na aplicacao. Exemplo: parametro de entrada easybpms_com_solicitarviagem_domain_Viagem_id 
		 */
		String inputParamName = "easybpms." + className + ".id";
		inputParamName = inputParamName.replace(".","_"); 	
        
		/**
		 * @param inputParamValue - valor do parametro de entrada buscado na aplicacao. Exemplo: valor do id da entidade Viagem
		 * Obs: Utiliza API de reflexao para invocar o metodo da instancia da entidade de dominio da aplicacao e seu respectivo valor
		 */
		String inputParamValue = null;
		try {
			Method m = arg.getClass().getMethod("getId");
			Long i = (Long)m.invoke(arg);
			inputParamValue = i.toString(); 
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		/**
		 * @param - listIp - lista de todas as instancias parametro do easybpms
		 */
		List<ParameterInstance> listIp = null;
		try {
			listIp = CRUDParameterInstance.readAll();
		} catch (NoResultException e) {		
			e.printStackTrace();
		} catch (CRUDException e) {
			e.printStackTrace();
		}
		
		List<ActivityInstance> taskInstances = new ArrayList<ActivityInstance>();
		
		/**
		 * Para cada instancia parametro da lista identifica as instancias atividades que tem parametro de 
		 * entrada igual ao id da entidade de dominio e que estao paradas no bpms (status = "Reserved"). 
		 * Em outras palavras, em que o inputParamName e igual a algum parametro de entrada do easybpms e o 
		 * inputParamValue e igual a alguma instancia parametro do easybpms.
		 */
		
		for (ParameterInstance ip : listIp){
			ActivityInstance activityInstance = ip.getActivityInstance();
			if (!activityInstance.isCurrentTransaction()) {
				if(ip.getActivityInstance().getActivity().getIdBpms().equals(taskIdBpms) && 
						ip.getActivityInstance().getStatus().equals("Reserved") && ip.getType().equals("input") && 
						ip.getParameter().getName().equals(inputParamName) && ip.getValue().equals(inputParamValue)){ 
					taskInstances.add(ip.getActivityInstance()); 
					break;
				}
			}
		}
		
		/**
		 * Para cada instancia atividade identificada, cria o parametro de saida
		 * O parametro de saida corresponde a algum atributo da entidade de dominio da aplicacao que e necessario
		 * para executar a atividade no processo. Ele e buscado por meio da API de reflexao. Exemplo: Para executar
		 * a atividade Analisar Solicitacao de Viagem, o atributo aprovar tem que ser true ou false. Ele sera um
		 * parametro de saida para a atividade para que o bpms de um passo no processo
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
		 * Para executar as instancias atividades, e necessario enviar o valor dos parametros de saida
		 * @param params - mapa que armazena o nome dos parametros de saida e seus respectivos valores
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
			
			System.out.println("Tarefa " + task.getActivity().getName() + " executada");
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
