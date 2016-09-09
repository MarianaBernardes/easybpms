package org.fixwo.dao;

import org.fixwo.domain.Ocorrencia;
import org.fixwo.process.FixwoNextflow;

public class OcorrenciaDAO extends FixwoNextflow{
	
	public void create (Ocorrencia o){
		executeTask(o);
	}
	
	public void update (Ocorrencia o){
		executeTask(o);
		
	}

}
