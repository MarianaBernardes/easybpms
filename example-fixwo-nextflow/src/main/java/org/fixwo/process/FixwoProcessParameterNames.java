package org.fixwo.process;

import java.lang.reflect.Method;

import org.nextflow.owm.mapping.ParameterNamesProvider;

public class FixwoProcessParameterNames implements ParameterNamesProvider {

	public String[] getParameterNamesFor(Method method) {
		if(method.getName().equals("criarOcorrencia")){
			return new String[]{"id"};
		}
		if (method.getName().equals("classificarEEncaminharAoSetorResponsavel")){
			return new String[]{"status","setor"};
		}
		if (method.getName().equals("enviarFeedbackAoSolicitante")){
			return new String[]{"status","feedback"};
		}
		if (method.getName().equals("avaliarSolucao")){
			return new String[]{"avaliacao"};
		}
		return null;
	}
	

}
