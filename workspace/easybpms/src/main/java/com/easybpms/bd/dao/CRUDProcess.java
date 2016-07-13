package com.easybpms.bd.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import com.easybpms.bd.CRUDException;
import com.easybpms.bd.Session;
import com.easybpms.domain.Process;

public class CRUDProcess {
	public static void create(Process entity, EntityManager session) throws CRUDException {
		try {			
			session.persist(entity);
		} catch (Exception ex) {			
			throw CRUDException.getExcecao(CRUDException.getInconformidadeCadastrar("processo"), ex);		
		}
	}
	
	public static void remove(Process entity, EntityManager session) throws CRUDException {
		try {			
			session.remove(entity);
		} catch (Exception ex) {			
			throw CRUDException.getExcecao(CRUDException.getInconformidadeExcluir("processo"), ex);		
		}
	}
	
	public static Process read(Process process, EntityManager session) throws CRUDException {
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
				System.out.println("Não foi possível carregar a entidade. Parâmetros não fornecidos.");
			}
		} catch (NoResultException ex1) {		
			throw ex1;
		} catch (Exception ex) {		
			throw CRUDException.getExcecao(CRUDException.getInconformidadeConsultar("processo"), ex);	
		}
		return process;
	}
	
	public static List<Process> readAll() throws CRUDException {
		EntityManager session = Session.getSession();
		List<Process> list = null;
		try {		
			list = session.createQuery("FROM Process", Process.class).getResultList();
		} catch (NoResultException ex1) {		
			throw ex1;
		} catch (Exception ex) {		
			throw CRUDException.getExcecao(CRUDException.getInconformidadeConsultar("processos"), ex);	
		}
		return list;
	}
}
