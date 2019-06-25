package com.company;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import com.company.Inode;
public class tools {
    public Inode userLogin(){
        System.out.println("登陆：请输入[用户名] [密码]：");
        Scanner sc = new Scanner(System.in);
        String un = sc.next();
        String ps = sc.next();
        User a = new User();
        a.init();
        ArrayList<String> users = a.users;
        if(a.isLogin(un,ps)){
            System.out.println("登陆成功！");
            return new Inode().getUserRootInode(un);
        }else{
            while(true) {
                System.out.println("登陆失败！\n重新登陆………………[0]\n注册…………[9]");
                int dnum = sc.nextInt();
                if (dnum == 0) {
                    break;
                } else if (dnum == 9){
                    System.out.println("注册：请输入[用户名] [密码]：");
                    String Run = sc.next();
                    String Rps = sc.next();
                    //注册
                    a.Register(Run,Rps);
                    System.out.println("注册成功！");
                    break;
                }
            }
        }
        return null;
    }
    //返回info的信息
    public String getInfo(){
        //读取info文件，获得相关属性信息
        ArrayList<String> infoList = new ArrayList<>(10);
        String infoPath = "E:\\fileInfo\\info.txt";
        try(FileReader reader = new FileReader(infoPath);
            BufferedReader br = new BufferedReader(reader)){
            String line;
            int lineNo = 1;
            while((line = br.readLine())!=null){
                if(lineNo >=2 && lineNo <= 5){
                    String []splited = line.split(" ");
                    for(String i : splited){
                        infoList.add(i);
                    }
                }
                lineNo++;
            }

        }catch (IOException e){
            e.printStackTrace();
        }
        int usedInodeCount = 0,freeInodeCount = 0;
        double dirSpace = (Integer.parseInt(infoList.get(0))/1024000);
        double freeDirSpace = (Integer.parseInt(infoList.get(3)));
        double usedDirSpace = (Integer.parseInt(infoList.get(4)));
        usedInodeCount = infoList.get(1).split(",").length;
        freeInodeCount = infoList.get(2).split(" ").length;

        //打印出来
        String ans = dirSpace + "M\t\t" + usedInodeCount + "\t\t\t\t" + freeInodeCount+ "\t\t\t\t" + String.format("%.2f",(freeDirSpace/usedDirSpace)*100) + "%";
        return ans;
    }
    //返回帮助
    public void getHelp(String helpFlag){
        if(helpFlag.equals("noLogin")){
            System.out.println("Login     登陆");
            System.out.println("Info     查询磁盘信息");
        }else if (helpFlag.equals("Login")){
            System.out.println("Dir\t\t列目录");
            System.out.println("Create\t\t创建文件");
            System.out.println("Delete\t\t删除文件");
            System.out.println("Open\t\t打开文件");
            System.out.println("Close\t\t关闭文件");
            System.out.println("Read\t\t读文件");
            System.out.println("Write\t\t写文件");
            System.out.println("Cd\t\t进出目录");
        }else{

        }
    }
    //返回当前目录下所有文件的文件名，物理地址，保护码和文件长度。
    public void getDir(Inode inode){
        int inodeID = inode.inodeID;
        ArrayList<Inode> list = dirInodeList(inodeID);
//      ArrayList<String> fileName = dirFileName(list);
        System.out.println("文件名\t\t\t物理地址\t\t保护码\t\t文件长度");
//        for (String i:fileName){
//            System.out.println(i);
//        }
        for(Inode i:list){
            String fileName =getFileNameByInode(i);
            if(fileName.length()<4){
                fileName += "\t";
            }
            if(fileName.length()<8){
                fileName += "\t";
            }
            System.out.println(fileName+"\t\t"+i.blockPointer+"\t\t\t"+i.power+"\t\t\t"+i.fileStore+"B");
        }
    }
    public String getCommandFront(Inode inode){
        String lastDir = new tools().lastDirectory(inode.nowDirectory);
        return ("["+inode.userName+"@OS "+lastDir+"]#:");
    }
    //根据inodeID返回文件名
    public String getFileNameByInode(Inode inode){
        int inodeID = inode.inodeID;
        String pathName = "fileinfo/inodeFile.txt";
        try(FileReader reader = new FileReader(pathName);
            BufferedReader br = new BufferedReader(reader)){
            String line;
            while((line = br.readLine())!=null){
                String []splited = line.split(" ");
                if(line == ""){
                    continue;
                }
                if(splited[0].equals(String.valueOf(inodeID))){
                    return splited[1];
                }
            }

        }catch (IOException e){
            e.printStackTrace();
        }
        return "";
    }
    //通过文件名找到inodeID
    public int getInodeIDByFileName(String fileName){
        String pathName = "fileinfo/inodeFile.txt";
        try(FileReader reader = new FileReader(pathName);
            BufferedReader br = new BufferedReader(reader)){
            String line;
            while((line = br.readLine())!=null){
                String []splited = line.split(" ");
                if(splited[0].equals("")){
                    continue;
                }
                if(splited[1].equals(fileName)){
                    return Integer.parseInt(splited[0]);
                }
            }

        }catch (IOException e){
            e.printStackTrace();
        }
        return -1;
    }
    //通过inodeID找到inode
    public Inode getInodeByInodeID(int inodeID){
        String pathName = "fileinfo/inode.txt";
        Inode inode = new Inode();
        try(FileReader reader = new FileReader(pathName);
            BufferedReader br = new BufferedReader(reader)){
            String line;
            while((line = br.readLine())!=null){
                String []splited = line.split(" ");
                if(Integer.parseInt(splited[8])==inodeID){
                    inode.setNowDirectory(splited[0]);
                    inode.setFileStore(Integer.parseInt(splited[1]));
                    inode.setUserName(splited[2]);
                    inode.setPower(Integer.parseInt(splited[3]));
                    inode.setIsOpen(Integer.parseInt(splited[4]));
                    inode.setFileType(Integer.parseInt(splited[5]));
                    inode.setFileID(Integer.parseInt(splited[6]));
                    inode.setFatherInodeID(Integer.parseInt(splited[7]));
                    inode.setInodeID(Integer.parseInt(splited[8]));
                    inode.setBlockPointer(Integer.parseInt(splited[9]));
                }
            }
            return inode;

        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }
    //通过父节点id找到所有子节点inode；
    public ArrayList<Inode> dirInodeList(int fatherInodeID){
        ArrayList<Inode> inodeList = new ArrayList<>(1000);
//        Inode inode = new Inode();
        String pathName = "fileinfo/inode.txt";
        try(FileReader reader = new FileReader(pathName);
            BufferedReader br = new BufferedReader(reader)){
            String line;
            while((line = br.readLine())!=null){
                String []splited = line.split(" ");
                if(Integer.parseInt(splited[7])==fatherInodeID){
                    Inode inode = new Inode();
                    inode.setNowDirectory(splited[0]);
                    inode.setFileStore(Integer.parseInt(splited[1]));
                    inode.setUserName(splited[2]);
                    inode.setPower(Integer.parseInt(splited[3]));
                    inode.setIsOpen(Integer.parseInt(splited[4]));
                    inode.setFileType(Integer.parseInt(splited[5]));
                    inode.setFileID(Integer.parseInt(splited[6]));
                    inode.setFatherInodeID(Integer.parseInt(splited[7]));
                    inode.setInodeID(Integer.parseInt(splited[8]));
                    inode.setBlockPointer(Integer.parseInt(splited[9]));
                    inodeList.add(inode);
                }
            }
            return inodeList;

        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }
    //根据inode返回文件名的array
    public ArrayList<String>dirFileName(ArrayList<Inode> inodeList){
        ArrayList<String> fileNameList = new ArrayList<>(1000);
        String pathName = "E:\\fileInfo\\inodeFile.txt";
       for (Inode inode:inodeList){
           int inodeID = inode.inodeID;
           try(FileReader reader = new FileReader(pathName);
               BufferedReader br = new BufferedReader(reader)){
               String line;
               while((line = br.readLine())!=null){
                   String []splited = line.split(" ");
                   if(splited[0].equals(String.valueOf(inodeID))){
                       fileNameList.add(splited[1]);
                       break;
                   }
               }
           }catch (IOException e){
               e.printStackTrace();
           }
       }
        return fileNameList;
    }
    //返回目录的最后一个目录
    public String lastDirectory(String dir){
        String []splited = dir.trim().split("/");
        //System.out.println(splited);
        return splited[splited.length-1];
    }
    //在block文件里寻找空闲block
    public int findFreePointer() {
        String pathname = "fileinfo/block.txt";
        int pointer = 0;
        try (FileReader reader = new FileReader(pathname);
             BufferedReader br = new BufferedReader(reader)) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] splited = line.split(" ");
                if (splited[1].equals("0")) {
                    pointer = Integer.parseInt(splited[0]);
                    return pointer;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }
    //在inode文件里寻找空闲inode
    public int findFreeInodeID(){
        int freeID = 0;
        String pathname = "fileinfo/inode.txt";
        int pointer = 0;
        try (FileReader reader = new FileReader(pathname);
             BufferedReader br = new BufferedReader(reader)) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] splited = line.split(" ");
                if (splited[0].equals("0")) {
                    freeID = Integer.parseInt(splited[8]);
                    return freeID;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;

    }
    //判断indeo是否在当前节点里
    public boolean isSubDirectory(Inode inode,String frontDirectory){
        String wholeDirectory = inode.nowDirectory;
        String []splited = wholeDirectory.trim().split("/");
        if(splited[splited.length-2].equals(frontDirectory)){
            return true;
        }else{
            return false;
        }
    }
    //打开文件,获得文件的文本内容
    public String openFile(Inode inode){
        String text = new tools().getBlockByInode(inode);
        return text;
    }
    //通过inodeID获得文件pointer地址
    public String getBlockByInode(Inode inode){
        String pathname = "fileinfo/block.txt";
        int pointer = 0;
        try (FileReader reader = new FileReader(pathname);
             BufferedReader br = new BufferedReader(reader)) {
            String line;
            String block = "";
            while ((line = br.readLine()) != null) {
                String[] splited = line.split(" ");
                if (splited[0].equals(String.valueOf(inode.blockPointer))) {
                    for(int i = 2;i<splited.length;i++){
                        block = block + splited[i] + " ";
                    }
                    return block;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "no text";
    }
    public void deleteInode(Inode inode){
        //将节点复位
        String pathname = "fileinfo/inode.txt";
        int pointer = 0;
        int lineNo = 1;
        try (FileReader reader = new FileReader(pathname);
             BufferedReader br = new BufferedReader(reader)) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] splited = line.split(" ");
                if (splited[splited.length-2].equals(String.valueOf(inode.inodeID))) {
                    break;
                }
                lineNo++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try{
            String ss = "0 0 0 0 0 0 0 0 " + inode.inodeID + " 0";
            File f = new File(pathname);
            new FileTools().insertStringInFile(f,lineNo,ss);
        }catch(Exception e){
            e.printStackTrace();
        }

    }
    public int getBlockLineNo(Inode inode){
        String pathname = "fileinfo/block.txt";
        int lineNo = 1;
        try (FileReader reader = new FileReader(pathname);
             BufferedReader br = new BufferedReader(reader)) {
            String line;
            String block = "";
            while ((line = br.readLine()) != null) {
                String[] splited = line.split(" ");
                if (splited[0].equals(String.valueOf(inode.blockPointer))) {
                    return lineNo;
                }
                lineNo++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }
    //获得那一行的Block
//    public String getBlockLine(Inode inode){
//        String pathname = "fileinfo/block.txt";
//        int pointer = 0;
//        try (FileReader reader = new FileReader(pathname);
//             BufferedReader br = new BufferedReader(reader)) {
//            String line;
//            String block = "";
//            while ((line = br.readLine()) != null) {
//                String[] splited = line.split(" ");
//                if (splited[0].equals(String.valueOf(inode.blockPointer))) {
//                    for(int i = 0;i<splited.length;i++){
//                        block = block + splited[i] + " ";
//                    }
//                    return block;
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return "0 0 0";
//    }
    public void writeBlockLine(int lineNO,Inode inode,String newLine){
        String pathname = "fileinfo/block.txt";
        int pointer = 0;
        String block = "";
        try (FileReader reader = new FileReader(pathname);
             BufferedReader br = new BufferedReader(reader)) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] splited = line.split(" ");
                if (splited[0].equals(String.valueOf(inode.blockPointer))) {
                    for(int i = 0;i<2;i++){
                        block = block + splited[i] + " ";
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try{
        File f = new File(pathname);
        new FileTools().insertStringInFile(f,lineNO,block+newLine);
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    //判断inode的父节点是否是根节点
    public boolean isHavingFatherInode(Inode inode){
        if(inode.fatherInodeID == 0){
            return false;
        }else{
            return true;
        }
    }
    public Inode getInodeByDirectory(String Directory){
        String pathName = "fileinfo/inode.txt";
        Inode inode = new Inode();
        try(FileReader reader = new FileReader(pathName);
            BufferedReader br = new BufferedReader(reader)){
            String line;
            while((line = br.readLine())!=null){
                String []splited = line.split(" ");
                if(splited[0].equals(Directory)){
                    inode.setNowDirectory(splited[0]);
                    inode.setFileStore(Integer.parseInt(splited[1]));
                    inode.setUserName(splited[2]);
                    inode.setPower(Integer.parseInt(splited[3]));
                    inode.setIsOpen(Integer.parseInt(splited[4]));
                    inode.setFileType(Integer.parseInt(splited[5]));
                    inode.setFileID(Integer.parseInt(splited[6]));
                    inode.setFatherInodeID(Integer.parseInt(splited[7]));
                    inode.setInodeID(Integer.parseInt(splited[8]));
                    inode.setBlockPointer(Integer.parseInt(splited[9]));
                }
            }
            return inode;

        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }
    //统计文件大小
    public int countFileStore(Inode inode){
        //首先判断inode是目录还是文件
            int space = new tools().getBlockByInode(inode).getBytes().length;
            space = space /1024 +1;
            return space;
    }
    public void updateInodeStore(Inode inode){
        String pathname = "fileinfo/inode.txt";
        int lineNo = 1;
        try(FileReader reader = new FileReader(pathname);
            BufferedReader br = new BufferedReader(reader)){
            String line;
            lineNo = 1;
            while((line = br.readLine())!=null){
                String []splited = line.split(" ");
                if(splited[8].equals(String.valueOf(inode.inodeID))){
                    break;
                }
                lineNo++;
            }

        }catch (IOException e){
            e.printStackTrace();
        }
        inode.fileStore = new tools().countFileStore(inode);
        inode.commonFlush(lineNo);
    }
    //文件的话直接读取block判断字符
}

