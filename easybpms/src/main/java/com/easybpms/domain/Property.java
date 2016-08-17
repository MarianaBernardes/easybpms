package com.easybpms.domain;

import javax.persistence.Entity;
import javax.persistence.ForeignKey;
//import org.hibernate.annotations.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.GenericGenerator;

@Entity
public class Property implements IEntity{
	@Id
	@GeneratedValue(generator="PropertyId")
	@GenericGenerator(name="PropertyId", strategy="increment")
	private int id;
	
	private String name;

	@ManyToOne
	//@JoinColumn(name = "process_id")
	//@ForeignKey(name = "process_property_FK")
    @JoinColumn(name = "process_id",foreignKey=@ForeignKey(name = "process_property_FK"))
    private Process process;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public Process getProcess() {
		return process;
	}

	public void setProcess(Process process) {
		this.process = process;
	}

}
