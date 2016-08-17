package com.easybpms.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import javax.persistence.ForeignKey;
//import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.GenericGenerator;

@Entity
public class Activity implements IEntity{
	@Id
	@GeneratedValue(generator="ActivityId")
	@GenericGenerator(name="ActivityId", strategy="increment")
	private int id;
	
	private String idBpms;
	
	private String name;
	
	
	@OneToMany(mappedBy = "activity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Parameter> parameters = new ArrayList<Parameter>();
	
	@OneToMany(mappedBy = "activity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ActivityInstance> activityInstances = new ArrayList<ActivityInstance>();

	@ManyToOne
	//@JoinColumn(name = "process_id")
	//@ForeignKey(name = "process_activity_FK")
    @JoinColumn(name = "process_id",foreignKey=@ForeignKey(name = "process_activity_FK"))
    private Process process;
	
	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	//@JoinColumn(name = "usergroup_id")
	//@ForeignKey(name = "usergroup_activity_FK")
    @JoinColumn(name = "usergroup_id",foreignKey=@ForeignKey(name = "usergroup_activity_FK"))
    private UserGroup userGroup;
	
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
	
	public String getIdBpms() {
		return idBpms;
	}

	public void setIdBpms(String idBpms) {
		this.idBpms = idBpms;
	}

	public Process getProcess() {
		return process;
	}

	public void setProcess(Process process) {
		this.process = process;
	}

	
	public UserGroup getUserGroup() {
		return userGroup;
	}

	public void setUserGroup(UserGroup userGroups) {
		this.userGroup = userGroups;
	}

	public List<ActivityInstance> getActivityInstances() {
		return activityInstances;
	}

	public void setActivityInstances(List<ActivityInstance> activityInstances) {
		this.activityInstances = activityInstances;
	}
	
	public List<Parameter> getParameters() {
		return parameters;
	}

	public void setParameters(List<Parameter> parameters) {
		this.parameters = parameters;
	}
	
	//sincroniza a associacao bidirecional Activity-Parameter
	public void addParameter(Parameter parameter){
		parameters.add(parameter);
		parameter.setActivity(this);
	}
	
	//sincroniza a associacao bidirecional Activity-Parameter
	public void removeParameter(Parameter parameter){
		parameters.remove(parameter);
		parameter.setActivity(null);
	}
	
	//sincroniza a associacao bidirecional Activity-ActivityInstance
	public void addActivityInstance(ActivityInstance activityInstance){
		activityInstances.add(activityInstance);
		activityInstance.setActivity(this);
	}
			
	//sincroniza a associacao bidirecional Activity-ActivityInstance
	public void removeActivity(ActivityInstance activityInstance){
		activityInstances.remove(activityInstance);
		activityInstance.setActivity(null);
	}
}
