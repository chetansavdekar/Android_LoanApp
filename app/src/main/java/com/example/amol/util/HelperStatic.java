package com.example.amol.util;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.Html;
import android.util.Log;
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.amol.fragment.BaseModel;
import com.example.amol.loanquote.APIResponseListener;
import com.example.amol.loanquote.AppController;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xml.sax.XMLReader;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;


public class HelperStatic {

    private static String tag = "Nilesh";

    public static int timeout = 300000;
    public static final String MESSAGE = "message";
    public static final String RESPONSE = "response";
    public static final String FAQ = "faq";
    public static final String SUBCATEGORY = "subcategory";
    public static final String LOCATION = "location";
    public static final String TNC = "tnc";
    public static final String CHANNEL = "channel";
    public static final String RESPONSE_MESSAGEID = "messageId";
    public static final String RESPONSE_MESSAGE = "message";
    public static final int CALL_NOTIFICATION_ID = 10;
    public static String TAG = "Nilesh";


    private HelperStatic() {
    }

    public static void log(String tag, String msg) {
        Log.e(tag, msg);
    }

    public static void jsonObjectRequest(Context context, int getPost, String url, JSONObject jsonObject,
                                         final boolean showProgress, final APIResponseListener listener, boolean isJSONObject) {
        final ProgressDialog dialog = new ProgressDialog(context);
        dialog.setMessage("Start loading");
        dialog.setCancelable(false);
        if (UiUtils.isNetworkConnected(context)) {
            Log.e(tag, "jsonObjectRequest: url " + url);
            Log.e(tag, "jsonObjectRequest: json " + jsonObject.toString());

            if (showProgress) {
                dialog.show();
            }


            if (isJSONObject) {
                JsonObjectRequest
                        request = new JsonObjectRequest(getPost, url, jsonObject,
                        new JSONObjectResponseListener(showProgress,
                                dialog, listener), new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(tag, "onErrorResponse: " + error.getMessage());
                        if (showProgress) {
                            dialog.dismiss();
                        }
                        listener.onErrorResponse(error);
                        NetworkResponse errorRes = error.networkResponse;
                        String stringData;
                        try {
                            stringData = new String(errorRes.data, "UTF-8");

                        } catch (Exception e) {
                            Log.e("Nilesh", "onErrorResponse: ");
                        }

                    }
                }) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        HashMap<String, String> map = new HashMap<>();
                        map.put("Content-Type", "application/json; charset=utf-8");
                        return map;
                    }
                };
                request.setShouldCache(false);

                request.setRetryPolicy(new DefaultRetryPolicy(
                        timeout,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                AppController.getInstance().addToRequestQueue(request, "request");
                Log.i("Request ::", request.toString());
            } else {
                JSONArray array = new JSONArray();

                JsonArrayRequest request = new JsonArrayRequest(getPost, url, array,
                        new JSONArrayResponseListener(showProgress,
                                dialog, listener), new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(tag, "onErrorResponse: " + error.getMessage());
                        if (showProgress) {
                            dialog.dismiss();
                        }
                        listener.onErrorResponse(error);
                        NetworkResponse errorRes = error.networkResponse;
                        String stringData;
                        try {
                            stringData = new String(errorRes.data, "UTF-8");

                        } catch (Exception e) {
                            Log.e("Nilesh", "onErrorResponse: ");
                        }

                    }
                }) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        HashMap<String, String> map = new HashMap<>();
                        map.put("Content-Type", "application/json; charset=utf-8");
                        return map;
                    }
                };
                request.setShouldCache(false);

                request.setRetryPolicy(new DefaultRetryPolicy(
                        timeout,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                AppController.getInstance().addToRequestQueue(request, "request");
                Log.i("Request ::", request.toString());
            }


        } else {
            listener.onErrorResponse(null);
            //((BaseActivity) context).showError("No Network" , false, null);
            Toast.makeText(AppController.getInstance(), "No Network", Toast.LENGTH_LONG).show();
        }
    }

    /*public static void jsonObjectRequestBeforeLogin(Context context, int getPost, String url, JSONObject jsonObject, final boolean showProgress, final JsonObjectResponseListener listener) {


        final ProgressDialog dialog = new ProgressDialog(context);
        dialog.setMessage("Loading...");
        dialog.setCancelable(false);
        if (UiUtils.isNetworkConnected(context)) {
            Log.e(tag, "jsonObjectRequest: url " + url);
            Log.e(tag, "jsonObjectRequest: json " + jsonObject.toString());

            if (showProgress) {
                dialog.show();
            }
            JsonObjectRequest request = new JsonObjectRequest(getPost, url, jsonObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.e(tag, "onResponse: " + response.toString());

                    if (showProgress) {
                        dialog.dismiss();
                    }
                    listener.onResponse(response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(tag, "onErrorResponse: " + error.getMessage());

                    if (showProgress) {
                        dialog.dismiss();
                    }

                    NetworkResponse errorRes = error.networkResponse;
                    String stringData;
                    try{
                        stringData = new String(errorRes.data, "UTF-8");
                        listener.onResponse(new JSONObject(stringData));
                    }
                    catch (Exception e)
                    {
                        Log.e("Nilesh", "onErrorResponse: "+e);
                    }
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> map = new HashMap<>();
                    map.put("Content-Type", "application/json; charset=utf-8");
                    return map;
                }

                @Override
                protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                    Log.e("Nilesh", "parseNetworkResponse: "+response.statusCode );

                    return super.parseNetworkResponse(response);
                }
            };

            request.setRetryPolicy(new DefaultRetryPolicy(
                    timeout,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            AppController.getInstance().addToRequestQueue(request, "request");
        } else {
            ((BaseActivity) context).showError(context.getString(R.string.no_network) , false, null);
        }
    }

*/
   /* public static void showMessageOKCancel(String message, Context context, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(context)
                .setMessage(message)
                .setPositiveButton(context.getString(R.string.ok), okListener)
                .setNegativeButton(context.getString(R.string.cancel), null)
                .create()
                .show();
    }
    public static void showMessageOKCancel(String message, Context context, DialogInterface.OnClickListener okListener, DialogInterface.OnClickListener cancelListener) {
        new AlertDialog.Builder(context)
                .setMessage(message)
                .setPositiveButton(context.getString(R.string.ok), okListener)
                .setNegativeButton(context.getString(R.string.cancel), cancelListener)
                .create()
                .show();
    }*/
    public static void closeIS(InputStream input) {
        try {
            if (input != null) {
                input.close();
            }
        } catch (IOException ioe) {
            Log.e(tag, "closeIS: " + ioe);
        }
    }

    public static void closeFOS(FileOutputStream input) {
        try {
            if (input != null) {
                input.close();
            }
        } catch (IOException ioe) {
            Log.e(tag, "closeFOS: " + ioe);
        }
    }

    public static String getTag() {
        return tag;
    }

    public static class UlTagHandler implements Html.TagHandler {
        boolean first = true;
        String parent = null;
        int index = 1;

        public void handleTag(final boolean opening, final String tag,
                              final Editable output, final XMLReader xmlReader) {

            if (tag.equals("ul")) {
                parent = "ul";
                index = 1;
            } else if (tag.equals("ol")) {
                parent = "ol";
                index = 1;
            }
            if (tag.equals("li")) {
                char lastChar = 0;
                if (output.length() > 0) {
                    lastChar = output.charAt(output.length() - 1);
                }
                if (parent.equals("ul")) {
                    if (first) {
                        if (lastChar == '\n') {
                            output.append("\t•  ");
                        } else {
                            output.append("\n\t•  ");
                        }
                        first = false;
                    } else {
                        first = true;
                    }
                } else {
                    if (first) {
                        if (lastChar == '\n') {
                            output.append("\t" + index + ". ");
                        } else {
                            output.append("\n\t" + index + ". ");
                        }
                        first = false;
                        index++;
                    } else {
                        first = true;
                    }
                }
            }
        }
    }


}

