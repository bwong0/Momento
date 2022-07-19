package com.example.momento.data;

import android.util.Log;

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
    private static final String TAG = "data source running";

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
                            //here to check if the user is new or not, if new, set result to newUser
                            boolean isNewUser = task.getResult().getAdditionalUserInfo().isNewUser();
                            if(isNewUser){
                                Log.d(TAG, "data source running");
                                callback.onLogin(new Result.newUser("No existing user, switch to registration page."));
                            }
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