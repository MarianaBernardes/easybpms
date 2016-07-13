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

import org.hibernate.annotations.GenericGenerator;

@Entity
public class ProcessInstance implements IEntity{
	@Id
	@GeneratedValue(generator="ProcessInstanceId")
	@GenericGenerator(name="ProcessInstanceId", strategy="increment")
	private int id;

	private String idBpms;
	
	private String status;
	
	@OneToMany(mappedBy = "processInstance", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ActivityInstance> activityInstances = new ArrayList<ActivityInstance>();
	
	@ManyToOne
	@JoinColumn(name = "process_id")
	@ForeignKey(name = "process_processinstance_FK")
    //@JoinColumn(name = "process_id",foreignKey=@ForeignKey(name = "process_processinstance_FK"))
    private Process process;

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
	public Process getProcess() {
		return process;
	}

	public void setProcess(Process process) {
		this.process = process;
	}
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<ActivityInstance> getActivityInstances() {
		return activityInstances;
	}

	public void setActivityInstances(List<ActivityInstance> activityInstances) {
		this.activityInstances = activityInstances;
	}

	//sincroniza a associação bidirecional Process-Activity
	public void addActivityInstance(ActivityInstance activityInstance){
		activityInstances.add(activityInstance);
		activityInstance.setProcessInstance(this);
	}
		
	//sincroniza a associação bidirecional Process-Activity
	public void removeActivity(ActivityInstance activityInstance){
		activityInstances.remove(activityInstance);
		activityInstance.setProcessInstance(null);
	}
}
