package com.client;

import java.io.*;

import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Client {
    private static Socket serverSocket;
    private final static String IP = "localhost";
    private final static int PORT =  8189;

    public void join() {
        try{
            serverSocket = new Socket(IP, PORT);
           Thread writeMessages = new Thread(() -> {
               try {
                   Scanner scanner = new Scanner(serverSocket.getInputStream());
                   while (true){
                       try {
                           String str = scanner.nextLine();
                           System.out.println(str);
                       }catch (NoSuchElementException e){
                           System.out.println("Соединение с сервером разорвано");
                           System.exit(0);
                       }
                   }
               } catch (IOException e) {
                   e.printStackTrace();
               }
           });
            writeMessages.setDaemon(true);
            writeMessages.start();

            Scanner scanner = new Scanner(System.in);
            PrintWriter printWriter = new PrintWriter(serverSocket.getOutputStream(), true);
                while (true){
                    String str9 = scanner.nextLine();
                    printWriter.println(str9);
                }


        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