class JSONObjectResponseListener implements Response.Listener<JSONObject> {

    private String tag = "Nilesh";
    private ProgressDialog dialog;
    private APIResponseListener listener;
    private boolean showProgress;


    public JSONObjectResponseListener(boolean showProgress, ProgressDialog dialog, APIResponseListener listener) {
        this.showProgress = showProgress;
        this.dialog = dialog;
        this.listener = listener;
    }

    @Override
    public void onResponse(JSONObject response) {
        Log.e(tag, "onResponse: " + response.toString());
        if (showProgress && dialog.isShowing()) {
            dialog.dismiss();
        }
        if (response != null)
            listener.onResponse(response.toString());
    }
}

class JSONArrayResponseListener implements Response.Listener<JSONArray> {


    private String tag = "";
    private ProgressDialog dialog;
    private APIResponseListener listener;
    private boolean showProgress;

    public JSONArrayResponseListener(boolean showProgress, ProgressDialog dialog, APIResponseListener listener) {
        this.showProgress = showProgress;
        this.dialog = dialog;
        this.listener = listener;
    }

    @Override
    public void onResponse(JSONArray response) {
        Log.e("Nilesh", "onResponse: " + response.toString());
        if (showProgress && dialog.isShowing()) {
            dialog.dismiss();
        }
            if(response != null)
            listener.onResponse(response.toString());

    }
}



