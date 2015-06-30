package com.markin.app.common;

import android.content.SharedPreferences;

import com.markin.app.model.User;

/**
 * Created by wonmook on 2015-03-22.
 */
public class AuthManager {
    public static final String LOGIN_PREF = "login";
    public static final String USER_ID = "user_id";
    public static final String PASSWORD = "password";
    public static final String EMAIL = "email";
    public static final String USER_NAME = "user_name";
    public static final String TOKEN = "token";


    private static AuthManager authManager = new AuthManager();
    private AuthManager(){}
    public static AuthManager getAuthManager(){
        return  authManager;
    }

    public static User getLoginInfo(SharedPreferences sharedPreferences){
        User user_info = null;
        if(sharedPreferences.contains(USER_ID) && sharedPreferences.contains(PASSWORD)){
            user_info = new User();
            user_info.setUserId(sharedPreferences.getString(USER_ID, ""));
            user_info.setPassword(sharedPreferences.getString(PASSWORD,""));
            user_info.setEmail(sharedPreferences.getString(EMAIL,""));
            user_info.setName(sharedPreferences.getString(USER_NAME,""));
        }
        return user_info;
    }

    public static String getToken(SharedPreferences sharedPreferences){
        String token = "";
        if(sharedPreferences.contains(TOKEN)){
            token = sharedPreferences.getString(TOKEN,"");
        }
        return token;
    }
    public static void register(SharedPreferences sharedPreferences, String user_id, String password, String name, String email){
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString(AuthManager.USER_ID, user_id);
        edit.putString(AuthManager.PASSWORD, password);
        edit.putString(AuthManager.EMAIL, email);
        edit.putString(AuthManager.USER_NAME, name);
        edit.commit();
    }
    public static void login(SharedPreferences sharedPreferences, String user_id, String password, String name, String email, String token){
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString(AuthManager.USER_ID, user_id);
        edit.putString(AuthManager.PASSWORD, password);
        edit.putString(AuthManager.EMAIL, email);
        edit.putString(AuthManager.USER_NAME, name);
        edit.putString(AuthManager.TOKEN, token);
        edit.commit();
    }

    public static void updateToken(SharedPreferences sharedPreferences,String token){
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString(AuthManager.TOKEN, token);
        edit.commit();
    }

    public static void logout(SharedPreferences sharedPreferences){
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.clear();
        edit.commit();
    }
}
