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
            System.out.print(tool.getCommandFront(userInode));
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
                    Inode newInode = new Inode(nowDirectory,0,userInode.userName,power,0,1,0,userInode.inodeID, userInode.inodeID+10,userInode.blockPointer+10);
                    newInode.flush("0");
                    InodeFile inodeFile = new InodeFile(newInode.inodeID,fileName);
                    inodeFile.flush();
                    //初始化inode
                }

            }

        }
    }
}
