package com.server;

import com.Commands;
import com.User;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.*;
import java.util.stream.Collectors;

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
                                PrintWriter printWriter = null;
                                try {
                                    scanner = new Scanner(finalClientSocket.getInputStream());
                                    printWriter = new PrintWriter(finalClientSocket.getOutputStream(), true);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                String name = "";
                                while (name.equals("")) {
                                    printWriter.println("Введите имя: ");
                                    name = scanner.nextLine();
                                }
                                User newUser = new User(name, finalClientSocket);
                                listUsers.add(newUser);
                                print("Server", "Пользователь " + name + " Вошел в чат");
                                while (true) {
                                    try {
                                        newUser.write();
                                        if(!newUser.getLastMessage().equals(""))
                                        arrayList.put(newUser);
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
            }
        };
        threadPoolExecutor.submit(waitUsers);

        Runnable runnableForOutput = new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        User user = arrayList.take();
                        if(user.isAmin() && user.getLastMessage().charAt(0) == '/')
                            commandsExecution(user.getLastMessage().substring(1), user.getName(), false);
                        else print(user);
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
                while(true) {
                    Scanner scanner = new Scanner(System.in);
                    String str = scanner.nextLine();
                    if(str.charAt(0) == '/') commandsExecution(str.substring(1),"Server", true);
                    else print("Server", str);
                }
            }
        };
        threadPoolExecutor.submit(printMessageForServer);
    }

    private void commandsExecution(String stringCommand, String namAuthor, boolean isServer){
        int indexSpace = stringCommand.indexOf(" ");
        String command = stringCommand.substring(0, indexSpace);
        String subject = stringCommand.substring(++indexSpace);
        if(command.equals(Commands.BAN.toString())) {
            ArrayList<User> tmp = listUsers.stream().filter(y-> y.getName().equals(subject))
                    .collect(Collectors.toCollection(ArrayList::new));
            for(User it: tmp){
                    try {
                        print(namAuthor, "пользователь " + it.getName()+ " получил бан");
                        it.getSocket().close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            listUsers = listUsers.stream().filter(y -> !y.getName().equals(subject))
                    .collect(Collectors.toCollection(CopyOnWriteArrayList::new));
        }
        else if (command.equals(Commands.GIVEADMIN.toString())){
            ArrayList<User> tmp = listUsers.stream().filter(y-> y.getName().equals(subject))
                    .collect(Collectors.toCollection(ArrayList::new));
            for(User it: tmp){
                it.setAmin(true);
                print(namAuthor, "даю пользователю " + it.getName()+ " права администратора");
            }
        }
        else{
            System.out.println("некоректная команда");
        }
    }

    private void print(String name, String text){
        try {
            for (User it : listUsers) {
                PrintWriter printWriter = new PrintWriter(it.getSocket().getOutputStream(), true);
                printWriter.println(name + ": " + text);
            }
            System.out.println(name + ": " + text);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void print(User user){
        try {
            PrintWriter printWriter = null;
            for (User it : listUsers) {
                if (user != it) {
                    printWriter = new PrintWriter(it.getSocket().getOutputStream(), true);
                    printWriter.println(user.getName() + ": " + user.getLastMessage());
                }
            }
            System.out.println(user.getName() + ": " + user.getLastMessage());
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
