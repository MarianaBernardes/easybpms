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
	 * Observador notificado todas as vezes que o método notifyObservers for chamado na aplicação
	 * Observador será escolhido quando o id do processo no bpmn (processidbpms) for igual ao nome da classe 
	 * que inicia o processo na aplicação (className). Exemplo: entidade com.solicitarviagem.domain.Viagem
	 * @param arg - Instância da classe que inicia o processo na aplicação
	 */
	public void update(Observable o, Object arg) {
		
		String processidbpms; //nomedopacote.nomedaclasse
		processidbpms = this.processIdBpms.replace("_",".");
		
		String className = arg.getClass().getName(); //nomedopacote.nomedaclasse
		
		System.out.println("\nStart Process Observer do processo " + processidbpms + " notificado.Objeto passado: " + className + "\n");
		
		if(!className.equals(processidbpms)){ 
			return;
		}
		
		/**
		 * @param inputParamName - parâmetro de entrada que deve ser inserido para todas as atividades de usuário
		 * do processo ne negócio. 
		 * Para iniciar o processo correto, esse parâmetro tem que ser correspondente ao  id da entidade de domínio 
		 * que inicia o processo na aplicação. Exemplo: parâmetro de entrada easybpms_com_solicitarviagem_domain_Viagem_id 
		 */
		String inputParamName = "easybpms." + className + ".id";
		inputParamName = inputParamName.replace(".","_"); 
		
		/**
		 * @param inputParamValue - valor do parâmetro de entrada buscado na aplicação. Exemplo: valor do id da entidade Viagem
		 * Obs: Utiliza API de reflexão para invocar o método da instância da entidade de domínio da aplicação e seu respectivo valor
		 */
		String inputParamValue = null;
		try {
			Method m = arg.getClass().getMethod("getId");
			Integer i = (Integer)m.invoke(arg);
			inputParamValue = i.toString(); 
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		/**
		 * @param - listIp - lista de todas as instâncias parâmetro do easybpms
		 */
		List<ParameterInstance> listIp = null;
		try {
			listIp = CRUDParameterInstance.readAll();
		} catch (CRUDException e) {
			e.printStackTrace();
		}
		
		/**
		 * Para cada instância parâmetro da lista verifica se existe uma instância da entidade de domínio
		 * que inicia o processo na aplicação. Ou seja, onde o inputParamName é igual à algum parâmetro de entrada
		 * do easybpms e o inputParamValue é igual a alguma instância parâmetro do easybpms.
		 * Se a condição for satisfeita significa que o processo já foi iniciado
		 */
		for (ParameterInstance ip : listIp){
			if(ip.getParameter().getName().equals(inputParamName) && ip.getValue().equals(inputParamValue)){ 
				return; //Processo ja iniciado
			}
		}
		
		/**
		 * Se a condição não for satisfeita significa que o processo não foi iniciado
		 * Para descobrir a definição de processo e criar uma instância, busca no easybpms o processo
		 * que tem id igual à processIdBpms. Exemplo: com_solicitarviagem_domain_Viagem
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
		 * Para iniciar o processo, é necessário enviar o valor das suas propriedades
		 * @param params - mapa que armazena o nome das propriedade e seus respectivos valores
		 * O valor de cada propriedade é o valor de cada atributo da entidade de domínio da aplicação, que é invocado por meio da API de reflexão
		 */
		Map<String,Object> params = new HashMap<String, Object>();
		for (Property pr : listP){
			String[] partsProperty = pr.getName().split("\\_");
			//String classProperty = partsProperty[partsProperty.length - 2];
			//if (className.contains(classProperty)){
				String methodName = partsProperty[partsProperty.length - 1]; 
				methodName = "get" + methodName.substring(0,1).toUpperCase() + methodName.substring(1);
				
				try {
					Method m = arg.getClass().getMethod(methodName); 
					Object i = m.invoke(arg); 
					String propertyValue = i.toString(); 
					params.put(pr.getName(), propertyValue);
				} catch (Exception e) {
					e.printStackTrace();
				}	
			//}
		}
		
		if (params.isEmpty()){
			params = null;
		}
		String processInstanceId = "";
		/**
		 * @param processInstanceId - recebe o id da instância processo criada no bpms
		 */
		processInstanceId = "" + AbstractBpmsInterface.getBpmsInterface().startProcess(this.processIdBpms, params);
		
	}

}
