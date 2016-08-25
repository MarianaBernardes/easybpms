package com.easybpms.codegen;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class MyFiles {
	
	public ArrayList<String> listFiles(String path) throws FileNotFoundException {
        ArrayList<String> listFiles = new ArrayList<String>();
        File dir = new File(path);
        File[] files = dir.listFiles();
        String exit = "";
        if (files != null) {
            int length = files.length;

            for (int i = 0; i < length; ++i) {
                File f = files[i];
                exit += f.getName() + ", ";
                listFiles.add(f.getAbsolutePath());
            }
        }
        return listFiles;

    }

    public StringBuilder getContentFile(String path) throws FileNotFoundException {
        StringBuilder out = new StringBuilder();

        try {
            FileReader arq = new FileReader(path);
            BufferedReader lerArq = new BufferedReader(arq);
            String linha = lerArq.readLine();
            while (linha != null) {
                out.append(linha).append("\n");
                linha = lerArq.readLine();
            }

            arq.close();
        } catch (IOException e) {
            System.err.printf("Erro na abertura do arquivo: %s.\n", e.getMessage());
        }
        return out;
    }

    //path - nome do caminho no qual o arquivo sera gerado, out - conteudo do arquivo
    public void writeFile(String path, String out) throws IOException {

        File arquivo = new File(path);
        //escreve no arquivo
        FileWriter fw = new FileWriter(arquivo, false);
        BufferedWriter bw = new BufferedWriter(fw);

        bw.write(out);
        bw.newLine();

        bw.close();
        fw.close();
    }
    
    public String pathToTitle(String file) {
        String[] aux = file.split("/");
        return aux[aux.length - 1];
    }
}
