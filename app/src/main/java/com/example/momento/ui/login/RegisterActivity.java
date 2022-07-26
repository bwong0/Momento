package com.example.momento.ui.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.momento.R;
import com.example.momento.database.AccountDB;
import com.example.momento.database.AccountType;
import com.example.momento.database.AdminDB;
import com.example.momento.databinding.ActivityRegisterBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

// TODO: Add margin around the whole interactable area
public class RegisterActivity extends AppCompatActivity {
    private Button next;
    private ActivityRegisterBinding binding;
    private FirebaseAuth mAuth;
    private AccountType admin = AccountType.ADMIN;
    private static String TAG = "register process: ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        final Button register = binding.proceedToAdmin;
        final EditText first = binding.editTextFirstName;
        final EditText last = binding.editTextLastName;
        final EditText address = binding.editTextAddress;
        final EditText ID = binding.editTextID;
        final EditText email = binding.editTextEmail;
        final EditText password = binding.editTextPassword;
        password.setTransformationMethod(new PasswordTransformationMethod());
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
        register.setOnClickListener(new View.OnClickListener(){
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
                    // create admin account here
                    // email password validation WIP
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
                                    user.sendEmailVerification()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        // email sent, and account created with info from EditText array
                                                        // after email is sent just logout the user
                                                        FirebaseAuth.getInstance().signOut();
                                                    }
                                                    else
                                                    {
                                                        // email not sent, so display message and restart the activity
                                                        overridePendingTransition(0, 0);
                                                        finish();
                                                        overridePendingTransition(0, 0);
                                                        startActivity(getIntent());
                                                    }
                                                }
                                            });
                                    AdminDB newAdmin = new AdminDB(user.getUid(), admin, first.getText().toString(),
                                            last.getText().toString(), email.getText().toString(), address.getText().toString());
                                    updateUI();
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

    public void openLoginActivity(){
        Intent intent = new Intent(this, LoginActivity.class);
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

    public void updateUI(){
        openLoginActivity();
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
