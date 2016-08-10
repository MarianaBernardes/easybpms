package com.easybpms.bd;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class Session {
	private static EntityManager manager;
	private static EntityManagerFactory factory;
	
	public static EntityManager getSession() {
		if (manager == null){
			factory = Persistence.createEntityManagerFactory("com.api.easybpms");
			manager = factory.createEntityManager(); 
		}
			
	    return manager;
	}
	
	public static void closeSession(){
		manager.close();
		factory.close();
	}
	
	public static void setEntityManagerFactory(EntityManagerFactory emfFactory) {
		factory = emfFactory;
		manager = factory.createEntityManager();;
	}
}
