package com.example.momento.ui.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
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
import com.example.momento.admin.admin.patientCreation;
import com.example.momento.database.AccountType;
import com.example.momento.database.AdminDB;
import com.example.momento.database.PatientDB;
import com.example.momento.database.ServerCallback;
import com.example.momento.databinding.ActivityPatientRegisterBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class patientRegister extends AppCompatActivity {
    private ActivityPatientRegisterBinding binding;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private AccountType patient = AccountType.PATIENT;
    private Date birth;
    private static String TAG = "patient register process: ";
    private boolean allFieldsPassed = false;
    String AdminUid;
    AdminDB adminDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_register);

        binding = ActivityPatientRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        AdminUid = getIntent().getStringExtra("uid");


        final Button patientRegister = binding.btnRegister;
        final EditText first = binding.editTextFirstName;
        final EditText last = binding.editTextLastName;
        final EditText address = binding.editTextAddress;
        final EditText email = binding.editTextEmail;
        final EditText password = binding.editTextPassword;
        final EditText healthCard = binding.editTextCardNum;
        final EditText birthDate = binding.editTextTextBirthDate;
        password.setTransformationMethod(new PasswordTransformationMethod());
        EditText[] editFields = {first, last, address, email, password, healthCard, birthDate};

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

        healthCard.setError("Enter the health card number.");
        healthCard.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //ignore
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length() != 9){
                    healthCard.setError("Health card number must be a 9 digit number.");
                    allFieldsPassed = false;
                }else if(charSequence.toString().matches("[0~9]+")){
                    healthCard.setError("Health card number must not contain any non integer values.");
                    allFieldsPassed = false;
                }else{
                    healthCard.setError(null);
                    allFieldsPassed = true;
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(healthCard.getText().toString().length() != 9){
                    healthCard.setError("Health card number must be a 9 digit number.");
                    allFieldsPassed = false;
                }else if(healthCard.getText().toString().matches("[0~9]+")){
                    healthCard.setError("Health card number must not contain any non integer values.");
                    allFieldsPassed = false;
                }else{
                    healthCard.setError(null);
                    allFieldsPassed = true;
                }

            }
        });

        birthDate.setError("Enter a birth date.");
        birthDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //ignore
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!dateCheck(charSequence.toString())){
                    birthDate.setError("Please follow provided syntax for date input.");
                    allFieldsPassed = false;
                }else if(charSequence.length() == 0){
                    birthDate.setError("Birth date can not be empty.");
                    allFieldsPassed = false;
                }else{
                    birthDate.setError(null);
                    allFieldsPassed = true;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(!dateCheck(birthDate.getText().toString())){
                    birthDate.setError("Please follow provided syntax for date input.");
                    allFieldsPassed = false;
                }else if(birthDate.getText().toString().length() == 0){
                    birthDate.setError("Birth date can not be empty.");
                    allFieldsPassed = false;
                }else{
                    birthDate.setError(null);
                    allFieldsPassed = true;
                }

            }
        });

        patientRegister.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

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
                }else{
                    // create patient account here
                    String emailString = email.getText().toString().trim();
                    String passwordString = password.getText().toString().trim();
                    try {
                        birth = new SimpleDateFormat("MM/dd/yyyy").parse(birthDate.getText().toString());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    mAuth.createUserWithEmailAndPassword(emailString, passwordString).addOnCompleteListener(patientRegister.this,
                            new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        Log.d(TAG, "createUserWithEmail:success");
                                        //TODO: still need email verification for patient?

                                        String uid = mAuth.getCurrentUser().getUid();

                                        Log.d(TAG, "patient uid is : " + uid);
                                        PatientDB newPatient = new PatientDB(uid, patient, first.getText().toString(),
                                                last.getText().toString(), emailString, address.getText().toString(),
                                                healthCard.getText().toString(), birth, new ServerCallback() {
                                            @Override
                                            public void isReadyCallback(boolean isReady) {
                                                Log.d(TAG,"before patient is ready");
                                                if(isReady) {
                                                    Log.d(TAG,"before admin is ready");
                                                    Log.d(TAG,AdminUid);
                                                    adminDB = new AdminDB(AdminUid, new ServerCallback() {
                                                        @Override
                                                        public void isReadyCallback(boolean isReady) {
                                                            Log.d(TAG,"before add is ready");
                                                            if (isReady) {
                                                                Log.d("patientRegister" , "before");
                                                                adminDB.addPatient(uid);

                                                                updateUI();
                                                            }
                                                        }
                                                    });
                                                }
                                            }
                                        });

                                    }else{
                                            // If sign in fails, display a message to the user.
                                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                            Toast.makeText(patientRegister.this, "Authentication failed.",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }


                }
            });
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
     * Checks for correct date format
     */
    public boolean dateCheck(String date){
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        dateFormat.setLenient(false);
        try {
            dateFormat.parse(date.trim());
        } catch (ParseException pe) {
            return false;
        }
        return true;
    }

    public void updateUI(){
        finish();
    }

}