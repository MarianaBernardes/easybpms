package com.easybpms.event;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import com.easybpms.bd.CRUDException;
import com.easybpms.bd.dao.CRUDEntity;
import com.easybpms.bd.dao.CRUDParameterInstance;
import com.easybpms.bpms.AbstractBpmsInterface;
import com.easybpms.domain.ParameterInstance;
import com.easybpms.domain.Process;
import com.easybpms.domain.Property;

public class StartProcessObserver implements Observer{
	private String processIdBpms; //nomedopacote_nomedaclasse
	
	/**
	 * StartProcessObserver instanciado no Context
	 * @param processIdBpms - id do processo no bpmn
	 */
	public StartProcessObserver (String processIdBpms){
		this.processIdBpms = processIdBpms; 
	}
	
	/**
	 * Observador notificado todas as vezes que o metodo notifyObservers for chamado na aplicacao
	 * Observador sera escolhido quando o id do processo no bpmn (processidbpms) for igual ao nome da classe 
	 * que inicia o processo na aplicacao (className). Exemplo: entidade com.solicitarviagem.domain.Viagem
	 * @param arg - Instancia da classe que inicia o processo na aplicacao
	 */
	public void update(Observable o, Object arg) {
		
		String processidbpms; //nomedopacote.nomedaclasse
		processidbpms = this.processIdBpms.replace("_",".");
		
		String className = arg.getClass().getName(); //nomedopacote.nomedaclasse
		
		//System.out.println("\nStart Process Observer do processo " + processidbpms + " notificado.Objeto passado: " + className + "\n");
		
		if(!className.equals(processidbpms)){ 
			return;
		}
		
		/**
		 * @param inputParamName - parametro de entrada que deve ser inserido para todas as atividades de usuario
		 * do processo ne negocio. 
		 * Para iniciar o processo correto, esse parametro tem que ser correspondente ao  id da entidade de dominio 
		 * que inicia o processo na aplicacao. Exemplo: parametro de entrada easybpms_com_solicitarviagem_domain_Viagem_id 
		 */
		String inputParamName = "easybpms." + className + ".id";
		inputParamName = inputParamName.replace(".","_"); 
		
		/**
		 * @param inputParamValue - valor do parametro de entrada buscado na aplicacao. Exemplo: valor do id da entidade Viagem
		 * Obs: Utiliza API de reflexao para invocar o metodo da instancia da entidade de dominio da aplicacao e seu respectivo valor
		 */
		String inputParamValue = null;
		try {
			Method m = arg.getClass().getMethod("getId");
			Long i = (Long)m.invoke(arg);
			inputParamValue = i.toString(); 
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		/**
		 * @param - listIp - lista de todas as instancias parametro do easybpms
		 */
		List<ParameterInstance> listIp = null;
		try {
			listIp = CRUDParameterInstance.readAll();
		} catch (CRUDException e) {
			e.printStackTrace();
		}
		
		/**
		 * Para cada instancia parametro da lista verifica se existe uma instancia da entidade de dominio
		 * que inicia o processo na aplicacao. Ou seja, onde o inputParamName e igual a  algum parametro de entrada
		 * do easybpms e o inputParamValue eh igual a alguma instancia parametro do easybpms.
		 * Se a condicao for satisfeita significa que o processo ja foi iniciado
		 */
		for (ParameterInstance ip : listIp){
			if(ip.getParameter().getName().equals(inputParamName) && ip.getValue().equals(inputParamValue)){ 
				return; //Processo ja iniciado
			}
		}
		
		/**
		 * Se a condicao nao for satisfeita significa que o processo nao foi iniciado
		 * Para descobrir a definicao de processo e criar uma instancia, busca no easybpms o processo
		 * que tem id igual ao processIdBpms. Exemplo: com_solicitarviagem_domain_Viagem
		 */
		//Processo nao iniciado
		Process p = new Process();
		p.setIdBpms(this.processIdBpms);
		
		List<Property> listP = null;
		
		try {
			p = (Process) CRUDEntity.read(p);
			/**
			 * @param listP - lista de todas as propriedades do respectivo processo 
			 */
			listP = p.getProperties();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		/**
		 * Para iniciar o processo, e necessario enviar o valor das suas propriedades
		 * @param params - mapa que armazena o nome das propriedade e seus respectivos valores
		 * O valor de cada propriedade e o valor de cada atributo da entidade de dominio da aplicacao, 
		 * que eh invocado por meio da API de reflexao
		 */
		Map<String,Object> params = new HashMap<String, Object>();
		for (Property pr : listP){
			String[] partsProperty = pr.getName().split("\\_");
			//String classProperty = partsProperty[partsProperty.length - 2];
			//if (className.contains(classProperty)){
				String attribute = partsProperty[partsProperty.length - 1]; 
				String methodName = "get" + attribute.substring(0,1).toUpperCase() + attribute.substring(1);
				
				try {
					Method m = arg.getClass().getMethod(methodName); 
					Object i = m.invoke(arg); 
					String propertyValue = i.toString(); 
					params.put(pr.getName(), propertyValue);
				} catch (Exception e) {
					System.out.println("Atributo " + attribute + " deve ser inicializado na aplicacao!");
					e.printStackTrace();
				}	
			//}
		}
		
		if (params.isEmpty()){
			params = null;
		}
		/**
		 * @param processInstanceId - recebe o id da instancia processo criada no bpms
		 */
		System.out.println("\nProcesso " + p.getName() + " iniciado!\n");
		AbstractBpmsInterface.getBpmsInterface().startProcess(this.processIdBpms, params);
		
	}

}
