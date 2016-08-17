package com.easybpms.event;

import com.easybpms.codegen.AbstractContext;

//public class CRUDObservable extends Observable{
public class CRUDObservable {

	public CRUDObservable() {
		/*AbstractContext context = AbstractContext.getContext();
		ArrayList<Observer> observers = (ArrayList<Observer>)context.getObservers(this.getClass().getSimpleName());
		for(Observer observer : observers){ 
			this.addObserver(observer);
		}*/
	}
	
	public void notifyObservers(Object arg){
		AbstractContext.getContext().notifyObservers(arg.getClass().getSimpleName(), arg);
	}
	/*@Override
    public void notifyObservers (Object arg){
        setChanged();
        super.notifyObservers(arg);
        
    }*/   
}
