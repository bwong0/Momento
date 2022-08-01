package com.example.momento.ui.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import com.example.momento.database.AccountType;
import com.example.momento.database.FamilyDB;
import com.example.momento.database.PatientDB;
import com.example.momento.database.ServerCallback;
import com.example.momento.databinding.ActivityFamilyHomeBinding;
import com.example.momento.databinding.ActivityFamilyRegisterBinding;
import com.example.momento.databinding.ActivityPatientRegisterBinding;
import com.example.momento.patient.patientHome;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Date;

public class familyRegister extends AppCompatActivity {
    private ActivityFamilyRegisterBinding binding;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private AccountType family = AccountType.FAMILY;
    private static String TAG = "family register process: ";
    private boolean allFieldsPassed = false;
    private String patientUid;
    private PatientDB patientDb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family_register);

        patientUid = getIntent().getStringExtra("patientUid");

        binding = ActivityFamilyRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        final Button familyRegister = binding.btnFamilyRegister;
        final EditText first = binding.editTextFName;
        final EditText last = binding.editTextLName;
        final EditText address = binding.editTextAddressFamily;
        final EditText email = binding.editTextEmailFamily;
        final EditText password = binding.editTextPasswordFamily;
        password.setTransformationMethod(new PasswordTransformationMethod());
        EditText[] editFields = {first, last, address, email, password};

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

        familyRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = getApplicationContext();
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
                    // create family account here
                    String emailFamily = email.getText().toString();
                    String passwordFamily = password.getText().toString();
                    mAuth.createUserWithEmailAndPassword(emailFamily, passwordFamily).addOnCompleteListener(familyRegister.this,
                            new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        String uid = mAuth.getCurrentUser().getUid();
                                        Log.d(TAG, "family account created, uid is : " + uid);
                                        FamilyDB newFamily = new FamilyDB(uid, family, first.getText().toString(),
                                            last.getText().toString(), emailFamily, address.getText().toString(),
                                            new ServerCallback() {
                                                @Override
                                                public void isReadyCallback(boolean isReady) {
                                                    if (isReady) {
                                                        patientDb = new PatientDB(patientUid, new ServerCallback() {
                                                            @Override
                                                            public void isReadyCallback(boolean isReady) {
                                                                if(isReady) {
                                                                    patientDb.addFamily(uid);
                                                                    updateUI();
                                                                }
                                                            }
                                                        });
                                                    }
                                                }
                                            });
                                    }
                                }
                            });
                }
            }
        });


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

    /**
     * Hides Software Keyboard
     * */
    private void hideKeyboard(View view) {
        InputMethodManager manager =
                (InputMethodManager) getSystemService( Context.INPUT_METHOD_SERVICE
                );
        manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void updateUI(){
        mAuth.signOut();
        finish();
    }
}