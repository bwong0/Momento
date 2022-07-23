package com.example.momento.ui.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.momento.R;
import com.example.momento.databinding.ActivityRegisterBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

// TODO: Add persistent text beside each EditText, so once the grey hint is gone, User can still know which field is for what.
// TODO: Add margin around the whole interactable area
public class RegisterActivity extends AppCompatActivity {
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
        final EditText password = binding.editTextPassword; // TODO: Should use a **** Password field
        EditText[] editFields = {first, last, address, ID, email, password}; // TODO: Keep this updated.

        // hides keyboard when not editing text
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
        // TODO: Change "Next" to "Register" button.
        next.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Context context = getApplicationContext();
                // TODO: (low priority) Look into how LoginFormState has warning in text box
                // if not all fields are filled
                if( !isFormComplete(editFields)){
                    Toast.makeText(context, "All fields must be filled.",
                            Toast.LENGTH_LONG).show();
                // if email is not an email
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email.getText()).matches()) {
                    Toast.makeText(context, "Invalid Email.",
                            Toast.LENGTH_LONG).show();
                } else {
                    // create admin account here, and go to admin home after successfully created
                    // email/password validation WIP
                    String emailString = email.getText().toString();
                    String passwordString = password.getText().toString();
                    mAuth.createUserWithEmailAndPassword( emailString, passwordString)
                        .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "createUserWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    // TODO: Go back to LoginActivity. Add email verification. Then they can sign in from Login.
                                    updateUI(user);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                }
            }
        });
    }

    // TODO: Delete this. We will add email verification.
    public void openAdminHome(){
        Intent intent = new Intent(this, adminHome.class);
        startActivity(intent);
    }

    /**
     * Check if an EditText object contains no characters.
     * @param editText EditText object.
     * @return True if length > 0. False otherwise.
     */
    private boolean isEmpty(EditText editText){
        if(editText.getText().toString().trim().length() == 0)
            return true;
        else
            return false;
    }

    /**
     * Check if an array of EditText objects are all filled.
     * @param editTextObjects EditText[]
     * @return True if none are empty. False if one or more is empty.
     */
    private boolean isFormComplete(EditText[] editTextObjects) {
        for (EditText eachField: editTextObjects) {
            if (isEmpty(eachField)) { return false; }
        }
        return true;
    }

    // TODO: Not necessary? Delete
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            reload();
        }
    }

    // TODO: Clean up if unused.
    public void reload(){}

    public void updateUI(FirebaseUser user){
        // TODO: redirect back to LoginActivity. Make sure mAuth is not signed in after create().
        openAdminHome();
    }

    /**
     * Hides Software Keyboard
     * */
    private void hideKeyboard(View view) {
        InputMethodManager manager =
                (InputMethodManager) getSystemService( Context.INPUT_METHOD_SERVICE
                );
        manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}