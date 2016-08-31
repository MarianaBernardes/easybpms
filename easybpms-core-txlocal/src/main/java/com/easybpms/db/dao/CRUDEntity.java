package com.easybpms.db.dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;

import com.easybpms.domain.Process;
import com.easybpms.db.CRUDException;
import com.easybpms.db.Session;
import com.easybpms.domain.*;

public class CRUDEntity {
	
	public static void create (IEntity entity) throws CRUDException{
		
		EntityManager session = Session.getSession();
		EntityTransaction transaction = session.getTransaction();
		
		try{
			transaction.begin();
			Class<? extends IEntity> entityClass = entity.getClass();
	        
			if (entityClass.equals(Process.class)) {
				CRUDProcess.create((Process)entity, session);
			}else if (entityClass.equals(Property.class)) {
				CRUDProperty.create((Property) entity, session);
			} else if (entityClass.equals(Activity.class)) {
				CRUDActivity.create((Activity) entity, session);
			} else if (entityClass.equals(Parameter.class)) {
				CRUDParameter.create((Parameter) entity, session);
			}else if (entityClass.equals(ProcessInstance.class)) {
				CRUDProcessInstance.create((ProcessInstance) entity, session);
			}else if (entityClass.equals(ActivityInstance.class)) {
				CRUDActivityInstance.create((ActivityInstance) entity, session);
			}else if (entityClass.equals(ParameterInstance.class)) {
				CRUDParameterInstance.create((ParameterInstance) entity, session);
			}else {
				throw new CRUDException("Nao existe create para a classe " + entityClass.getSimpleName());
			}

			transaction.commit();
			
		}catch (Exception ex) {
			System.err.println(ex.getMessage());
			transaction.rollback();
			throw CRUDException.getException("Inconformidade ao persistir " + entity.getClass().getSimpleName(), ex);
		}
	}
	
	public static void remove (IEntity entity) throws CRUDException{
		EntityManager session = Session.getSession();
		EntityTransaction transaction = session.getTransaction();
		
		try{
			transaction.begin();
			Class<? extends IEntity> entityName = entity.getClass();
	           
			if (entityName.equals(Process.class)) {
				CRUDProcess.remove((Process)entity, session);
			}else if (entityName.equals(Property.class)) {
				CRUDProperty.remove((Property) entity, session);
			} else if (entityName.equals(Activity.class)) {
				CRUDActivity.remove((Activity) entity, session);
			} else if (entityName.equals(Parameter.class)) {
				CRUDParameter.remove((Parameter) entity, session);
			}else if (entityName.equals(ProcessInstance.class)) {
				CRUDProcessInstance.remove((ProcessInstance) entity, session);
			}else if (entityName.equals(ActivityInstance.class)) {
				CRUDActivityInstance.remove((ActivityInstance) entity, session);
			}else if (entityName.equals(ParameterInstance.class)) {
				CRUDParameterInstance.remove((ParameterInstance) entity, session);
			}else {
				throw new CRUDException("Nao existe remove para a classe " + entityName.getSimpleName());
			}

			transaction.commit();
			
		}catch (Exception ex) {
			System.err.println(ex.getMessage());
			transaction.rollback();
			throw CRUDException.getException("Inconformidade ao excluir " + entity.getClass().getSimpleName(), ex);
		}
	}
	
	public static IEntity read(IEntity entity) throws CRUDException {
		IEntity retorno;
		EntityManager session = Session.getSession();
		
		try {
			Class<? extends IEntity> entityName = entity.getClass();

			if (entityName.equals(Process.class)) {
				retorno = CRUDProcess.read((Process) entity, session);
			} else if (entityName.equals(Activity.class)) {
				retorno = CRUDActivity.read((Activity) entity, session);
			} else if (entityName.equals(Parameter.class)) {
				retorno = CRUDParameter.read((Parameter) entity, session);
			} else if (entityName.equals(Property.class)) {
				retorno = CRUDProperty.read((Property) entity, session);
			} else if (entityName.equals(ProcessInstance.class)) {
				retorno = CRUDProcessInstance.read((ProcessInstance) entity, session);
			} else if (entityName.equals(ActivityInstance.class)) {
				retorno = CRUDActivityInstance.read((ActivityInstance) entity, session);
			} else if (entityName.equals(ParameterInstance.class)) {
				retorno = CRUDParameterInstance.read((ParameterInstance) entity, session);
			} else {
				throw new CRUDException("Nao existe read para a classe" + entityName.getSimpleName());
			}

			return retorno;
		
		} catch (NoResultException ex) {
			throw ex;
		} catch (Exception ex) {
			System.err.println(ex.getMessage());
			throw CRUDException.getException("Inconformidade ao consultar " + entity.getClass().getSimpleName(), ex);
		}
	}	
}
