package com.example.amol.loanquote;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.amol.fragment.UserModel;

/**
 * Created by amol13704 on 8/4/2017. //commit
 */

public class AppController extends Application {


    public static final String TAG = "Nilesh";
    private RequestQueue mRequestQueue;
    private static AppController appContext;

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = this;
    }

    public static synchronized AppController getInstance() {
        return appContext;
    }

    public UserModel getUserModel() {
        return userModel;
    }

    public void setUserModel(UserModel userModel) {
        this.userModel = userModel;
    }

    private UserModel userModel;

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }
}
