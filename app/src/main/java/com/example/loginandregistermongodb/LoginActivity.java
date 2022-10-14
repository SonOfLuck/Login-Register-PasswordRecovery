package com.example.loginandregistermongodb;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    private EditText email, password;
    private AppCompatButton login;
    private TextView createUser, recoverPassword;
    MongoDBConnection mongoDBapi;
    MyInternetConnection internetConnection;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        LoginActivity.this.getSupportActionBar().hide();
        mongoDBapi = new MongoDBConnection();
        internetConnection = new MyInternetConnection();
        createUser = findViewById(R.id.createUser);
        login = findViewById(R.id.login);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        recoverPassword = findViewById(R.id.recoverPassword);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String e_mail = email.getText().toString();
                String pass = password.getText().toString();
                if(TextUtils.isEmpty(e_mail) || TextUtils.isEmpty(pass)){
                    Toast.makeText(LoginActivity.this, "All fields Required.", Toast.LENGTH_LONG).show();
                }
                else{
                    if(internetConnection.checkInternetApiCall(LoginActivity.this)){
                        if(android.util.Patterns.EMAIL_ADDRESS.matcher(e_mail).matches()){
                            // Api call, get values and do something with it.
                            mongoDBapi.loginRequest(new VolleyCallBack() {
                                @Override
                                public void onSuccessResponse(String result) {
                                    try {
                                        JSONArray response = new JSONArray(result);
                                        JSONObject jobj = response.getJSONObject(0);
                                        String idPlayer = jobj.getString("idPlayer");
                                        Toast.makeText(LoginActivity.this, "Loged in: id Player " + idPlayer, Toast.LENGTH_LONG).show();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                @Override
                                public void onErrorResponse(String result) {
                                    Log.d("ERROR", result);
                                }
                            }, e_mail,pass, LoginActivity.this);
                        // place here your intent to the homePage
                        // Intent intent = new Intent(....
                        }
                    }
                }
            }
        });

        recoverPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(internetConnection.checkInternetApiCall(LoginActivity.this)){
                    Intent intent = new Intent(getApplicationContext(),PasswordRecovery.class);
                    startActivity(intent);
                }
            }
        });

        createUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(internetConnection.checkInternetApiCall(LoginActivity.this)){
                    Intent intent = new Intent(getApplicationContext(),signup.class);
                    startActivity(intent);
                }
            }
        });
    }
}