package org.fixwo.dao;

import org.fixwo.domain.Ocorrencia;
import org.fixwo.process.FixwoJbpm;

public class OcorrenciaDAO extends FixwoJbpm{

	public void create (Ocorrencia o){
		executeTask(o);
	}
	
	public void update (Ocorrencia o){
		executeTask(o);
	}

}
