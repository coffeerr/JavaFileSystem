package com.company;
import com.company.test;

import java.util.ArrayList;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) {
        while(true){
            System.out.println("登陆：请输入[用户名] [密码]：");
            Scanner sc = new Scanner(System.in);
            String un = sc.next();
            String ps = sc.next();
            test a = new test();
            a.init();
            ArrayList<String> users = a.users;
            if(a.isLogin(un,ps)){
                System.out.println("登陆成功！");
                break;
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
                        //a.Register(Run,Rps);
                        System.out.println("注册成功！");
                        break;
                    }
                }
            }
        }
    }
}
