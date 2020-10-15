package com;

import java.net.Socket;

public class User {
    String name;
    Socket socket;

    public User(String name, Socket socket) {
        this.name = name;
        this.socket = socket;
    }
}
