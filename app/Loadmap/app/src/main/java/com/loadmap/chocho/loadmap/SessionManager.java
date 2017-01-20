package com.loadmap.chocho.loadmap;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.HashMap;

/**
 * Created by q on 2017-01-20.
 */

public class SessionManager {
    SharedPreferences pref;

    SharedPreferences.Editor editor;
    Context _context;
    int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "LoadmapPref";
    private static final String IS_LOGIN = "IsLoggedIn";
    public static final String KEY_NAME = "name";
    public static final String KEY_USERNAME = "username";

    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    /**
     *  Create login session
     */
    public void createLoginSession(String name, String username) {
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_USERNAME, username);
        editor.commit();
    }

    /**
     *  Get stored session data
     */
    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        user.put(KEY_NAME, pref.getString(KEY_NAME, null));
        user.put(KEY_USERNAME, pref.getString(KEY_USERNAME, null));
        return user;
    }

    /**
     * Check user login status
     * if false => login page
     * else => won't do anything
     */
     public void checkLogin() {
         if (!this.isLoggedIn()) {
             Intent i = new Intent(_context, LoginActivity.class);
             i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
             i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
             _context.startActivity(i);
         }
     }

    /**
     * Logout - Clear session details
     */
    public void logoutUser() {
        editor.clear();
        editor.commit();

        Intent i = new Intent(_context, LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        _context.startActivity(i);
    }

    /**
     *  Quick check for login
     */
    public boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGIN, false);
    }
}
