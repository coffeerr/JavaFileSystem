package com.company;

import com.company.SuperBlock;
import com.company.FileTools;
import com.company.Inode;
import java.io.File;
import java.util.Scanner;

import com.company.InodeFile;
public class outTest {
    public String lastDirectory(String dir){
        String []splited = dir.trim().split("/");
        //System.out.println(splited);
        return splited[splited.length-1];
    }
    public static void main(String[] args) {
       Inode inode = new Inode();
       inode.setBlockPointer(618);
       System.out.println(new tools().getBlockByInode(inode));
    }
}
