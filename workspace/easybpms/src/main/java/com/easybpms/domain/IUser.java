package com.easybpms.domain;

import java.util.List;


public interface IUser {

	/**
	 * idApp is Application id
	 * 
	 * @return
	 */
	public String getIdApp();
	public String getName();
	public List<String> getUserGroupNames();
}
