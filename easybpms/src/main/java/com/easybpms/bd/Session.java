package com.easybpms.bd;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class Session {
	
	private static EntityManager entityManager;
	private static EntityManagerFactory entityManagerFactory;
	//private PlatformTransactionManager transactionManager;

	// Precisa ser apagado depois, pois a sessão não pode ser estática
	public static EntityManager getSession() {
		if (entityManager == null) {
			entityManagerFactory = Persistence.createEntityManagerFactory("com.easybpms.persistence.jpa");
			entityManager = entityManagerFactory.createEntityManager();
		}

		// return factory.createEntityManager();
		return entityManager;
	}

	// Precisa ser apagado depois, deve ser utilizado o metodo setEntityManager
	public static void startSession() {
		entityManager = entityManagerFactory.createEntityManager();
	}

	public static void closeSession() {
		entityManager.close();
		entityManagerFactory.close();
	}

	
	/*public static void setEntityManagerFactory(EntityManagerFactory emfFactory) { 
		 entityManagerFactory = emfFactory; 
		 entityManager = entityManagerFactory.createEntityManager(); 
	}*/
	
	public void setEntityManagerFactory(EntityManagerFactory emfFactory) { 
		 entityManagerFactory = emfFactory; 
		 entityManager = entityManagerFactory.createEntityManager(); 
	}

	public static void setEntityManager(EntityManager emanager) {
		entityManager = emanager;
	}


	/*public void setTransactionManager(PlatformTransactionManager transactionManager) {
		this.transactionManager = transactionManager;
	}*/

}
