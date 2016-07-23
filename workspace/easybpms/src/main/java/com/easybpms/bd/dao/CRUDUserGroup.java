package com.easybpms.bd.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import com.easybpms.bd.CRUDException;
import com.easybpms.bd.Session;
import com.easybpms.domain.UserGroup;

public class CRUDUserGroup {
	public static void create(UserGroup entity, EntityManager session) throws CRUDException {
		try {			
			session.persist(entity);
		} catch (Exception ex) {			
			throw CRUDException.getExcecao(CRUDException.getInconformidadeCadastrar("grupo de usuario"), ex);		
		}
	}
	public static void remove(UserGroup entity, EntityManager session) throws CRUDException {
		try {			
			session.remove(entity);
		} catch (Exception ex) {			
			throw CRUDException.getExcecao(CRUDException.getInconformidadeExcluir("grupo de usuario"), ex);		
		}
	}
	public static UserGroup read(UserGroup userGroup, EntityManager session) throws CRUDException {
		try {
					
			if(userGroup.getId() > 0){
				return session.find(UserGroup.class, userGroup.getId());
			}
			else if(userGroup.getName() != null){
				return session.createQuery("FROM UserGroup WHERE name = '" + userGroup.getName() + "'", UserGroup.class).getSingleResult();
			}
			/*else if(userGroup.getActivity() != null){
				return session.createQuery("FROM UserGroup WHERE activity_id = '" + userGroup.getActivity().getId() + "'", UserGroup.class).getSingleResult();
			}*/
			else{
				System.out.println("Não foi possível carregar a entidade. Parâmetros não fornecidos.");
			}
		} catch (NoResultException ex1) {	
			return null;
			//throw ex1;
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
