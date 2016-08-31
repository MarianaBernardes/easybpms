package com.easybpms.db.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import com.easybpms.db.CRUDException;
import com.easybpms.db.Session;
import com.easybpms.domain.Property;

public class CRUDProperty {
	public static void create(Property entity, EntityManager session) throws Exception {
		try {			
			session.persist(entity);
		} catch (Exception ex) {			
			throw ex;		
		}
	}
	
	public static void remove(Property entity, EntityManager session) throws Exception {
		try {			
			session.remove(entity);
		} catch (Exception ex) {			
			throw ex;		
		}
	}
	
	public static Property read(Property property, EntityManager session) throws Exception {
		try {
					
			if(property.getId() > 0){
				return session.find(Property.class, property.getId());
			}
			else if(property.getName() != null){
				return session.createQuery("FROM Property WHERE name = '" + property.getName() + "'", Property.class).getSingleResult();
			}
			else if(property.getProcess() != null){
				return session.createQuery("FROM Property WHERE process_id = '" + property.getProcess().getId() + "'", Property.class).getSingleResult();
			}
			else{
				System.out.println("Nao foi possivel carregar a entidade Property. Parametros nao fornecidos");
			}
		} catch (NoResultException ex) {		
			throw ex;
		} catch (Exception ex) {		
			throw ex;	
		}
		return property;
	}
	
	public static List<Property> readAll() throws CRUDException {
		EntityManager session = Session.getSession();
		List<Property> list = null;
		try {		
			list = session.createQuery("FROM Property", Property.class).getResultList();
		} catch (NoResultException ex) {		
			throw ex;
		} catch (Exception ex) {
			throw CRUDException.getException("Inconformidade ao consultar lista de Variaveis de Processo", ex);
		}
		return list;
	}
}
