package com.server;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class InitializationOfUsers extends Server {
    public static User initialization(Socket clientSocket) {
        Scanner scanner;
        PrintWriter printWriter;
        String str = "";
        try {
            scanner = new Scanner(clientSocket.getInputStream());
            printWriter = new PrintWriter(clientSocket.getOutputStream(), true);
            printWriter.println("вы зарегистрированы в чате? (y/n):");
            str = scanner.nextLine();
            while (!str.equals("Y") && !str.equals("N") && !str.equals("y") && !str.equals("n")) {
                printWriter.println("введите коректоное значение: ");
                str = scanner.nextLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (str.equals("N") || str.equals("n")) {
            return   RegistrationOfUsers.registration(clientSocket);
        } else {
            return   AuthenticationOfUsers.authentication(clientSocket);
        }
    }
}

