package com.easybpms.db;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class Session {
	
	private static EntityManager entityManager;
	private static EntityManagerFactory entityManagerFactory;

	// Metodo deve ser apagado, pois a sessao nao pode ser estatica
	public static EntityManager getSession() {
		if (entityManager == null) {
			entityManagerFactory = Persistence.createEntityManagerFactory("com.easybpms.persistence.jpa");
			entityManager = entityManagerFactory.createEntityManager();
		}
		return entityManager;
	}

	public static void startSession() {
		entityManager = entityManagerFactory.createEntityManager();
	}

	public static void closeSession() {
		entityManager.close();
		entityManagerFactory.close();
	}

	public void setEntityManagerFactory(EntityManagerFactory emfFactory) { 
		 entityManagerFactory = emfFactory; 
		 entityManager = entityManagerFactory.createEntityManager(); 
	}

	public static void setEntityManager(EntityManager emanager) {
		entityManager = emanager;
	}

}
