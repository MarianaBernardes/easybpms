package com.easybpms.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import org.hibernate.annotations.GenericGenerator;

@Entity
public class UserGroup implements IUserGroup {
	@Id
	@GeneratedValue(generator="UserGroupId")
	@GenericGenerator(name="UserGroupId", strategy="increment")
	private int id;
	
	private String name;
	
	/*@OneToMany(mappedBy = "userGroup", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<User> users = new ArrayList<User>();*/
	
	@ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	private List<User> users = new ArrayList<User>();
	
	/*@ManyToOne
    @JoinColumn(name = "activity_id",foreignKey=@ForeignKey(name = "activity_usergroup_FK"))
    private Activity activity;*/
	
	/*@ManyToMany(mappedBy = "userGroups")
	private List<Activity> activities = new ArrayList<Activity>();*/
	
	@OneToMany(mappedBy = "userGroup", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Activity> activities = new ArrayList<Activity>();
	
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

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}
	//sincroniza a associacao bidirecional UserGroup-User
	public void addUser(User user){
		users.add(user);
		user.getUserGroups().add(this);
		//user.setUserGroup(this);
	}
			
	//sincroniza a associacao bidirecional UserGroup-User
	public void removeUser(User user){
		users.remove(user);
		user.getUserGroups().remove(this);
		//user.setUserGroup(null);
	}
	
	//sincroniza a associacao bidirecional UserGroup-Activity
	public void addActivity(Activity activity){
		activities.add(activity);
		activity.setUserGroup(this);
	}
					
	//sincroniza a associacao bidirecional UserGroup-Activity
	public void removeActivity(Activity activity){
		activities.remove(activity);
		activity.setUserGroup(null);
	}
}
