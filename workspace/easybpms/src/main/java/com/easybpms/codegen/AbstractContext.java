package com.easybpms.codegen;

import java.util.List;
import java.util.Observer;


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
	
	public abstract List<Observer> getObservers(String nameClasse);
	/*public List<Observer> getObservers(String nameClasse) {
		List<Observer> observers = null;
		try {
			Class<?> concreteContextClass = Class.forName("com.easybpms.codegen.Context");
			Method m = concreteContextClass.getClass().getMethod("getObservers");
			observers = (List<Observer>)m.invoke(nameClasse);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return observers;
	}*/
}
