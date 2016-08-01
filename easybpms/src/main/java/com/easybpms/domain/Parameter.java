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
public class Parameter implements IEntity{

	@Id
	@GeneratedValue(generator="ParameterId")
	@GenericGenerator(name="ParameterId", strategy="increment")
	private int id;
	
	private String name;
	
	private String type;
	
	@OneToMany(mappedBy = "parameter", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ParameterInstance> parameterInstances = new ArrayList<ParameterInstance>();
	
	@ManyToOne
	@JoinColumn(name = "activity_id")
	@ForeignKey(name = "activity_parameter_FK")
    //@JoinColumn(name = "activity_id",foreignKey=@ForeignKey(name = "activity_parameter_FK"))
    private Activity activity;
	
	/*@ManyToOne
    @JoinColumn(name = "activity_input_id",foreignKey=@ForeignKey(name = "activity_inputparameter_FK"))
    private Activity activityInput;
	
	@ManyToOne
    @JoinColumn(name = "activity_output_id",foreignKey=@ForeignKey(name = "activity_outputparameter_FK"))
    private Activity activityOutput;*/
	

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


	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	/*public Activity getActivityInput() {
		return activityInput;
	}

	public void setActivityInput(Activity activityInput) {
		this.activityInput = activityInput;
	}

	public Activity getActivityOutput() {
		return activityOutput;
	}

	public void setActivityOutput(Activity activityOutput) {
		this.activityOutput = activityOutput;
	}*/

	public Activity getActivity() {
		return activity;
	}

	public void setActivity(Activity activity) {
		this.activity = activity;
	}

	public List<ParameterInstance> getParameterInstances() {
		return parameterInstances;
	}

	public void setParameterInstances(List<ParameterInstance> parameterInstances) {
		this.parameterInstances = parameterInstances;
	}
	
	//sincroniza a associação bidirecional Parameter-ParameterInstance
	public void addParameterInstance(ParameterInstance parameterInstance){
		parameterInstances.add(parameterInstance);
		parameterInstance.setParameter(this);
	}
				
	//sincroniza a associação bidirecional Parameter-ParameterInstance
	public void removeParameterInstance(ParameterInstance parameterInstance){
		parameterInstances.remove(parameterInstance);
		parameterInstance.setParameter(null);
	}
}
