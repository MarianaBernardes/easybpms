package org.fixwo.dao;

import org.fixwo.domain.Ocorrencia;
import com.easybpms.event.CRUDObservable;

public class OcorrenciaDAO extends CRUDObservable{
	
	public void create (Ocorrencia o){
		notifyObservers(o);
	}
	
	public void update (Ocorrencia o){
		notifyObservers(o);
	}

}
