package com.yourorg.srs.ui;

import com.yourorg.srs.serviceimpl.DataSeeder;

public class Main {
    public static void main(String[] args) {
        // Uncomment if you want to pre-seed DB
        // new DataSeeder().seedAll();

        Menu menu = new Menu();

        boolean running = true;
        while (running) {
            boolean loggedOut = menu.start();

            if (!loggedOut) {
                System.out.println("Exiting system...");
                running = false;
            }
        }
    }
}
