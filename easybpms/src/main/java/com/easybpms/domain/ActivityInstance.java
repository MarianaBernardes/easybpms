package com.easybpms.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
//import org.hibernate.annotations.ForeignKey;
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
	
	
	@ManyToOne
	//@JoinColumn(name = "processInstance_id")
	//@ForeignKey(name = "processinstance_activityinstance_FK")
    @JoinColumn(name = "processInstance_id",foreignKey=@ForeignKey(name = "processinstance_activityinstance_FK"))
    private ProcessInstance processInstance;
	
	@ManyToOne
	//@JoinColumn(name = "activity_id")
	//@ForeignKey(name = "activity_activityInstance_FK")
    @JoinColumn(name = "activity_id",foreignKey=@ForeignKey(name = "activity_activityInstance_FK"))
    private Activity activity;
	
	@ManyToOne
	//@JoinColumn(name = "user_id")
	//@ForeignKey(name = "user_activityInstance_FK")
    @JoinColumn(name = "user_id",foreignKey=@ForeignKey(name = "user_activityInstance_FK"))
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

	//sincroniza a associacao bidirecional ActivityInstance-ParameterInstance
	public void addParameterInstance(ParameterInstance parameterInstance){
		parameterInstances.add(parameterInstance);
		parameterInstance.setActivityInstance(this);
	}
			
	//sincroniza a associacao bidirecional ActivityInstance-ParameterInstance
	public void removeParameterInstance(ParameterInstance parameterInstance){
		parameterInstances.remove(parameterInstance);
		parameterInstance.setActivityInstance(null);
	}


}
