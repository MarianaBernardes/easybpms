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

	public TaskExecutedObserver (String taskName) {
		this.taskIdBpms = taskName;
	}

	public void update(Observable o, Object arg) {
		System.out.println("\nTaskExecuted da tarefa " + this.taskIdBpms + " notificado. Objeto passado: " + arg.getClass().getName());
		
		String className = arg.getClass().getName();
		
		String inputParamName = "easybpms." + className + ".id";
		inputParamName = inputParamName.replace(".","_"); 	
        
		String inputParamValue = null;
		try {
			Method m = arg.getClass().getMethod("getId");
			Integer i = (Integer)m.invoke(arg);
			inputParamValue = i.toString(); 
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		List<ParameterInstance> listIp = null;
		try {
			listIp = CRUDParameterInstance.readAll();
		} catch (CRUDException e) {
			e.printStackTrace();
		}
		
		List<ActivityInstance> taskInstances = new ArrayList<ActivityInstance>();
		
		/*identifica as instancias atividades que tem parametro de entrada
		igual ao id da entidade de dominio e que estao paradas no bpms*/
		for (ParameterInstance ip : listIp){
			if(ip.getActivityInstance().getActivity().getIdBpms().equals(taskIdBpms) && 
					ip.getActivityInstance().getStatus().equals("Reserved") && ip.getType().equals("input") && 
					ip.getParameter().getName().equals(inputParamName) && ip.getValue().equals(inputParamValue)){ 
				taskInstances.add(ip.getActivityInstance()); 
				break;
			}
		}
		
		/*criar o parametro de saida de cada instancia atividade identificada*/
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
		
		//executar instancias atividades
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
			statusTask = "" + AbstractBpmsInterface.getBpmsInterface().executeTask(Long.valueOf(task.getIdBpms()), "Administrator", params);
			try {
				CRUDActivityInstance.update(task,statusTask);
			} catch (CRUDException e) {
				e.printStackTrace();
			}
			
		}
	}

}
