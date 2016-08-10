package com.easybpms.bd.dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;

import com.easybpms.bd.CRUDException;
import com.easybpms.bd.Session;
import com.easybpms.domain.Process;
import com.easybpms.domain.*;

public class CRUDEntity {
	public static void create (IEntity entity) throws CRUDException{
		EntityManager session = Session.getSession();
//		EntityTransaction transaction = session.getTransaction();
		
		try{
//			transaction.begin();
			Class<? extends IEntity> entityClass = entity.getClass();
	        
			if (entityClass.equals(Process.class)) {
				CRUDProcess.create((Process)entity, session);
			}else if (entityClass.equals(Property.class)) {
				CRUDProperty.create((Property) entity, session);
			} else if (entityClass.equals(Activity.class)) {
				CRUDActivity.create((Activity) entity, session);
			} else if (entityClass.equals(Parameter.class)) {
				CRUDParameter.create((Parameter) entity, session);
			/*}else if (entityClass.equals(UserGroup.class)) {
				CRUDUserGroup.create((UserGroup) entity, session);*/
			}else if (entityClass.equals(ProcessInstance.class)) {
				CRUDProcessInstance.create((ProcessInstance) entity, session);
			}else if (entityClass.equals(ActivityInstance.class)) {
				CRUDActivityInstance.create((ActivityInstance) entity, session);
			}else if (entityClass.equals(ParameterInstance.class)) {
				CRUDParameterInstance.create((ParameterInstance) entity, session);
			/*}else if (entityClass.equals(User.class)) {
				CRUDUser.create((User) entity, session);*/
			}else {
				throw new CRUDException("Não há create para a classe " + entityClass.getSimpleName());
			}

//			transaction.commit();
			
		} catch (RuntimeException re) {
//            if(transaction.isActive()) {
//            	transaction.rollback();
//            }
            throw re;
			
		}catch(CRUDException ex1) {
			
//			transaction.rollback();
			throw ex1;
			
		}catch (Exception ex) {
			System.out.println(ex.getMessage());
//			transaction.rollback();
			
			throw CRUDException.getExcecao("Inconformidade ao persistir " + entity.getClass().getSimpleName() + "!", ex);
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
			/*}else if (entityName.equals(UserGroup.class)) {
				CRUDUserGroup.remove((UserGroup) entity, session);*/
			}else if (entityName.equals(ProcessInstance.class)) {
				CRUDProcessInstance.remove((ProcessInstance) entity, session);
			}else if (entityName.equals(ActivityInstance.class)) {
				CRUDActivityInstance.remove((ActivityInstance) entity, session);
			}else if (entityName.equals(ParameterInstance.class)) {
				CRUDParameterInstance.remove((ParameterInstance) entity, session);
			/*}else if (entityName.equals(User.class)) {
				CRUDUser.remove((User) entity, session);*/
			}else {
				throw new CRUDException("Não há remove para a classe " + entityName.getSimpleName());
			}

			transaction.commit();
			
		} catch (RuntimeException re) {
            if(transaction.isActive()) {
            	transaction.rollback();
            }
            throw re;
            
		}catch(CRUDException ex1) {
			
			transaction.rollback();
			throw ex1;
			
		}catch (Exception ex) {
			System.out.println(ex.getMessage());
			transaction.rollback();
			
			throw CRUDException.getExcecao("Inconformidade ao excluir " + entity.getClass().getSimpleName() + "!", ex);
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
			/*} else if (entityName.equals(UserGroup.class)) {
				retorno = CRUDUserGroup.read((UserGroup) entity, session);*/
			} else if (entityName.equals(Property.class)) {
				retorno = CRUDProperty.read((Property) entity, session);
			} else if (entityName.equals(ProcessInstance.class)) {
				retorno = CRUDProcessInstance.read((ProcessInstance) entity, session);
			} else if (entityName.equals(ActivityInstance.class)) {
				retorno = CRUDActivityInstance.read((ActivityInstance) entity, session);
			} else if (entityName.equals(ParameterInstance.class)) {
				retorno = CRUDParameterInstance.read((ParameterInstance) entity, session);
			/*} else if (entityName.equals(User.class)) {
				retorno = CRUDUser.read((User) entity, session);*/
			} else {
				throw new CRUDException("Não há read para a classe" + entityName.getSimpleName());
			}

			return retorno;

		} catch (NoResultException ex1) {
			throw ex1;
		} catch (CRUDException ex1) {
			throw ex1;
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
			throw CRUDException.getExcecao("Inconformidade ao consultar " + entity.getClass().getSimpleName() + "!", ex);
		}
	}
	
	/*public static void update(IEntity entity) throws CRUDException {
		EntityManager session = Session.getSession();
		EntityTransaction transaction = session.getTransaction();
		
		try{
			transaction.begin();
			Class<? extends IEntity> entityName = entity.getClass();

			if (entityName.equals(Process.class)) {
				retorno = CRUDProcess.update((Process) entity, session);
			} else if (entityName.equals(Activity.class)) {
				retorno = CRUDActivity.update((Activity) entity, session);
			} else if (entityName.equals(Parameter.class)) {
				retorno = CRUDParameter.update((Parameter) entity, session);
			} else if (entityName.equals(UserGroup.class)) {
				retorno = CRUDUserGroup.update((UserGroup) entity, session);
			} else if (entityName.equals(Property.class)) {
				retorno = CRUDProperty.update((Property) entity, session);
			} else if (entityName.equals(ProcessInstance.class)) {
				retorno = CRUDProcessInstance.update((ProcessInstance) entity, session);
			} else 
				if (entityName.equals(ActivityInstance.class)) {
				CRUDActivityInstance.update((ActivityInstance) entity, session);
			} else if (entityName.equals(ParameterInstance.class)) {
				retorno = CRUDParameterInstance.update((ParameterInstance) entity, session);
			} else if (entityName.equals(User.class)) {
				retorno = CRUDUser.update((User) entity, session);
			} else {
				throw new CRUDException("Não há update para a classe" + entityName.getSimpleName());
			}
			transaction.commit();
		
			
		} catch (RuntimeException re) {
            if(transaction.isActive()) {
            	transaction.rollback();
            }
            throw re;
            
		}catch(CRUDException ex1) {
			transaction.rollback();
			throw ex1;	
			
		}catch (Exception ex) {
			System.out.println(ex.getMessage());
			transaction.rollback();
			throw CRUDException.getExcecao("Inconformidade ao atualizar " + entity.getClass().getSimpleName() + "!", ex);
		}
	}*/
	
	
}
