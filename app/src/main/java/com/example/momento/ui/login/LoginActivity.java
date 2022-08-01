package com.example.momento.ui.login;

import android.Manifest;
import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.momento.R;
import com.example.momento.admin.admin.patientCreation;
import com.example.momento.database.AccountDB;
import com.example.momento.database.AccountType;
import com.example.momento.databinding.ActivityLoginBinding;
import com.example.momento.family.familyHome;
import com.example.momento.patient.patientHome;
import com.example.momento.family.ProfileCreation;
import com.example.momento.patient.patientHome;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    /* Class Variables: */
    private LoginViewModel loginViewModel;
    private ActivityLoginBinding binding;
    private Button next;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseAuth.AuthStateListener authStateListener;
    private static final String TAG = "LoginActivity";


    /* Class Constants: */

    // Constants for checking App permissions; arbitrary numbers, but must be unique
    private static final int INTERNET_PERMISSION_CODE = 100;
    private static final int ACCESS_NETWORK_STATE_PERMISSION_CODE = 101;
    // TODO: Check other permissions such as file storage access


    /* Lifecycle Methods */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /************** UI/UX **************/

        //this keeps the first login user, redirect to home page for testing
        //TODO: add log out button to revoke current user
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() != null) {
                    //openHome();
                }
            }
        };

        setContentView(R.layout.activity_login);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);

        final EditText usernameEditText = binding.username;
        final EditText passwordEditText = binding.password;
        final Button loginButton = binding.login;
//        final Button next = binding.next;
        final ProgressBar loadingProgressBar = binding.loading;
        final Button adminRegister = binding.register;

        // hides keyboard when not editing text
        EditText[] editFields = {usernameEditText, passwordEditText};
        for (EditText eachField : editFields) {
            eachField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {
                        hideKeyboard(v);
                    }
                }
            });
        }

        // Login Button
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingProgressBar.setVisibility(View.VISIBLE);
                loginViewModel.login(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        });

        // Register Button
        adminRegister.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                openRegister();
            }
        });

        // Test without Login Button
        next = (Button) findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            // TODO: Remove this after finishing registration functionality
            @Override
            public void onClick(View view) {
                openHome();
            }
        });


        /* Check App Permissions */
        checkPermission(Manifest.permission.INTERNET, INTERNET_PERMISSION_CODE);
        checkPermission(
                Manifest.permission.ACCESS_NETWORK_STATE,
                ACCESS_NETWORK_STATE_PERMISSION_CODE
        );


        /************** LOGIN MODEL **************/
        loginViewModel.getLoginFormState().observe(this, new Observer<LoginFormState>() {
            @Override
            public void onChanged(@Nullable LoginFormState loginFormState) {
                if (loginFormState == null) {
                    return;
                }
                loginButton.setEnabled(loginFormState.isDataValid());
                if (loginFormState.getUsernameError() != null) {
                    usernameEditText.setError(getString(loginFormState.getUsernameError()));
                }
                if (loginFormState.getPasswordError() != null) {
                    passwordEditText.setError(getString(loginFormState.getPasswordError()));
                }
            }
        });

        loginViewModel.getLoginResult().observe(this, new Observer<LoginResult>() {
            @Override
            public void onChanged(@Nullable LoginResult loginResult) {
                if (loginResult == null) {
                    return;
                }
                loadingProgressBar.setVisibility(View.GONE);
                if (loginResult.getError() != null) {
                    showLoginFailed(loginResult.getError());
                }
                if (loginResult.getSuccess() != null) {
                    getAccountType(accType -> {
                        updateUiWithUser(loginResult.getSuccess(), accType);
                    });
                }

                setResult(Activity.RESULT_OK);

                //Complete and destroy login activity once successful
                finish();
            }
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                loginViewModel.loginDataChanged(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        };
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    loginViewModel.login(usernameEditText.getText().toString(),
                            passwordEditText.getText().toString());
                }
                return false;
            }
        });
        /************** END OF LOGIN MODEL **************/

    } // END OF onCreate()


    /* Class Methods */

    /**
     * Navigate to "Register" Activity
     */
    private void openRegister() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    /**
     * Navigate to "Home" Activity
     */
    private void openHome() {
        Intent intent = new Intent(this, Home.class);
        startActivity(intent);
    }

    /**
     * Navigate to "adminHome" Activity
     */
    private void openAdminHome(){
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Intent intent = new Intent(this, patientCreation.class);
        intent.putExtra("uid",uid);
        startActivity(intent);
    }

    private void openFamilyHome(){
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Intent intent = new Intent(this, ProfileCreation.class);
        intent.putExtra("uid",uid);
        startActivity(intent);
    }

    private void openPatientHome(){
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Intent intent = new Intent(this, patientHome.class);
        intent.putExtra("uid",uid);
        startActivity(intent);
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

    /**
     * Action after failed account authentication.
     * @param errorString
     */
    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
        // Recreate this Activity upon failed login
        finish();
        startActivity(getIntent());
    }

    /** Check for current user
     *  if current user exists, directly goes to the correct page
     *  currently set to home page for testing
     */
    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(authStateListener);
    }

    /**
     * Check App permissions.
     * @param permission Android Manifest Permission
     * @param requestCode Associated request code constants set in this Class.
     */
    public void checkPermission(String permission, int requestCode) {
        // Checking if permission is not granted
        if ( ContextCompat.checkSelfPermission(this, permission) ==
                PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this,
                    new String[] { permission },
                    requestCode);
        }
        else {
            Toast.makeText(this, "Permission already granted",
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * This function is called when the user accepts or decline the permission.
     * @param requestCode int constant used to check which permission called in this function.
     * @param permissions String[] See superclass constructor
     * @param grantResults int[] See superclass constructor
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode,
                permissions,
                grantResults);

        if (requestCode == INTERNET_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this,
                        "Internet Permission Granted",
                        Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(this, "Internet Permission Denied",
                        Toast.LENGTH_SHORT).show();
            }
        }
        else if (requestCode == ACCESS_NETWORK_STATE_PERMISSION_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "ACCESS_NETWORK_STATE Permission Granted",
                        Toast.LENGTH_SHORT).show();
                // TODO: remove this in final version
            } else {
                Toast.makeText(this, "ACCESS_NETWORK_STATE Permission Denied",
                        Toast.LENGTH_SHORT).show();
                // TODO: remove this in final version
            }
        }
    }

    /**
     * Pass an EditView to hide keyboard after exiting focus.
     * @param view
     */
    private void hideKeyboard(View view) {
        InputMethodManager manager =
                (InputMethodManager) getSystemService( Context.INPUT_METHOD_SERVICE
                );
        manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}