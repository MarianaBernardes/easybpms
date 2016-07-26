package com.easybpms.codegen;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.xml.sax.SAXException;


public class GenerateContext {
	
	private String bpmsPackage;
	
	public GenerateContext(String bpmsPackage) {
		this.bpmsPackage = bpmsPackage;
	}
	
	public String gerarContext(ArrayList<String> files) throws ParserConfigurationException, SAXException, IOException{
		
		ParserDOM parserDom = null;
        List<ProcessXml> listProcesses = new ArrayList<ProcessXml>();
 
        for (String file : files) {	            
            File f = new File(file);                       

            parserDom = new ParserDOM();
            parserDom.xmlToDOM(file);      
            parserDom.getProcess().setFilePath(f.getAbsolutePath().replace("\\","\\\\"));
            listProcesses.add(parserDom.getProcess());
        }

        return generate(listProcesses);
		
	}
	
	public String generate(List<ProcessXml> listProcesses) throws IOException {
		// inicializando o velocity
		VelocityEngine ve = new VelocityEngine();
		//Descomentar caso a pasta de templates nao esteja dentro da pasta resources
		ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
		ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
		ve.init();

		// criando o contexto que liga o java ao template
		VelocityContext context = new VelocityContext();
		//context - armazena as variaveis que vao ser utilizadas no template
		StringWriter writer = new StringWriter();
		// Recuperando os Eventos de Inicio

		// escolhendo o template
		//Template t = ve.getTemplate("./src/main/resources/templates/TemplateContext");
		Template t = ve.getTemplate("resources/templates/TemplateContext");
		//t = define a estrutura do codigo gerado

		context.put("listProcesses", listProcesses);
		context.put("bpmsPackage", bpmsPackage);
		
		// mistura o contexto com o template
		t.merge(context, writer);

		return writer.toString();
	}


}
