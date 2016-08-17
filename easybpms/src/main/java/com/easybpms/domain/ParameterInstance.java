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
public class ParameterInstance implements IEntity{
	@Id
	@GeneratedValue(generator="ParameterInstanceId")
	@GenericGenerator(name="ParameterInstanceId", strategy="increment")
	private int id;

	private String idBpms;
	
	private String value;
	
	private String type;
	
	@ManyToOne
	//@JoinColumn(name = "activityInstance_id")
	//@ForeignKey(name = "activityInstance_parameterInstance_FK")
    @JoinColumn(name = "activityInstance_id",foreignKey=@ForeignKey(name = "activityInstance_parameterInstance_FK"))
    private ActivityInstance activityInstance;
	
	@ManyToOne
	//@JoinColumn(name = "parameter_id")
	//@ForeignKey(name = "parameter_parameterinstance_FK")
    @JoinColumn(name = "parameter_id",foreignKey=@ForeignKey(name = "parameter_parameterinstance_FK"))
    private Parameter parameter;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getIdBpms() {
		return idBpms;
	}

	public void setIdBpms(String idBpms) {
		this.idBpms = idBpms;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Parameter getParameter() {
		return parameter;
	}

	public void setParameter(Parameter parameter) {
		this.parameter = parameter;
	}

	public ActivityInstance getActivityInstance() {
		return activityInstance;
	}

	public void setActivityInstance(ActivityInstance activityInstance) {
		this.activityInstance = activityInstance;
	}

}
