package com.example.loginandregistermongodb;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PasswordRecovery extends AppCompatActivity {
    private EditText email;
    private AppCompatButton resetPassword;
    MongoDBConnection mongoDBapi;
    MyInternetConnection internetConnection;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.passwordrecovery);
        PasswordRecovery.this.getSupportActionBar().hide();
        mongoDBapi = new MongoDBConnection();
        internetConnection = new MyInternetConnection();
        email = findViewById(R.id.email);
        resetPassword = findViewById(R.id.passwordrecovery);

        resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String e_mail = email.getText().toString();
                if (TextUtils.isEmpty(e_mail)) {
                    Toast.makeText(PasswordRecovery.this, "Fill in email field.", Toast.LENGTH_LONG).show();
                }
                else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(e_mail).matches()){
                    Toast.makeText(PasswordRecovery.this, "Invallid email address.", Toast.LENGTH_LONG).show();
                }
                else if (internetConnection.checkInternetApiCall(PasswordRecovery.this))
                {
                    // Send email to backend for recovery password.
                    // Here in this path we will check if the email exists in our system.
                    // if yes, we create a unique token that expires to reset the password in the database.
                    // this link will bring us to an html page to reset the password.
                    mongoDBapi.recoverPasswordRequest(new VolleyCallBack() {
                        @Override
                        public void onSuccessResponse(String result) {
                            Log.d(null, " " + result);
                            Toast.makeText(PasswordRecovery.this, "Email has been send to  " + result +". Check Spam folder.", Toast.LENGTH_LONG).show();
                        }
                        @Override
                        public void onErrorResponse(String result) {
                            Log.d("ERROR", "message " + result);
                        }
                    }, e_mail, PasswordRecovery.this);

                    // Use mail to go on website and set the new password.
                    // login on app after.
                    // Go to main page.
                    Intent intent = new Intent(getApplicationContext(), signup.class);
                    startActivity(intent);
                }
            }
        });
    }
}
