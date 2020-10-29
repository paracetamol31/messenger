package com.server;

import com.User;

import java.io.*;
import java.util.Scanner;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public class Output extends Server {
    public static void print(String nameAuthor, String text) {
        try {
            for (User it : listUsers) {
                if(it.isClosed()){
                    deleteUser(it);
                }
                else {
                    PrintWriter printWriter = new PrintWriter(it.getSocket().getOutputStream(), true);
                    printWriter.println(nameAuthor + ": " + text);
                }
            }
            System.out.println(nameAuthor + ": " + text);
            addToStory(nameAuthor, text);
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

    public static void storyOutput(User user){
        File file = new File(PATH_TO_THE_LIST_MESSAGES);
        try {
            Scanner readingFromFile = new Scanner(file);
            while (readingFromFile.hasNextLine()) {
                String nameAuthor = readingFromFile.nextLine();
                String msg = "";
                if(readingFromFile.hasNextLine()){
                    msg = readingFromFile.nextLine();
                }
                sendToOneUser(user, msg, nameAuthor);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void addToStory(String nameAuthor, String text){
        OutputStreamWriter writer = null;
        try {
            writer = new OutputStreamWriter(new FileOutputStream(PATH_TO_THE_LIST_MESSAGES, true));
            writer.append(nameAuthor).append(" ").append(String.valueOf('\n'))
                    .append(text).append(String.valueOf('\n'));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (writer != null)
                    writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
