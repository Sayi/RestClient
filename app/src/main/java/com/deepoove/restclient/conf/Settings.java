package com.deepoove.restclient.conf;

import android.content.Context;
import android.content.SharedPreferences;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Map;

public class Settings {


    private final SharedPreferences settings;

    /**
     * @param act The context from which to pick SharedPreferences
     */
    public Settings(Context act, String name) {
        settings = act.getSharedPreferences(name, Context.MODE_PRIVATE);
    }


    public void put(String key, String value) {
        SharedPreferences.Editor editor = settings.edit();
        if (null == value) {
            editor.remove(key);
        } else {
            editor.putString(key, value);
        }
        editor.apply();
        editor.commit();


    }

    public void put(long key, Object value) {
        put(key + "", value);
    }

    public void put(String key, Object value) {
        SharedPreferences.Editor editor = settings.edit();
        if (null == value) {
            editor.remove(key);
        } else {
            Gson gson = new GsonBuilder().create();
            editor.putString(key, gson.toJson(value));
        }
        editor.apply();
        editor.commit();
    }

    public void clearAll() {
        SharedPreferences.Editor editor = settings.edit();
        editor.clear();
        editor.apply();
        editor.commit();
    }

    public Map<String, ?> getAll() {
        return settings.getAll();
    }

    public String get(String key) {
        return settings.getString(key, null);
    }

    public int getInt(String key) {
        return settings.getInt(key, 0);
    }

    public <T> T get(String key, Class<T> clazz) {
        String value = settings.getString(key, null);
        if (null == value) return null;
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(value, clazz);
    }

    public <T> T get(long key, Class<T> clazz) {
        String value = settings.getString(key + "", null);
        if (null == value) return null;
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(value, clazz);
    }


    // Check if there are any stored settings.
    // can be used to automatically load the settings page
    // where necessary
    private boolean hasSettings() {
        // We just check if a username has been set
        //return (!settings.getString(USERNAME_KEY, "").equals(""));
        return false;
    }

}

