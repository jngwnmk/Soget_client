package com.markin.app.common;

import android.content.SharedPreferences;

/**
 * Created by wonmook on 15. 5. 12..
 */
public class SettingManager {

    public static final String IS_NEED = "isneed";
    public static final String SHARED_URL = "sharedurl";


    public static final String LASTDISCOVER = "lastdiscover";
    public static final String LASTDISCOVERDATE ="date";


    private static SettingManager settingManager = new SettingManager();
    private SettingManager(){}
    public static SettingManager getSettingManager(){
        return  settingManager;
    }

    public static void setSharedUrlAdd(SharedPreferences sharedPreferences, boolean need){
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putBoolean(SettingManager.IS_NEED, need);
        edit.commit();
    }

    public static boolean needSharedUrlAddd(SharedPreferences sharedPreferences){
        Boolean need = false;
        if(sharedPreferences.contains(IS_NEED)){
            need = sharedPreferences.getBoolean(IS_NEED,false);
        }
        return need;
    }

    /*public static void setLastDiscoverDate(SharedPreferences sharedPreferences, long date){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(SettingManager.LASTDISCOVERDATE,date);
        editor.commit();
    }

    public static long getLastDiscoverDate(SharedPreferences sharedPreferences){
        long date = System.currentTimeMillis();
        if(sharedPreferences.contains(SettingManager.LASTDISCOVERDATE)){
            date = sharedPreferences.getLong(SettingManager.LASTDISCOVERDATE, System.currentTimeMillis());
        }
        return date;
    }*/





}
