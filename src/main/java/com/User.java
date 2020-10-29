package com;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class User {
    private String name;
    private  Socket socket;
    private String lastMessage;
    private boolean admin;

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
    }

    public void write() throws IOException {
        Scanner scanner = new Scanner(socket.getInputStream());
        String str = scanner.nextLine();
        lastMessage = str;
    }

    public boolean isClosed(){
       return socket.isClosed();
    }
}
