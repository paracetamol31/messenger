package com.server;

import com.User;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.*;

public class Server {
    private static ServerSocket serverSocket;
    CopyOnWriteArrayList<User> listUsers = new CopyOnWriteArrayList<>();
    LinkedBlockingQueue<User> arrayList = new LinkedBlockingQueue<User>(20);

    public void start() throws IOException {
        serverSocket = new ServerSocket(8189);
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(51, 51,
                50, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(1));
        Runnable waitUsers = new Runnable() {
            @Override
            public void run() {
                ThreadPoolExecutor threadPoolExecutorForUsers = new ThreadPoolExecutor(50, 50,
                        50, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(1));
                try {
                    Socket clientSocket = new Socket();
                    while (true) {
                        clientSocket = serverSocket.accept();
                        Socket finalClientSocket = clientSocket;
                        Runnable initUsers = new Runnable() {
                            @Override
                            public void run() {
                                Scanner scanner = null;
                                try {
                                    scanner = new Scanner(finalClientSocket.getInputStream());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                PrintWriter printWriter = null;
                                try {
                                    printWriter = new PrintWriter(finalClientSocket.getOutputStream(), true);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                String name = "";
                                while (name.equals("")){
                                    printWriter.println("Введите имя:");
                                    name = scanner.nextLine();
                                }
                                User newUser = new User(name, finalClientSocket);
                                listUsers.add(newUser);
                                while (true) {
                                    try {
                                        newUser.write();
                                        arrayList.put(newUser);
                                    } catch (IOException | InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    //System.out.println(arrayList);
                                }
                            }
                        };
                        threadPoolExecutorForUsers.submit(initUsers);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        threadPoolExecutor.submit(waitUsers);

       Runnable runnableForOutput = new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        User user = arrayList.take();
                        PrintWriter printWriter = null;
                        for (User it : listUsers) {
                            if(user!= it) {
                                printWriter = new PrintWriter(it.getSocket().getOutputStream(), true);
                                printWriter.println(user.getLastMesseng());
                            }
                        }
                    } catch (InterruptedException | IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
       threadPoolExecutor.submit(runnableForOutput);
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
