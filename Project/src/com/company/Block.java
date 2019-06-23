package com.company;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Block {
    String pathname = "fileinfo/block.txt";

    public int pointer;
    public int isUsed;//0表示未使用,1表示已经用过了
    public String content;
    public Block(int pointer, int isUsed, String content) {
        this.pointer = pointer;
        this.isUsed = isUsed;
        this.content = content;
    }

    public Block() {

    }

    //通过pointer获得content
    public Block getByPointer(int pointer) {
        ArrayList<String> list = new ArrayList<>(1000);
        try (FileReader reader = new FileReader(pathname);
             BufferedReader br = new BufferedReader(reader)) {
            String line;
            String sContent = "";
            while ((line = br.readLine()) != null) {
                String[] splited = line.split(" ");
                if (splited[0].equals(String.valueOf(pointer))) {
                    for (int i = 0; i <= splited.length - 1; i++) {
                        if (i >= 0 && i <= 1) {
                            list.add(splited[i]);
                        } else {
                            sContent = sContent + splited[i] + " ";
                        }
                    }
                }
            }
            this.pointer = Integer.parseInt(list.get(0));
            this.isUsed = Integer.parseInt(list.get(1));
            this.content = sContent;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    //寻找空闲pointer
    public int findFreePointer() {
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
}
