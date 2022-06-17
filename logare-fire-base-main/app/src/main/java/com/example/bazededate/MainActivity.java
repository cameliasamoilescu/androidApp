package com.example.bazededate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements UserOperations{
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);

        if (currentUser != null) {
            Intent intent = new Intent(getApplicationContext(), HelloActivity.class);
            startActivity(intent);
        }



        findViewById(R.id.button).setOnClickListener(view ->
                // makeSharePreferences
                // insertUsers()
                goToSignUpActivity());

        findViewById(R.id.button3).setOnClickListener(view ->
                gotoSignInActivity());
    }

    public void updateUI(FirebaseUser account){

        if(account != null){
            Toast.makeText(this,"U Signed In successfully",Toast.LENGTH_LONG).show();
            // startActivity(new Intent(this,AnotherActivity.class));

        }else {
            Toast.makeText(this,"U Didnt signed in",Toast.LENGTH_LONG).show();
        }

    }

    private void gotoSignInActivity() {
        Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
        startActivity(intent);
    }

    private void goToSignUpActivity() {
        Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
        startActivity(intent);
    }

    private void insertUsers() {
        User user1 = new User( "mara", "oanea", 20, "bulina");

        User[] users = {user1};

        new InsertUserOperation(this).execute(users);
    }

    @Override
    public void insertAll(String result) {
        Toast.makeText(this, result, Toast.LENGTH_LONG).show();
    }
}