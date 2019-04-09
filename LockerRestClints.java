package com.project.lockerapp;

import android.os.Looper;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;

public class LockerRestClints {

    private static final String BASE_URL = "http://10.0.2.2/LockerAPI/";

    public static AsyncHttpClient syncHttpClient  = new SyncHttpClient();
    public static AsyncHttpClient asyncHttpClient = new AsyncHttpClient();


    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {

        getClient().get(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {

        getClient().post(getAbsoluteUrl(url), params, responseHandler);
    }

    public static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }

    private static AsyncHttpClient getClient()
    {
        if (Looper.myLooper() == null)
            return syncHttpClient;
        return asyncHttpClient;
    }

}
