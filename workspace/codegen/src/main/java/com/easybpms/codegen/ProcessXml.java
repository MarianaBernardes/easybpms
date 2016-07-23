package com.easybpms.codegen;

import java.util.ArrayList;
import java.util.HashMap;

public class ProcessXml extends Marcacao{
	
	private HashMap<String, ActivityXml> listUserTask;
	private ArrayList<PropertyXml> listProperty;
	private ArrayList<TaskServiceXml> listServiceTask;
    private String filePath;
    private String entityProcess;
    

    public ProcessXml() {
    }

    public HashMap<String, ActivityXml> getListUserTask() {
        return listUserTask;
    }

    public void setListUserTask(HashMap<String, ActivityXml> listUserTask) {
        this.listUserTask = listUserTask;
    }
    
    public ArrayList<PropertyXml> getListProperty() {
		return listProperty;
	}

	public void setListProperty(ArrayList<PropertyXml> listProperty) {
		this.listProperty = listProperty;
	}

	public ArrayList<TaskServiceXml> getListServiceTask() {
		return listServiceTask;
	}

	public void setListServiceTask(ArrayList<TaskServiceXml> listServiceTask) {
		this.listServiceTask = listServiceTask;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getEntityProcess() {
		return entityProcess;
	}

	public void setEntityProcess(String entityProcess) {
		this.entityProcess = entityProcess;
	}
}
