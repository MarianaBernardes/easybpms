package org.fixwo.dao;

import org.fixwo.domain.Occurrence;
import org.fixwo.process.FixwoJbpm;

public class CRUDOccurrence extends FixwoJbpm{
	
	public CRUDOccurrence(){
		super();
	}
	
	public void create (Occurrence o){
		executeFlow(o);
	}
	
	public void update (Occurrence o){
		executeFlow(o);
	}

}
