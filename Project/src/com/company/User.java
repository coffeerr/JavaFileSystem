package com.company;

import java.io.*;
import java.util.ArrayList;
import com.company.FileTools;

public class User {
    public static String userName;
    public static String passWord;
    ArrayList<String> users = new ArrayList<>(100);
    String pathname = "fileinfo/info.txt";
    public void init(){
        try(FileReader reader = new FileReader(pathname);
            BufferedReader br = new BufferedReader(reader)){
            String line;
            while((line = br.readLine())!=null){
                String []splited = line.split("\\s+");
                for(String i : splited){
                    users.add(i);
                }
                break;
            }

    }catch (IOException e){
            e.printStackTrace();
        }
    }
    boolean isLogin(String us,String ps){
        if(users.contains(us)&&((users.indexOf(us)+2)%2==0)){
            if(ps.equals(users.get(users.indexOf(us)+1))){
                return true;
            }else{
                return false;
            }
        }else{
            return false;
        }
    }
    void Register(String us,String ps){
        FileTools tools = new FileTools();
        try{
            File f = new File(pathname);
            tools.addStringInFile(f,1,us + " " + ps);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
