package com;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class User {
    private String name;
    private Socket socket;
    private String lastMessage;
    private boolean amin;

    public String getName() {
        return name;
    }

    public Socket getSocket() {
        return socket;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public boolean isAmin() {
        return amin;
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

    public void setAmin(boolean amin) {
        this.amin = amin;
    }

    public User(String name, Socket socket) {
        this.name = name;
        this.socket = socket;
        this.lastMessage = "";
        amin = false;
    }

    public void write() throws IOException {
        Scanner scanner = new Scanner(socket.getInputStream());
        String str = scanner.nextLine();
        lastMessage = str;
    }
}
