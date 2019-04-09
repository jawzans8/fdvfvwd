package com.project.lockerapp;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {

    private SharedPreferences        mSharedPreferences;
    private SharedPreferences.Editor mEditor;

    private Context mContext;

    private int PRIVATE_MODE = 0;
    private String PREF_NAME = "locker";

    public static String KEY_ID              = "id";
    public static String KEY_USERNAME        = "username";
    public static String KEY_PASSWORD        = "password";
    public static String KEY_EMAIL           = "email";
    public static String KEY_PHONE           = "phone";
    public static String KEY_CREDIT          = "credit";
    public static String KEY_LOCKER          = "lock";

    public SessionManager (Context mContext)
    {
        mSharedPreferences = mContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        mEditor            = mSharedPreferences.edit();
        this.mContext      = mContext;
    }

    public int getKeyID() {
        return mSharedPreferences.getInt(KEY_ID, 0);
    }

    public void setKeyID(int keyID) {
        mEditor.putInt(KEY_ID, keyID);
        mEditor.commit();
    }


    public String getKeyUsername() {
        return mSharedPreferences.getString(KEY_USERNAME, "");
    }

    public void setKeyUsername(String keyUsername) {
        mEditor.putString(KEY_USERNAME, keyUsername);
        mEditor.commit();
    }



    public String getKeyPassword() {
        return mSharedPreferences.getString(KEY_PASSWORD, "");
    }

    public void setKeyPassword(String keyPassword) {
        mEditor.putString(KEY_PASSWORD, keyPassword);
        mEditor.commit();
    }


    public String getKeyEmail() {
        return mSharedPreferences.getString(KEY_EMAIL, "");
    }

    public void setKeyEmail(String keyEmail) {
        mEditor.putString(KEY_EMAIL, keyEmail);
        mEditor.commit();
    }



    public int getKeyPhone() {
        return mSharedPreferences.getInt(KEY_PHONE, 0);
    }

    public void setKeyPhone(int keyPhone) {
        mEditor.putInt(KEY_PHONE, keyPhone);
        mEditor.commit();
    }


    public String getKeyCredit() {
        return mSharedPreferences.getString(KEY_CREDIT, "");
    }

    public void setKeyCredit(String keyCredit) {
        mEditor.putString(KEY_CREDIT, keyCredit);
        mEditor.commit();
    }

    public int getKeyLocker() {
        return mSharedPreferences.getInt(KEY_LOCKER, 0);
    }

    public void setKeyLocker(int keyLocker) {
        mEditor.putInt(KEY_LOCKER, keyLocker);
        mEditor.commit();
    }
}
