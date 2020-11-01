package com.server;

import java.io.IOException;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class User extends Server {
    private String name;
    private  Socket socket;
    private String lastMessage;
    private boolean admin;
    private Scanner scanner;

    public String getName() {
        return name;
    }

    public Socket getSocket() {
        return socket;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public User(String name, Socket socket) {
        this.name = name;
        this.socket = socket;
        this.lastMessage = "";
        admin = false;
        try {
            scanner = new Scanner(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startGiveMessages() {
        Thread giveMsg = new Thread(() -> {
            boolean flag = true;
            while (flag) {
                flag = giveMessages();
            }
        });
        giveMsg.start();
    }

    public void read() throws NoSuchElementException {
        try {
            scanner = new Scanner(socket.getInputStream());
            lastMessage = scanner.nextLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public boolean giveMessages(){
        try {
            read();
        }catch (NoSuchElementException e){
            deleteUser(this);
            return false;
        }
        if (!lastMessage.equals("")) {
            try {
                messages.put(this);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    public void stop(){
        Output.print("Server", "Пользователь " + name + " вышел из чата");
        scanner.close();
    }
}