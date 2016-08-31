package com.easybpms.db.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import com.easybpms.db.CRUDException;
import com.easybpms.db.Session;
import com.easybpms.domain.Activity;

public class CRUDActivity {
	public static void create(Activity entity, EntityManager session) throws Exception {
		try {			
			session.persist(entity);
		} catch (Exception ex) {			
			throw ex;		
		}
	}
	public static void remove(Activity entity, EntityManager session) throws Exception {
		try {			
			session.remove(entity);
		} catch (Exception ex) {			
			throw ex;		
		}
	}
	public static Activity read(Activity activity, EntityManager session) throws Exception {
		try {
					
			if(activity.getId() > 0){
				return session.find(Activity.class, activity.getId());
			}
			else if(activity.getIdBpms() != null && activity.getName() != null && activity.getProcess() != null){
				return session.createQuery("FROM Activity WHERE name = '" + activity.getName() + 
				"' AND idBpms = '" + activity.getIdBpms() +
				"' AND process_id = '" + activity.getProcess().getId() + 
				"'", Activity.class).getSingleResult();
			}
			else{
				System.err.println("Nao foi possivel carregar a entidade Atividade. Parametros nao fornecidos");
			}
		} catch (NoResultException ex) {		
			throw ex;
		} catch (Exception ex) {		
			throw ex;	
		}
		return activity;
	}
	
	public static List<Activity> readAll() throws CRUDException {
		EntityManager session = Session.getSession();
		List<Activity> list = null;
		try {		
			list = session.createQuery("FROM Activity", Activity.class).getResultList();
		} catch (NoResultException ex) {		
			throw ex;
		} catch (Exception ex) {
			throw CRUDException.getException("Inconformidade ao consultar lista de Atividades", ex);
		}
		return list;
	}

}
