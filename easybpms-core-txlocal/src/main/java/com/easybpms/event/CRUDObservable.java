package com.easybpms.event;

import com.easybpms.codegen.AbstractContext;

public class CRUDObservable {

	public CRUDObservable() {}
	
	public void notifyObservers(Object arg){
		AbstractContext.getContext().notifyObservers(arg.getClass().getSimpleName(), arg);
	}   
}
