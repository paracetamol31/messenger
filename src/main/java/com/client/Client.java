package com.client;

import java.io.*;

import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Client {
    private static Socket serverSocket;
    private static BufferedReader in;
    private static BufferedWriter out;

    public void join() {
        try{
            serverSocket = new Socket("localhost", 8189);
            ThreadPoolExecutor threadPoolExecutorForUsers = new ThreadPoolExecutor(2, 4,
                    50, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(1));

                //System.out.println("ввод");
                PrintWriter printWriter = new PrintWriter(serverSocket.getOutputStream(), true);
                Scanner scannerForThread = new Scanner(serverSocket.getInputStream());
                String str = scannerForThread.nextLine();
                System.out.println(str);
                Scanner scanner = new Scanner(System.in);
                String str3 = scanner.nextLine();
                printWriter.println(str3);
            Runnable runnableForPrint = new Runnable() {
                @Override
                public void run() {
                    try {
                        Scanner scanner = new Scanner(serverSocket.getInputStream());
                        while (true){
                            //System.out.println("жду");
                            String str = scanner.nextLine();
                            System.out.println(str);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            };
            threadPoolExecutorForUsers.submit(runnableForPrint);
                while (true){
                    //System.out.println("сообщение:");
                    String str9 = scanner.nextLine();
                    printWriter.println(str9);
                }


        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
