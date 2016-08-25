package com.easybpms.db.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import com.easybpms.db.CRUDException;
import com.easybpms.db.Session;
import com.easybpms.domain.IUserGroup;
import com.easybpms.domain.UserGroup;

public class CRUDUserGroup {
	public static void create(UserGroup userGroup) throws CRUDException {
		
		EntityManager session = Session.getSession();
//		EntityTransaction transaction = session.getTransaction();
		try {
//			transaction.begin();
			session.persist(userGroup);
//			transaction.commit();
		} catch (RuntimeException re) {
//            if(transaction.isActive()) {
//            	transaction.rollback();
//            }
            re.printStackTrace();;
			
		} catch (Exception ex) {
//			transaction.rollback();		
			throw CRUDException.getExcecao(CRUDException.getInconformidadeCadastrar("grupo de usuario"), ex);		
		}
	}
	
	public static void remove(IUserGroup entity) throws CRUDException {
		
		EntityManager session = Session.getSession();
//		EntityTransaction transaction = session.getTransaction();
		
		try {		
//			transaction.begin();
			session.remove(entity);
//			transaction.commit();
			
		} catch (RuntimeException re) {
//            if(transaction.isActive()) {
//            	transaction.rollback();
//            }
            re.printStackTrace();;
			
		} catch (Exception ex) {
//			transaction.rollback();
			throw CRUDException.getExcecao(CRUDException.getInconformidadeExcluir("grupo de usuario"), ex);		
		}
		
	}

	public static UserGroup read(UserGroup userGroup, EntityManager session) throws CRUDException {
		try {
					
			if(userGroup.getName() != null){
				return session.createQuery("FROM UserGroup WHERE name = '" + userGroup.getName() + "'", UserGroup.class).getSingleResult();
			}
			else{
				System.out.println("Nao foi possivel carregar a entidade. Parametros nao fornecidos.");
			}
		} catch (NoResultException ex1) {	
			return null;
		} catch (Exception ex) {		
			throw CRUDException.getExcecao(CRUDException.getInconformidadeConsultar("grupo usuario"), ex);	
		}
		return userGroup;
	}
	
	public static UserGroup read(UserGroup userGroup) throws CRUDException {
		EntityManager session = Session.getSession();
		try {
					
			if(userGroup.getName() != null){
				return session.createQuery("FROM UserGroup WHERE name = '" + userGroup.getName() + "'", UserGroup.class).getSingleResult();
			}
			else{
				System.out.println("Nao foi possivel carregar a entidade. Parametros nao fornecidos.");
			}
		} catch (NoResultException ex1) {	
			return null;
		} catch (Exception ex) {		
			throw CRUDException.getExcecao(CRUDException.getInconformidadeConsultar("grupo usuario"), ex);	
		}
		return userGroup;
	}
	
	public static List<UserGroup> readAll() throws CRUDException {
		EntityManager session = Session.getSession();
		List<UserGroup> list = null;
		try {		
			list = session.createQuery("FROM UserGroup", UserGroup.class).getResultList();
		} catch (NoResultException ex1) {		
			throw ex1;
		} catch (Exception ex) {		
			throw CRUDException.getExcecao(CRUDException.getInconformidadeConsultar("grupos de usuario"), ex);	
		}
		return list;
	}
}
