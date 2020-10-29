package com.server;

import com.User;
import com.Validator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class AuthenticationOfUsers extends InitializationOfUsers {
    public static User authentication(Socket clientSocket){
        Scanner scanner = null;
        PrintWriter printWriter = null;
        String name = "";
        try {
            scanner = new Scanner(clientSocket.getInputStream());
            printWriter = new PrintWriter(clientSocket.getOutputStream(), true);

            boolean flag = true;
            while (flag) {
                printWriter.println("Введите имя: ");
                name = scanner.nextLine();
                while (name.equals("") || Validator.validateThereIsSpace(name)) {
                    printWriter.println("имя некоректно или уже используется, введите новое: ");
                    name = scanner.nextLine();
                }

                String password = "";
                printWriter.println("Введите пароль: ");
                password = scanner.nextLine();
                while (password.equals("") || Validator.validateThereIsSpace(password)) {
                    printWriter.println("Некоректный пароль, введите новый: ");
                    password = scanner.nextLine();
                }
                flag = isTheUserRegistered(name + " " + password);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        User newUser = new User(name, clientSocket);
        listUsers.add(newUser);
        return newUser;
    }

    private static boolean isTheUserRegistered(String nameUser){
        File file = new File(PATH_TO_THE_LIST_OF_USERS);
        Scanner readingFromFile = null;
        String str = "";
        try {
            readingFromFile = new Scanner(file);
            while (readingFromFile.hasNextLine()) {
                str = readingFromFile.nextLine();
                if(nameUser.equals(str)){
                    return false;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return true;
    }
}
