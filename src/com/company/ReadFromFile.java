package com.company;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by Mati on 2017-06-10.
 */
public class ReadFromFile {
    BufferedReader br;
    Graph gr;
    ReadFromFile(){
        gr=new Graph();
    }

    void readFromFile(String path){
        gr.clear();
        String line;
        try{
            br = new BufferedReader(new FileReader(path));
            createVertices(br.readLine());
            while ((line=br.readLine())!=null)
                createEdges(line);

        } catch (FileNotFoundException fnf) {
            fnf.printStackTrace();
            System.out.println("Nie znaleziono pliku");
        }

        catch (IOException ioExc){
            ioExc.printStackTrace();
            System.out.println("Błąd odczytu pliku");
        }
        finally {
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        gr.minSpanningTree(1);
    }

    void createVertices(String line){
        String [] splittedLine=line.split(" ");
        int nrOfVertices=Integer.parseInt(splittedLine[0]);
        for (int i=0;i<nrOfVertices+1;i++)
            gr.add(Integer.toString(i));
    }

    void createEdges(String line){
        String [] spliitedLine=line.split(" ");
        gr.addEdge(spliitedLine[0],spliitedLine[1],Integer.parseInt(spliitedLine[2]));
        gr.addEdge(spliitedLine[1],spliitedLine[0],Integer.parseInt(spliitedLine[2]));
    }
}
