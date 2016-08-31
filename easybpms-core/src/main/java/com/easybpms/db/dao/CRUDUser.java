package com.easybpms.db.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import com.easybpms.db.CRUDException;
import com.easybpms.db.Session;
import com.easybpms.domain.IUser;
import com.easybpms.domain.User;
import com.easybpms.domain.UserGroup;

public class CRUDUser {
	public static void create(IUser appUser) throws CRUDException {

		User user = new User();
		user.setIdApp(appUser.getIdApp());
		user.setName(appUser.getName());
		user.setTenancy(appUser.getTenancy());
	
		EntityManager session = Session.getSession();
		
		List<String> userGroupNames = appUser.getUserGroupNames();
		if (userGroupNames != null) {
			for (String groupName : userGroupNames){
				UserGroup userGroup = new UserGroup();
				userGroup.setName(groupName);
				userGroup = CRUDUserGroup.read(userGroup,session);
				if (userGroup != null){
					userGroup.addUser(user);
					try {	
						session.persist(user);
					} catch (Exception ex) {
						throw CRUDException.getException("Inconformidade ao persistir Usuario" , ex);		
					}
					
				}
			}
		}
	}
	
	public static void remove(IUser user) throws CRUDException {
		EntityManager session = Session.getSession();
		
		try {		
			session.remove(user);
		} catch (Exception ex) {
			throw CRUDException.getException("Inconformidade ao excluir Usuario" , ex);		
		}
	}
	
	public static User read(User user) throws CRUDException {
		EntityManager session = Session.getSession();
		try {
					
			if(user.getId() > 0){
				return session.find(User.class, user.getId());
			}
			else if(user.getName() != null){
				return session.createQuery("FROM User WHERE name = '" + user.getName() + "'", User.class).getSingleResult();
			}
			else if(user.getIdApp() != null){
				return session.createQuery("FROM User WHERE idApp = '" + user.getIdApp() + "'", User.class).getSingleResult();
			}
			else{
				System.err.println("Nao foi possivel carregar a entidade User. Parametros nao fornecidos");
			}
		} catch (NoResultException ex) {		
			throw ex;
		} catch (Exception ex) {		
			throw CRUDException.getException("Inconformidade ao consultar Usuario", ex);	
		}
		return user;
	}
	
	public static List<? extends IUser> readAll() throws CRUDException {
		EntityManager session = Session.getSession();
		List<? extends IUser> list = null;
		try {		
			list = session.createQuery("FROM User", User.class).getResultList();
		} catch (NoResultException ex) {		
			throw ex;
		} catch (Exception ex) {		
			throw CRUDException.getException("Inconformidade ao consultar lista de Usuarios", ex);	
		}
		return list;
	}
}
