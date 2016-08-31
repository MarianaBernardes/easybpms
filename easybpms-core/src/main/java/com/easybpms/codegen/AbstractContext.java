package com.easybpms.codegen;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import javax.persistence.NoResultException;

import com.easybpms.bpms.BpmsSession;
import com.easybpms.db.CRUDException;
import com.easybpms.db.dao.CRUDEntity;
import com.easybpms.db.dao.CRUDUserGroup;
import com.easybpms.domain.Activity;
import com.easybpms.domain.UserGroup;
import com.easybpms.domain.Process;


public abstract class AbstractContext {

	private static AbstractContext instance = null;

	private Map<String,UserGroup> userGroupMap = new HashMap<String,UserGroup>();

	private Map<String, BpmsObservable> mapObservables;
	private Map<String, List<Observer>> mapObservers = new HashMap<String, List<Observer>>();

	protected class BpmsObservable extends Observable {
		@Override
		protected void setChanged() {
			super.setChanged();
		}
	}

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
	
	protected void setUserGroup(UserGroup userGroup, Activity activity) {
		UserGroup userGroupAux = userGroupMap.get(userGroup.getName());
		if (userGroupAux == null) {
			try {
				userGroupAux = (UserGroup) CRUDUserGroup.read(userGroup);
			} catch (NoResultException e) {	
				userGroupAux = null;
			} catch (CRUDException e) {
				e.printStackTrace();
			}
		}
		if (userGroupAux==null){
			try {
				userGroupAux = userGroup;
				userGroupMap.put(userGroupAux.getName(), userGroupAux);
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		activity.setUserGroup(userGroupAux);
		
	}	
	
	protected void setProcess (Process process){
		Process aux;
      	boolean existProcess;
		aux = new Process();
		aux.setIdBpms(process.getIdBpms());

		existProcess = true;
		try {
			aux = (Process) CRUDEntity.read(aux);
		} catch (NoResultException e) {
			existProcess = false;
		} catch (CRUDException e) {
			e.printStackTrace();
		}

		if(!existProcess){
			try {
				CRUDEntity.create(process);
			}catch (CRUDException e) {
				e.printStackTrace();
			}
		}
	}
	public abstract void setBpmsSession(BpmsSession bpmsSession);

	public void notifyObservers(String className, Object arg) {
		BpmsObservable observable = this.getObservable("CRUD" + className);
		observable.setChanged();
		observable.notifyObservers(arg);
	}
	
	public List<Observer> getObservers(String nameClasse){
		return this.mapObservers.get(nameClasse);
	}
	
	protected BpmsObservable getObservable(String className) {
		if (this.mapObservables == null) {
			this.mapObservables = new HashMap<String,BpmsObservable>();
			for (String key : this.mapObservers.keySet()) {
				BpmsObservable observable = new BpmsObservable();
				for (Observer observer : this.mapObservers.get(key)) {
					observable.addObserver(observer);
				}
				this.mapObservables.put(key, observable);
			}
		}
		return this.mapObservables.get(className);
	}

	protected void addMapping(String key, List<Observer> observers) {
		if(this.mapObservers.containsKey(key)){
			this.mapObservers.get(key).addAll(observers);
		}else{
			this.mapObservers.put(key, observers);
		}
	}
	

}
