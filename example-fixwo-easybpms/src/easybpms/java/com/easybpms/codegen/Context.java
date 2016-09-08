package com.easybpms.codegen;

import java.util.ArrayList;
import java.util.Observer;

import com.easybpms.domain.Process;
import com.easybpms.domain.*;
import com.easybpms.event.StartProcessObserver;
import com.easybpms.event.TaskExecutedObserver;
import com.easybpms.bpms.BpmsSession;
import com.easybpms.jbpm .ConcreteBpmsInterface;
import com.easybpms.jbpm .JbpmSession;

public class Context extends AbstractContext {
	
	ArrayList<String> processPaths = new ArrayList<String>();
	
	public Context() {

		Process process;
		Property property;
		UserGroup userGroup;
		Activity activity;
		Parameter parameter;

		ArrayList<Observer> listObservers = new ArrayList<Observer>();

		/*
		 * Cria e mapeia observador de inicio de processo
		 */ 
		StartProcessObserver spo = new StartProcessObserver("org_fixwo_domain_Ocorrencia");
		listObservers.add(spo);
		addMapping("CRUDOcorrencia", listObservers);


		/*
		 * Cria e mapeia observadores de tarefas
		 */ 
		TaskExecutedObserver teo; 
		
		listObservers = new ArrayList<Observer>();
		teo = new TaskExecutedObserver("UserTask_1");
		listObservers.add(teo); 
		addMapping("CRUDOcorrencia", listObservers);
		
		listObservers = new ArrayList<Observer>();
		teo = new TaskExecutedObserver("UserTask_2");
		listObservers.add(teo); 
		addMapping("CRUDOcorrencia", listObservers); 
		          
		listObservers = new ArrayList<Observer>();
		teo = new TaskExecutedObserver("UserTask_3");
		listObservers.add(teo); 
		addMapping("CRUDOcorrencia", listObservers);           



		/*
		 * Cria definicao do processo
		 */

		//Processo fixwo
		process = new Process();
		process.setName("fixwo");
		process.setIdBpms("org_fixwo_domain_Ocorrencia");
		processPaths.add("C:\\Users\\saudeuser\\Documents\\GitLocal\\easybpms\\example-fixwo-easybpms\\src\\main\\resources\\processes\\fixwoProcess.bpmn2");

		//Variaveis do Processo fixwo  
		property = new Property();
		property.setName("org_fixwo_domain_Ocorrencia_id");
		process.addVariable(property);	
		property = new Property();
		property.setName("org_fixwo_domain_Ocorrencia_tenancy");
		process.addVariable(property);	
		property = new Property();
		property.setName("org_fixwo_domain_Ocorrencia_existeArea");
		process.addVariable(property);	
		property = new Property();
		property.setName("org_fixwo_domain_Ocorrencia_status");
		process.addVariable(property);	
		property = new Property();
		property.setName("org_fixwo_domain_Ocorrencia_setor");
		process.addVariable(property);	
		property = new Property();
		property.setName("org_fixwo_domain_Ocorrencia_feedback");
		process.addVariable(property);	
		property = new Property();
		property.setName("org_fixwo_domain_Ocorrencia_avaliacao");
		process.addVariable(property);	

		//Atividades de Usuario do Processo fixwo
		activity = new Activity();
		activity.setName("Enviar Feedback ao Solicitante");
		activity.setIdBpms("UserTask_2");

		//Parametros de Entrada da Atividade Enviar Feedback ao Solicitante   
		parameter = new Parameter();
		parameter.setName("easybpms_org_fixwo_domain_Ocorrencia_id");
		parameter.setType("input");
		activity.addParameter(parameter);	
		parameter = new Parameter();
		parameter.setName("easybpms_org_fixwo_domain_Ocorrencia_tenancy");
		parameter.setType("input");
		activity.addParameter(parameter);	

		//Parametros de Saida da Atividade Enviar Feedback ao Solicitante
		parameter = new Parameter();
		parameter.setName("easybpms_org_fixwo_domain_Ocorrencia_feedback");
		parameter.setType("output");
		activity.addParameter(parameter);
		parameter = new Parameter();
		parameter.setName("easybpms_org_fixwo_domain_Ocorrencia_status");
		parameter.setType("output");
		activity.addParameter(parameter);

		//Grupos de Usuario da Atividade Enviar Feedback ao Solicitante
		userGroup = new UserGroup();
		userGroup.setName("Responsavel Setor");
		setUserGroup(userGroup,activity);

		process.addActivity(activity);

		activity = new Activity();
		activity.setName("Classificar e Encaminhar ao Setor Responsavel");
		activity.setIdBpms("UserTask_1");

		//Parametros de Entrada da Atividade Classificar e Encaminhar ao Setor Responsavel   
		parameter = new Parameter();
		parameter.setName("easybpms_org_fixwo_domain_Ocorrencia_id");
		parameter.setType("input");
		activity.addParameter(parameter);	
		parameter = new Parameter();
		parameter.setName("easybpms_org_fixwo_domain_Ocorrencia_tenancy");
		parameter.setType("input");
		activity.addParameter(parameter);	

		//Parametros de Saida da Atividade Classificar e Encaminhar ao Setor Responsavel
		parameter = new Parameter();
		parameter.setName("easybpms_org_fixwo_domain_Ocorrencia_status");
		parameter.setType("output");
		activity.addParameter(parameter);
		parameter = new Parameter();
		parameter.setName("easybpms_org_fixwo_domain_Ocorrencia_setor");
		parameter.setType("output");
		activity.addParameter(parameter);

		//Grupos de Usuario da Atividade Classificar e Encaminhar ao Setor Responsavel
		userGroup = new UserGroup();
		userGroup.setName("Triador");
		setUserGroup(userGroup,activity);

		process.addActivity(activity);

		activity = new Activity();
		activity.setName("Avaliar Solucao");
		activity.setIdBpms("UserTask_3");

		//Parametros de Entrada da Atividade Avaliar Solucao   
		parameter = new Parameter();
		parameter.setName("easybpms_org_fixwo_domain_Ocorrencia_id");
		parameter.setType("input");
		activity.addParameter(parameter);	
		parameter = new Parameter();
		parameter.setName("easybpms_org_fixwo_domain_Ocorrencia_tenancy");
		parameter.setType("input");
		activity.addParameter(parameter);	

		//Parametros de Saida da Atividade Avaliar Solucao
		parameter = new Parameter();
		parameter.setName("easybpms_org_fixwo_domain_Ocorrencia_avaliacao");
		parameter.setType("output");
		activity.addParameter(parameter);

		//Grupos de Usuario da Atividade Avaliar Solucao
		userGroup = new UserGroup();
		userGroup.setName("Solicitante");
		setUserGroup(userGroup,activity);

		process.addActivity(activity);


		setProcess(process);

		//Fim do processo fixwo
		
	}

	@Override
	public void setBpmsSession(BpmsSession bpmsSession) {
		ConcreteBpmsInterface bpms = new ConcreteBpmsInterface();
		bpms.setBpmsSession((JbpmSession)bpmsSession);
	}
	
	@Override
	public void connect(){
		ConcreteBpmsInterface bpms = new ConcreteBpmsInterface();
		bpms.startBPMS(processPaths);
	}
}
