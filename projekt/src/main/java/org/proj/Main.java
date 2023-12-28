package org.proj;

import javafx.application.Application;

public class Main {
    public static void main(String[] args) {
        System.out.println("system wystartował");

        Application.launch(PropsFormApp.class, args);

        System.out.println("system zakończył działanie");
    }
}