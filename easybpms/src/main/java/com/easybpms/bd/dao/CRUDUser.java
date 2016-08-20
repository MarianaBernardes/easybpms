package com.easybpms.bd.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import com.easybpms.bd.CRUDException;
import com.easybpms.bd.Session;
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
//					EntityTransaction transaction = session.getTransaction();
					try {	
//						transaction.begin();
						session.persist(user);
//						transaction.commit();
					} catch (RuntimeException re) {
//						if(transaction.isActive()) {
//							transaction.rollback();
//						}
			            re.printStackTrace();;
						
					} catch (Exception ex) {
//						transaction.rollback();
						throw CRUDException.getExcecao(CRUDException.getInconformidadeCadastrar("usuario"), ex);		
					}
					
				}
			}
		}
	}
	
	public static void remove(IUser user) throws CRUDException {
		EntityManager session = Session.getSession();
//		EntityTransaction transaction = session.getTransaction();
		
		try {		
//			transaction.begin();
			session.remove(user);
//			transaction.commit();
			
		} catch (RuntimeException re) {
//            if(transaction.isActive()) {
//           	transaction.rollback();
//            }
            re.printStackTrace();;
			
		} catch (Exception ex) {
//			transaction.rollback();
			throw CRUDException.getExcecao(CRUDException.getInconformidadeExcluir("usuario"), ex);		
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
			/*else if(user.getUserGroup() != null){
				return session.createQuery("FROM User WHERE userGroup_id = '" + ((UserGroup)user.getUserGroup()).getId() + "'", User.class).getSingleResult();
			}*/
			else{
				System.out.println("Nao foi possivel carregar a entidade. Parametros nao fornecidos.");
			}
		} catch (NoResultException ex1) {		
			throw ex1;
		} catch (Exception ex) {		
			throw CRUDException.getExcecao(CRUDException.getInconformidadeConsultar("usuario"), ex);	
		}
		return user;
	}
	
	public static List<? extends IUser> readAll() throws CRUDException {
		EntityManager session = Session.getSession();
		List<? extends IUser> list = null;
		try {		
			list = session.createQuery("FROM User", User.class).getResultList();
		} catch (NoResultException ex1) {		
			throw ex1;
		} catch (Exception ex) {		
			throw CRUDException.getExcecao(CRUDException.getInconformidadeConsultar("usuarios"), ex);	
		}
		return list;
	}
}
