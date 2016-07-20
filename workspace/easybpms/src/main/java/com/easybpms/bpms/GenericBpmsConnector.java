package com.easybpms.bpms;

import java.util.List;
import java.util.Map;

import javax.persistence.NoResultException;

import com.easybpms.bd.CRUDException;
import com.easybpms.bd.dao.*;
import com.easybpms.domain.Process;
import com.easybpms.domain.*;

public class GenericBpmsConnector {
	
	public void execute(String taskIdBpms, String taskName, String taskInstanceId, String statusTask, Map<String, Object> params, String processId, String processInstanceId) {
		
		Activity activity = new Activity();
		Process process = new Process();
		Parameter parameter = new Parameter();
		ProcessInstance processInstance = new ProcessInstance();
		ActivityInstance activityInstance = new ActivityInstance();
		ParameterInstance parameterInstance = new ParameterInstance();
		String tenancy = null;
		
		//Buscar processo
		process.setIdBpms(processId);
		try {
			process = (Process) CRUDEntity.read(process);
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
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		//Buscar atividade
		activity.setName(taskName);
		activity.setIdBpms(taskIdBpms);
		activity.setProcess(process);
		try {
			activity = (Activity) CRUDEntity.read(activity);
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
					//Descobrir o tenancy que a atividade pertence
					if (entry.getKey().contains("tenancy")){
						tenancy = (String) entry.getValue();
					}else{
						//Buscar parametro
						parameter.setName(entry.getKey());
						parameter.setType("input");
						parameter.setActivity(activity);
						try {
							parameter = (Parameter)CRUDEntity.read(parameter);
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
			}
			
			List<User> users = activity.getUserGroup().getUsers();
			User user = users.get(0);
			//procurar usuario com menor qtd de instancias atividades que pertence ao grupo de usuario da atividade passada como parametro
			for (int i = 0; i<users.size(); i++){
				if ((users.get(i).getTenancy().equals(tenancy)) && (users.get(i).getActivityInstances().size() <= user.getActivityInstances().size())){
					user = users.get(i);
				}
			}
			user.addActivityInstance(activityInstance);
			activity.addActivityInstance(activityInstance);
			processInstance.addActivityInstance(activityInstance);
			
			try {
				//Criar instancia atividade
				CRUDEntity.create(activityInstance);
			} catch (CRUDException e1) {
				e1.printStackTrace();
			}
		}catch (Exception e) {
			e.printStackTrace();
		}	

	}
	
	public static void endProcess(String processId, String processInstanceId) {
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
			pi = (ProcessInstance) CRUDEntity.read(pi);
			CRUDProcessInstance.update(pi,"Completed");
		} catch (CRUDException e) {
			e.printStackTrace();
		}
	}

}
