package com.easybpms.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

@Entity
public class User implements IUser {
	
	@Id
	@GeneratedValue(generator="UserId")
	@GenericGenerator(name="UserId", strategy="increment")
	private int id;
	
	private String idApp;
	
	private String name;
	
	//@transient - não persiste no bd
	@Transient
	private List<String> userGroupNames;
	
	/*@ManyToOne
    @JoinColumn(name = "userGroup_id",foreignKey=@ForeignKey(name = "usergroup_user_FK"))
    private UserGroup userGroup;*/
	
	@ManyToMany(mappedBy = "users")
    private List<UserGroup> userGroups = new ArrayList<UserGroup>();
	
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ActivityInstance> activityInstances = new ArrayList<ActivityInstance>();

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getIdApp() {
		return idApp;
	}

	public void setIdApp(String idApp) {
		this.idApp = idApp;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<UserGroup> getUserGroups() {
		return userGroups;
	}

	public void setUserGroups(List<UserGroup> userGroups) {
		this.userGroups = userGroups;
	}

	public List<ActivityInstance> getActivityInstances() {
		return activityInstances;
	}

	public void setActivityInstances(List<ActivityInstance> activityInstances) {
		this.activityInstances = activityInstances;
	}
	//sincroniza a associação bidirecional User-ActivityInstance
	public void addActivityInstance(ActivityInstance activityInstance){
		activityInstances.add(activityInstance);
		activityInstance.setUser(this);
	}
				
	//sincroniza a associação bidirecional User-ActivityInstance
	public void removeActivityInstance(ActivityInstance activityInstance){
		activityInstances.remove(activityInstance);
		activityInstance.setUser(null);
	}
	
	public List<String> getUserGroupNames() {
		return userGroupNames;
	}

	public void setUserGroupNames(List<String> userGroupNames) {
		this.userGroupNames = userGroupNames;
	}

}
