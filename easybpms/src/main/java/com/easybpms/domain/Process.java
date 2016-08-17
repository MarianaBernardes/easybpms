package com.easybpms.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.hibernate.annotations.GenericGenerator;

@Entity
public class Process implements IEntity{

	@Id
	@GeneratedValue(generator="ProcessId")
	@GenericGenerator(name="ProcessId", strategy="increment")
	private int id;
	
	private String idBpms;
	
	private String name;
	
	/*Save BD - Ao persistir a entidade Processo, o "cascade" permitira a persistencia das Atividades tambem
	 Remocao BD - Ao remover uma atividade da lista de atividades, o "orphanRemoval" permitira a remocao da respectiva Atividade*/
	@OneToMany(mappedBy = "process", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Activity> activities = new ArrayList<Activity>();
	
	@OneToMany(mappedBy = "process", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Property> properties = new ArrayList<Property>();
	
	@OneToMany(mappedBy = "process", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProcessInstance> processInstances = new ArrayList<ProcessInstance>();
	
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
	
	public List<Activity> getActivities() {
		return activities;
	}

	public void setActivities(List<Activity> activities) {
		this.activities = activities;
	}
	
	public List<Property> getProperties() {
		return properties;
	}

	public void setProperties(List<Property> properties) {
		this.properties = properties;
	}
	
	public List<ProcessInstance> getProcessInstances() {
		return processInstances;
	}

	public void setProcessInstances(List<ProcessInstance> processInstances) {
		this.processInstances = processInstances;
	}
	
	public String getIdBpms() {
		return idBpms;
	}

	public void setIdBpms(String idBpms) {
		this.idBpms = idBpms;
	}
	
	/*
	 * OneToMany unidirecional (relacao somente no lado do pai):
	 * Ineficientes ao remover entidades filho, pois o Hibernate exclui todos os filhos do BD e reinsere os que ainda se encontram na memoria
	 * Ex:process.getActivities().remove(activity1);
	 * 
	 * OneToMany bidirecional (relacao no pai e no filho):
	 * A remocao da entidade filho requer uma unica atualizacao(em que a coluna de chave estrangeira eh definida como NULL)
	 * Ex:process.removeActivity(activity1);
	 */


	//sincroniza a associação bidirecional Process-Activity
	public void addActivity(Activity activity){
		activities.add(activity);
		activity.setProcess(this);
	}
	
	//sincroniza a associação bidirecional Process-Activity
	public void removeActivity(Activity activity){
		activities.remove(activity);
		activity.setProcess(null);
	}
	
	//sincroniza a associação bidirecional Process-Variable
	public void addVariable(Property variable){
		properties.add(variable);
		variable.setProcess(this);
	}
		
	//sincroniza a associação bidirecional Process-Variable
	public void removeVariable(Property variable){
		properties.remove(variable);
		variable.setProcess(null);
	}
	
	//sincroniza a associação bidirecional Process-ProcessInstance
	public void addProcessInstance(ProcessInstance processInstance){
		processInstances.add(processInstance);
		processInstance.setProcess(this);
	}
			
	//sincroniza a associação bidirecional Process-ProcessInstance
	public void removeProcessInstance(ProcessInstance processInstance){
		processInstances.remove(processInstance);
		processInstance.setProcess(null);
	}
}
