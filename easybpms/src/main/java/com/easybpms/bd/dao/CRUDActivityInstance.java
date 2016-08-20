package com.easybpms.bd.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import com.easybpms.bd.CRUDException;
import com.easybpms.bd.Session;
import com.easybpms.domain.ActivityInstance;
import com.easybpms.domain.User;

public class CRUDActivityInstance {
	public static void create(ActivityInstance entity, EntityManager session) throws CRUDException {
		try {			
			session.persist(entity);
		} catch (Exception ex) {			
			throw CRUDException.getExcecao(CRUDException.getInconformidadeCadastrar("instancia atividade"), ex);		
		}
	}
	public static void remove(ActivityInstance entity, EntityManager session) throws CRUDException {
		try {			
			session.remove(entity);
		} catch (Exception ex) {			
			throw CRUDException.getExcecao(CRUDException.getInconformidadeExcluir("instancia atividade"), ex);		
		}
	}
	public static ActivityInstance read(ActivityInstance activityInstance, EntityManager session) throws CRUDException {
		try {
					
			if(activityInstance.getId() > 0){
				return session.find(ActivityInstance.class, activityInstance.getId());
			}
			/*else if(activityInstance.getIdBpms() != null){
				return session.createQuery("FROM ActivityInstance WHERE idBpms = '" + activityInstance.getIdBpms() + "'", ActivityInstance.class).getSingleResult();
			}
			else if(activityInstance.getStatus() != null){
				return session.createQuery("FROM ActivityInstance WHERE status = '" + activityInstance.getStatus() + "'", ActivityInstance.class).getSingleResult();
			}
			else if(activityInstance.getActivity() != null){
				return session.createQuery("FROM ActivityInstance WHERE activity_id = '" + activityInstance.getActivity().getId() + "'", ActivityInstance.class).getSingleResult();
			}
			else if(activityInstance.getProcessInstance() != null){
				return session.createQuery("FROM ActivityInstance WHERE processInstance_id = '" + activityInstance.getProcessInstance().getId() + "'", ActivityInstance.class).getSingleResult();
			}*/
			else if(activityInstance.getIdBpms() != null && activityInstance.getStatus() != null && activityInstance.getActivity() != null && activityInstance.getProcessInstance() != null){
				return session.createQuery("FROM ActivityInstance WHERE idBpms = '" + activityInstance.getIdBpms() + 
				"' AND activity_id = '" + activityInstance.getActivity().getId() + 
				"' AND processInstance_id = '" + activityInstance.getProcessInstance().getId() + "'", ActivityInstance.class).getSingleResult();
			}
			else if(activityInstance.getUser() != null){
				return session.createQuery("FROM ActivityInstance WHERE user_id = '" + activityInstance.getUser().getId() + "'", ActivityInstance.class).getSingleResult();
			}
			else{
				System.out.println("Nao foi possivel carregar a entidade. Parametros nao fornecidos.");
			}
		} catch (NoResultException ex1) {		
			throw ex1;
		} catch (Exception ex) {		
			throw CRUDException.getExcecao(CRUDException.getInconformidadeConsultar("instancia atividade"), ex);	
		}
		return activityInstance;
	}
	
	public static void update(ActivityInstance activityInstance, String status) throws CRUDException {
//		EntityManager session = Session.getSession();
//		EntityTransaction transaction = session.getTransaction();
		
		try{
//			transaction.begin();
			activityInstance.setStatus(status);
//			transaction.commit();
		} catch (Exception ex) {		
			throw CRUDException.getExcecao(CRUDException.getInconformidadeAlterar("instancia atividade"), ex);	
		}
		
	}
			
	public static List<ActivityInstance> readAll() throws CRUDException {
		EntityManager session = Session.getSession();
		List<ActivityInstance> list = null;
		try {		
			list = session.createQuery("FROM ActivityInstance", ActivityInstance.class).getResultList();
		} catch (NoResultException ex1) {		
			throw ex1;
		} catch (Exception ex) {		
			throw CRUDException.getExcecao(CRUDException.getInconformidadeConsultar("instancias atividade"), ex);	
		}
		return list;
	}
	
	public static List<ActivityInstance> readActivityInstances(String tenancy, String usuarioId) throws CRUDException {
		EntityManager session = Session.getSession();
		List<ActivityInstance> list = null;
		User user = session.createQuery("FROM User WHERE idApp = '" + usuarioId + "' AND tenancy = '" + tenancy + "'", User.class).getSingleResult(); 
		try {		
			list = session.createQuery("FROM ActivityInstance WHERE user_id = '" + user.getId() + "' AND status = '" + "Reserved" + "'", ActivityInstance.class).getResultList();
		} catch (NoResultException ex1) {		
			throw ex1;
		} catch (Exception ex) {		
			throw CRUDException.getExcecao(CRUDException.getInconformidadeConsultar("instancias atividade"), ex);	
		}
		return list;
	}
}
