package com.example.loginandregistermongodb;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;

public class signup extends AppCompatActivity {
    private EditText username, password, repassword, email;
    private CheckBox userAgreement;
    private AppCompatButton createUser, loginPage;
    MongoDBConnection mongoDBapi;
    MyInternetConnection internetConnection;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);
        signup.this.getSupportActionBar().hide();
        mongoDBapi = new MongoDBConnection();
        internetConnection = new MyInternetConnection();

        username = findViewById(R.id.username);
        password= findViewById(R.id.password);
        repassword = findViewById(R.id.repassword);
        email= findViewById(R.id.email);
        userAgreement = findViewById(R.id.checkboxUserAgreement);
        createUser = findViewById(R.id.signup);
        loginPage = findViewById(R.id.signin);

        createUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = username.getText().toString();
                String pass = password.getText().toString();
                String repass = repassword.getText().toString();
                String e_mail = email.getText().toString();
                boolean userAgree = userAgreement.isChecked();

                if(TextUtils.isEmpty(user) || TextUtils.isEmpty(pass)|| TextUtils.isEmpty(repass)|| TextUtils.isEmpty(e_mail)){
                    Toast.makeText(signup.this, "All fields Required.", Toast.LENGTH_LONG).show();
                }
                else{
                    // Check if email exists
                    if(!android.util.Patterns.EMAIL_ADDRESS.matcher(e_mail).matches()){
                        Toast.makeText(signup.this, "Invalid email address.", Toast.LENGTH_LONG).show();
                    }
                    // Check if passwords are equal
                    else if(!pass.equals(repass)){
                        Toast.makeText(signup.this, "Your passwords have to be identical.", Toast.LENGTH_LONG).show();
                    }
                    // Check if user terms and agreements are checked.
                    else if(!userAgree){
                        Toast.makeText(signup.this, "User agreement is not checked.", Toast.LENGTH_LONG).show();
                    }
                    // Check if there is internet connectivity
                    else if(internetConnection.checkInternetApiCall(signup.this)){
                        String idPlayer = UUID.randomUUID().toString(); // create a random UUID as a unique ID for the user.

                        // Connect to your backend createUserRequest
                        // This should look for a user = to our unique email or username.
                        // If username or email already exist in database, return error code.
                        mongoDBapi.createUserRequest(new VolleyCallBack() {
                            @Override
                            public void onSuccessResponse(String result) {
                                try {
                                    JSONObject response = new JSONObject(result);
                                    String message = response.getString("message");
                                    if(!(message == "")){
                                        Toast.makeText(signup.this, "Error: " + message, Toast.LENGTH_LONG).show();
                                    } else{
                                        Toast.makeText(signup.this, "Created a new player!", Toast.LENGTH_LONG).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            @Override
                            public void onErrorResponse(String result) {
                                Log.d("ERROR", result);
                            }
                        }, user, pass, e_mail, idPlayer, signup.this);

                        // Go to login screen after creating a new account.
                        Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                        startActivity(intent);
                    }
                }
            }
        });
        loginPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}
