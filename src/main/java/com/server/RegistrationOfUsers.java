package com.server;

import com.User;
import com.Validator;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class RegistrationOfUsers extends InitializationOfUsers {
    public static User registration(Socket clientSocket) {
        String name = "";
        String password = "";
        try {
            Scanner scanner = new Scanner(clientSocket.getInputStream());
            PrintWriter printWriter = new PrintWriter(clientSocket.getOutputStream(), true);
            printWriter.println("Введите имя: ");
            name = scanner.nextLine();
            while (name.equals("") || Validator.validateThereIsSpace(name) || isUserExists(name)) {
                printWriter.println("имя некоректно или уже используется, введите новое: ");
                name = scanner.nextLine();
            }

            printWriter.println("Введите пароль: ");
            password = scanner.nextLine();
            while (password.equals("") || Validator.validateThereIsSpace(password)) {
                printWriter.println("Некоректный пароль, введите новый: ");
                password = scanner.nextLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        addingUserToDatabase(name, password);

        User newUser = new User(name, clientSocket);
        listUsers.add(newUser);

        Output.print("Server", "Пользователь " + name + " Вошел в чат");
        return newUser;
    }

    private static void addingUserToDatabase(String name, String password){
        OutputStreamWriter writer = null;
        try {
            writer = new OutputStreamWriter(new FileOutputStream(FILE_PATH, true));
            writer.append(name).append(" ").append(password).append(String.valueOf('\n'));
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

    private static boolean isUserExists(String name){
        File file = new File(FILE_PATH);
        try {
            Scanner readingFromFile = new Scanner(file);
            while (readingFromFile.hasNext()) {
                String str = readingFromFile.next();
                if(name.equals(str)){
                    return true;
                }
                readingFromFile.next();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }
}
