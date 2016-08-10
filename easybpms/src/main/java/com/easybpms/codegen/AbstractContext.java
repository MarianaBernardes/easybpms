package com.easybpms.codegen;

import java.util.List;
import java.util.Observer;

import com.easybpms.bpms.BpmsSession;


public abstract class AbstractContext {

	private static AbstractContext instance = null;

	public static AbstractContext getContext() {
		if (instance == null){
			try {
				Class<?> concreteContextClass = Class.forName("com.easybpms.codegen.Context");
				instance = (AbstractContext)concreteContextClass.newInstance();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return instance;
	}
	
	public abstract void setBpmsSession(BpmsSession bpmsSession);
	
	public abstract List<Observer> getObservers(String nameClasse);
}
