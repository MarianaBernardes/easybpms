package com.easybpms.event;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import com.easybpms.bd.CRUDException;
import com.easybpms.bd.dao.CRUDEntity;
import com.easybpms.bd.dao.CRUDParameterInstance;
import com.easybpms.bpms.AbstractBpmsInterface;
import com.easybpms.domain.ParameterInstance;
import com.easybpms.domain.Process;
import com.easybpms.domain.Property;

public class StartProcessObserver implements Observer{
	private String processIdBpms; //nomedopacote_nomedaclasse
	
	public StartProcessObserver (String processIdBpms){
		this.processIdBpms = processIdBpms; 
	}

	public void update(Observable o, Object arg) {
		
		String processidbpms; //nomedopacote.nomedaclasse
		processidbpms = this.processIdBpms.replace("_",".");
		
		String className = arg.getClass().getName();
		
		System.out.println("\nStart Process Observer do processo " + processidbpms + " notificado.Objeto passado: " + className + "\n");
		
		if(!className.equals(processidbpms)){ 
			return;
		}
		
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
		
		for (ParameterInstance ip : listIp){
			if(ip.getParameter().getName().equals(inputParamName) && ip.getValue().equals(inputParamValue)){ 
				return; //Processo ja iniciado
			}
		}
		
		//Processo nao iniciado
		Process p = new Process();
		p.setIdBpms(this.processIdBpms);
		
		List<Property> listP = null;
		
		try {
			p = (Process) CRUDEntity.read(p);
			listP = p.getProperties();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		Map<String,Object> params = new HashMap<String, Object>();
		for (Property pr : listP){
			String[] partsProperty = pr.getName().split("\\_");
			//String classProperty = partsProperty[partsProperty.length - 2];
			//if (className.contains(classProperty)){
				String methodName = partsProperty[partsProperty.length - 1]; 
				methodName = "get" + methodName.substring(0,1).toUpperCase() + methodName.substring(1);
				
				try {
					Method m = arg.getClass().getMethod(methodName); 
					Object i = m.invoke(arg); 
					String propertyValue = i.toString(); 
					params.put(pr.getName(), propertyValue);
				} catch (Exception e) {
					e.printStackTrace();
				}	
			//}
		}
		
		if (params.isEmpty()){
			params = null;
		}
		String processInstanceId = "";
		processInstanceId = "" + AbstractBpmsInterface.getBpmsInterface().startProcess(this.processIdBpms, params);
		
	}

}
