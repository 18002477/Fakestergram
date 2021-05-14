package com.cedric.fakestergramapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    EditText txtEmail, txtPassword;
    Button btnLogin,btnRegister, btnSignOut;
    private FirebaseAuth mAuth;
    FirebaseUser currentUser;
    CardView loginCardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();

        txtEmail = findViewById(R.id.et_email);
        txtPassword = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_login);
        btnRegister = findViewById(R.id.btn_register);
        btnSignOut = findViewById(R.id.btn_signOut);
        currentUser = mAuth.getCurrentUser();
        loginCardView = findViewById(R.id.cardView_Login);

        // IF USER IS CURRENTLY LOGGED IN
        if (currentUser != null){

            loginCardView.setVisibility(View.GONE);
            loadFragment();
        }
        else
        {
            // IF NO USER IS LOGGED IN
            loginCardView.setVisibility(View.VISIBLE);
        }

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String enteredEmail = txtEmail.getText().toString().trim();
                String enteredPassword = txtPassword.getText().toString().trim();
                mAuth.createUserWithEmailAndPassword(enteredEmail,enteredPassword)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()){
                                    Toast.makeText(MainActivity.this,"User "
                                            +mAuth.getCurrentUser().getEmail()+" Successfully registered",
                                            Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    Toast.makeText(MainActivity.this,"Registration has been unsuccessful", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, e.getMessage() , Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String enteredEmail = txtEmail.getText().toString().trim();
                String enteredPassword = txtPassword.getText().toString().trim();

                mAuth.signInWithEmailAndPassword(enteredEmail,enteredPassword)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()){
                                    loginCardView.setVisibility(View.GONE);

                                    Toast.makeText(MainActivity.this, "Logged in "+
                                            mAuth.getCurrentUser().getEmail()+" Successfully", Toast.LENGTH_SHORT).show();
                                    loadFragment();
                                }
                                else {
                                    Toast.makeText(MainActivity.this, "Login Unsuccessful", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                Toast.makeText(MainActivity.this, "You have Signed out", Toast.LENGTH_SHORT).show();
                loginCardView.setVisibility(View.VISIBLE);
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();

        if (currentUser != null)
        {
            Toast.makeText(this,"You are already logged in" +mAuth.getCurrentUser().getEmail(),Toast.LENGTH_SHORT).show();
        }
    }

    // Calling the fragment
    public void loadFragment()
    {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.menu_fragment_placeHolder,MenuFragment.getInstance());
        transaction.commit();

    }
}