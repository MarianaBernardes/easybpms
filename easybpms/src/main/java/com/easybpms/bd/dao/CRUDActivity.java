package com.easybpms.bd.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import com.easybpms.bd.CRUDException;
import com.easybpms.bd.Session;
import com.easybpms.domain.Activity;

public class CRUDActivity {
	public static void create(Activity entity, EntityManager session) throws CRUDException {
		try {			
			session.persist(entity);
		} catch (Exception ex) {			
			throw CRUDException.getExcecao(CRUDException.getInconformidadeCadastrar("atividade"), ex);		
		}
	}
	public static void remove(Activity entity, EntityManager session) throws CRUDException {
		try {			
			session.remove(entity);
		} catch (Exception ex) {			
			throw CRUDException.getExcecao(CRUDException.getInconformidadeExcluir("atividade"), ex);		
		}
	}
	public static Activity read(Activity activity, EntityManager session) throws CRUDException {
		try {
					
			if(activity.getId() > 0){
				return session.find(Activity.class, activity.getId());
			}
			/*else if(activity.getIdBpms() != null){
				return session.createQuery("FROM Activity WHERE idBpms = '" + activity.getIdBpms() + "'", Activity.class).getSingleResult();
			}
			else if(activity.getName() != null){
				return session.createQuery("FROM Activity WHERE name = '" + activity.getName() + "'", Activity.class).getSingleResult();
			}
			else if(activity.getProcess() != null){
				return session.createQuery("FROM Activity WHERE process_id = '" + activity.getProcess().getId() + "'", Activity.class).getSingleResult();
			}*/
			else if(activity.getIdBpms() != null && activity.getName() != null && activity.getProcess() != null){
				return session.createQuery("FROM Activity WHERE name = '" + activity.getName() + 
				"' AND idBpms = '" + activity.getIdBpms() +
				"' AND process_id = '" + activity.getProcess().getId() + 
				"'", Activity.class).getSingleResult();
			}
			else{
				System.out.println("Nao foi possivel carregar a entidade. Parametros nao fornecidos.");
			}
		} catch (NoResultException ex1) {		
			throw ex1;
		} catch (Exception ex) {		
			throw CRUDException.getExcecao(CRUDException.getInconformidadeConsultar("atividade"), ex);	
		}
		return activity;
	}
	
	public static List<Activity> readAll() throws CRUDException {
		EntityManager session = Session.getSession();
		List<Activity> list = null;
		try {		
			list = session.createQuery("FROM Activity", Activity.class).getResultList();
		} catch (NoResultException ex1) {		
			throw ex1;
		} catch (Exception ex) {		
			throw CRUDException.getExcecao(CRUDException.getInconformidadeConsultar("atividades"), ex);	
		}
		return list;
	}

}
