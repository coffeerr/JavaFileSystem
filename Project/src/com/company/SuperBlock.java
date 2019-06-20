package com.company;

import java.io.*;
import java.util.ArrayList;

public class SuperBlock {
    public int storage;
    public ArrayList<Integer> freeInode = new ArrayList<Integer>(100);
    public ArrayList<Integer> busyInode = new ArrayList<Integer>(100);
    public int usedSpace,freeSpace;
    String pathname = "E:\\fileInfo\\info.txt";
    //[storage,freeInode,busyInode,usedSpace,freeSpace]
    ArrayList<String> superBlock = new ArrayList<>(100);
    //读取上一次的数据，这里默认每次关闭文件都会刷新记录在文本里的数值
    public void init(){
        //读取文件，获得超级块的属性
        try(FileReader reader = new FileReader(pathname);
            BufferedReader br = new BufferedReader(reader)){
            String line;
            int lineIndex = 0;
            while((line = br.readLine())!=null){
                lineIndex++;
                if(lineIndex >= 2 && lineIndex <= 5){
                    String []splited = line.split("\\s+");
                    for(String i : splited){
                        superBlock.add(i);
                    }
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        storage = Integer.parseInt(superBlock.get(0));
        String []stringFreeInode = superBlock.get(1).split(",");
        for(int i = 0;i < stringFreeInode.length;i++){
           freeInode.add(Integer.parseInt(stringFreeInode[i]));
        }
        String []stringBusyInode = superBlock.get(2).split(",");
        for(int i = 0;i < stringBusyInode.length;i++){
            busyInode.add(Integer.parseInt(stringBusyInode[i]));
        }
        usedSpace = Integer.parseInt(superBlock.get(3));
        freeSpace = Integer.parseInt(superBlock.get(4));
    }
    //更新文本文件里的数据
    public void flush(){
        String pathname = "E:\\fileInfo\\info1.txt";
        File f = new File(pathname);
        FileTools tool = new FileTools();
        try{
            tool.insertStringInFile(f,2,String.valueOf(storage));
            String line3 = "";
            for (int i:freeInode){
                line3 = line3 + i + ",";
            }
            line3 = line3.substring(0,line3.length()-1);
            tool.insertStringInFile(f,3,line3);
            String line4 = "";
            for (int i:freeInode){
                line4 = line4 + i + ",";
            }
            line4 = line4.substring(0,line4.length()-1);
            tool.insertStringInFile(f,4,line4);
            String line5 = String.valueOf(usedSpace) + String.valueOf(freeSpace);
            tool.insertStringInFile(f,5,line5);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public SuperBlock(){
        init();
    }
    //更新文本文件的值
}
