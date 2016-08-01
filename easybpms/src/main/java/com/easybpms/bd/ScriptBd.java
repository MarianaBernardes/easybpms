package com.easybpms.bd;

import java.util.List;

import com.easybpms.bd.dao.CRUDEntity;
import com.easybpms.domain.ProcessInstance;
import com.easybpms.domain.Process;

public class ScriptBd {

	public static void main(String[] args) throws CRUDException {

		Session.getSession();
		
		ProcessInstance p = new ProcessInstance();
		p.setId(1);
		//CRUDEntity.create(p);
		p = (ProcessInstance) CRUDEntity.read(p);
		CRUDEntity.remove(p);
		
		
		/*for (int i=1; i<=6; i++){
			UserGroup u = new UserGroup();
			u.setId(i);
			u = (UserGroup) CRUDEntity.read(u);
			CRUDEntity.remove(u);
		}*/
		
		/*GenericBpmsConnector bpms = new GenericBpmsConnector();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("easybpms_com_tdm_Viagem_id", "123");
		bpms.execute("User Task 2", "3", "Reserved", params, "processName", "1");*/
		
		
		/*Variable v = new Variable();
		v.setName("Variavel4");
		p.addVariable(v);
		//p.getVariables().add(v);
		CRUDEntity.create(v);*/
		
		/*System.out.println ("Id Processo da Variavel " + v.getProcess().getId());
		
		p.removeVariable(v);
		CRUDEntity.remove(v);*/
		
		
		/*Activity a1 = new Activity();
		Activity a2 = new Activity();
		
		
		a1.setProcess(session.find(Process.class,p.getId()));
		a2.setProcess(session.find(Process.class,p.getId()));
		a1.setName("Atividade1");
		a2.setName("Atividade2");
		p.addActivity(a1);
		p.addActivity(a2);
		
		p.removeActivity(a1);
		p.removeActivity(a2);
		
		transaction.begin();
		//session.persist(p);
		session.remove(p);
		transaction.commit();
		//session.flush(); //obriga a escrever os dados no disco*/
		
		//List<Variable> vlist =  p.getVariables();
		//System.out.println("Tamanho da lista de variavel " + vlist.size());
		
		/*Variable v2 = new Variable();
		v2.setName("Variavel2");
		p.addVariable(v2);
		CRUDEntity.create(v2);
		
		vlist =  p.getVariables();
		System.out.println("Tamanho da lista de variavel " + vlist.size());*/
		
		Session.closeSession();
	}
	
	public static void deleteProcessesInstances(List<Integer> listProcessesInstances){
		if (listProcessesInstances != null){
			Session.getSession();
			for (Integer i : listProcessesInstances){
				ProcessInstance p = new ProcessInstance();
				p.setId(i);
				try {
					p = (ProcessInstance) CRUDEntity.read(p);
					CRUDEntity.remove(p);
				} catch (CRUDException e) {
					e.printStackTrace();
				}	
			}
			Session.closeSession();
		}
	}
	
	public static void deleteProcesses(List<Integer> listProcesses){
		if (listProcesses != null){
			Session.getSession();
			for (Integer i : listProcesses){
				Process p = new Process();
				p.setId(i);
				try {
					p = (Process) CRUDEntity.read(p);
					CRUDEntity.remove(p);
				} catch (CRUDException e) {
					e.printStackTrace();
				}	
			}
			Session.closeSession();
		}
	}
}
