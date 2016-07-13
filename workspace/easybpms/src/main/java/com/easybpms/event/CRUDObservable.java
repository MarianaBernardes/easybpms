package com.easybpms.event;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import com.easybpms.codegen.AbstractContext;

public class CRUDObservable extends Observable{

	public CRUDObservable() {
		AbstractContext context = AbstractContext.getContext();
		ArrayList<Observer> observers = (ArrayList<Observer>)context.getObservers(this.getClass().getSimpleName());
		for(Observer observer : observers){ 
			this.addObserver(observer);
		}
	}
	@Override
    public void notifyObservers (Object arg){
        setChanged();
        super.notifyObservers(arg);
        
    }   
}
