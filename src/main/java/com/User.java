package com;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class User {
    String name;
    Socket socket;
    String lastMesseng;

    public String getName() {
        return name;
    }

    public Socket getSocket() {
        return socket;
    }

    public String getLastMesseng() {
        return lastMesseng;
    }

    public User(String name, Socket socket) {
        this.name = name;
        this.socket = socket;
        this.lastMesseng = "";
    }

    public void write() throws IOException {
        Scanner scanner = new Scanner(socket.getInputStream());
        String str = name + ": " ;
        str+= scanner.nextLine();
        lastMesseng = str;
    }
}
