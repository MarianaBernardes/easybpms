package com.easybpms.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
//import javax.persistence.ForeignKey;
import org.hibernate.annotations.ForeignKey;
import javax.persistence.OneToMany;

import org.hibernate.annotations.GenericGenerator;

@Entity
public class Activity implements IEntity{
	@Id
	@GeneratedValue(generator="ActivityId")
	@GenericGenerator(name="ActivityId", strategy="increment")
	private int id;
	
	private String idBpms;
	
	private String name;
	
	/*@ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<UserGroup> userGroups = new ArrayList<UserGroup>();*/
	
	@OneToMany(mappedBy = "activity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Parameter> parameters = new ArrayList<Parameter>();
	
	/*@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Parameter> inputParameters = new ArrayList<Parameter>();
	
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Parameter> outputParameters = new ArrayList<Parameter>();*/
	
	@OneToMany(mappedBy = "activity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ActivityInstance> activityInstances = new ArrayList<ActivityInstance>();

	@ManyToOne
	@JoinColumn(name = "process_id")
	@ForeignKey(name = "process_activity_FK")
    //@JoinColumn(name = "process_id",foreignKey=@ForeignKey(name = "process_activity_FK"))
    private Process process;
	
	@ManyToOne
	@JoinColumn(name = "usergroup_id")
	@ForeignKey(name = "usergroup_activity_FK")
    //@JoinColumn(name = "usergroup_id",foreignKey=@ForeignKey(name = "usergroup_activity_FK"))
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

	/*public List<Parameter> getInputParameters() {
		return inputParameters;
	}

	public void setInputParameters(List<Parameter> inputParameters) {
		this.inputParameters = inputParameters;
	}

	public List<Parameter> getOutputParameters() {
		return outputParameters;
	}

	public void setOutputParameters(List<Parameter> outputParameters) {
		this.outputParameters = outputParameters;
	}*/
	
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

	/*//sincroniza a associação bidirecional Activity-UserGroup
	public void addUserGroup(UserGroup userGroup){
		userGroups.add(userGroup);
		userGroup.getActivities().add(this);
	}
			
	//sincroniza a associação bidirecional Activity-UserGroup
	public void removeuserGroup(UserGroup userGroup){
		userGroups.remove(userGroup);
		userGroup.getActivities().remove(this);
	}*/
	
	//sincroniza a associação bidirecional Activity-InputParameter
	public void addParameter(Parameter parameter){
		parameters.add(parameter);
		parameter.setActivity(this);
	}
	
	//sincroniza a associação bidirecional Activity-InputParameter
	public void removeParameter(Parameter parameter){
		parameters.remove(parameter);
		parameter.setActivity(null);
	}
	
	/*//sincroniza a associação bidirecional Activity-InputParameter
	public void addInputParameter(Parameter parameter){
		inputParameters.add(parameter);
		parameter.setActivityInput(this);
	}
	
	//sincroniza a associação bidirecional Activity-InputParameter
	public void removeInputParameter(Parameter parameter){
		inputParameters.remove(parameter);
		parameter.setActivityInput(null);
	}
	
	//sincroniza a associação bidirecional Activity-OutputParameter
	public void addOutputParameter(Parameter parameter){
		outputParameters.add(parameter);
		parameter.setActivityOutput(this);
	}
	
	//sincroniza a associação bidirecional Activity-OutputParameter
	public void removeOutputParameter(Parameter parameter){
		outputParameters.remove(parameter);
		parameter.setActivityOutput(null);
	}*/
	
	//sincroniza a associação bidirecional Activity-ActivityInstance
	public void addActivityInstance(ActivityInstance activityInstance){
		activityInstances.add(activityInstance);
		activityInstance.setActivity(this);
	}
			
	//sincroniza a associação bidirecional Activity-ActivityInstance
	public void removeActivity(ActivityInstance activityInstance){
		activityInstances.remove(activityInstance);
		activityInstance.setActivity(null);
	}
}
