package com.easybpms.codegen.javaapp;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.easybpms.codegen.GenerateContext;
import com.easybpms.codegen.MyFiles;


public class GerarContextMain {

	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException{
		
		//MyFiles my = new MyFiles();
		//ArrayList<String> files = my.listFiles("./src/main/resources/xml");
		/*ArrayList<String> files = my.listFiles("C:\\Users\\saudeuser\\SkyDrive\\Documentos\\workspace\\solicitarviagem\\src\\main\\resources");
		GenerateContext gc = new GenerateContext("com.bpms.jbpm");
		String fileOut = gc.gerarContext(files);*/
		//my.writeFile("./src/main/java/com/easybpms/codegen/Context.java", fileOut);
		//my.writeFile("C:\\Users\\saudeuser\\SkyDrive\\Documentos\\workspace\\solicitarviagem\\src\\easybpms\\java\\com\\easybpms\\codegen\\Context.java", fileOut);
		
		
		MyFiles my = new MyFiles();
		String pathIn = (GerarContextMain.getFile()).replace("\\","\\\\");
		
		ArrayList<String> files = my.listFiles(pathIn);
		GenerateContext gc = new GenerateContext("com.bpms.jbpm");
		String fileOut = gc.gerarContext(files);
		
		String pathOut = (GerarContextMain.getFile() + "\\src\\easybpms\\java\\com\\easybpms\\codegen\\Context.java").replace("\\","\\\\");
		my.writeFile(pathOut, fileOut);
		
	}
	
	public static String getFile() {
        JFileChooser fileChooser = new JFileChooser("\\C:/");
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int result = fileChooser.showOpenDialog(fileChooser);
        if (!(result == JFileChooser.CANCEL_OPTION)) {           
            File fileName = fileChooser.getSelectedFile();
            if ((fileName == null) || (fileName.getName().equals(""))) {
                JOptionPane.showMessageDialog(fileChooser, "Inavlid File Name",
                        "Invalid File Name", JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }
            return fileName.getAbsolutePath();
        }else{
            return null;
        } 
    }
	
}
