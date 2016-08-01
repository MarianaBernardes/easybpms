package com.bpms.jbpm;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.jbpm.bpmn2.handler.ServiceTaskHandler;
import org.jbpm.bpmn2.handler.WorkItemHandlerRuntimeException;
import org.kie.api.runtime.process.WorkItem;
import org.kie.api.runtime.process.WorkItemManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ServiceTaskWorkItem extends ServiceTaskHandler {
	
private static final Logger logger = LoggerFactory.getLogger(ServiceTaskWorkItem.class);
	
	@Override
	public void executeWorkItem(WorkItem workItem, WorkItemManager manager) {
		String interfaceName = (String) workItem.getParameter("Interface");
        String interfaceImplementationRef = (String) workItem.getParameter("interfaceImplementationRef"); 
        String operation = (String) workItem.getParameter("Operation");
        String parameterType = (String) workItem.getParameter("ParameterType");
        Object parameter = workItem.getParameter("Parameter");
        
        String[] services = {interfaceImplementationRef,interfaceName};
        Class<?> c = null;
        
        for(String serv : services) {
            try {
                c = Class.forName(serv);
                break;
            } catch (ClassNotFoundException cnfe) {
            	if(serv.compareTo(services[services.length - 1]) == 0) {
                    handleException(cnfe, interfaceName, interfaceImplementationRef, operation, parameterType, parameter);
                }
            }
        }
        
        try {
            Object instance = c.newInstance();
            /*Class<?>[] classes = null;
            Object[] params = null;
            if (parameterType != null) {
                classes = new Class<?>[] {
                    Class.forName(parameterType)
                };
                params = new Object[] {
                    parameter
                };
            }*/
            //Method method = c.getMethod(operation, classes);
            Method method = c.getMethod(operation);
            //Object result = method.invoke(instance, params);
            Object result = method.invoke(instance);
            
            Map<String, Object> results = new HashMap<String, Object>();
            results.put("Result", result);
            manager.completeWorkItem(workItem.getId(), results);
        /*} catch (ClassNotFoundException cnfe) {
           handleException(cnfe, interfaceName, interfaceImplementationRef, operation, parameterType, parameter);*/
        } catch (InstantiationException ie) {
            handleException(ie, interfaceName, interfaceImplementationRef, operation, parameterType, parameter);
        } catch (IllegalAccessException iae) {
            handleException(iae, interfaceName, interfaceImplementationRef, operation, parameterType, parameter);
        } catch (NoSuchMethodException nsme) {
            handleException(nsme, interfaceName, interfaceImplementationRef, operation, parameterType, parameter);
        } catch (InvocationTargetException ite) {
            handleException(ite, interfaceName, interfaceImplementationRef, operation, parameterType, parameter);
        } catch( Throwable cause ) { 
            handleException(cause, interfaceName, interfaceImplementationRef, operation, parameterType, parameter);
        }
	}
	
	private void handleException(Throwable cause, String service, String interfaceImplementationRef, String operation, String paramType, Object param) { 
        logger.debug("Handling exception {} inside service {} or {} and operation {} with param type {} and value {}",
                cause.getMessage(), service, operation, paramType, param);
        WorkItemHandlerRuntimeException wihRe;
        if( cause instanceof InvocationTargetException ) { 
            Throwable realCause = cause.getCause();
            wihRe = new WorkItemHandlerRuntimeException(realCause);
            wihRe.setStackTrace(realCause.getStackTrace());
        } else { 
            wihRe = new WorkItemHandlerRuntimeException(cause);
            wihRe.setStackTrace(cause.getStackTrace());
        }
        wihRe.setInformation("Interface", service);
        wihRe.setInformation("InterfaceImplementationRef", interfaceImplementationRef);
        wihRe.setInformation("Operation", operation);
        wihRe.setInformation("ParameterType", paramType);
        wihRe.setInformation("Parameter", param);
        wihRe.setInformation(WorkItemHandlerRuntimeException.WORKITEMHANDLERTYPE, this.getClass().getSimpleName());
        throw wihRe;
        
    }
	
	@Override
	public void abortWorkItem(WorkItem workItem, WorkItemManager manager) {
		//Tarefa não pode ser abortada
	}
}
