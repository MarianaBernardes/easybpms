package com.easybpms.db.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import com.easybpms.db.CRUDException;
import com.easybpms.db.Session;
import com.easybpms.domain.ActivityInstance;
import com.easybpms.domain.User;

public class CRUDActivityInstance {
	public static void create(ActivityInstance entity, EntityManager session) throws Exception {
		try {			
			session.persist(entity);
		} catch (Exception ex) {			
			throw ex;		
		}
	}
	public static void remove(ActivityInstance entity, EntityManager session) throws Exception {
		try {			
			session.remove(entity);
		} catch (Exception ex) {			
			throw ex;		
		}
	}
	public static ActivityInstance read(ActivityInstance activityInstance, EntityManager session) throws Exception {
		try {
					
			if(activityInstance.getId() > 0){
				return session.find(ActivityInstance.class, activityInstance.getId());
			}
			else if(activityInstance.getIdBpms() != null && activityInstance.getStatus() != null && activityInstance.getActivity() != null && activityInstance.getProcessInstance() != null){
				return session.createQuery("FROM ActivityInstance WHERE idBpms = '" + activityInstance.getIdBpms() + 
				"' AND activity_id = '" + activityInstance.getActivity().getId() + 
				"' AND processInstance_id = '" + activityInstance.getProcessInstance().getId() + "'", ActivityInstance.class).getSingleResult();
			}
			else if(activityInstance.getUser() != null){
				return session.createQuery("FROM ActivityInstance WHERE user_id = '" + activityInstance.getUser().getId() + "'", ActivityInstance.class).getSingleResult();
			}
			else{
				System.err.println("Nao foi possivel carregar a entidade ActivityInstance. Parametros nao fornecidos");
			}
		} catch (NoResultException ex) {		
			throw ex;
		} catch (Exception ex) {		
			throw ex;	
		}
		return activityInstance;
	}
	
	public static void update(ActivityInstance activityInstance, String status) throws CRUDException {

		try{
			activityInstance.setStatus(status);
		} catch (Exception ex) {
			throw CRUDException.getException("Inconformidade ao atualizar ActivityInstance", ex);
		}
		
	}
			
	public static List<ActivityInstance> readAll() throws CRUDException {
		EntityManager session = Session.getSession();
		List<ActivityInstance> list = null;
		try {		
			list = session.createQuery("FROM ActivityInstance", ActivityInstance.class).getResultList();
		} catch (NoResultException ex) {		
			throw ex;
		} catch (Exception ex) {
			throw CRUDException.getException("Inconformidade ao consultar lista de Instancias Atividade", ex);
		}
		return list;
	}
	
	public static List<ActivityInstance> readActivityInstances(String tenancy, String usuarioId) throws CRUDException {
		EntityManager session = Session.getSession();
		List<ActivityInstance> list = null;
		User user = session.createQuery("FROM User WHERE idApp = '" + usuarioId + "' AND tenancy = '" + tenancy + "'", User.class).getSingleResult(); 
		try {		
			list = session.createQuery("FROM ActivityInstance WHERE user_id = '" + user.getId() + "' AND status = '" + "Reserved" + "'", ActivityInstance.class).getResultList();
		} catch (NoResultException ex) {		
			throw ex;
		} catch (Exception ex) {
			throw CRUDException.getException("Inconformidade ao consultar lista de Instancias Atividade", ex);
		}
		return list;
	}
}
