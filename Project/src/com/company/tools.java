package com.company;

import java.io.BufferedReader;
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
        int fatherID = inode.fatherInodeID;
        ArrayList<Inode> list = dirInodeList(fatherID);
//      ArrayList<String> fileName = dirFileName(list);
        System.out.println("文件名\t\t\t物理地址\t\t保护码\t\t文件长度");
//        for (String i:fileName){
//            System.out.println(i);
//        }
        for(Inode i:list){
            System.out.println(getFileNameByInode(i)+"\t\t"+i.blockPointer+"\t\t\t"+i.power+"\t\t\t"+i.fileStore+"B");
        }
    }
    public String getCommandFront(Inode inode){
        String lastDir = new tools().lastDirectory(inode.nowDirectory);
        return ("["+inode.userName+"@OS "+lastDir+"]#:");
    }
    //根据inodeID返回文件名
    public String getFileNameByInode(Inode inode){
        int inodeID = inode.inodeID;
        String pathName = "E:\\fileInfo\\inodeFile.txt";
        try(FileReader reader = new FileReader(pathName);
            BufferedReader br = new BufferedReader(reader)){
            String line;
            while((line = br.readLine())!=null){
                String []splited = line.split(" ");
                if(splited[0].equals(String.valueOf(inodeID))){
                    return splited[1];
                }
            }

        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }
    //通过父节点id找到所有子节点inode；
    public ArrayList<Inode> dirInodeList(int fatherInodeID){
        ArrayList<Inode> inodeList = new ArrayList<>(1000);
//        Inode inode = new Inode();
        String pathName = "E:\\fileInfo\\inode.txt";
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
}

