package com.company;

import java.io.*;
import java.util.ArrayList;

public class test {
    public static String userName;
    public static String passWord;
    ArrayList<String> users = new ArrayList<>(100);
    String pathname = "E:\\fileInfo\\info.txt";
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
//    void Register(String us,String ps){
//        try(FileWriter fw = new FileWriter(pathname,true);
//            BufferedWriter bw = new BufferedWriter(fw)){
//            bw.write(us + " " + ps + " ");
//        }catch (IOException e){
//            e.printStackTrace();
//        }
//    }

}
