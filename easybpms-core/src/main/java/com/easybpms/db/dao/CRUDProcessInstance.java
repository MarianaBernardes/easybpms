package com.easybpms.db.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import com.easybpms.db.CRUDException;
import com.easybpms.db.Session;
import com.easybpms.domain.ProcessInstance;

public class CRUDProcessInstance {
	public static void create(ProcessInstance entity, EntityManager session) throws CRUDException {
		try {			
			session.persist(entity);
		} catch (Exception ex) {			
			throw CRUDException.getExcecao(CRUDException.getInconformidadeCadastrar("instancia processo"), ex);		
		}
	}
	public static void remove(ProcessInstance entity, EntityManager session) throws CRUDException {
		try {			
			session.remove(entity);
		} catch (Exception ex) {			
			throw CRUDException.getExcecao(CRUDException.getInconformidadeExcluir("instancia processo"), ex);		
		}
	}
	public static ProcessInstance read(ProcessInstance processInstance, EntityManager session) throws CRUDException {
		try {
					
			if(processInstance.getId() > 0){
				return session.find(ProcessInstance.class, processInstance.getId());
			}
			else if(processInstance.getIdBpms() != null && processInstance.getProcess() != null){
				return session.createQuery("FROM ProcessInstance WHERE idBpms = '" + processInstance.getIdBpms() +  
				"' AND process_id = '"  + processInstance.getProcess().getId() + "'", ProcessInstance.class).getSingleResult();
			}
			else{
				System.out.println("Nao foi possivel carregar a entidade. Parametros nao fornecidos.");
			}
		} catch (NoResultException ex1) {		
			throw ex1;
		} catch (Exception ex) {		
			throw CRUDException.getExcecao(CRUDException.getInconformidadeConsultar("instancia processo"), ex);	
		}
		return processInstance;
	}
	
	public static void update(ProcessInstance processInstance, String status) throws CRUDException {
//		EntityManager session = Session.getSession();
//		EntityTransaction transaction = session.getTransaction();
		
		try{
//			transaction.begin();
			processInstance.setStatus(status);
//			transaction.commit();
		} catch (Exception ex) {		
			throw CRUDException.getExcecao(CRUDException.getInconformidadeAlterar("instancia processo"), ex);	
		}
		
	}
	
	public static List<ProcessInstance> readAll() throws CRUDException {
		EntityManager session = Session.getSession();
		List<ProcessInstance> list = null;
		try {		
			list = session.createQuery("FROM ProcessInstance", ProcessInstance.class).getResultList();
		} catch (NoResultException ex1) {		
			throw ex1;
		} catch (Exception ex) {		
			throw CRUDException.getExcecao(CRUDException.getInconformidadeConsultar("instancias processo"), ex);	
		}
		return list;
	}
}
