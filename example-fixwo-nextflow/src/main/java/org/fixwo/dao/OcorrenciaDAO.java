package org.fixwo.dao;

import org.fixwo.domain.Ocorrencia;
import org.fixwo.process.FixwoNextflow;

public class OcorrenciaDAO extends FixwoNextflow{
	
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
