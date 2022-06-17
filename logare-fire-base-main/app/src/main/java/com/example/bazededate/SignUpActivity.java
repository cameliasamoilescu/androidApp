package com.example.bazededate;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity implements UserOperations {

    private EditText emailField;
    private EditText userNameField;
    private Button button;
    private EditText passwordField;
    FirebaseDatabase database;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // daca nu pui asta, nu va da refresh pe ce layout e si un va gasi niciun buton
        setContentView(R.layout.activity_sign_up);

        emailField = findViewById(R.id.email);
        userNameField = findViewById(R.id.username);

        button = findViewById(R.id.button2);
        passwordField = findViewById(R.id.editTextTextPassword);

        database = FirebaseDatabase.getInstance();


        button.setOnClickListener(view ->
                // makeSharePreferences
                // insertUsers()
                doTheSigninUp());

    }

    private void doTheSigninUp() {
        String userName = userNameField.getText().toString();
//        int age =  Integer.parseInt(ageField.getText().toString());
        String password = passwordField.getText().toString();
//        System.out.println(password);

        String email = emailField.getText().toString().trim();

        mAuth = FirebaseAuth.getInstance();

        // new InsertUserOperation(this).execute(users);

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("authentication", "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(userName)
                                    .build();
                            user.updateProfile(profileUpdates)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Log.d("Register", "User profile updated.");
                                            }
                                        }
                                    });
                            updateUI(user);
                            Intent intent = new Intent(getApplicationContext(), HelloActivity.class);
                            startActivity(intent);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("authentication", "createUserWithEmail:failure", task.getException());
                            System.out.println("Authentication failed.");
                            updateUI(null);
                        }

                        // ...
                    }
                });
    }

    public void updateUI(FirebaseUser account){

        if(account != null){
            Toast.makeText(this,"U Signed In successfully",Toast.LENGTH_LONG).show();
            // startActivity(new Intent(this,AnotherActivity.class));

        }else {
            Toast.makeText(this,"U Didnt signed in",Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void insertAll(String result) {
        Toast.makeText(this, result, Toast.LENGTH_LONG).show();
    }
}
