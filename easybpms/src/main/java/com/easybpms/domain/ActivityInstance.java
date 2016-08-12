package com.easybpms.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
//import javax.persistence.ForeignKey;
import org.hibernate.annotations.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

@Entity
public class ActivityInstance implements IEntity{
	@Id
	@GeneratedValue(generator="ActivityInstanceId")
	@GenericGenerator(name="ActivityInstanceId", strategy="increment")
	private int id;

	private String idBpms;
	
	private String status;

	@Transient
	private boolean currentTransaction = false;

	public boolean isCurrentTransaction() {
		return currentTransaction;
	}

	public void setCurrentTransaction(boolean currentTransaction) {
		this.currentTransaction = currentTransaction;
	}

	@OneToMany(mappedBy = "activityInstance", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ParameterInstance> parameterInstances = new ArrayList<ParameterInstance>();
	
	/*@OneToMany(mappedBy = "activityInstanceInput", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ParameterInstance> inputParameterInstances = new ArrayList<ParameterInstance>();
	
	@OneToMany(mappedBy = "activityInstanceOutput", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ParameterInstance> outputParameterInstances = new ArrayList<ParameterInstance>();*/
	
	@ManyToOne
	@JoinColumn(name = "processInstance_id")
	@ForeignKey(name = "processinstance_activityinstance_FK")
    //@JoinColumn(name = "processInstance_id",foreignKey=@ForeignKey(name = "processinstance_activityinstance_FK"))
    private ProcessInstance processInstance;
	
	@ManyToOne
	@JoinColumn(name = "activity_id")
	@ForeignKey(name = "activity_activityInstance_FK")
    //@JoinColumn(name = "activity_id",foreignKey=@ForeignKey(name = "activity_activityInstance_FK"))
    private Activity activity;
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	@ForeignKey(name = "user_activityInstance_FK")
    //@JoinColumn(name = "user_id",foreignKey=@ForeignKey(name = "user_activityInstance_FK"))
    private User user;

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

	public ProcessInstance getProcessInstance() {
		return processInstance;
	}

	public void setProcessInstance(ProcessInstance processInstance) {
		this.processInstance = processInstance;
	}

	public Activity getActivity() {
		return activity;
	}

	public void setActivity(Activity activity) {
		this.activity = activity;
	}

	/*public List<ParameterInstance> getInputParameterInstances() {
		return inputParameterInstances;
	}

	public void setInputParameterInstances(
			List<ParameterInstance> inputParameterInstances) {
		this.inputParameterInstances = inputParameterInstances;
	}

	public List<ParameterInstance> getOutputParameterInstances() {
		return outputParameterInstances;
	}

	public void setOutputParameterInstances(
			List<ParameterInstance> outputParameterInstances) {
		this.outputParameterInstances = outputParameterInstances;
	}*/
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	public List<ParameterInstance> getParameterInstances() {
		return parameterInstances;
	}

	public void setParameterInstances(List<ParameterInstance> parameterInstances) {
		this.parameterInstances = parameterInstances;
	}

	//sincroniza a associa��o bidirecional ActivityInstance-InputParameter
	public void addParameterInstance(ParameterInstance parameterInstance){
		parameterInstances.add(parameterInstance);
		parameterInstance.setActivityInstance(this);
	}
			
	//sincroniza a associa��o bidirecional ActivityInstance-InputParameter
	public void removeParameterInstance(ParameterInstance parameterInstance){
		parameterInstances.remove(parameterInstance);
		parameterInstance.setActivityInstance(null);
	}

	/*//sincroniza a associa��o bidirecional ActivityInstance-InputParameter
	public void addInputParameterInstance(ParameterInstance parameterInstance){
		inputParameterInstances.add(parameterInstance);
		parameterInstance.setActivityInstanceInput(this);
	}
			
	//sincroniza a associa��o bidirecional ActivityInstance-InputParameter
	public void removeInputParameterInstance(ParameterInstance parameterInstance){
		inputParameterInstances.remove(parameterInstance);
		parameterInstance.setActivityInstanceInput(null);
	}
		
	//sincroniza a associa��o bidirecional ActivityInstance-OutputParameter
	public void addOutputParameterInstance(ParameterInstance parameterInstance){
		outputParameterInstances.add(parameterInstance);
		parameterInstance.setActivityInstanceOutput(this);
	}
				
	//sincroniza a associa��o bidirecional ActivityInstance-OutputParameter
	public void removeOutputParameterInstance(ParameterInstance parameterInstance){
		outputParameterInstances.remove(parameterInstance);
		parameterInstance.setActivityInstanceOutput(null);
	}*/

}
