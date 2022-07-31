package com.example.momento.ui.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

import org.w3c.dom.Text;

// TODO: Add margin around the whole interactable area
public class RegisterActivity extends AppCompatActivity {
    private ActivityRegisterBinding binding;
    private FirebaseAuth mAuth;
    private AccountType admin = AccountType.ADMIN;
    private static String TAG = "register process: ";
    private boolean allFieldsPassed = false;

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

        first.setError("Enter first name.");
        first.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //ignore
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length()==0){
                    first.setError("First name can not be empty.");
                    allFieldsPassed = true;
                }else {
                    first.setError(null);
                    allFieldsPassed = false;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(first.getText().toString().isEmpty()){
                    first.setError("First name can not be empty.");
                    allFieldsPassed = false;
                }else{
                    first.setError(null);
                    allFieldsPassed = true;
                }
            }
        });

        last.setError("Enter last name.");
        last.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //ignore
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length()==0){
                    last.setError("Last name can not be empty.");
                    allFieldsPassed = false;
                }else {
                    last.setError(null);
                    allFieldsPassed = true;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                //ignore
            }
        });

        address.setError("Enter address.");
        address.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //ignore
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length()==0){
                    address.setError("Address can not be empty.");
                    allFieldsPassed = false;
                }else {
                    address.setError(null);
                    allFieldsPassed = true;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(address.getText().toString().isEmpty()){
                    address.setError("Address can not be empty.");
                    allFieldsPassed = false;
                }else{
                    address.setError(null);
                    allFieldsPassed = true;
                }
            }
        });

        ID.setError("Enter User ID");
        ID.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //ignore
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length()==0){
                    ID.setError("User ID can not be empty.");
                    allFieldsPassed = false;
                }else
                    allFieldsPassed = true;
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(ID.getText().toString().isEmpty()){
                    ID.setError("User ID can not be empty.");
                    allFieldsPassed = false;
                }else{
                    ID.setError(null);
                    allFieldsPassed = true;
                }
            }
        });

        email.setError("Enter email.");
        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //ignore
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length() == 0){
                    email.setError("Email can not be empty.");
                    allFieldsPassed = false;
                }else{
                    email.setError(null);
                    allFieldsPassed = true;
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(email.getText().toString().isEmpty()){
                    email.setError("Email can not be empty.");
                    allFieldsPassed = false;
                }else if(!Patterns.EMAIL_ADDRESS.matcher(email.getText()).matches()){
                    email.setError("Email invalid.");
                    allFieldsPassed = false;
                }else{
                    email.setError(null);
                    allFieldsPassed = true;
                }

            }
        });

        password.setError("Enter password.");
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //ignore
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length()==0) {
                    password.setError("Password can not be empty.");
                    allFieldsPassed = false;
                }
                else if(charSequence.length() > 0 && charSequence.length() <= 5) {
                    password.setError("Password must be longer than 5 characters.");
                    allFieldsPassed = false;
                }else
                    allFieldsPassed = true;
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(password.getText().toString().isEmpty()){
                    first.setError("Password can not be empty.");
                    allFieldsPassed = false;
                }else{
                    password.setError(null);
                    allFieldsPassed = true;
                }
            }
        });



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
                // if any fields does not meet requirement
                } else if(!allFieldsPassed){
                    Toast.makeText(context, "All fields must fulfill their conditions.",
                            Toast.LENGTH_LONG).show();
                } else{
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
