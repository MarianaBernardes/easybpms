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
		try {
			session.persist(userGroup);
		} catch (Exception ex) {
			System.err.println(ex.getMessage());
			throw CRUDException.getException("Inconformidade ao persistir Grupo de Usuario", ex);		
		}
	}
	
	public static void remove(IUserGroup entity) throws CRUDException {
		
		EntityManager session = Session.getSession();
		
		try {		
			session.remove(entity);
		} catch (Exception ex) {
			System.err.println(ex.getMessage());
			throw CRUDException.getException("Inconformidade ao excluir Grupo de Usuario", ex);		
		}
	}

	public static UserGroup read(UserGroup userGroup, EntityManager session) throws CRUDException {
		try {
					
			if(userGroup.getName() != null){
				return session.createQuery("FROM UserGroup WHERE name = '" + userGroup.getName() + "'", UserGroup.class).getSingleResult();
			}
			else{
				System.err.println("Nao foi possivel carregar a entidade UserGroup. Parametros nao fornecidos");
			}
		} catch (NoResultException ex) {	
			return null;
		} catch (Exception ex) {		
			System.err.println(ex.getMessage());
			throw CRUDException.getException("Inconformidade ao consultar Grupo de Usuario", ex);	
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
				System.out.println("Nao foi possivel carregar a entidade UserGroup. Parametros nao fornecidos");
			}
		} catch (NoResultException ex) {	
			return null;
		} catch (Exception ex) {		
			System.err.println(ex.getMessage());
			throw CRUDException.getException("Inconformidade ao consultar Grupo de Usuario", ex);	
		}
		return userGroup;
	}
	
	public static List<UserGroup> readAll() throws CRUDException {
		EntityManager session = Session.getSession();
		List<UserGroup> list = null;
		try {		
			list = session.createQuery("FROM UserGroup", UserGroup.class).getResultList();
		} catch (NoResultException ex) {		
			throw ex;
		} catch (Exception ex) {		
			System.err.println(ex.getMessage());
			throw CRUDException.getException("Inconformidade ao consultar lista de Grupos de Usuario", ex);	
		}
		return list;
	}
}
