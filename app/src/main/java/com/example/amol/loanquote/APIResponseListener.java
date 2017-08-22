package com.example.amol.loanquote;

import com.android.volley.VolleyError;
import com.example.amol.fragment.BaseModel;

import org.json.JSONObject;

/**
 * Created by amol13704 on 8/4/2017.
 */

public interface APIResponseListener {
    void onResponse(String response);
    void onErrorResponse(VolleyError error);
}
