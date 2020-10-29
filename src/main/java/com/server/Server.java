package com.server;

import com.User;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.*;

public class Server {
    private static ServerSocket serverSocket;
    protected static CopyOnWriteArrayList<User> listUsers = new CopyOnWriteArrayList<>();
    protected static LinkedBlockingQueue<User> messages = new LinkedBlockingQueue<>(20);
    final static protected String FILE_PATH = "src/main/resources/users.txt";
    final static private int PORT = 8189;

    public void start() throws IOException {
        serverSocket = new ServerSocket(PORT);
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(51, 51,
                50, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(1));
        Runnable waitUsers = () -> {
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
                            User newUser = InitializationOfUsers.initialization(finalClientSocket);
                            while (true) {
                                try {
                                    newUser.write();
                                    if (!newUser.getLastMessage().equals(""))
                                        messages.put(newUser);
                                } catch (IOException | InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    };
                    threadPoolExecutorForUsers.submit(initUsers);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        };
        threadPoolExecutor.submit(waitUsers);

        Runnable runnableForOutput = new Runnable() {
            @Override
            public void run() {
                User user = null;
                while (true) {
                    try {
                        user = messages.take();
                        if (user.isAdmin() && user.getLastMessage().charAt(0) == '/') {
                            MessengerCommands.callingCommandByUser(user);
                        }
                        else Output.print(user.getName(), user.getLastMessage());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        threadPoolExecutor.submit(runnableForOutput);

        Runnable printMessageForServer = new Runnable() {
            @Override
            public void run() {
                Scanner scanner = new Scanner(System.in);
                while (true) {
                    String str = scanner.nextLine();
                    if (str.charAt(0) == '/') {
                        MessengerCommands.callingCommandByServer(str);
                    }
                    else Output.print("Server", str);
                }
            }
        };
        threadPoolExecutor.submit(printMessageForServer);
    }
}

