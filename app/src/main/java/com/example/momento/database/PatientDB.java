package com.example.momento.database;


import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PatientDB {

    private static final String TAG = "PatientDB";
    private String uid;
    private FirebaseDatabase database;
    private DatabaseReference myRef;


    // constructor

    public PatientDB(String patient_uid) {
        this.uid = patient_uid;
    }
    // reference should point to the specific patient (information from LoggedInUser.java)

    public static class User {

        public String user_firstname;
        public String user_lastname;
        public String address;
        public String patient_uid;

        public User() {
            // Default constructor required for calls to DataSnapshot.getValue(User.class)
        }
        public User(String firstname,String lastname, String address) {
            this.user_firstname = firstname;
            this.user_lastname = lastname;
            this.address = address;

        }

    }

    //setter
    public void writeNewUser(String firstname,String lastname, String address,String patient_uid) {
        User user = new User(firstname,lastname,address);

        myRef.child("Patient").child(patient_uid).setValue(user);
    }


    // getters
    public void getPatientValue() {
        myRef.child("Patient").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    Log.d("firebase", String.valueOf(task.getResult().getValue()));

                }
            }
        });
    }







}
