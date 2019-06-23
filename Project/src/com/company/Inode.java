package com.company;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import com.company.FileTools;

public class Inode {

    public String nowDirectory;//当前目录
    public int fileStore;//文件大小
    public String userName;//用户名
    public int power;//权限 1代表只读，2代表读写
    public int isOpen;//是否打开 1代表打开，2表示关闭
    public int fileType;//文件类型 1表示目录，2表示普通文件
    public int fileID;//文件块的编号

    public Inode(String nowDirectory, int fileStore, String userName, int power, int isOpen, int fileType, int fileID, int fatherInodeID, int inodeID, int blockPointer) {
        this.nowDirectory = nowDirectory;
        this.fileStore = fileStore;
        this.userName = userName;
        this.power = power;
        this.isOpen = isOpen;
        this.fileType = fileType;
        this.fileID = fileID;
        this.fatherInodeID = fatherInodeID;
        this.inodeID = inodeID;
        this.blockPointer = blockPointer;
    }

    public int fatherInodeID;//父节点ID，0表示根目录
    public int inodeID;//当前节点ID
    public int blockPointer;//文件数据的block地址
    public String pathname = "fileinfo/inode.txt";
    ArrayList<String> list = new ArrayList<String>(10);

    FileTools tools = new FileTools();
    //类初始化，把文件里的数据载入类变量
    public void setNowDirectory(String nowDirectory) {
        this.nowDirectory = nowDirectory;
    }

    public void setFileStore(int fileStore) {
        this.fileStore = fileStore;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public void setIsOpen(int isOpen) {
        this.isOpen = isOpen;
    }

    public void setFileType(int fileType) {
        this.fileType = fileType;
    }

    public void setFileID(int fileID) {
        this.fileID = fileID;
    }

    public void setFatherInodeID(int fatherInodeID) {
        this.fatherInodeID = fatherInodeID;
    }

    public void setInodeID(int inodeID) {
        this.inodeID = inodeID;
    }

    public void setBlockPointer(int blockPointer) {
        this.blockPointer = blockPointer;
    }

    public void setPathname(String pathname) {
        this.pathname = pathname;
    }

    public void setList(ArrayList<String> list) {
        this.list = list;
    }

    public void setTools(FileTools tools) {
        this.tools = tools;
    }

    public void init(String userNameReal){
        try(FileReader reader = new FileReader(pathname);
            BufferedReader br = new BufferedReader(reader)){
            String line;
            int lineIndex = 0;
            while((line = br.readLine())!=null){
                String []splited = line.split(" ");
                if(splited[2].equals(userNameReal)){
                    for(String i : splited){
                        list.add(i);
                    }
                }
            }
            nowDirectory  = list.get(0);
            fileStore = Integer.parseInt(list.get(1));
            userName = userNameReal;
            power = Integer.parseInt(list.get(3));
            isOpen = Integer.parseInt(list.get(4));
            fileType = Integer.parseInt(list.get(5));
            fileID = Integer.parseInt(list.get(6));
            fatherInodeID = Integer.parseInt(list.get(7));
            inodeID = Integer.parseInt(list.get(8));
            blockPointer = Integer.parseInt(list.get(9));


        }catch (IOException e){
            e.printStackTrace();
        }

    }
    //用来创建文件使用
    public void flush(String createFlag){
        pathname = "fileinfo/inode.txt";
        int lineNo = tools.getInodeLine(createFlag);
        String thisline = "";
        thisline = nowDirectory +" " + String.valueOf(fileStore) + " " + userName + " " +
                String.valueOf(power) + " " + String.valueOf(isOpen) + " " +
                String.valueOf(fileType) + " " + String.valueOf(fileID) + " " +
                String.valueOf(fatherInodeID) + " " + String.valueOf(inodeID) + " " +
                String.valueOf(blockPointer);
        File f = new File(pathname);
        try{
            tools.insertStringInFile(f,lineNo,thisline);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public Inode(String userNameReal){
        init(userNameReal);
    }
    public Inode(){

    }
    public Inode getUserRootInode(String userName){
        try(FileReader reader = new FileReader(pathname);
            BufferedReader br = new BufferedReader(reader)){
            String line;
            Inode newInode = new Inode();
            while((line = br.readLine())!=null){
                String []splited = line.split(" ");
                if(splited[2].equals(userName)){
                    for(String i : splited){
                        list.add(i);
                    }
                }
            }
            newInode.setNowDirectory(list.get(0));
            newInode.setFileStore(Integer.parseInt(list.get(1)));
            newInode.setUserName(userName);
            newInode.setPower(Integer.parseInt(list.get(3)));
            newInode.setIsOpen(Integer.parseInt(list.get(4)));
            newInode.setFileType(Integer.parseInt(list.get(5)));
            newInode.setFileID(Integer.parseInt(list.get(6)));
            newInode.setFatherInodeID(Integer.parseInt(list.get(7)));
            newInode.setInodeID(Integer.parseInt(list.get(8)));
            newInode.setBlockPointer(Integer.parseInt(list.get(9)));
            return newInode;

        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }
    //Create创建新节点
    public Inode(String fileName,int fatherInodeID,String userName){
        //寻找空闲节点
        try(FileReader reader = new FileReader(pathname);
            BufferedReader br = new BufferedReader(reader)) {
            String line;
            boolean isFreeInode = false;
            while ((line = br.readLine()) != null) {
                String[] splited = line.split(" ");
                if (splited[0].equals("0")) {
                    isFreeInode = true;
                    System.out.println("已找到空闲节点");
                    //改变inode文件的数据
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

}
