package com.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {
    private static Socket clientSocket;
    private static ServerSocket serverSocket;
    private static BufferedReader in;
    private static BufferedWriter out;

    public void start() throws IOException {
        try {
            serverSocket = new ServerSocket(8189);
            System.out.println("Server starting!");
            clientSocket = serverSocket.accept();
            try {
                while (true) {

                    System.out.println("кто-то подключился...");
                    Scanner scanner = new Scanner(clientSocket.getInputStream());
                    PrintWriter printWriter = new PrintWriter(clientSocket.getOutputStream(), true);
                    String str = scanner.nextLine();
                    System.out.println(str);
                    printWriter.println("Слышу");
                }
            }
            finally {
                clientSocket.close();
                in.close();
                out.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            serverSocket.close();
        }
    }

}
