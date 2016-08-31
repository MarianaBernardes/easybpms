package com.easybpms.db.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import com.easybpms.db.CRUDException;
import com.easybpms.db.Session;
import com.easybpms.domain.ParameterInstance;

public class CRUDParameterInstance {
	public static void create(ParameterInstance entity, EntityManager session) throws Exception {
		try {			
			session.persist(entity);
		} catch (Exception ex) {			
			throw ex;		
		}
	}
	public static void remove(ParameterInstance entity, EntityManager session) throws Exception {
		try {			
			session.remove(entity);
		} catch (Exception ex) {			
			throw ex;		
		}
	}
	public static ParameterInstance read(ParameterInstance parameterInstance, EntityManager session) throws Exception {
		try {
					
			if(parameterInstance.getId() > 0){
				return session.find(ParameterInstance.class, parameterInstance.getId());
			}
			else if(parameterInstance.getIdBpms() != null){
				return session.createQuery("FROM ParameterInstance WHERE idBpms = '" + parameterInstance.getIdBpms() + "'", ParameterInstance.class).getSingleResult();
			}
			else if(parameterInstance.getType() != null && parameterInstance.getValue() != null && parameterInstance.getActivityInstance() != null && parameterInstance.getParameter() != null){
				return session.createQuery("FROM ParameterInstance WHERE type = '" + parameterInstance.getType() + 
				"' AND value = '" + parameterInstance.getValue() +
				"' AND activityInstance_id = '" + parameterInstance.getActivityInstance().getId() + 
				"' AND parameter_id = '" + parameterInstance.getParameter().getId() + "'", ParameterInstance.class).getSingleResult();
			}
			else{
				System.err.println("Nao foi possivel carregar a entidade ParameterInstance. Parametros nao fornecidos");
			}
		} catch (NoResultException ex) {		
			throw ex;
		} catch (Exception ex) {		
			throw ex;	
		}
		return parameterInstance;
	}
	
	public static List<ParameterInstance> readAll() throws CRUDException {
		EntityManager session = Session.getSession();
		List<ParameterInstance> list = null;
		try {		
			list = session.createQuery("FROM ParameterInstance", ParameterInstance.class).getResultList();
		} catch (NoResultException ex) {		
			throw ex;
		} catch (Exception ex) {
			System.err.println(ex.getMessage());
			throw CRUDException.getException("Inconformidade ao consultar lista de Instancias Parametro", ex);
		}
		return list;
	}
}
