package com.coursework.Application.util;

public class UserSession {
    private static boolean loggedIn = false;
    private static String username = null;

    public static void login(String name) {
        loggedIn = true;
        username = name;
    }

    public static boolean isLoggedIn() {
        return loggedIn;
    }

    public static String getUsername() {
        return username;
    }

    public static void logout() {
        loggedIn = false;
        username = null;
    }

    public static void clear() {
        username = null;
    }

}
