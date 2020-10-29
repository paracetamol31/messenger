package com.server;

import com.User;

import java.io.IOException;
import java.io.PrintWriter;

public class Output extends Server {
    public static void print(String name, String text) {
        try {
            for (User it : listUsers) {
                PrintWriter printWriter = new PrintWriter(it.getSocket().getOutputStream(), true);
                printWriter.println(name + ": " + text);
            }
            System.out.println(name + ": " + text);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void sendToOneUser(User user, String msg, String nameAuthor){
        try {
            PrintWriter printWriter = new PrintWriter(user.getSocket().getOutputStream(), true);
            printWriter.println(nameAuthor + ": " + msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
