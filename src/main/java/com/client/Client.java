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
            clientSocket = new Socket("192.168.1.38", 8189);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
            while (true) {
                System.out.println("ввод");
                PrintWriter printWriter = new PrintWriter(clientSocket.getOutputStream(), true);
                Scanner scannerForThread = new Scanner(clientSocket.getInputStream());
                Scanner scanner = new Scanner(System.in);
                String str = scanner.nextLine();
                printWriter.println(str);
                String str2 = scannerForThread.nextLine();
                System.out.println(str2);
            }


        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
