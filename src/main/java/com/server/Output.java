package com.server;

import com.User;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public class Output extends Server {
    public static void print(String name, String text) {
        try {
            for (User it : listUsers) {
                if(it.isClosed()){
                    deleteUser(it);
                }
                else {
                    PrintWriter printWriter = new PrintWriter(it.getSocket().getOutputStream(), true);
                    printWriter.println(name + ": " + text);
                }
            }
            System.out.println(name + ": " + text);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void sendToOneUser(User user, String msg, String nameAuthor){
        try {
            if(user.isClosed()){
                deleteUser(user);
            }
            else {
                PrintWriter printWriter = new PrintWriter(user.getSocket().getOutputStream(), true);
                printWriter.println(nameAuthor + ": " + msg);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
