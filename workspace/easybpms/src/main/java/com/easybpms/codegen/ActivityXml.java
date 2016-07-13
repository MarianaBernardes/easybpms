package com.easybpms.codegen;

import java.util.ArrayList;

public class ActivityXml extends Marcacao{
	
	private ArrayList<ParameterXml> inputParameter;
    private ArrayList<ParameterXml> outputParameter;
    private ArrayList<UserGroupXml> userGroup;
   

    public ActivityXml() {
    }

    public ArrayList<ParameterXml> getInputParameter() {
        return inputParameter;
    }

    public void setInputParameter(ArrayList<ParameterXml> inputParameter) {
        this.inputParameter = inputParameter;
    }

    public ArrayList<ParameterXml> getOutputParameter() {
        return outputParameter;
    }

    public void setOutputParameter(ArrayList<ParameterXml> outputParameter) {
        this.outputParameter = outputParameter;
        
    }

	public ArrayList<UserGroupXml> getUserGroup() {
		return userGroup;
	}

	public void setUserGroup(ArrayList<UserGroupXml> userGroup) {
		this.userGroup = userGroup;
	}

}
