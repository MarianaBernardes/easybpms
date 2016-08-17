package com.easybpms.bd;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class Session {
	private static EntityManager manager;
	private static EntityManagerFactory factory;
	
	//Precisa ser apagado depois, pois a sessão não pode ser estática
	public static EntityManager getSession() {
		if (manager == null){
			factory = Persistence.createEntityManagerFactory("com.easybpms.persistence.jpa");
			manager = factory.createEntityManager(); 
		}
			
//		return factory.createEntityManager();
	    return manager;
	}
	
	//Precisa ser apagado depois, deve ser utilizado o metodo setEntityManager
	public static void startSession() {
		manager = factory.createEntityManager();
	}
	
	public static void closeSession(){
		manager.close();
		factory.close();
	}
	
	public static void setEntityManagerFactory(EntityManagerFactory emfFactory) {
		factory = emfFactory;
		manager = factory.createEntityManager();
	}

	public static void setEntityManager(EntityManager emanager) {
		manager = emanager;
	}

}
