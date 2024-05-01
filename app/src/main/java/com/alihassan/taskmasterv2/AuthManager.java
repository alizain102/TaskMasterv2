package com.alihassan.taskmasterv2;

import android.content.Context;
import android.content.SharedPreferences;

public class AuthManager {
    private static final String PREF_NAME = "TaskMasterPrefs";
    private static final String KEY_LOGGED_IN_USERNAME = "loggedInUsername";

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public AuthManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public boolean signUp(String username, String password) {
        if (!isUserExists(username)) {
            editor.putString(username, password);
            editor.apply();
            return true;
        }
        return false;
    }

    public boolean logIn(String username, String password) {
        String savedPassword = sharedPreferences.getString(username, "");
        if (password.equals(savedPassword)) {
            editor.putString(KEY_LOGGED_IN_USERNAME, username);
            editor.apply();
            return true;
        }
        return false;
    }

    public boolean isLoggedIn() {
        return sharedPreferences.contains(KEY_LOGGED_IN_USERNAME);
    }

    public void logOut() {
        editor.remove(KEY_LOGGED_IN_USERNAME);
        editor.apply();
    }

    public String getCurrentUser() {
        return sharedPreferences.getString(KEY_LOGGED_IN_USERNAME, "");
    }

    private boolean isUserExists(String username) {
        return sharedPreferences.contains(username);
    }
}