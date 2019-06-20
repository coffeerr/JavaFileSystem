package com.company;

import java.io.*;

public class FileTools {
    //在制定行后添加数据（行数从1开始）
    public void addStringInFile(File inFile, int lineno, String lineToBeInserted)
            throws Exception {
        // 临时文件
        File outFile = File.createTempFile("name", ".tmp");

        // 输入
        FileInputStream fis = new FileInputStream(inFile);
        BufferedReader in = new BufferedReader(new InputStreamReader(fis));

        // 输出
        FileOutputStream fos = new FileOutputStream(outFile);
        PrintWriter out = new PrintWriter(fos);

        // 保存一行数据
        String thisLine;
        // 行号从1开始
        int i = 1;
        while ((thisLine = in.readLine()) != null) {
            // 如果行号等于目标行，则输出要插入的数据
            if (i == lineno) {
                thisLine = thisLine + lineToBeInserted;
            }
            // 输出读取到的数据
            out.println(thisLine);
            // 行号增加
            i++;
        }
        out.flush();
        out.close();
        in.close();

        // 删除原始文件
        inFile.delete();
        // 把临时文件改名为原文件名
        outFile.renameTo(inFile);
    }
    //删除指定行再插入数据
    public void insertStringInFile(File inFile, int lineno, String lineToBeInserted)
            throws Exception {
        // 临时文件
        File outFile = File.createTempFile("name", ".tmp");

        // 输入
        FileInputStream fis = new FileInputStream(inFile);
        BufferedReader in = new BufferedReader(new InputStreamReader(fis));

        // 输出
        FileOutputStream fos = new FileOutputStream(outFile);
        PrintWriter out = new PrintWriter(fos);

        // 保存一行数据
        String thisLine;
        // 行号从1开始
        int i = 1;
        while ((thisLine = in.readLine()) != null) {
            // 如果行号等于目标行，则输出要插入的数据
            if (i == lineno) {
                thisLine = lineToBeInserted;
            }
            // 输出读取到的数据
            out.println(thisLine);
            // 行号增加
            i++;
        }
        out.flush();
        out.close();
        in.close();

        // 删除原始文件
        inFile.delete();
        // 把临时文件改名为原文件名
        outFile.renameTo(inFile);
    }
//    public void insertNewWord(long skip, String str, String fileName) {
//        try {
//            RandomAccessFile randomAccessFile = new RandomAccessFile(fileName, "rw");
//            if (skip < 0 || skip > randomAccessFile.length()) {
//                return;
//            }
//            byte[] b = str.getBytes();
//            System.out.println(b.length);
//            System.out.println(randomAccessFile.length());
//            randomAccessFile.setLength(randomAccessFile.length() + b.length);
//            //把后面的内容往后面挪
//            for (long i = randomAccessFile.length() - 1; i > b.length + skip - 1; i--) {
//                randomAccessFile.seek(i - b.length);
//                byte temp = randomAccessFile.readByte();
//                randomAccessFile.seek(i);
//                randomAccessFile.writeByte(temp);
//            }
//            randomAccessFile.seek(skip);
//            randomAccessFile.write(b);
//            randomAccessFile.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}
