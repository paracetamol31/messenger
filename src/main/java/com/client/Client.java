package com.client;

import java.io.*;

import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {
    private static Socket clientSocket;
    private static BufferedReader in;
    private static BufferedWriter out;

    public void join() {
        try{
            clientSocket = new Socket("localhost", 8189);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
            while (true) {
                System.out.println("ввод");
                PrintWriter printWriter = new PrintWriter(clientSocket.getOutputStream(), true);
                Scanner scannerForThread = new Scanner(clientSocket.getInputStream());
                String str = scannerForThread.nextLine();
                System.out.println(str);
                Scanner scanner = new Scanner(System.in);
                String str3 = scanner.nextLine();
                printWriter.println(str3);
              /*  String str2 = scannerForThread.nextLine();
                System.out.println(str2);*/
            }


        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
