package com.server;

import com.Commands;
import com.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public class MessengerCommands extends Server {

    public static void callingCommandByUser(User user) {
        int indexSpace = user.getLastMessage().indexOf(" ");
        String command = user.getLastMessage().substring(1, indexSpace);
        String subject = user.getLastMessage().substring(++indexSpace);
        if(callingCommand(command, subject, user.getName())){
            Output.sendToOneUser(user, "некоректная команда", "Server");
        }
    }

    public static void callingCommandByServer(String message) {
        int indexSpace = message.indexOf(" ");
        String command = message.substring(1, indexSpace);
        String subject = message.substring(++indexSpace);
        if(callingCommand(command, subject, "Server")){
            System.out.println("вызвана некоректная команда");
        }
    }

    private static boolean callingCommand(String command, String subject, String nameAuthor){
        if (command.equals(Commands.BAN.toString())) {
            ban(subject, nameAuthor);
            return false;
        } else if (command.equals(Commands.GIVEADMIN.toString())) {
            giveAdmin(subject, nameAuthor);
            return false;
        } else {
            return true;
        }
    }

    private static void ban(String subject, String nameAuthor){
        ArrayList<User> tmp = listUsers.stream().filter(y -> !y.isClosed()).filter(y -> y.getName().equals(subject))
                .collect(Collectors.toCollection(ArrayList::new));
        for (User it : tmp) {
            try {
                Output.print(nameAuthor, "пользователь " + it.getName() + " получил бан");
                it.getSocket().close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        listUsers = listUsers.stream().filter(y -> !y.isClosed()).filter(y -> !y.getName().equals(subject))
                .collect(Collectors.toCollection(CopyOnWriteArrayList::new));
    }

    private static void giveAdmin(String subject, String nameAuthor){
        ArrayList<User> tmp = listUsers.stream().filter(y -> !y.isClosed()).filter(y -> y.getName().equals(subject))
                .collect(Collectors.toCollection(ArrayList::new));
        for (User it : tmp) {
            it.setAdmin(true);
            Output.print(nameAuthor, "даю пользователю " + it.getName() + " права администратора");
        }
    }
}
