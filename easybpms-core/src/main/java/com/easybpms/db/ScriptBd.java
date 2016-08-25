package com.easybpms.db;

import java.util.List;

import com.easybpms.domain.ProcessInstance;
import com.easybpms.db.dao.CRUDEntity;
import com.easybpms.domain.Process;

public class ScriptBd {

	
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
		}
	}
}
