package org.fixwo.dao;

import org.fixwo.domain.Ocorrencia;
import org.fixwo.process.FixwoJbpm;

public class OcorrenciaDAO extends FixwoJbpm{
	
	public OcorrenciaDAO(){
		super();
	}
	
	public void create (Ocorrencia o){
		executeFlow(o);
	}
	
	public void update (Ocorrencia o){
		executeFlow(o);
	}

}
