package com.example.momento.data;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.momento.data.model.LoggedInUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.IOException;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {

    private FirebaseAuth mAuth;
    private FirebaseUser firebaseUser;
    private String Tag = "LoginDataSource: ";

    // Constructor
    public LoginDataSource() {
        this.mAuth = FirebaseAuth.getInstance();
    }

    public void login(String email, String password, LoginCallbacks callback) {
        // sign in to Firebase
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //if email not verified, log out current user and display error message
                            //if(!mAuth.getCurrentUser().isEmailVerified()){
                            //    logout();
                            //    callback.onLogin(new Result.Error(new IOException("Failed to sign in.")));
                            //}
                            firebaseUser = mAuth.getCurrentUser();
                            String uid = firebaseUser.getUid();
                            String name = firebaseUser.getDisplayName();
                            LoggedInUser user = new LoggedInUser(uid, name);
                            callback.onLogin(new Result.Success<>(user));
                        } else {
                            callback.onLogin(
                                    new Result.Error(
                                            new IOException("Failed to sign in.")
                                    ));
                        }
                    }
                });
        }

    public void logout() {
        mAuth.signOut();
    }

}