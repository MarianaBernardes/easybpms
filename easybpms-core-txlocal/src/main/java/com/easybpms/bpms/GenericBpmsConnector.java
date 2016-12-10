package com.easybpms.bpms;

import java.util.List;
import java.util.Map;

import javax.persistence.NoResultException;

import com.easybpms.db.CRUDException;
import com.easybpms.db.dao.*;
import com.easybpms.domain.Process;
import com.easybpms.domain.*;

public class GenericBpmsConnector {
	
	static int cont = 0;
	static String idEndProcess = null;
	
	public void executeHumanTask(String taskIdBpms, String taskName, String taskInstanceId, String statusTask, Map<String, Object> params, String processId, String processInstanceId) {
		
		Activity activity = new Activity();
		Process process = new Process();
		Parameter parameter = new Parameter();
		ProcessInstance processInstance = new ProcessInstance();
		ActivityInstance activityInstance = new ActivityInstance();
		activityInstance.setCurrentTransaction(true);
		ParameterInstance parameterInstance = new ParameterInstance();
		User user = null;
		
		//Buscar processo
		process.setIdBpms(processId);
		try {
			process = (Process) CRUDEntity.read(process);
		} catch (NoResultException e) {		
			e.printStackTrace();
		} catch (CRUDException e) {
			e.printStackTrace();
		}
		
		//Definir instancia processo
		processInstance.setIdBpms(processInstanceId);
		processInstance.setProcess(process);
		try {
			//Buscar instancia processo
			processInstance = (ProcessInstance) CRUDEntity.read(processInstance);
		}catch (NoResultException e) { 
			//Criar instancia processo
			try { 
				CRUDProcessInstance.update(processInstance,"Active");
				process.addProcessInstance(processInstance);
				CRUDEntity.create(processInstance);
			} catch (CRUDException e1) {
				e1.printStackTrace();
			}
		}catch (CRUDException e) {
			e.printStackTrace();
		}
		
		//Buscar atividade
		activity.setName(taskName);
		activity.setIdBpms(taskIdBpms);
		activity.setProcess(process);
		try {
			activity = (Activity) CRUDEntity.read(activity);
		} catch (NoResultException e) {		
			e.printStackTrace();
		} catch (CRUDException e) {
			e.printStackTrace();
		}
		
		//Definir instancia atividade
		activityInstance.setIdBpms(taskInstanceId);
		activityInstance.setStatus(statusTask);
		activityInstance.setActivity(activity);
		activityInstance.setProcessInstance(processInstance);
		try {
			//Buscar instancia atividade
			activityInstance = (ActivityInstance) CRUDEntity.read(activityInstance);
		}catch (NoResultException e) { 
			for (Map.Entry<String, Object> entry : params.entrySet())
			{
				if (entry.getKey().contains("easybpms")){
					//Buscar parametro
					parameter.setName(entry.getKey());
					parameter.setType("input");
					parameter.setActivity(activity);
					try {
						parameter = (Parameter)CRUDEntity.read(parameter);
					} catch (NoResultException e1) {		
						e1.printStackTrace();
					} catch (CRUDException e1) {
						e1.printStackTrace();
					}
					//Definir instancia parametro
					parameterInstance.setValue(entry.getValue().toString());
					parameterInstance.setType("input");
					parameter.addParameterInstance(parameterInstance);
					activityInstance.addParameterInstance(parameterInstance);
				}
			}
			
			try{
				//Buscar Usuario
				List<User> users = activity.getUserGroup().getUsers();
				user = users.get(0);
				for (int i = 0; i<users.size(); i++){
					if (users.get(i).getActivityInstances().size() <= user.getActivityInstances().size()){
						user = users.get(i);
					}
				}
				//Alocar usuario para a tarefa
				user.addActivityInstance(activityInstance);
			}catch(Exception e1) {
				System.err.println("Nao existe usuarios para o grupo " + activity.getUserGroup().getName()
						           + "\nTarefa " + activityInstance.getActivity().getName() + " nao foi "
						           + "alocada para nenhum usuario!");
				
			}	
			
			activity.addActivityInstance(activityInstance);
			processInstance.addActivityInstance(activityInstance);
			
			try {
				//Criar instancia atividade
				CRUDEntity.create(activityInstance);
			} catch (CRUDException e1) {
				e1.printStackTrace();
			}
		}catch (CRUDException e) {
			e.printStackTrace();
		}	

	}
	
	public static void endProcess(String processId, String processInstanceId) {
		/**
		 * Variaveis cont e idEndProcess:
		 * contador criado para garantir que o fim de processo seja contabilizado somente
		 * uma vez para a mesma instancia processo. Foi necessario criar ele pois o BPMS
		 * esta chamando o conector de fim de processo mais de uma vez para a mesma instancia
		 * processo
		 */
		if (idEndProcess != null && idEndProcess.equals(processInstanceId)){
			cont++;
		}
		Process p = new Process();
	    p.setIdBpms(processId);
	    
	    try {
			p = (Process) CRUDEntity.read(p);
		} catch (CRUDException e) {
			e.printStackTrace();
		}
	    
	    ProcessInstance pi = new ProcessInstance();
		pi.setIdBpms(String.valueOf(processInstanceId));
		pi.setProcess(p);
		
		try {
			if (cont == 0){
				pi = (ProcessInstance) CRUDEntity.read(pi);
				CRUDProcessInstance.update(pi,"Completed");
				System.out.println("Processo " + pi.getProcess().getName() + " finalizado");
			}
		} catch (NoResultException ex) {
			if (cont == 0){
				System.out.println("Processo " + p.getName() + " finalizado");
			}
		} catch (CRUDException e) {
			e.printStackTrace();
		}
		idEndProcess = processInstanceId;
		cont = 0;
		
		
	}

}
