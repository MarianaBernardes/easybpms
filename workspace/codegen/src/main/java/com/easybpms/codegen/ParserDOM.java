package com.easybpms.codegen;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.*;


public class ParserDOM {
	
	String nameSpace = "";
    ProcessXml process = new ProcessXml();
	
	public void xmlToDOM (String path) throws ParserConfigurationException, SAXException, IOException {
		
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(new File(path));
        
      //Recupera o no raiz do XML
        Element root = document.getDocumentElement();
        
        String nomeProcesso = root.getNodeName();
        String[] nameSpaceAux = nomeProcesso.split(":");
        
     // Se existe namespace
        if (nameSpaceAux.length > 1) {
            nameSpace = nameSpaceAux[0];
            nameSpace = nameSpace + ":";
        }
        
      //Recupera os processos
        NodeList processes = document.getElementsByTagName(nameSpace + "process");

        Element idProcessElement = (Element) processes.item(0);
        String idProcess = idProcessElement.getAttribute("id");
        String nameProcess = idProcessElement.getAttribute("name");
      
      //Cria novo processo
        process.setId(idProcess);
        process.setName(nameProcess);
        
        String[] partsEntity = process.getId().split("\\_");
        if (partsEntity.length > 1) {
        	//partsEntity[partsEntity.length - 1] = nome da entidade
            String primeiraLetra = partsEntity[partsEntity.length - 1].substring(0, 1);
            process.setEntityProcess(partsEntity[partsEntity.length - 1].replaceFirst(primeiraLetra, primeiraLetra.toUpperCase()));
        }
        
      //Recupera um conjunto de variaveis de processo
        ArrayList<PropertyXml> listVariable = this.getProperties(processes.item(0));
      
	    //adiciona variaveis do processo
	    if (listVariable.size() > 0) {
	        process.setListProperty(listVariable);
	    }
      
        
      //Recupera um conjunto de tarefas de usuario
        HashMap<String, ActivityXml> listUserTask = this.getUserTasks(processes.item(0));
      
	    //adiciona tarefas de usuario para o processo
	    if (listUserTask.size() > 0) {
	        process.setListUserTask(listUserTask);
	    }
	}
	
	//retorna processo
	public ProcessXml getProcess() {
        return process;
    }

	private ArrayList<PropertyXml> getProperties(Node process) {

        Element element = (Element) process;
        NodeList listVariable = element.getElementsByTagName(nameSpace + "property");
        ArrayList<PropertyXml> retorno = new ArrayList<PropertyXml>();

        for (int i = 0; i < listVariable.getLength(); i++) {
            Element aux = (Element) listVariable.item(i);
            PropertyXml auxv = new PropertyXml();
            auxv.setName(aux.getAttribute("name"));  
            retorno.add(auxv);
        }
        return retorno;
    }
	
    
    private ActivityXml getUserTask(Node userTask) {
        Element element = (Element) userTask;
        ArrayList<ParameterXml> inputParameters = this.getListParameters(userTask, nameSpace + "dataInput");
        ArrayList<ParameterXml> outputParameters = this.getListParameters(userTask, nameSpace + "dataOutput");
        ArrayList<UserGroupXml> usersGroup = this.getListUsers(userTask, nameSpace + "potentialOwner");
        
        ActivityXml aux = new ActivityXml();
        aux.setId(element.getAttribute("id"));
        aux.setName(element.getAttribute("name"));
        aux.setInputParameter(inputParameters);
        aux.setOutputParameter(outputParameters);
        aux.setUserGroup(usersGroup);
        //EntityTask add para que a atividade tenha somente um observador. Somente um parâmetro de entrada da lista será escolhido para obter o observador
        aux.setEntityTask(inputParameters.get(0).getEntityTask());
        return aux;
    }
    
    private HashMap<String, ActivityXml> getUserTasks(Node process) {
        HashMap<String, ActivityXml> listTasks = new HashMap<String, ActivityXml>();
        Element elementProcess = (Element) process;
        NodeList listTask = elementProcess.getElementsByTagName(nameSpace + "userTask");

        for (int i = 0; i < listTask.getLength(); i++) {
            ActivityXml task = this.getUserTask(listTask.item(i));
            listTasks.put(task.getId(), task);
        }

        return listTasks;
    }
    
    private ArrayList<UserGroupXml> getListUsers(Node userTask, String userGroup) {

        Element element = (Element) userTask;
        NodeList listUserGroup = element.getElementsByTagName(userGroup);
        ArrayList<UserGroupXml> retorno = new ArrayList<UserGroupXml>();

        for (int i = 0; i < listUserGroup.getLength(); i++) {

            Element aux = (Element) listUserGroup.item(i);
            NodeList users = aux.getElementsByTagName(nameSpace + "formalExpression");
            for (int j = 0; j < users.getLength(); j++) {
            	UserGroupXml user = new UserGroupXml();
                user.setName(users.item(j).getTextContent());
                retorno.add(user);
            }
        }
        return retorno;
    }
    
    private ArrayList<ParameterXml> getListParameters(Node userTask, String input_output) {

        Element element = (Element) userTask;
        NodeList listParameter = element.getElementsByTagName(input_output);
        ArrayList<ParameterXml> retorno = new ArrayList<ParameterXml>();

        for (int i = 0; i < listParameter.getLength(); i++) {

            Element aux = (Element) listParameter.item(i);
            if (aux.getAttribute("name").contains("easybpms")) {
                ParameterXml auxp = new ParameterXml();
                auxp.setId(aux.getAttribute("id"));
                auxp.setName(aux.getAttribute("name"));
                String[] partsEntity = auxp.getName().split("\\_");
                if (partsEntity.length > 1) {
                	//partsEntity[partsEntity.length - 2] = nome da entidade
                    String primeiraLetra = partsEntity[partsEntity.length - 2].substring(0, 1);
                    auxp.setEntityTask(partsEntity[partsEntity.length - 2].replaceFirst(primeiraLetra, primeiraLetra.toUpperCase()));
                }
                
                retorno.add(auxp);
            }
        }
        return retorno;
    }

}
