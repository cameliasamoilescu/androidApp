package com.example.bazededate;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.net.Inet4Address;

public class SignInActivity extends AppCompatActivity implements UserOperations {
    private EditText nameField;
    private EditText passwordField;
    private Button button;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        signInRequest = BeginSignInRequest.builder()
                .setGoogleIdTokenRequestOptions(GoogleIdTokenRequestOptions.builder()
                        .setSupported(true)
                        // Your server's client ID, not your Android client ID.
                        .setServerClientId(getString(R.string.default_web_client_id))
                        // Only show accounts previously used to sign in.
                        .setFilterByAuthorizedAccounts(true)
                        .build())
                .build();

        mAuth = FirebaseAuth.getInstance();

        // daca nu pui asta, nu va da refresh pe ce layout e si un va gasi niciun buton
        setContentView(R.layout.activity_sign_in);

        nameField = findViewById(R.id.nameField);
        passwordField = findViewById(R.id.passwordField);
        button = findViewById(R.id.button4);

        button.setOnClickListener(view ->
                // makeSharePreferences
                // insertUsers()
                doTheSignIn());
    }

    private void doTheSignIn() {
        String[] strings = {nameField.getText().toString(), passwordField.getText().toString()};
        // new GetUserOperation(this).execute(strings);
        String email = nameField.getText().toString().trim();
        String pass = passwordField.getText().toString().trim();



        if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(pass)){


            mAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(this,new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Intent intent = new Intent(getApplicationContext(), HelloActivity.class);
                        startActivity(intent);
                        finish();
                    }else{
                        System.out.println("Sign-in Failed: " + task.getException().getMessage());
                        Toast.makeText(SignInActivity.this,"Erorr Login", Toast.LENGTH_LONG).show();
                    }
                }
            });

        }
    }





    @Override
    public void insertAll(String result) {

    }



}
