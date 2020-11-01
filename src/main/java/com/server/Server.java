package com.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class Server {
    private static ServerSocket serverSocket;
    protected static CopyOnWriteArrayList<User> listUsers = new CopyOnWriteArrayList<>();
    protected static LinkedBlockingQueue<User> messages = new LinkedBlockingQueue<>(20);
    final static protected String PATH_TO_THE_LIST_OF_USERS = "src/main/resources/users.txt";
    final static protected String PATH_TO_THE_LIST_MESSAGES = "src/main/resources/messages.txt";
    final static private int PORT = 8189;

    public void start() throws IOException {
        serverSocket = new ServerSocket(PORT);
        Output.outputStoryForServer();
        Thread connectWithNewClients = new Thread(() -> {
            while (true) {
                try {
                    Socket finalClientSocket = serverSocket.accept();
                    User newUser =  InitializationOfUsers.initialization(finalClientSocket);
                    if(newUser == null) {
                        finalClientSocket.close();
                        continue;
                    }
                    Output.outputStoryForUser(newUser);
                    Output.print("Server", "Пользователь " + newUser.getName() + " Вошел в чат");
                    newUser.startGiveMessages();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        connectWithNewClients.start();

        Thread sendsMessagesToUsers = new Thread(() -> {
            while (true) {
                try {
                    User user = messages.take();
                    if (user.isAdmin() && user.getLastMessage().charAt(0) == '/') {
                        MessengerCommands.callingCommandByUser(user);
                    } else Output.print(user.getName(), user.getLastMessage());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        sendsMessagesToUsers.start();

        Thread readingMessagesFromTheServer = new Thread(() -> {
            while (true) {
                Scanner scanner = new Scanner(System.in);
                String str = scanner.nextLine();
                if (str.charAt(0) == '/') {
                    MessengerCommands.callingCommandByServer(str);
                } else Output.print("Server", str);
            }
        });
        readingMessagesFromTheServer.start();
    }

    protected static void deleteUser(User user){
        listUsers = listUsers.stream().filter(y -> y != user)
                .collect(Collectors.toCollection(CopyOnWriteArrayList::new));
        user.stop();
    }
}
