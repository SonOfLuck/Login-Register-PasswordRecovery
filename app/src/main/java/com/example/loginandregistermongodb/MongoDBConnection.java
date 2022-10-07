package com.example.loginandregistermongodb;

import android.app.Activity;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MongoDBConnection {
    // TODO: implement your api connection here
    public void loginRequest(final VolleyCallBack callBack, String email, String password, Activity activity)
    {
        String url = "http://XX.XX.XXX.XXX:PORT/home/login/" + email + "/" +  password;

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                callBack.onSuccessResponse(response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callBack.onErrorResponse(error.getMessage());
            }
        });
        MySingleton.getInstance(activity).addToRequestQueue(request);
    }

    public void createUserRequest(final VolleyCallBack callBack, String username, String password, String email,String UUID, Activity activity)
    {
        Log.d(null,"username " + username);
        JSONObject createUser = new JSONObject();
        try {
            createUser.put("e_mail", email);
            createUser.put("username", username);
            createUser.put("password", password);
            createUser.put("id_Player", UUID);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String url = "http://XX.XX.XXX.XXX:PORT/home/createPlayer";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, createUser, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                callBack.onSuccessResponse(response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callBack.onErrorResponse(error.getMessage());
            }
        });
        MySingleton.getInstance(activity).addToRequestQueue(request);
    }

    public void recoverPasswordRequest(final VolleyCallBack callBack, String email, Activity activity)
    {
        String url = "http://XX.XX.XXX.XXX:PORT/home/forgot-password/" + email;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                callBack.onSuccessResponse(response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callBack.onErrorResponse(error.getMessage());
            }
        });
        MySingleton.getInstance(activity).addToRequestQueue(request);
    }
}
