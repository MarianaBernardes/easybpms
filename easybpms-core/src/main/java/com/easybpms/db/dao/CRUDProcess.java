package com.easybpms.db.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import com.easybpms.db.CRUDException;
import com.easybpms.db.Session;
import com.easybpms.domain.Process;

public class CRUDProcess {
	
	public static void create(Process entity, EntityManager session) throws Exception {
		try {			
			session.persist(entity);
		} catch (Exception ex) {			
			throw ex;		
		}
	}
	
	public static void remove(Process entity, EntityManager session) throws Exception {
		try {			
			session.remove(entity);
		} catch (Exception ex) {			
			throw ex;		
		}
	}
	
	public static Process read(Process process, EntityManager session) throws Exception {
		try {
					
			if(process.getId() > 0){
				return session.find(Process.class, process.getId());
			}
			else if(process.getName() != null){
				return session.createQuery("FROM Process WHERE name = '" + process.getName() + "'", Process.class).getSingleResult();
			}
			else if(process.getIdBpms() != null){
				return session.createQuery("FROM Process WHERE idBpms = '" + process.getIdBpms() + "'", Process.class).getSingleResult();
			}
			else{
				System.err.println("Nao foi possivel carregar a entidade Processo. Parametros nao fornecidos");
			}
		} catch (NoResultException ex) {		
			throw ex;
		} catch (Exception ex) {			
			throw ex;		
		}
		return process;
	}
	
	public static List<Process> readAll() throws CRUDException {
		EntityManager session = Session.getSession();
		List<Process> list = null;
		try {		
			list = session.createQuery("FROM Process", Process.class).getResultList();
		} catch (NoResultException ex) {		
			throw ex;
		} catch (Exception ex) {
			System.err.println(ex.getMessage());
			throw CRUDException.getException("Inconformidade ao consultar lista de Processos", ex);
		}
		return list;
	}
}
