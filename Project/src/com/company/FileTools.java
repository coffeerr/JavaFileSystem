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
    //获得当前用户名在inode目录的行数
    public int getInodeLine(String userName){
        String pathname = "fileinfo/inode.txt";
        try(FileReader reader = new FileReader(pathname);
            BufferedReader br = new BufferedReader(reader)){
            String line;
            int lineNo = 1;
            while((line = br.readLine())!=null){
                String []splited = line.split(" ");
                if(splited[2].equals(userName)){
                    return lineNo;
                }
                lineNo++;
            }

        }catch (IOException e){
            e.printStackTrace();
        }
        return -1;
    }
}
