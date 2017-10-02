package com.grument.doittestproject.util;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;
import static android.content.Context.USER_SERVICE;


public class PreferenceHelper {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;


    private final static String AVATAR_LINK = "AVATAR_LINK";
    private final static String USER_TOKEN = "USER_TOKEN";
    private final static String USER_LOGIN = "USER_LOGIN";
    private final static String USER_PASSWORD = "USER_PASSWORD";
    private final static String AUTHORIZATION_STATE = "AUTHORIZATION_STATE";

    public PreferenceHelper(Context context) {
        sharedPreferences = context.getSharedPreferences(USER_SERVICE, MODE_PRIVATE);
    }

    public void saveAvatarImageLink(String avatarLink) {
        editor = sharedPreferences.edit();
        editor.putString(AVATAR_LINK, avatarLink);
        editor.apply();
    }

    public String getAvatarImageLink() {
        return sharedPreferences.getString(AVATAR_LINK, "");
    }

    public void saveUserToken(String token) {
        editor = sharedPreferences.edit();
        editor.putString(USER_TOKEN, token);
        editor.apply();
    }

    public String getUserToken() {
        return sharedPreferences.getString(USER_TOKEN, "");
    }

    public void saveUserLogin(String userLogin) {
        editor = sharedPreferences.edit();
        editor.putString(USER_LOGIN, userLogin);
        editor.apply();
    }


    public String getUserLogin() {
        return sharedPreferences.getString(USER_LOGIN, "");
    }

    public void saveUserPassword(String userPassword) {
        editor = sharedPreferences.edit();
        editor.putString(USER_PASSWORD, userPassword);
        editor.apply();
    }


    public String getUserPassword() {
        return sharedPreferences.getString(USER_PASSWORD, "");
    }

    public void saveAuthorizationState(boolean authorizationState) {
        editor = sharedPreferences.edit();
        editor.putBoolean(AUTHORIZATION_STATE, authorizationState);
        editor.apply();
    }


    public boolean getAuthorizationState() {
        return sharedPreferences.getBoolean(AUTHORIZATION_STATE, false);
    }





}


