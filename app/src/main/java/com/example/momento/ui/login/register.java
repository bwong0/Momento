package com.example.momento.ui.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.momento.R;
import com.example.momento.databinding.ActivityLoginBinding;
import com.example.momento.databinding.ActivityRegisterBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class register extends AppCompatActivity {
    private Button next;
    private ActivityRegisterBinding binding;
    private FirebaseAuth mAuth;
    private static String TAG = "register process: ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        final Button next = binding.proceedToAdmin;
        final EditText first = binding.editTextFirstName;
        final EditText last = binding.editTextLastName;
        final EditText address = binding.editTextAddress;
        final EditText ID = binding.editTextID;
        final EditText email = binding.editTextEmail;
        final EditText password = binding.editTextPassword;

        next.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(empty(first) || empty(last) || empty(address) || empty(ID) || empty(email) || empty(password)){
                    finish();
                    startActivity(getIntent());
                }else{
                    // create admin account here, and go to admin home after successfully created
                    // email/password validation WIP
                    mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                            .addOnCompleteListener(register.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d(TAG, "createUserWithEmail:success");
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        updateUI(user);
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                        Toast.makeText(register.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                        finish();
                                        startActivity(getIntent());
                                    }
                                }
                            });

                }
            }
        });
    }

    public void openAdminHome(){
        Intent intent = new Intent(this, adminHome.class);
        startActivity(intent);
    }

    public boolean empty(EditText a){
        if(a.getText().toString().trim().length() == 0)
            return true;
        else
            return false;
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            reload();
        }
    }

    public void reload(){}

    public void updateUI(FirebaseUser user){
        //redirect to admin home with data, WIP
        openAdminHome();
    }

}