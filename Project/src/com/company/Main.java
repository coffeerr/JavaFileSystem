package com.company;

import java.util.ArrayList;
import java.util.Scanner;
import java.awt.Robot;
import com.company.tools;
import com.company.Inode;

public class Main {
    public static void main(String[] args) {
        Inode userInode = new Inode();
        tools tool = new tools();
        System.out.println("-------------欢迎使用模拟Linux文件系统！---------------");
        tool.getHelp("noLogin");
        while (true) {
            System.out.println("请输入指令：");
            Scanner sc = new Scanner(System.in);
            String ss;
            ss = sc.nextLine();
            String[] cmd = ss.trim().split(" ");
            //登陆模块
            if (cmd[0].equals("Login")) {
                userInode = tool.userLogin();
                if (userInode.blockPointer != 0) {
                    //登陆成功
                    System.out.println("-------------欢迎使用模拟Linux文件系统！---------------");
                    tool.getHelp("Login");
                    break;
                } else {
                    System.out.println("登陆失败");
                }
            } else if (cmd[0].equals("Info")) {
                System.out.println("磁盘容量\t\t已使用节点数\t\t空闲节点数\t\t磁盘已使用\t\t");
                System.out.println(tool.getInfo());
            } else if (cmd[0].equals("Help")) {
                tool.getHelp("noLogin");
            } else {
                System.out.println("指令格式输入错误！");
            }
        }
        while(true){
            Scanner sc2 = new Scanner(System.in);
            String ss2;
            String commandFront = "[root@OS " + tool.lastDirectory(userInode.nowDirectory) + "]#";
            System.out.print(commandFront);
            ss2 = sc2.nextLine();
            String[] cmd2 = ss2.trim().split(" ");
            switch (cmd2[0]){
                //返回当前目录下所有的文件
                case "Dir":{
                    tool.getDir(userInode);
                    break;
                }
                case  "Help":{
                    tool.getHelp("Login");
                    break;
                }
                case "Create":{
                    //创建文件夹
                    String fileName = cmd2[1];
                    int power = 0;
                    if(cmd2[2].equals("r")){
                        power = 1;
                    }else if(cmd2[2].equals("rw")){
                        power = 2;
                    }else{
                        System.out.println("文件指令错误");
                    }
                    String nowDirectory = userInode.nowDirectory+"/"+fileName;
                    //获得当前目录，以当前目录inode为父节点，在inode文件里寻找一个空闲节点，更新磁盘情况info
                    Inode newInode = new Inode(nowDirectory,0,userInode.userName,power,0,1,0,userInode.inodeID, tool.findFreeInodeID(),userInode.blockPointer+10);
                    newInode.flush("0",tool.findFreePointer());
                    InodeFile inodeFile = new InodeFile(newInode.inodeID,fileName);
                    inodeFile.flush();
                    break;
                    //初始化inode
                }
                case "Delete" :{
                    //获得文件名
                    String fileName = cmd2[1];
                    //在inode-file里找到对应的inode
                    int inodeID = tool.getInodeIDByFileName(fileName);
                    Inode deleteInode = tool.getInodeByInodeID(inodeID);
                    //判断是不是该inode在不在目录里
                    if(tool.isSubDirectory(deleteInode,tool.lastDirectory(userInode.nowDirectory))){
                        tool.deleteInode(deleteInode);
                    }else{
                        System.out.println("Delete [fileName]");
                    }
                    break;
                }
                case "Open":{
                    String fileName = cmd2[1];
                    //在inode-file里找到对应的inode
                    int inodeID = tool.getInodeIDByFileName(fileName);
                    Inode openInode = tool.getInodeByInodeID(inodeID);
                    if(tool.isSubDirectory(openInode,tool.lastDirectory(userInode.nowDirectory))&&openInode.fileType==2){
                        System.out.println(tool.getBlockByInode(openInode));
                    }else{
                        System.out.println("指令格式输入错误");
                    }
                    break;
                }
                case "Close":{
                    break;
                }
                case "Read":{
                    String fileName = cmd2[1];
                    //在inode-file里找到对应的inode
                    int inodeID = tool.getInodeIDByFileName(fileName);
                    Inode openInode = tool.getInodeByInodeID(inodeID);
                    if(tool.isSubDirectory(openInode,tool.lastDirectory(userInode.nowDirectory))&&openInode.fileType==2){
                        System.out.println(tool.getBlockByInode(openInode));
                    }else{
                        System.out.println("指令格式输入错误");
                    }
                    break;
                }
                case "Write":{
                    String fileName = cmd2[1];
                    //在inode-file里找到对应的inode
                    int inodeID = tool.getInodeIDByFileName(fileName);
                    Inode openInode = tool.getInodeByInodeID(inodeID);
                    if(tool.isSubDirectory(openInode,tool.lastDirectory(userInode.nowDirectory))&&openInode.fileType==2){
                        System.out.println(tool.getBlockByInode(openInode));
                        if(openInode.power == 2){
                            System.out.println("请输入内容！");
                            Scanner sc = new Scanner(System.in);
                            String text = sc.nextLine();
                            int lineNo = tool.getBlockLineNo(openInode);
                            tool.writeBlockLine(lineNo,openInode,text);
                            openInode.fileStore = tool.countFileStore(openInode);
                            //将文件大小写入inode文件
                            tool.updateInodeStore(openInode);
//                            openInode.f
                        }else{
                            System.out.println("您没有读取该文件的权限！");
                        }
                    }else{
                        System.out.println("指令格式输入错误");
                    }
                    break;
                }
                case "Copy":{
                    //copy [目录1] [目录2]：将目录1的文件，复制在目录2下面
                    //copy /home/new2 /home
                    //前者是定位那个inode，后者是定位父inode
                    //copy首先，copy的是block，其他都不相同，也就是说，申请一个新的inode，block需要复制一下，block的pointer也不相同
                    String dir1 = cmd2[1];
                    String dir2 = cmd2[2];
                    Inode inode1 = tool.getInodeByDirectory(dir1);
                    Inode inode2 = tool.getInodeByDirectory(dir2);
                    if(inode2.fileType==2){
                        System.out.println("请选择文件夹！");
                        break;
                    }
                    //申请一个inode
                    Inode copyInode = new Inode();
                    copyInode.setNowDirectory(inode2.nowDirectory+"/"+tool.getFileNameByInode(inode1)+"_copy");
                    copyInode.setFileStore(inode1.fileStore);
                    copyInode.setUserName(inode1.userName);
                    copyInode.setPower(inode1.power);
                    copyInode.setIsOpen(inode1.isOpen);
                    copyInode.setFileType(inode1.fileType);
                    copyInode.setFileID(inode1.fileID);
                    copyInode.setFatherInodeID(inode2.inodeID);
                    copyInode.setInodeID(tool.findFreeInodeID());
                    copyInode.setBlockPointer(tool.findFreePointer());
                    //写入inode
                    copyInode.flush("0",copyInode.blockPointer);
                    InodeFile inodeFile = new InodeFile(copyInode.inodeID,tool.getFileNameByInode(inode1)+"_copy");
                    inodeFile.flush();
                    //复制block
                    String text = tool.getBlockByInode(inode1);
                    int lineNo = tool.getBlockLineNo(copyInode);
                    tool.writeBlockLine(lineNo,copyInode,text);
                    //把inode1复制到inode2里去
                    break;
                }
                case "cd":{
                    String fileName = cmd2[1];
                    if(fileName.equals("..")){
                        //判断userInode是有非根目录父节点
                        if(tool.isHavingFatherInode(userInode)){
                            userInode = tool.getInodeByInodeID(userInode.fatherInodeID);
                        }else{
                            System.out.println("当前目录已经是根节点！");
                        }
                    }
                    else{
                        int inodeID = tool.getInodeIDByFileName(fileName);
                        if(inodeID==-1){
                            System.out.println("文件名输入错误");
                            break;
                        }
                        userInode = tool.getInodeByInodeID(inodeID);
                        }
                        //有则根据父节点，将userInode换成父节点
                    break;
                }
                default:{
                    System.out.println("指令格式输入错误，输入Help查看帮助！");
                }

            }

        }
    }
}
