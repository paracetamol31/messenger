package com.server;

import com.User;
import sun.nio.ch.ThreadPool;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.*;

public class Server {
   // private static Socket clientSocket;
    private static ServerSocket serverSocket;
    CopyOnWriteArrayList<User> listUsers = new CopyOnWriteArrayList<>();


    public void start() throws IOException {
        serverSocket = new ServerSocket(8189);
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(2, 8,
                50, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(100));
        Runnable waitUsers = new Runnable() {
            @Override
            public void run() {
                try {
                    Socket clientSocket = serverSocket.accept();
                    Scanner scanner = new Scanner(clientSocket.getInputStream());
                    PrintWriter printWriter = new PrintWriter(clientSocket.getOutputStream(), true);
                    String name = "";
                    while (name.equals("")){
                        printWriter.println("Введите имя:");
                        name = scanner.nextLine();
                    }
                    User newUser = new User(name, clientSocket);
                    listUsers.add(newUser);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        threadPoolExecutor.submit(waitUsers);
       /* try {
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
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            serverSocket.close();
        }*/
    }

}
