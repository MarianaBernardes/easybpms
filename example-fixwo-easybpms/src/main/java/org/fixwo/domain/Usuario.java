package org.fixwo.domain;

import java.util.List;

import com.easybpms.domain.IUser;

public class Usuario implements IUser{

	String name;
	String idApp;
	String tenancy;
	List<String> userGroupNames;
	
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
	
	public void setUserGroupNames(List<String> userGroupNames) {
		this.userGroupNames = userGroupNames;
	}
	
	public List<String> getUserGroupNames() {
		return userGroupNames;
	}

	public String getTenancy() {
		return tenancy;
	}

	public void setTenancy(String tenancy) {
		this.tenancy = tenancy;
	}

}
