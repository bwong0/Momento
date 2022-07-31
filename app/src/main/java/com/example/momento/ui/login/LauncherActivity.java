package com.example.momento.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.momento.ui.login.LoginActivity;

import androidx.annotation.NonNull;

import com.example.momento.database.AccountDB;
import com.example.momento.database.AccountType;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Map;

public class LauncherActivity {

    private FirebaseAuth mAuth;
    private LoginViewModel loginViewModel;

    public void onCreate(Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
    };

    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser != null) //Current User is Signed-In
        {
            //Checks the user type and redirect.
            getAccountType(accType -> {
                updateUiWithUser(loginResult.getSuccess(), accType);
            });
        }
        else //The user must sign in register
        {
            //TODO: Go to the Admin Login/Registeration Page
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
    }

    /**
     * Callback function to get AccountType using currently logged in user
     * @param accountCb Callback to return AccountType
     */
    private void getAccountType(LoginUICallbacks accountCb) {
        FirebaseUser user = mAuth.getCurrentUser();
        String currentUID = user.getUid();
        // Initialize database reference point
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        // Get accountType from Firebase
        mDatabase.child(AccountDB.ACCOUNT_NODE).child(currentUID).get()
                .addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (task.isSuccessful()) {
                            // fetch value and store into a Map
                            DataSnapshot snapshot = task.getResult();
                            Map<String, Object> info = (Map<String, Object>) snapshot.getValue();
                            // if the UID is already in Firebase, fetch data members
                            if (snapshot.exists()) {
                                // Return accountType by Callback
                                accountCb.getAccType(
                                        AccountType.valueOf(info.get(AccountDB.ACCOUNT_TYPE).toString())
                                );
                            } else {
                                // What if the uid does not exist?
                                // TODO: Decide on this with team.
                            }
                        } else {
                            Log.d(TAG, "failed: " + String.valueOf(task.getResult().getValue()));
                        }
                    }
                });
    }

    /**
     * Navigate to "adminHome" Activity
     */
    private void openAdminHome(){
        Intent intent = new Intent(this, adminHome.class);
        startActivity(intent);
    }

    private void openFamilyHome(){
        //Intent intent = new Intent(this, familyHome.class);
        //startActivity(intent);
    }

    private void openPatientHome(){
        //Intent intent = new Intent(this, patientHome.class);
        //startActivity(intent);
    }

    /**
     * Action after successful account authentication.
     * @param model LoggedInUserView
     */
    private void updateUiWithUser(LoggedInUserView model, AccountType accountType) {
        // TODO : initiate successful logged in experience
        Log.d(TAG, accountType.toString());

        if(accountType == AccountType.ADMIN){
            Log.d(TAG, "admin type");
            openAdminHome();
        }
        else if(accountType == AccountType.FAMILY) {
            Log.d(TAG, "family type");
            openFamilyHome();
        }
        else{
            Log.d(TAG, "patient type");
            openPatientHome();
        }
    }


}
