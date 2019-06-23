package com.company;

import javafx.collections.ArrayChangeListener;

import java.io.*;
import java.util.ArrayList;

public class InodeFile {
    public InodeFile(int inodeID, String fileName) {
        this.inodeID = inodeID;
        this.fileName = fileName;
    }

    public int inodeID;
    public String fileName;
    String pathName = "fileinfo/inodeFile.txt";
//    ArrayList<String> list = new ArrayList<String>(100);
//    public InodeFile(){
//        init();
//    }
//    public void init(){
//
//    }
    public void flush(){
        String thisline = "";
        thisline = "\n"+ inodeID  + " " + fileName;
        try(FileWriter writer = new FileWriter(pathName,true);
            BufferedWriter wr = new BufferedWriter(writer)){
                wr.write(thisline);

        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public int get_Inode(String fName){
        try(FileReader reader = new FileReader(pathName);
            BufferedReader br = new BufferedReader(reader)){
            String line;
            int lineIndex = 0;
            while((line = br.readLine())!=null){
                String []splited = line.split(" ");
                if(splited[1].equals(fName)){
                    return(Integer.parseInt(splited[0]));
                }
            }

        }catch (IOException e){
            e.printStackTrace();
        }
        return -1;
    }


}
